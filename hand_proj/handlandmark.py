import cv2
import mediapipe as mp
import numpy as np
import os

# Mediapipe 손 모듈 초기화
mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils

# 손 찾기 세부 설정
hands = mp_hands.Hands(
    max_num_hands=2,  # 탐지할 최대 손의 갯수
    min_detection_confidence=0.5,  # 표시할 손의 최소 정확도
    min_tracking_confidence=0.5  # 표시할 관찰의 최소 정확도
)

# 캡처된 이미지를 저장할 폴더 경로
output_folder = "hand_img"

# 폴더가 존재하지 않으면 생성
if not os.path.exists(output_folder):
    os.makedirs(output_folder)

def capture_and_detect():
    # 웹캠 초기화
    cap = cv2.VideoCapture(0)
    img_counter = 0

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            print("프레임을 가져올 수 없습니다.")
            break

        # 프레임을 BGR에서 RGB로 변환
        img_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        
        # 손 탐지
        result = hands.process(img_rgb)

        # 찾은 손 표시하기 및 랜드마크 좌표 추출
        if result.multi_hand_landmarks:
            for hand_landmarks in result.multi_hand_landmarks:
                mp_drawing.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)

        # 화면에 출력
        cv2.imshow('Webcam Feed', frame)

        # 키보드 입력 처리
        key = cv2.waitKey(1) & 0xFF
        if key == ord('q'):
            break
        elif key == ord('c'):
            # 'c' 키를 눌렀을 때 현재 프레임 캡처 및 손 랜드마크 추출
            if result.multi_hand_landmarks:
                for hand_landmarks in result.multi_hand_landmarks:
                    # 랜드마크 좌표 추출
                    landmarks = []
                    for lm in hand_landmarks.landmark:
                        landmarks.append([lm.x, lm.y, lm.z])
                    landmarks = np.array(landmarks).flatten()

                    # 좌표 출력
                    print("Hand Landmarks:", landmarks.tolist())

                    # 캡처한 이미지 저장
                    img_name = os.path.join(output_folder, f'captured_hand_{img_counter}.jpg')
                    cv2.imwrite(img_name, frame)
                    print(f"이미지 저장 완료: {img_name}")
                    img_counter += 1

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    capture_and_detect()
