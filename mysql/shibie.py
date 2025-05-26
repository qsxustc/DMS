import requests
import base64
import json

from driver_result import driver_result

filename='/Users/qsx/Desktop/DMS/mysql/files/test/2.png'
resultfilename='/Users/qsx/Desktop/DMS/mysql/files/test/2res.jpeg'
fontsize=20
def driver_behavior(filename,resultfilename):
    request_url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior"
    # 二进制方式打开图片文件
    f = open(filename, 'rb')
    img = base64.b64encode(f.read())
    params = {"image": img}
    access_token = '24.8a9c3d5ec3915542c7c3f62fca3acc62.2592000.1683857383.282335-32289721'
    request_url = request_url + "?access_token=" + access_token
    headers = {'content-type': 'application/x-www-form-urlencoded'}
    response = requests.post(request_url, data=params, headers=headers)
    if response:
        # print(response.json())
        res = response.json()
        print(json.dumps(res, indent=3))  # 格式化输出
        print('人数:', res['person_num'])
        # print(data)
        result = res['person_info']
        driver_result(filename, result, resultfilename, 20, "blue")

driver_behavior(filename,resultfilename)


