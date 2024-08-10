from fastapi import FastAPI, File, UploadFile, HTTPException, Depends
from pydantic import BaseModel
import torch
from torchvision import transforms
from PIL import Image
import io
from ultralytics import YOLO
import yaml
import logging
from sqlalchemy import create_engine, Column, Integer, String, Enum
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session
from random import choice
from typing import List
import re
from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship



# 로깅 레벨을 WARNING으로 설정
logging.basicConfig(level=logging.WARNING)

app = FastAPI()

# MySQL 데이터베이스 설정
DATABASE_URL = "mysql+pymysql://admin:thflthsrmf1@sorisonsoon.cxw0iu6oc8k4.ap-northeast-2.rds.amazonaws.com:3306/sorisonsoon"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# SQLAlchemy 모델 정의
class GameRiddle(Base):
    __tablename__ = "game_riddle"

    riddle_id = Column(Integer, primary_key=True, index=True)
    question = Column(String(255), nullable=False)
    video = Column(String(255), nullable=True)
    category = Column(Enum("DAILY_LIFE", "EMOTION", "ANIMALS_PLANTS", "JOB", "FOOD_CLOTHING", "PLACE", "ETC"), nullable=False)
    difficulty = Column(Enum("LEVEL_1", "LEVEL_2", "LEVEL_3"), nullable=False)
    label = Column(String(255), nullable=False)
    
    steps = relationship("GameRiddleStep", back_populates="riddle")

# 새로운 GameRiddleStep 모델
class GameRiddleStep(Base):
    __tablename__ = "game_riddle_step"

    riddle_step_id = Column(Integer, primary_key=True, index=True)
    riddle_id = Column(Integer, ForeignKey('game_riddle.riddle_id'))
    step = Column(Integer, nullable=False)
    answer = Column(String(255), nullable=False)

    riddle = relationship("GameRiddle", back_populates="steps")

# GPU 사용 설정 (가능한 경우)
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
logging.warning(f"Using device: {device}")

# YOLOv8 모델 로드
model = YOLO("models/best (2).pt", task='detect').to(device)

# data.yaml 파일에서 레이블 이름 읽기
with open("data.yaml", 'r') as stream:
    data = yaml.safe_load(stream)

# 레이블 맵 생성
label_map = {i: name for i, name in enumerate(data['names'])}

# 이미지 변환 정의
transform = transforms.Compose([
    transforms.Resize((640, 640)),
    transforms.ToTensor(),
])

# DTO 및 응답 모델 정의
class HandQuestionResponse(BaseModel):
    riddleId: int
    question: str
    totalStep: int

class PredictionResponse(BaseModel):
    label: str
    accuracy: float
    is_correct: bool

# 의존성 주입을 통한 세션 연결
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/api/v1/sign/game-start", response_model=HandQuestionResponse)
async def game_start(db: Session = Depends(get_db)):
    selected_difficulty = choice(["LEVEL_1", "LEVEL_2", "LEVEL_3"])
    
    riddles = db.query(GameRiddle).filter(GameRiddle.difficulty == selected_difficulty).all()
    if not riddles:
        raise HTTPException(status_code=404, detail="No riddle found for the selected difficulty.")
    
    selected_riddle = choice(riddles)

    # 선택된 riddle의 총 단계 수 계산
    total_steps = db.query(GameRiddleStep).filter(GameRiddleStep.riddle_id == selected_riddle.riddle_id).count()

    return HandQuestionResponse(
        riddleId=selected_riddle.riddle_id,
        question=selected_riddle.question,
        totalStep=total_steps
    )

@app.post("/api/v1/sign/question-image", response_model=PredictionResponse)
async def get_question_image(riddleId: int, file: UploadFile = File(...), db: Session = Depends(get_db)):
    # 데이터베이스에서 문제를 조회
    riddle = db.query(GameRiddle).filter(GameRiddle.riddle_id == riddleId).first()
    if not riddle:
        raise HTTPException(status_code=404, detail="Riddle not found.")
    
    # 이미지를 읽고 변환
    contents = await file.read()
    image = Image.open(io.BytesIO(contents)).convert("RGB")
    
    image = transform(image)
    image = image.unsqueeze(0).to(device)

    with torch.no_grad():
        results = model(image, conf=0.25)
        predictions = results[0].boxes
        
        if len(predictions) > 0:
            best_prediction = predictions[0]
            predicted_label_idx = int(best_prediction.cls.item())
            accuracy = float(best_prediction.conf.item())
            predicted_label = label_map.get(predicted_label_idx, "Unknown")
        else:
            predicted_label = "Unknown"
            accuracy = 0.0
    
    # 예측된 레이블을 정규화하여 동작과 레이블을 구분
    match = re.match(r'^(.*)_(\d+)$', predicted_label)
    if match:
        normalized_label = match.group(1)  # "happy"
        predicted_step = int(match.group(2))  # "1"
    else:
        normalized_label = predicted_label
        predicted_step = None

    # 정답과 비교 (레이블과 스텝을 각각 비교)
    is_correct = (
        normalized_label == riddle.label and 
        predicted_step == 1 and  # 현재는 totalStep이 1인 문제만 다룸
        accuracy >= 0.8
    )

    return PredictionResponse(
        label=predicted_label,
        accuracy=accuracy,
        is_correct=is_correct
    )



if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
