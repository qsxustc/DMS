# -*- coding: utf-8 -*-

import paramiko
import os
import stat
# 判断sftp服务端中文件路径是否存在，若不存在则创建
def create_dir(sftp, remoteDir):
    try:
        if stat.S_ISDIR(sftp.stat(remoteDir).st_mode):  # 如果remoteDir存在且为目录，则返回True
            pass
    except Exception as e:
        sftp.mkdir(remoteDir)
        print("在远程sftp上创建目录：{}".format(remoteDir))
# 上传
def sftp_upload(sftp, localDir, remoteDir):
    if os.path.isdir(localDir):  # 判断本地localDir是否为目录
        for file in os.listdir(localDir):
            remoteDirTmp = os.path.join(remoteDir, file)
            localDirTmp = os.path.join(localDir, file)
            if os.path.isdir(localDirTmp):  # 如果本地localDirTmp为目录，则对远程sftp服务器进行判断
                create_dir(sftp, remoteDirTmp)  # 判断sftp服务端文件目录是否存在,若不存在则创建
            sftp_upload(sftp, localDirTmp, remoteDirTmp)
    else:
        print("upload file:", localDir)
        try:
            sftp.put(localDir, remoteDir)
        except Exception as e:
            print('upload error:', e)
if __name__ == '__main__':
    host = '39.105.215.78'  # sftp主机
    port = 22  # 端口
    username = 'root'  # sftp用户名
    password = '********'  # 密码
    localDir = '/Users/qsx/Desktop/DMS/mysql/videos/'  # 本地文件或目录
    remoteDir = '/home/dd/'  # 远程文件或目录（注意远程路径要存在）
    sf = paramiko.Transport((host, port))
    sf.connect(username=username, password=password)
    sftp = paramiko.SFTPClient.from_transport(sf)
    sftp_upload(sftp, localDir, remoteDir)
    sf.close()