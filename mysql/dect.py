import base64
import json
import datetime
import cv2
import time
import requests
import pymysql
import os
from PIL import Image
from io import BytesIO
import pyttsx3
from pathlib import Path

frameRate = 5  # 帧数截取间隔（每隔frameRate帧截取一帧）
maxframenum = 100  # 每个视频的帧数
driver_name = "武一行"
maxabthres = 1
userid = int(15)
carid = int(1)
username = 'driver'

def draw_text(img, point, text, drawType="custom"):
    fontScale = 0.5  # 字体大小
    thickness = 5
    text_thickness = 1
    bg_color = (255, 0, 0)
    fontFace = cv2.FONT_HERSHEY_SIMPLEX
    if drawType == "custom":
        text_size, baseline = cv2.getTextSize(str(text), fontFace, fontScale, thickness)
        text_loc = (point[0], point[1] + text_size[1])
        cv2.rectangle(img, (text_loc[0] - 2 // 2, text_loc[1] - 2 - baseline),
                      (text_loc[0] + text_size[0], text_loc[1] + text_size[1]), bg_color, -1)
        # draw score value
        cv2.putText(img, str(text), (text_loc[0], text_loc[1] + baseline), fontFace, fontScale,
                    (255, 255, 255), text_thickness, 8)
    elif drawType == "simple":
        # 图片 添加的文字 位置 字体 字体大小 字体颜色 字体粗细
        cv2.putText(img, '%d' % (text), point, fontFace, fontScale, (255, 0, 0))
    return img

def draw_text_line(img, point, text_line: str, drawType="custom"):
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

def video_demo(path1, path2, videoid):
    global carid
    global userid
    global username
    video_path = path1
    request_url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior"
    # cap = cv2.VideoCapture(video_path)
    url = "rtsp://admin:********@192.168.3.233:554/stream1"
    url = "rtsp://admin:********@10.153.3.151:554/stream1"
    cap = cv2.VideoCapture(url)
    camera_width = cap.get(cv2.CAP_PROP_FRAME_WIDTH)
    camera_height = cap.get(cv2.CAP_PROP_FRAME_HEIGHT)
    video_width = int(camera_width)
    video_height = int(camera_height)
    # 设置相机宽度
    cap.set(cv2.CAP_PROP_FRAME_WIDTH, video_width)
    # 设置相机高度
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, video_height)
    # 设置视频编码，帧率，宽高
    fourcc = cv2.VideoWriter_fourcc('X', '2', '6', '4')
    out = cv2.VideoWriter(str(path2), fourcc, 30, (video_width, video_height))
    c = 0
    saveflag = False
    sqlflag = False
    warn = ''
    bothhandsleavingwheelflag = False
    eyesclosedflag = False
    nofacemaskflag = False
    notbucklingupflag = False
    smokeflag = False
    notfacingfrontflag = False
    cellphoneflag = False
    yawningflag = False
    head_loweredflag = False
    warning = ""
    a = [0, 0, 0, 0, 0, 0, 0, 0, 0]
    abtype = ["bothhandsleavingwheel",
              "eyesclosed",
              "nofacemask",
              "notbucklingup",
              "smoke",
              "notfacingfront",
              "cellphone",
              "yawning",
              "head_lowered"]
    b = [0, 0, 0, 0, 0, 0, 0, 0, 0]
    while cap.isOpened():
        reg, frame = cap.read()
        c = c + 1
        if reg:
            if (c % frameRate == 0):
                # frame = cv2.flip(frame, 1)  # 图片左右调换
                # cv2.imshow('window', frame)
                img = frame2base64(frame)
                params = {"image": img}
                access_token = '24.8a9c3d5ec3915542c7c3f62fca3acc62.2592000.1683857383.282335-32289721'
                request_url = request_url + "?access_token=" + access_token
                headers = {'content-type': 'application/x-www-form-urlencoded', 'Connection': 'close'}
                response = requests.post(request_url, data=params, headers=headers)
                if response:
                    res = response.json()
                    print(json.dumps(res, indent=3))  # 格式化输出
                    try:
                        per = res["person_info"][0]
                        attributes = per["attributes"]
                    except Exception as e:
                        print("当前摄像头检测区域未出现驾驶员")
                        zuihou = 1
                        continue
                    result = ""
                    max = 0
                    maxtype = ""
                    # 1双手离开方向盘
                    both_hands_leaving_wheel_score = attributes['both_hands_leaving_wheel']['score']
                    both_hands_leaving_wheel_threshold = attributes['both_hands_leaving_wheel']['threshold']
                    if both_hands_leaving_wheel_score > both_hands_leaving_wheel_threshold:
                        a[0] = a[0] + 1
                        if bothhandsleavingwheelflag == False:
                            warning = warning + '双手离开方向盘 '
                            bothhandsleavingwheelflag = True
                        if both_hands_leaving_wheel_score > max:
                            max = both_hands_leaving_wheel_score
                            maxtype = "bothhandsleavingwheel"
                    result = result + (
                        'both_hands_leaving_wheel_score: {:.5f} \n'.format(both_hands_leaving_wheel_score))
                    # 2闭眼
                    eyes_closed_score = attributes['eyes_closed']['score']
                    eyes_closed_threshold = attributes['eyes_closed']['threshold']
                    if eyes_closed_score > eyes_closed_threshold:
                        a[1] = a[1] + 1
                        if eyesclosedflag == False:
                            warning = warning + '闭眼 '
                            eyesclosedflag = True
                        if eyes_closed_score > max:
                            max = eyes_closed_score
                            maxtype = "eyesclosed"
                    result = result + ('eyes_closed_score: {:.5f} \n'.format(eyes_closed_score))
                    # 不戴口罩
                    no_face_mask_score = attributes['no_face_mask']['score']
                    no_face_mask_threshold = attributes['no_face_mask']['threshold']
                    if no_face_mask_score > no_face_mask_threshold:
                        a[2] = a[2] + 1
                        # warning = warning + '不戴口罩 '
                        if nofacemaskflag == False:
                            warning = warning + '不戴口罩 '
                            nofacemaskflag = True
                        if no_face_mask_score > max:
                            max = no_face_mask_score
                            maxtype = "nofacemask"
                    result = result + ('no_face_mask_score: {:.5f} \n'.format(no_face_mask_score))
                    # 未系安全带
                    not_buckling_up_score = attributes['not_buckling_up']['score']
                    not_buckling_up_threshold = attributes['not_buckling_up']['threshold']
                    if not_buckling_up_score > not_buckling_up_threshold:
                        a[3] = a[3] + 1
                        # warning = warning + '未系安全带 '
                        if notbucklingupflag == False:
                            warning = warning + '未系安全带 '
                            notbucklingupflag = True
                        if not_buckling_up_score > max:
                            max = not_buckling_up_score
                            maxtype = "notbucklingup"
                    result = result + ('not_buckling_up_score: {:.5f} \n'.format(not_buckling_up_score))
                    # 抽烟
                    smoke_score = attributes['smoke']['score']
                    smoke_threshold = attributes['smoke']['threshold']
                    if smoke_score > smoke_threshold:
                        # warning = warning + '抽烟 '
                        a[4] = a[4] + 1
                        if smokeflag == False:
                            warning = warning + '抽烟 '
                            smokeflag = True
                        if smoke_score > max:
                            max = smoke_score
                            maxtype = "smoke"
                    result = result + ('smoke_score: {:.5f} \n'.format(smoke_score))
                    # 不看前方
                    not_facing_front_score = attributes['not_facing_front']['score']
                    not_facing_front_threshold = attributes['not_facing_front']['threshold']
                    if not_facing_front_score > not_facing_front_threshold:
                        # warning = warning + '不看前方 '
                        a[5] = a[5] + 1
                        if notfacingfrontflag == False:
                            warning = warning + '不看前方 '
                            notfacingfrontflag = True
                        if not_facing_front_score > max:
                            max = not_facing_front_score
                            maxtype = "notfacingfront"
                    result = result + ('not_facing_front_score: {:.5f} \n'.format(not_facing_front_threshold))
                    # 使用手机
                    cellphone_score = attributes['cellphone']['score']
                    cellphone_threshold = attributes['cellphone']['threshold']
                    if cellphone_score > cellphone_threshold:
                        # warning = warning + '使用手机 '
                        a[6] = a[6] + 1
                        if cellphoneflag == False:
                            warning = warning + '使用手机 '
                            cellphoneflag = True
                        if cellphone_score > max:
                            max = cellphone_score
                            maxtype = "cellphone"
                    result = result + ('cellphone_score: {:.5f} \n'.format(cellphone_score))
                    # 打哈欠
                    yawning_score = attributes['yawning']['score']
                    yawning_threshold = attributes['yawning']['threshold']
                    if yawning_score > yawning_threshold:
                        a[7] = a[7] + 1
                        # warning = warning + '打哈欠 '
                        if yawningflag == False:
                            warning = warning + '打哈欠 '
                            yawningflag = True
                        if yawning_score > max:
                            max = yawning_score
                            maxtype = "yawning"
                    result = result + ('yawning_score: {:.5f} \n'.format(yawning_score))
                    # 低头
                    head_lowered_score = attributes['head_lowered']['score']
                    head_lowered_threshold = attributes['head_lowered']['threshold']
                    if head_lowered_score > head_lowered_threshold:
                        # warning = warning + '低头 '
                        a[8] = a[8] + 1
                        if head_loweredflag == False:
                            warning = warning + '低头 '
                            head_loweredflag = True
                        if head_lowered_score > max:
                            max = head_lowered_score
                            maxtype = "headlowered"
                    result = result + ('head_lowered_score: {:.5f} \n'.format(head_lowered_score))
                    gesture = per['location']
                    cv2.rectangle(frame, (gesture['left'], gesture['top']), (gesture['left'] + gesture['width'],
                                                                             gesture['top'] + gesture['height']),
                                  (0, 0, 255),
                                  1)
                    print(warning)
                    warn = warning
                    # 图片 添加的文字 位置 字体 字体大小 字体颜色 字体粗细
                    # cv2.putText(frame, result, (50, 50), cv2.FONT_HERSHEY_PLAIN, 2, (100, 200, 200), 3)
                    image2 = draw_text_line(frame, (50, 50), result)
                    # 判断是否存在异常行为 如果存在异常行为 那就存储其中的一次数据到数据库中 同时存储数据到
                    if (max > 0.5):
                        if saveflag == False:
                            saveflag = True  # 如果最终为False 则删除视频
                        if sqlflag == False:
                            # 修正mask值
                            sqlflag = True  # 防止数据多次存储
                            time = str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
                            abtype = maxtype
                            videoid = int(videoid)
                            # 默认userid 以及carID 固定摄像头
                            remarks = warning
                            both_hands_leaving_wheel = str(both_hands_leaving_wheel_score)
                            eyes_closed = str(eyes_closed_score)
                            smoke = str(smoke_score)
                            not_facing_front = str(not_facing_front_score)
                            no_face_mask = str(no_face_mask_score)
                            not_buckling_up = str(not_buckling_up_score)
                            cellphone = str(cellphone_score)
                            yawning = str(yawning_score)
                            head_lowered = str(head_lowered_score)
                            conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                                                   , user='root'  # 用户名
                                                   , passwd='********'  # 密码
                                                   , port=3306  # 端口，默认为3306
                                                   , db='DMS'  # 数据库名称
                                                   , charset='utf8'  # 字符编码
                                                   )
                            cur = conn.cursor()  # 生成游标对象
                            sql = """INSERT into abb(time,abtype,videoid,userid,carid,remarks,bothhandsleavingwheel,eyesclosed,smoke,notfacingfront,nofacemask,notbucklingup,cellphone,yawning,headlowered) VALUES ("%s", "%s", "%s", "%s", "%s", "%s","%s", "%s", "%s", "%s", "%s", "%s"," %s", "%s", "%s");""" % (
                                time, abtype, videoid, userid, carid, remarks, both_hands_leaving_wheel, eyes_closed,
                                no_face_mask,
                                not_buckling_up, smoke, not_facing_front, cellphone, yawning, head_lowered)
                            # SQL语句
                            try:
                                cur.execute(sql)
                                print("存储异常信息到数据库中")
                                conn.commit()
                            except Exception as e:
                                conn.rollback()
                                print(e)
                            cur.close()  # 关闭游标
                            conn.close()  # 关闭连
                    out.write(frame)
        else:
            break
        if c > maxframenum:
            break
    # 释放资源
    cap.release()
    out.release()


    conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                           , user='root'  # 用户名
                           , passwd='********'  # 密码
                           , port=3306  # 端口，默认为3306
                           , db='DMS'  # 数据库名称
                           , charset='utf8'  # 字符编码
                           )
    cur = conn.cursor()  # 生成游标对象
    sql5 = "select mask from user where username='%s'" % (username);
    cur.execute(sql5)
    data = cur.fetchall()
    mask = data[0][0]
    conn.commit()
    cur.close()  # 关闭游标
    conn.close()  # 关闭连接

    conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                           , user='root'  # 用户名
                           , passwd='********'  # 密码
                           , port=3306  # 端口，默认为3306
                           , db='DMS'  # 数据库名称
                           , charset='utf8'  # 字符编码
                           )
    cur = conn.cursor()  # 生成游标对象
    sql4 = "select * from abtype;"
    data = cur.execute(sql4)
    data = cur.fetchall()
    bothhandsleavingwheel = data[0][2]
    eyesclosed = data[1][2]
    nofacemask = data[2][2]
    notbucklingup = data[3][2]
    smoke = data[4][2]
    notfacingfront = data[5][2]
    cellphone = data[6][2]
    yawning = data[7][2]
    head_lowered = data[8][2]
    conn.commit()
    cur.close()  # 关闭游标
    conn.close()  # 关闭连接

    maskkou = 0
    for i in range(0, len(a)):
        if a[i] > maxabthres:
            maskkou += int(data[i][2])
    mask -= maskkou
    # if (maxtype[0] == "bothhandsleavingwheel"):
    #     mask -= bothhandsleavingwheel
    # if (maxtype[1] == "eyesclosed"):
    #     mask -= eyesclosed
    # if (maxtype[2] == "nofacemask"):
    #     mask -= nofacemask
    # if (maxtype[3] == "notbucklingup"):
    #     mask -= notbucklingup
    # if (maxtype[4] == "smoke"):
    #     mask -= smoke
    # if (maxtype[5] == "notfacingfront"):
    #     mask -= notfacingfront
    # if (maxtype[6] == "cellphone"):
    #     mask -= cellphone
    # if (maxtype[7] == "yawning"):
    #     mask -= yawning
    # if (maxtype[8] == "head_lowered"):
    #     mask -= head_lowered
    conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                           , user='root'  # 用户名
                           , passwd='********'  # 密码
                           , port=3306  # 端口，默认为3306
                           , db='DMS'  # 数据库名称
                           , charset='utf8'  # 字符编码
                           )
    cur = conn.cursor()  # 生成游标对象

    sql10 = "update abb set mask = '%s',remarks= '%s' where videoid = '%s';" % (maskkou, warn, videoid)
    cur.execute(sql10)
    print("扣分更新完成，目前扣分为" + str(maskkou))

    sql3 = "update user set mask= '%s' where username = '%s';" % (mask, username)
    cur.execute(sql3)
    print("分数更新完成，目前得分为" + str(mask))
    conn.commit()
    cur.close()  # 关闭游标
    conn.close()  # 关闭连接
    if saveflag == False:
        print("删除文件" + video_path)
        # os.remove("demo.txt")
    else:
        conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                               , user='root'  # 用户名
                               , passwd='********'  # 密码
                               , port=3306  # 端口，默认为3306
                               , db='DMS'  # 数据库名称
                               , charset='utf8'  # 字符编码
                               )
        cur = conn.cursor()  # 生成游标对象
        time = str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
        id = int(videoid)
        name = "res" + str(videoid) + ".mp4"
        url = "res" + str(videoid) + ".mp4"
        md5 = ""
        size = int(os.path.getsize(path2))
        ownerid = int(1)
        carid = int(1)
        sql2 = "insert into video(id,name,createtime,url,md5,size,ownerid,carid) values ('%s','%s','%s','%s','%s','%s','%s','%s');" % (
            id, name, time, url, md5, size, ownerid, carid)
        try:
            cur.execute(sql2)
            print("视频保存完成")
            conn.commit()
            engine = pyttsx3.init()
            msg = "驾驶员" + driver_name + "你好，系统检测您" + warning + "请立刻纠正"
            engine.say(msg)
            engine.runAndWait()
        except Exception as e:
            conn.rollback()
            print(e)

def main():
    videoid = 8000
    path1 = "./videos/resyuan" + str(videoid) + ".mp4"
    path2 = "./videos/res" + str(videoid) + ".mp4"
    my_file = Path(path2)
    while my_file.is_file():
        # 指定的文件存在
        videoid = videoid + 1
        path2 = "./videos/res" + str(videoid) + ".mp4"
        my_file = Path(path2)
    video_demo(path1, path2, videoid)
    cv2.destroyAllWindows()
main()