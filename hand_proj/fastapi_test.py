from fastapi import FastAPI, File, UploadFile
from pydantic import BaseModel
import torch
from torchvision import transforms
from PIL import Image
import io
from ultralytics import YOLO
import yaml
import logging

# 로깅 레벨을 WARNING으로 설정
logging.basicConfig(level=logging.WARNING)

app = FastAPI()

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

class PredictionResponse(BaseModel):
    label: str
    accuracy: float

@app.post("/predict/", response_model=PredictionResponse)
async def predict(file: UploadFile = File(...)):
    contents = await file.read()
    image = Image.open(io.BytesIO(contents)).convert("RGB")
    
    image = transform(image)
    image = image.unsqueeze(0).to(device)

    with torch.no_grad():
        results = model(image, conf=0.25)
        predictions = results[0].boxes
        
        if len(predictions) > 0:
            best_prediction = predictions[0]
            label = int(best_prediction.cls.item())
            accuracy = float(best_prediction.conf.item())
        else:
            label = -1
            accuracy = 0.0
    
    label_text = label_map.get(label, "알 수 없음")

    return PredictionResponse(label=label_text, accuracy=accuracy)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
