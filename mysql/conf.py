import datetime

import pymysql

conn = pymysql.connect(host='39.105.215.78'  # 连接名称，默认127.0.0.1
                       , user='root'  # 用户名
                       , passwd='*****'  # 密码
                       , port=3306  # 端口，默认为3306
                       , db='DMS'  # 数据库名称
                       , charset='utf8'  # 字符编码
                       )
cur = conn.cursor()  # 生成游标对象
#
# time = str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
# abtype = "both_hands_leaving_wheel"
# videoid = int("1")
# userid = int("1")
# carid = int("1")
# remarks = "d"
# both_hands_leaving_wheel = str(11)
# eyes_closed = str(11)
# smoke = str(1)
# not_facing_front = str(1)
# no_face_mask = str(2)
# not_buckling_up = str(2)
# cellphone = str(3)
# yawning = str(4)
# head_lowered = str(5)
#
#
# id=int(11)
# name="ceshi"
# url=name
# md5=""
# size=int(1)
# ownerid=int(1)
# carid=int(1)
#
# sql2 = "insert into video(id,name,createtime,url,md5,size,ownerid,carid) values ('%s','%s','%s','%s','%s','%s','%s','%s');" % (
#     id, name, time, url, md5, size, ownerid, carid)

sql4="select * from abtype;"
data=cur.execute(sql4)


data=cur.fetchall()

bothhandsleavingwheel=data[0][2]
eyesclosed=data[1][2]
nofacemask=data[2][2]
notbucklingup=data[3][2]
smoke=data[4][2]
notfacingfront=data[5][2]
cellphone=data[6][2]
yawning=data[7][2]
head_lowered=data[8][2]
#print(data)


sql5="select mask from user where username='qsx'";
cur.execute(sql5)
data=cur.fetchall()
mask=data[0][0]


sql3="update user set mask= '%s' where username = 'qsx';"% (mask)

#sql = "INSERT into abb(time,abtype,videoid,userid,carid,remarks,both_hands_leaving_wheel,eyes_closed,smoke,not_facing_front,no_face_mask,not_buckling_up,cellphone,yawning,head_lowered) VALUES (%s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s, %s, %s);"
# 需要先获取到
#sql="""INSERT into abb(not_facing_front,no_face_mask，head_lowered) VALUES ( "%s", "%s", "%s"," %s", "%s", "%s");"""%(time, abtype, videoid, userid, carid, remarks, both_hands_leaving_wheel, eyes_closed, no_face_mask, not_buckling_up,smoke, not_facing_front, cellphone, yawning, head_lowered)
# SQL语句
try:
    cur.execute(sql3)
    conn.commit()
except Exception as e:
    conn.rollback()
    print(e)

