import sys
import fasttext
import re
from wordcloud import WordCloud
import boto3
import io
import uuid
import requests

# .env 파일에서 환경 변수 로드
load_dotenv()

s3_bucket_name = os.getenv('S3_BUCKET_NAME')
s3_region = os.getenv('S3_REGION')
s3_access_key = os.getenv('S3_ACCESS_KEY')
s3_secret_key = os.getenv('S3_SECRET_KEY')

# Python 스크립트의 첫 번째 인자는 사용자로부터 받은 키워드
keywords = sys.argv[1]

# 미리 훈련된 한국어 FastText 모델 로드
model = fasttext.load_model('model/cc.ko.300.bin')

# 유사한 단어 찾기
similar_words = model.get_nearest_neighbors(keywords, k=200)

# 한글 단어 필터링
filtered_words = [similar_word for _, similar_word in similar_words if re.match("^[가-힣]+$", similar_word)]

# 워드 클라우드를 생성하기 위한 단어들을 하나의 문자열로 결합
text = ' '.join(filtered_words)

# 워드 클라우드 생성
wordcloud = WordCloud(
    font_path='malgun.ttf',
    background_color='white',
    width=800,
    height=600
).generate(text)

buffer = io.BytesIO()
wordcloud.to_image().save(buffer, format='JPEG')
buffer.seek(0)

# S3 클라이언트 생성
s3_client = boto3.client(
    's3',
    region_name=s3_region,
    aws_access_key_id=s3_access_key,
    aws_secret_access_key=s3_secret_key
)

# UUID를 사용하여 고유한 파일 이름 생성
unique_id = uuid.uuid4()
s3_key = f'wordCloud/{unique_id}_wordcloud.jpg'

# S3에 업로드
s3_client.upload_fileobj(
    buffer,
    s3_bucket_name,
    s3_key,
    ExtraArgs={'ContentType': 'image/jpeg'}
)

# S3에 업로드된 파일 URL 생성
file_url = f'https://sorisonsoon.s3.{s3_region}.amazonaws.com/{s3_key}'

# Spring Boot API 엔드포인트
api_url = 'http://localhost:8080/api/v1/page/wordcloud'  # 실제 엔드포인트 URL로 변경

# API 호출
response = requests.post(api_url, json={'wordCloudUrl': file_url})

if response.status_code == 200:
    print("URL이 성공적으로 전송되었습니다.")
else:
    print("URL 전송에 실패했습니다.")
