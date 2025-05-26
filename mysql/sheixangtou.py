import cv2

url = "rtsp://admin:********@192.168.3.232:554/stream1"
cap = cv2.VideoCapture(url)

while True:
    ret, frame = cap.read()
    if ret:
        cv2.imshow("video", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
cap.release()