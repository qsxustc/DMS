import base64
import json

import cv2
import requests

from PIL import Image
from io import BytesIO


def draw_text(img, point, text, drawType="custom"):
    '''
    :param img:
    :param point:
    :param text:
    :param drawType: custom or custom
    :return:
    '''
    fontScale = 0.4
    thickness = 5
    text_thickness = 1
    bg_color = (255, 0, 0)
    fontFace = cv2.FONT_HERSHEY_SIMPLEX
    # fontFace=cv2.FONT_HERSHEY_SIMPLEX
    if drawType == "custom":
        text_size, baseline = cv2.getTextSize(str(text), fontFace, fontScale, thickness)
        text_loc = (point[0], point[1] + text_size[1])
        cv2.rectangle(img, (text_loc[0] - 2 // 2, text_loc[1] - 2 - baseline),
                      (text_loc[0] + text_size[0], text_loc[1] + text_size[1]), bg_color, -1)
        # draw score value
        cv2.putText(img, str(text), (text_loc[0], text_loc[1] + baseline), fontFace, fontScale,
                    (255, 255, 255), text_thickness, 8)
    elif drawType == "simple":
        cv2.putText(img, '%d' % (text), point, fontFace, 0.5, (255, 0, 0))
    return img


def draw_text_line(img, point, text_line: str, drawType="custom"):
    '''
    :param img:
    :param point:
    :param text:
    :param drawType: custom or custom
    :return:
    '''
    fontScale = 0.4
    thickness = 5
    fontFace = cv2.FONT_HERSHEY_SIMPLEX
    # fontFace=cv2.FONT_HERSHEY_SIMPLEX
    text_line = text_line.split("\n")
    # text_size, baseline = cv2.getTextSize(str(text_line), fontFace, fontScale, thickness)
    text_size, baseline = cv2.getTextSize(str(text_line), fontFace, fontScale, thickness)
    for i, text in enumerate(text_line):
        if text:
            draw_point = [point[0], point[1] + (text_size[1] + 2 + baseline) * i]
            img = draw_text(img, draw_point, text, drawType)
    return img


def frame2base64(frame):
    img = Image.fromarray(frame)  # 将每一帧转为Image
    output_buffer = BytesIO()  # 创建一个BytesIO
    img.save(output_buffer, format='JPEG')  # 写入output_buffer
    byte_data = output_buffer.getvalue()  # 在内存读取
    base64_data = base64.b64encode(byte_data)  # 转为BASE64
    return base64_data  # 转码成功 返回base64编码


def video_demo():
    cap = cv2.VideoCapture(1)  # 电脑自身摄像头
    i = 0  # 定时装置初始值
    fourcc = cv2.VideoWriter_fourcc(*"mp4v")
    out = cv2.VideoWriter("dd.mp4", fourcc, 20.0, (1280, 720))
    request_url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior"
    while True:
        i = i + 1
        reg, frame = cap.read()
        frame = cv2.flip(frame, 1)  # 图片左右调换
        cv2.imshow('window', frame)
        img = frame2base64(frame)
        params = {"image": img}
        access_token = '24.8a9c3d5ec3915542c7c3f62fca3acc62.2592000.1683857383.282335-32289721'
        request_url = request_url + "?access_token=" + access_token
        headers = {'content-type': 'application/x-www-form-urlencoded'}
        response = requests.post(request_url, data=params, headers=headers)
        if i > 5:
            break
        if response:
            res = response.json()
            print(json.dumps(res, indent=3))  # 格式化输出
            #print('人数:', res['person_num'])
            per=res["person_info"][0]
            attributes = per["attributes"]
            result=""
            warning=""

            # 1双手离开方向盘
            both_hands_leaving_wheel_score = attributes['both_hands_leaving_wheel']['score']
            both_hands_leaving_wheel_threshold = attributes['both_hands_leaving_wheel']['threshold']
            if both_hands_leaving_wheel_score > both_hands_leaving_wheel_threshold:
                warning = warning + '双手离开方向盘 '
            result = result + ('both_hands_leaving_wheel_score: {:.5f} \n'.format(both_hands_leaving_wheel_score))

            # 2闭眼
            eyes_closed_score = attributes['eyes_closed']['score']
            eyes_closed_threshold = attributes['eyes_closed']['threshold']
            if eyes_closed_score > eyes_closed_threshold:
                warning = warning + '闭眼 '
            result = result + ('eyes_closed_score: {:.5f} \n'.format(eyes_closed_score))

            # 不戴口罩
            no_face_mask_score = attributes['no_face_mask']['score']
            no_face_mask_threshold = attributes['no_face_mask']['threshold']
            if no_face_mask_score > no_face_mask_threshold:
                warning = warning + '不戴口罩 '
            result = result + ('no_face_mask_score: {:.5f} \n'.format(no_face_mask_score))

            # 未系安全带
            not_buckling_up_score = attributes['not_buckling_up']['score']
            not_buckling_up_threshold = attributes['not_buckling_up']['threshold']
            if not_buckling_up_score > not_buckling_up_threshold:
                warning = warning + '未系安全带 '
            result = result + ('not_buckling_up_score: {:.5f} \n'.format(not_buckling_up_score))

            # 抽烟
            smoke_score = attributes['smoke']['score']
            smoke_threshold = attributes['smoke']['threshold']
            if smoke_score > smoke_threshold:
                warning = warning + '抽烟 '
            result = result + ('smoke_score: {:.5f} \n'.format(smoke_score))

            # 不看前方
            not_facing_front_score = attributes['not_facing_front']['score']
            not_facing_front_threshold = attributes['not_facing_front']['threshold']
            if not_facing_front_score > not_facing_front_threshold:
                warning = warning + '不看前方 '
            result = result + ('not_facing_front_score: {:.5f} \n'.format(not_facing_front_threshold))

            # 使用手机
            cellphone_score = attributes['cellphone']['score']
            cellphone_threshold = attributes['cellphone']['threshold']
            if cellphone_score > cellphone_threshold:
                warning = warning + '使用手机 '
            result = result + ('cellphone_score: {:.5f} \n'.format(cellphone_score))

            # 打哈欠
            yawning_score = attributes['yawning']['score']
            yawning_threshold = attributes['yawning']['threshold']
            if yawning_score > yawning_threshold:
                warning = warning + '打哈欠 '
            result = result + ('yawning_score: {:.5f} \n'.format(yawning_score))
            # 低头
            head_lowered_score = attributes['head_lowered']['score']
            head_lowered_threshold = attributes['head_lowered']['threshold']
            if head_lowered_score > head_lowered_threshold:
                warning = warning + '低头 '
            result = result + ('head_lowered_score: {:.5f} \n'.format(head_lowered_score))

            gesture = per['location']
            cv2.rectangle(frame, (gesture['left'], gesture['top']), (gesture['left'] + gesture['width'],
                                                                   gesture['top'] + gesture['height']), (0, 0, 255),
                          1)
            #cv2.imshow("image", img)
            #print(result)
            # 图片 添加的文字 位置 字体 字体大小 字体颜色 字体粗细
            #cv2.putText(frame, result, (50, 50), cv2.FONT_HERSHEY_PLAIN, 2, (100, 200, 200), 3)
            image2 = draw_text_line(frame, (50, 50), result)
            out.write(frame)

            #cv2.imwrite("ceshi.png", image2)
        if cv2.waitKey(1) & 0xff == ord('q'):
            break
    # 释放资源
    cap.release()
    out.release()


video_demo()
cv2.destroyAllWindows()
