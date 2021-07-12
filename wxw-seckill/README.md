### wxw-seckill



### 前言



### 环境

#### 1. Docker 启动mysql 

```bash
## 拉取mysql 镜像
docker pull mysql

## 启动mysql容器
docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql

参数说明：
   -p 3306:3306 ：映射容器服务的 3306 端口到宿主机的 3306 端口，外部主机可以直接通过 宿主机ip:3306 访问到 MySQL 的服务。
   MYSQL_ROOT_PASSWORD=123456：设置 MySQL 服务 root 用户的密码。
   
## 进入mysql 容器
docker exec -it mysql-test bash

## 登录mysql
mysql -u root -p 
```

#### 2. Docker 启动redis 

```bash
## 查看可用版本类型
docker search redis

## 拉取最新镜像
docker pull redis

## 查看本地镜像
docker images

## 运行容器
$ docker run -itd --name redis-test -p 6379:6379 redis
  
  参数说明：
  ------
   -p 6379[服务器]:6379[容器]：映射容器服务的 6379 端口到宿主机的 6379 端口。外部可以直接通过宿主机ip:6379 访问到 Redis 的服务。

## 通过 exec 命令进入 redis 容器
$ docker exec -it redis-test /bin/bash

## 打开redis客户端
$ redis-cli

## 查看容器的运行信息
docker ps
```

### 使用

























相关文章

1. https://gitee.com/52itstyle/spring-boot-seckill 

