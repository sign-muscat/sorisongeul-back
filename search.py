from fastapi import FastAPI, File, UploadFile, HTTPException, Depends
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import torch
from ultralytics import YOLO
from PIL import Image
import io
import yaml
import logging
from sqlalchemy import create_engine, Column, Integer, String, Enum
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from sqlalchemy import ForeignKey
from typing import List

# 로깅 설정
logging.basicConfig(level=logging.WARNING)

app = FastAPI()

# 데이터베이스 설정
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

class GameRiddleStep(Base):
    __tablename__ = "game_riddle_step"

    riddle_step_id = Column(Integer, primary_key=True, index=True)
    riddle_id = Column(Integer, ForeignKey('game_riddle.riddle_id'))
    step = Column(Integer, nullable=False)
    answer = Column(String(255), nullable=False)

    riddle = relationship("GameRiddle", back_populates="steps")

# YOLOv8 모델 로드
model = YOLO("model/best (2).pt")

# data.yaml 파일에서 라벨 정보 읽기
with open("data.yaml", "r") as yaml_file:
    yaml_data = yaml.safe_load(yaml_file)
    labels = yaml_data.get("names", [])

# Pydantic 모델
class QuestionSearchRequest(BaseModel):
    question: str

class SearchResponse(BaseModel):
    video: str

class RecognitionResponse(BaseModel):
    video_url: str

# 데이터베이스 의존성
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/question-search", response_model=SearchResponse)
async def question_search(request: QuestionSearchRequest, db: Session = Depends(get_db)):
    # DB에서 question과 일치하는 GameRiddle 검색
    riddle = db.query(GameRiddle).filter(GameRiddle.question == request.question).first()
    if riddle:
        return SearchResponse(video=riddle.video)
    else:
        raise HTTPException(status_code=404, detail="Question not found in database")

@app.post("/motion-search", response_model=RecognitionResponse)
async def motion_search(file: UploadFile = File(...), db: Session = Depends(get_db)):
    contents = await file.read()
    image = Image.open(io.BytesIO(contents)).convert("RGB")
    
    # YOLOv8 모델로 이미지 분석
    results = model(image)[0]
    
    if len(results.boxes) > 0:
        best_prediction = results.boxes[0]
        label_index = int(best_prediction.cls)
        full_label = labels[label_index] if label_index < len(labels) else "Unknown"
        
        # '_'를 기준으로 라벨을 분리하고 첫 번째 부분만 사용
        label = full_label.split('_')[0]
        
        # DB에서 해당 라벨에 대한 동영상 URL 검색
        riddle = db.query(GameRiddle).filter(GameRiddle.label == label).first()
        video_url = riddle.video if riddle else ""
    else:
        video_url = ""

    return RecognitionResponse(video_url=video_url)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)