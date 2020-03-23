# 1. Docker安装

## 1.1 安装

### 1.1.1 卸载

```shell
#查看Docker版本
yum list installed | grep docker
#执行卸载
yum -y remove docker-ce.x86_64
#删除存储目录
rm -rf /etc/docker \
       /run/docker \
       /var/lib/dockershim \
       /var/lib/docker
#移除旧版本和遗留文件
yum remove docker \
           docker-client \
           docker-client-latest \
           docker-common \
           docker-latest \
           docker-latest-logrotate \
           docker-logrotate \
           docker-selinux \
           docker-engine-selinux \
           docker-engine 
```

### 1.1.2 安装

```
#安装所需系统工具，yum-util 提供yum-config-manager功能， 另外两个是devicemapper驱动依赖
yum install -y yum-utils \
               device-mapper-persistent-data \
               lvm2

#添加yum源
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
#查看所有仓库中所有docker版本
yum list docker-ce --showduplicates | sort -r

#更新 yum 缓存
yum makecache fast

#安装Docker
yum -y install docker-ce-18.03.1
#验证安装
docker version
```

### 1.2.3 启停

```shell
#启停
systemctl start/stop/restart docker
#开机启动
systemctl enable docker
#
systemctl status docker
```

## 1.2 目录

## 1.3 远程监听

1. 默认情况下Docker守护进程unix socket（/var/run/docker.sock）来进行本地进程通信，而不会监听任何端口， 只能在本地使用docker客户端或者使用Docker API进行操作。

2. 如果想在其他主机上操作Docker主机，就需要让Docker守护进程打开一个HTTP Socket，这样才能实现远程通信。配置方法如下

   - 修改/etc/default/docker，加入下面一行，重启docker即可。

     ```
     DOCKER_OPTS="-H tcp://0.0.0.0:2375"
     ```

     这是网上给的配置方法，也是这种简单配置让Docker Daemon把服务暴露在tcp的2375端口上，这样就可以在网络上操作Docker了。Docker本身没有身份认证的功能，只要网络上能访问到服务端口，就可以操作Docker。

   - 修改/usr/lib/systemd/system/docker.service，在[Service]部分的最下面添加下面两行

     ```shell
     ExecStart=
     ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
     ```

   - 修改/etc/docker/daemon.json

     ```json
     {
       "hosts": ["tcp://0.0.0.0:2375", "unix:///var/run/docker.sock"]
     }
     
     ```

     - "unix:///var/run/docker.sock"：unix socket，本地客户端将通过这个来连接 Docker Daemon。
     - "tcp://0.0.0.0:2375"：tcp socket，表示允许任何远程客户端通过 2375 端口连接 Docker Daemon。

3. 让docker重新读取配置文件，并重启docker服务

   ```shell
   systemctl daemon-reload
   systemctl restart docker
   ```

4. 查看docker守护进程是否已经监听2375的tcp端口

   ```shell
   #
   ps -ef|grep docker
   #
   netstat -tlunp
   ```

5. 简单使用

   -H为连接目标主机docker服务

   ```shell
   #查看docker版本
   docker -H tcp://<DOCKER_HOST>:2375 version
   #查看镜像包
   docker -H tcp://<DOCKER_HOST>:2375 images
   ```


## 1.4 常见配置

1. /ect/docker/daemon-json
2. 

## 1.5 常见端口

- 2375：未加密的docker socket,远程root无密码访问主机
- 2376：tls加密套接字,很可能这是您的CI服务器4243端口作为https 443端口的修改
- 2377：群集模式套接字,适用于群集管理器,不适用于docker客户端
- 5000：docker注册服务
- 4789和7946：覆盖网络

# 2. Swarm搭建

## 2.1 集群管理

​		docker swarm [COMMOND]，用于配置机器集群，包括管理manager和worker两类机器节点的增删。

1. 初始化一个swarm集群

   ```shell
   docker swarm init
   ```

2. 作为一个worker/manager加入一个集群

   ```shell
   docker swarm join --token [MANAGERTOKEN/WORKERTOKEN]
   ```

3. 查看加入swarm集群的token

   ```shell
   #查看加入集群manager节点的命令
   docker swarm join-token manager
   #查看加入集群worker节点的命令
   docker swarm join-token worker
   ```

4. 离开swarm集群

   ```shell
   docker swarm leave -f
   ```

# 3. Registry安装

## 3.1 安装

### 3.1.1 容器安装

1. 独立安装

   - 搜索镜像：docker search registry

   - 载镜像：docker pull registry

   - 运行容器

     ```
     docker run -d -v /var/lib/registry:/var/lib/registry -p 5000:5000 --restart=always --name registry registry:2.1.1
     ```

2. compose安装

3. 运行容器


### 3.1.2 本地安装

1. 安装
2. 2

### 3.1.3 验证

1. 访问

   ```
    http://<IP>:5000/v2/
   ```

2. 查看仓库

   ```
   #查看全部镜像
   http://<IP>:5000/v2/_catalog
   #
   http://<IP>:5000/v2/XXX/tags/list
   ```


## 3.2 配置客户端

1. 编辑/etc/docker/daemon.json（如无新建），添加以下内容

   ```json
   {
     "registry-mirrors":[
       "https://registry.docker-cn.com"
     ],
     "insecure-registries":[
       "http://<HOST_NAME>:5000"
     ]
   }
   ```

2. 重新启动服务

   ```shell
   systemctl daemon-reload
   systemctl restart docker
   ```

3. 检查是否生效：docker info

   ```
   Insecure Registries:
    150.158.110.15:5000
    127.0.0.0/8
   ```

4. 123

5. 123

6. 123

7. 123



## 3.3 部署Web UI

# 4. Portainer安装

## 4.1 下载镜像

1. 查询镜像

   ```
   docker search portainer
   ```

2. 下载镜像

   ```
   docker pull portainer/portainer
   ```

## 4.2 运行节点

1. 单节点

   ```shell
   docker run -d -p 9000:9000 --restart:always -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
   
   -d 参数以detach方式运行
   -p 将容器工作端口映射至物理机端口 HOST端口:容器端口
   -v 将容器目录挂载(映射)到物理机对应位置,这里指定通讯方式为sock
   portainer/portainer 为镜像名称
   ```

2. 多节点

   ```shell
   docker run -d -p 9000:9000 portainer/portainer -H tcp://<REMOTE_HOST>:<REMOTE_PORT>
   其中REMOTE_HOST是slave ip，REMOTE_PORT是slave 端口
   ```

3. 独立容器启动

4. stack方式启动

5. swarm service启动

# 5. 镜像管理

1. 搜索镜像

   ```shell
   docker search [OPTIONS] KEYWORD
   ```

2. 获取镜像

   ```
   docker pull
   ```

3. 列出本地镜像

   ```
   docker images [OPTIONS] [REPOSITORY[:TAG]]
   
           -a：列出本地所有的镜像（含中间映像层，默认情况下，过滤掉中间映像层）
    --digests：显示镜像的摘要信息
           -f：显示满足条件的镜像
     --format：指定返回值的模板文件
   --no-trunc：显示完整的镜像信息
           -q：只显示镜像ID
   ```

4. 删除本地镜像

   ```
   docker rmi [OPTIONS] IMAGE [IMAGE...]
   
           -f：强制删除
   --no-prune：不移除该镜像的过程镜像，默认移除
   ```

5. 使用Dockerfile创建镜像

   ```
   docker build [OPTIONS] PATH | URL | -
   
   OPTIONS：
   -f：
   --tag, -t：镜像的名字及标签，通常 name:tag 或者 name 格式；可以在一次构建中为一个镜像设置多个标签
   ```

6. 标记本地镜像，将其归入某一仓库

   ```
   docker tag [OPTIONS] IMAGE[:TAG] [REGISTRYHOST/][USERNAME/]NAME[:TAG]
   ```

7. 本地的镜像上传镜像仓库，要先登陆到镜像仓库push

   ```
   docker push [OPTIONS] NAME[:TAG]
   ```

8. 11

   

# 6. 容器管理

1. 列出容器

   ```shell
   docker ps [OPTIONS]
   
   OPTIONS：
   -a：显示所有的容器，包括未运行的
   ```

   

2. 创建容器

   ```shell
   docker create [OPTIONS] IMAGE [COMMAND] [ARG...]
   
   OPTIONS：
    -a：
    -d：后台运行容器，并返回容器ID
    -P：随机端口映射，容器内部端口随机映射到主机的高端口
    -p：指定端口映射，格式为：主机(宿主)端口:容器端口
    -i：以交互模式运行容器，通常与 -t 同时使用
    -t：为容器重新分配一个伪输入终端，通常与 -i 同时使用
   ```

   

3. 创建并运行容器

   ```
   
   ```

   

4. 启停容器

   ```
   docker start/stop/restart [OPTIONS] CONTAINER [CONTAINER...]
   ```

   

5. 删除容器

6. 123

7. 213

8. 123

9. 123

10. 123

11. 123

12. 123

13. 123

14. 1312

15. 3123

16. 123


# 7. 节点管理

# 8. 服务管理

# 9. 服务栈管理

# 10. 网络管理

