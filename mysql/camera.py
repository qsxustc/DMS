import cv2
def video_demo():
    cap = cv2.VideoCapture(0)  # 电脑自身摄像头
    i = 0  # 定时装置初始值
    photoname = 1  # 文件名序号初始值
    while True:
        i = i + 1
        reg, frame = cap.read()
        frame = cv2.flip(frame, 1)  # 图片左右调换
        cv2.imshow('window', frame)
        if i == 3:  # 定时装置，定时截屏，可以修改。等多少秒
            filename = str(photoname) + '.png'  # filename为图像名字，将photoname作为编号命名保存的截图
            cv2.imwrite('/Users/qsx/Desktop/DMS/mysql/files/test/' + filename, frame)  # 截图 前面为放在桌面的路径 frame为此时的图像
            print(filename + '保存成功')  # 打印保存成功
            i = 0  # 清零

            photoname = photoname + 1
            if photoname >= 10:  # 最多截图20张 然后退出（如果调用photoname = 1 不用break为不断覆盖图片）
                # photoname = 1
                break
        if cv2.waitKey(1) & 0xff == ord('q'):
            break
    # 释放资源
    cap.release()

video_demo()
cv2.destroyAllWindows()
