from fastapi import FastAPI, File, UploadFile, HTTPException, Depends, Header
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import torch
import torch.nn as nn
from torchvision import transforms, models
from PIL import Image
import io
import yaml
import logging
from sqlalchemy import create_engine, Column, Integer, String, Enum, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from random import choice
from typing import List, Optional
import uuid
import os

# 로깅 설정
logging.basicConfig(level=logging.WARNING)

# 데이터베이스 설정
DATABASE_URL = "mysql+pymysql://admin:thflthsrmf1@sorisonsoon.cxw0iu6oc8k4.ap-northeast-2.rds.amazonaws.com:3306/sorisonsoon"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# 데이터베이스 모델
class GameRiddle(Base):
    __tablename__ = "game_riddle"

    riddle_id = Column(Integer, primary_key=True, index=True)
    question = Column(String(255), nullable=False)
    video = Column(String(255), nullable=True)
    category = Column(Enum("DAILY_LIFE", "EMOTION", "ANIMALS_PLANTS", "JOB", "FOOD_CLOTHING", "PLACE", "ETC"), nullable=False)
    difficulty = Column(Enum("LEVEL_1", "LEVEL_2", "LEVEL_3"), nullable=False)
    label = Column(String(255), nullable=False)
    
    steps = relationship("GameRiddleStep", back_populates="riddle")

class GameRiddleStep(Base):
    __tablename__ = "game_riddle_step"

    riddle_step_id = Column(Integer, primary_key=True, index=True)
    riddle_id = Column(Integer, ForeignKey('game_riddle.riddle_id'))
    step = Column(Integer, nullable=False)
    answer = Column(String(255), nullable=False)

    riddle = relationship("GameRiddle", back_populates="steps")

# FastAPI 앱 생성
app = FastAPI()

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 실제 배포 시 프론트엔드 도메인으로 제한
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 세션 저장소
game_sessions = {}

# 세션 모델
class GameSession(BaseModel):
    session_id: str
    riddle_id: int
    current_step: int
    total_steps: int

# 응답 모델
class GameStartResponse(BaseModel):
    session_id: str
    question: str
    total_steps: int

class PredictionResponse(BaseModel):
    is_correct: bool
    current_step: int
    total_steps: int
    feedback: str

# 데이터베이스 의존성
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# GPU 설정
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
logging.warning(f"Using device: {device}")

# 모델 로드 함수
def load_model():
    model = models.resnet50(weights=None)
    num_ftrs = model.fc.in_features
    with open("data.yaml", 'r') as stream:
        data = yaml.safe_load(stream)
    num_classes = len(data['names'])
    model.fc = nn.Linear(num_ftrs, num_classes)
    model.load_state_dict(torch.load('model/best_resnet_multilabel_model.pth', map_location=device))
    model.to(device)
    model.eval()
    return model, data['names']

model, class_names = load_model()

# 이미지 변환
transform = transforms.Compose([
    transforms.Resize((640, 640)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# 게임 시작 엔드포인트
@app.post("/api/v1/sign/game-start", response_model=GameStartResponse)
async def game_start(db: Session = Depends(get_db)):
    selected_difficulty = choice(["LEVEL_1", "LEVEL_2", "LEVEL_3"])
    
    riddles = db.query(GameRiddle).filter(GameRiddle.difficulty == selected_difficulty).all()
    if not riddles:
        raise HTTPException(status_code=404, detail="No riddle found for the selected difficulty.")
    
    selected_riddle = choice(riddles)
    total_steps = db.query(GameRiddleStep).filter(GameRiddleStep.riddle_id == selected_riddle.riddle_id).count()

    session_id = str(uuid.uuid4())
    game_sessions[session_id] = GameSession(
        session_id=session_id,
        riddle_id=selected_riddle.riddle_id,
        current_step=1,
        total_steps=total_steps
    )

    return GameStartResponse(
        session_id=session_id,
        question=selected_riddle.question,
        total_steps=total_steps
    )

# 이미지 예측 엔드포인트
@app.post("/api/v1/sign/predict", response_model=PredictionResponse)
async def predict_image(file: UploadFile = File(...), session_id: str = Header(...), db: Session = Depends(get_db)):
    if session_id not in game_sessions:
        raise HTTPException(status_code=400, detail="Invalid session ID")
    
    session = game_sessions[session_id]
    current_step_before_prediction = session.current_step
    
    # 이미지 처리 및 예측
    contents = await file.read()
    image = Image.open(io.BytesIO(contents)).convert("RGB")
    image = transform(image).unsqueeze(0).to(device)

    with torch.no_grad():
        outputs = model(image)
        probabilities = torch.sigmoid(outputs)
        max_prob, predicted_idx = torch.max(probabilities, 1)
        predicted_label = class_names[predicted_idx.item()]

    # 현재 단계의 정답 확인
    current_step = db.query(GameRiddleStep).filter(
        GameRiddleStep.riddle_id == session.riddle_id,
        GameRiddleStep.step == session.current_step
    ).first()

    if not current_step:
        raise HTTPException(status_code=400, detail="Invalid game state")

    correct_label = f"{current_step.riddle.label}_{current_step.step}"
    is_correct = (predicted_label == correct_label)

    feedback = "정답입니다!" if is_correct else "틀렸습니다. 다시 시도해보세요."

    if is_correct:
        session.current_step += 1
        if session.current_step > session.total_steps:
            del game_sessions[session_id]
            feedback = "모든 단계를 완료했습니다!"

    return PredictionResponse(
        is_correct=is_correct,
        current_step=current_step_before_prediction,
        total_steps=session.total_steps,
        feedback=feedback
    )
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)