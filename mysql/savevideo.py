import cv2
def save_video(path):
    url = "rtsp://admin:********@192.168.3.233:554/stream1"
    url = "rtsp://admin:********@10.153.3.151:554/stream1"
    cap = cv2.VideoCapture("shujuji.mp4")
    # cap = cv2.VideoCapture()
    # 定义编解码器并创建VideoWriter对象
    camera_width = cap.get(cv2.CAP_PROP_FRAME_WIDTH)
    camera_height = cap.get(cv2.CAP_PROP_FRAME_HEIGHT)
    video_width = int(camera_width)
    video_height = int(camera_height)
    # 设置相机宽度
    cap.set(cv2.CAP_PROP_FRAME_WIDTH,video_width)
    # 设置相机高度
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT,video_height)
    # 设置视频编码，帧率，宽高
    fourcc = cv2.VideoWriter_fourcc('X','2','6','4')
    out = cv2.VideoWriter(path, fourcc,30,(video_width,video_height))
    i=1
    while cap.isOpened():
        i=i+1
        ret, frame = cap.read()
        if not ret:
            break
        if i<300:
            continue
        if i>500:
            break
        # frame = cv2.flip(frame,1)
        # 写翻转的框架
        out.write(frame)
        cv2.imshow('frame', frame)
        if cv2.waitKey(1) == ord('q'):
            break
    # 完成工作后释放所有内容
    cap.release()
    out.release()
    cv2.destroyAllWindows()

save_video("ceshi.mp4")