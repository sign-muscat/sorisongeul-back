#@markdown We implemented some functions to visualize the hand landmark detection results. <br/> Run the following cell to activate the functions.

from mediapipe import solutions
from mediapipe.framework.formats import landmark_pb2
import numpy as np

MARGIN = 10  # pixels
FONT_SIZE = 1
FONT_THICKNESS = 1
HANDEDNESS_TEXT_COLOR = (88, 205, 54) # vibrant green

def draw_landmarks_on_image(rgb_image, detection_result):
  hand_landmarks_list = detection_result.hand_landmarks
  handedness_list = detection_result.handedness
  annotated_image = np.copy(rgb_image)

  # Loop through the detected hands to visualize.
  for idx in range(len(hand_landmarks_list)):
    hand_landmarks = hand_landmarks_list[idx]
    handedness = handedness_list[idx]

    # Draw the hand landmarks.
    hand_landmarks_proto = landmark_pb2.NormalizedLandmarkList()
    hand_landmarks_proto.landmark.extend([
      landmark_pb2.NormalizedLandmark(x=landmark.x, y=landmark.y, z=landmark.z) for landmark in hand_landmarks
    ])
    solutions.drawing_utils.draw_landmarks(
      annotated_image,
      hand_landmarks_proto,
      solutions.hands.HAND_CONNECTIONS,
      solutions.drawing_styles.get_default_hand_landmarks_style(),
      solutions.drawing_styles.get_default_hand_connections_style())

    # Get the top left corner of the detected hand's bounding box.
    height, width, _ = annotated_image.shape
    x_coordinates = [landmark.x for landmark in hand_landmarks]
    y_coordinates = [landmark.y for landmark in hand_landmarks]
    text_x = int(min(x_coordinates) * width)
    text_y = int(min(y_coordinates) * height) - MARGIN

    # Draw handedness (left or right hand) on the image.
    cv2.putText(annotated_image, f"{handedness[0].category_name}",
                (text_x, text_y), cv2.FONT_HERSHEY_DUPLEX,
                FONT_SIZE, HANDEDNESS_TEXT_COLOR, FONT_THICKNESS, cv2.LINE_AA)

  return annotated_image



# STEP 1: Import the necessary modules.
import mediapipe as mp
from mediapipe.tasks import python
from mediapipe.tasks.python import vision

# STEP 2: Create an HandLandmarker object.
base_options = python.BaseOptions(model_asset_path='models\hand_landmarker (1).task')
options = vision.HandLandmarkerOptions(base_options=base_options,
                                       num_hands=2)
detector = vision.HandLandmarker.create_from_options(options)

# STEP 3: Load the input image.
image = mp.Image.create_from_file("images\hand.jpg")

# STEP 4: Detect hand landmarks from the input image.
detection_result = detector.detect(image)

# STEP 5: Process the classification result. In this case, visualize it.
annotated_image = draw_landmarks_on_image(image.numpy_view(), detection_result)
cv2_imshow(cv2.cvtColor(annotated_image, cv2.COLOR_RGB2BGR))






# # 웹 캠으로 여는 법
# import cv2
# import numpy as np
# from mediapipe import solutions
# from mediapipe.framework.formats import landmark_pb2
# from mediapipe.tasks import python
# from mediapipe.tasks.python import vision

# # 상수 정의
# MARGIN = 10  # 픽셀
# FONT_SIZE = 1
# FONT_THICKNESS = 1
# HANDEDNESS_TEXT_COLOR = (88, 205, 54)  # 선명한 초록색

# # 손 랜드마크를 이미지에 그리는 함수
# def draw_landmarks_on_image(rgb_image, detection_result):
#     hand_landmarks_list = detection_result.hand_landmarks
#     handedness_list = detection_result.handedness
#     annotated_image = np.copy(rgb_image)

#     # 인식된 손을 루프 돌며 시각화
#     for idx in range(len(hand_landmarks_list)):
#         hand_landmarks = hand_landmarks_list[idx]
#         handedness = handedness_list[idx]

#         # 손 랜드마크 그리기
#         hand_landmarks_proto = landmark_pb2.NormalizedLandmarkList()
#         hand_landmarks_proto.landmark.extend([
#             landmark_pb2.NormalizedLandmark(x=landmark.x, y=landmark.y, z=landmark.z) for landmark in hand_landmarks
#         ])
#         solutions.drawing_utils.draw_landmarks(
#             annotated_image,
#             hand_landmarks_proto,
#             solutions.hands.HAND_CONNECTIONS,
#             solutions.drawing_styles.get_default_hand_landmarks_style(),
#             solutions.drawing_styles.get_default_hand_connections_style())

#         # 손의 바운딩 박스 좌상단 좌표 구하기
#         height, width, _ = annotated_image.shape
#         x_coordinates = [landmark.x for landmark in hand_landmarks]
#         y_coordinates = [landmark.y for landmark in hand_landmarks]
#         text_x = int(min(x_coordinates) * width)
#         text_y = int(min(y_coordinates) * height) - MARGIN

#         # 이미지에 왼손 또는 오른손 여부 텍스트로 그리기
#         cv2.putText(annotated_image, f"{handedness[0].category_name}",
#                     (text_x, text_y), cv2.FONT_HERSHEY_DUPLEX,
#                     FONT_SIZE, HANDEDNESS_TEXT_COLOR, FONT_THICKNESS, cv2.LINE_AA)

#     return annotated_image

# # HandLandmarker 초기화
# model_path = "/mnt/data/hand_landmarker (1).task"
# base_options = python.BaseOptions(model_asset_path=model_path)
# options = vision.HandLandmarkerOptions(base_options=base_options, num_hands=2)
# detector = vision.HandLandmarker.create_from_options(options)

# # 웹캠 열기
# cap = cv2.VideoCapture(0)

# while cap.isOpened():
#     success, frame = cap.read()
#     if not success:
#         break

#     # 프레임을 RGB로 변환
#     rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

#     # 프레임에서 MediaPipe 이미지 생성
#     mp_image = mp.Image(image_format=mp.ImageFormat.SRGB, data=rgb_frame)

#     # 프레임에서 손 랜드마크 인식
#     detection_result = detector.detect(mp_image)

#     # 손 랜드마크가 그려진 프레임
#     annotated_frame = draw_landmarks_on_image(rgb_frame, detection_result)

#     # 주석이 달린 프레임 표시
#     cv2.imshow('Hand Landmarker', cv2.cvtColor(annotated_frame, cv2.COLOR_RGB2BGR))

#     if cv2.waitKey(1) & 0xFF == 27:  # 'ESC' 키를 누르면 종료
#         break

# cap.release()
# cv2.destroyAllWindows()
