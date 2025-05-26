# DMS
驾驶员异常行为管理系统

本文件夹是源代码文件夹，设计三个部分

src：本文件夹中涉及的是后端部分的代码，也就是基于springboot开发的后端服务器，可以找到SpringBoot Application进行启动，中间需要加载meaven，可以查看pom.xml确定需要的依赖项，如果运行需要加载。java jdk可以选择java 1.8，java 17，java 18.需要调整数据库以及相关application.yml文件内容。

Vue:本文件是前端界面的开发源码，启动时需要进入本文件夹，然后终端输入npm run serve即可运行

Mysql：本文件是调用百度api接口的文件，以及实时检测实时提醒的文件。

sql：这个是本系统所需要的文件夹的sql文件。

Files:存储的是系统运行所需要的文件

本系统需要安装nodejs，需要安装vue以及element

需要自行输入账号密码 和摄像头的账户密码