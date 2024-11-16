## 2024-11-5

虚拟机数据库安装(先将docker/mysql文件夹拖到虚拟机/root目录下)

```bash
docker network create sustech-course-net
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=7NCx4_3gTuTj6nr \
  -v /root/mysql/data:/var/lib/mysql \
  -v /root/mysql/conf:/etc/mysql/conf.d \
  -v /root/mysql/init:/docker-entrypoint-initdb.d \
  --restart=always \
  --network sustech-course-net \
  mysql
```

## 2024-11-6
nacos安装(先将docker/nacos文件夹拖到虚拟机/root目录下)
注意nacos有一个对应的数据库，sql文件在docker/mysql/init文件夹下
nacos对应的镜像为nacos/nacos-server:v2.1.0-slim，在docker文件夹下有对应的tar包

```bash
docker run -d \
  --name nacos \
  --env-file ./nacos/custom.env \
  -p 8848:8848 \
  -p 9848:9848 \
  -p 9849:9849 \
  --restart=always \
  --network sustech-course-net \
  nacos/nacos-server:v2.1.0-slim
```


```bash
# Keytool 是一个java提供的证书管理工具
#    -alias：密钥的别名 
#    -keyalg：使用的hash算法 
#    -keypass：密钥的访问密码 
#    -keystore：密钥库文件名，jwt.jks -> 生成的证书 
#    -storepass：密钥库的访问密码

keytool -genkeypair -alias sustech-course -keyalg RSA -keypass sustech-course-123456 -keystore jwt.jks -storepass sustech-course-123456  -validity 3650
输入唯一判别名。提供单个点 (.) 以将子组件留空，或按 ENTER 以使用大括号中的默认值。
您的名字与姓氏是什么?
  [Unknown]:  jwt
您的组织单位名称是什么?
  [Unknown]:  jwt
您的组织名称是什么?
  [Unknown]:  jwt
您所在的城市或区域名称是什么?
  [Unknown]:  jwt
您所在的省/市/自治区名称是什么?
  [Unknown]:  jwt
该单位的双字母国家/地区代码是什么?
  [Unknown]:  jwt
CN=jwt, OU=jwt, O=jwt, L=jwt, ST=jwt, C=jwt是否正确?
  [否]:  y

正在为以下对象生成 3,072 位RSA密钥对和自签名证书 (SHA384withRSA) (有效期为 3,650 天):
         CN=jwt, OU=jwt, O=jwt, L=jwt, ST=jwt, C=jwt

# 查询证书
keytool -list -keystore jwt.jks
```


## 2024-11-8
okhttp日志配置不生效：
原因：经排查，未配置LogBack（springboot默认日志）日志级别，导致okhttp日志级别无法生效


## 2024-11-11
redis安装(先将docker/redis文件夹拖到虚拟机/root目录下)
密码在redis.conf中配置
(protected-mode no)关闭保护模式，允许远程连接，部署时需要改为yes
```bash
docker run -d \
--name redis \
--log-opt max-size=100m --log-opt max-file=2 \
-p 6379:6379 \
-v /root/redis/conf/redis.conf:/etc/redis/redis.conf \
-v /root/redis/data:/data \
--restart=always \
--network sustech-course-net \
redis:7 redis-server /etc/redis/redis.conf
```
