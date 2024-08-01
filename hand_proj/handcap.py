import cv2
import os

# 캡처된 이미지를 저장할 폴더 경로
output_folder = "hand_img_cap"

# 폴더가 존재하지 않으면 생성
if not os.path.exists(output_folder):
    os.makedirs(output_folder)

def capture_and_save():
    # 웹캠 초기화
    cap = cv2.VideoCapture(0)
    img_counter = 0

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            print("프레임을 가져올 수 없습니다.")
            break

        # 화면에 출력
        cv2.imshow('Webcam Feed', frame)

        # 키보드 입력 처리
        key = cv2.waitKey(1) & 0xFF
        if key == ord('q'):
            break
        elif key == ord('c'):
            # 'c' 키를 눌렀을 때 현재 프레임 캡처
            img_name = os.path.join(output_folder, f'captured_frame_{img_counter}.jpg')
            cv2.imwrite(img_name, frame)
            print(f"이미지 저장 완료: {img_name}")
            img_counter += 1

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    capture_and_save()




# # 타이머 기능 추가
# import cv2
# import os
# import time

# # 캡처된 이미지를 저장할 폴더 경로
# output_folder = "hand_img_cap"

# # 폴더가 존재하지 않으면 생성
# if not os.path.exists(output_folder):
#     os.makedirs(output_folder)

# def capture_and_save(interval=5):
#     # 웹캠 초기화
#     cap = cv2.VideoCapture(0)
#     img_counter = 0

#     last_capture_time = time.time()

#     while cap.isOpened():
#         ret, frame = cap.read()
#         if not ret:
#             print("프레임을 가져올 수 없습니다.")
#             break

#         # 화면에 출력
#         cv2.imshow('Webcam Feed', frame)

#         # 키보드 입력 처리
#         key = cv2.waitKey(1) & 0xFF
#         if key == ord('q'):
#             break
#         elif key == ord('c') or (time.time() - last_capture_time) >= interval:
#             # 'c' 키를 눌렀거나 타이머가 만료된 경우 현재 프레임 캡처
#             img_name = os.path.join(output_folder, f'captured_frame_{img_counter}.jpg')
#             cv2.imwrite(img_name, frame)
#             print(f"이미지 저장 완료: {img_name}")
#             img_counter += 1
#             last_capture_time = time.time()  # 타이머 리셋

#     cap.release()
#     cv2.destroyAllWindows()

# if __name__ == "__main__":
#     capture_and_save(interval=5)  # 5초 간격으로 프레임 캡처
