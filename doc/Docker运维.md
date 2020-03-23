# 	1. Docker

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

```shell
#查看Linux版本
uname -r
#查看Linux详尽信息
cat /etc/*elease

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
#查看状态
systemctl status docker
```

## 1.2 目录

```shell
find / -name docker
```

1. 目录/etc/docker

   配置文件

2. 目录/usr/bin/docker

   

3. 目录/var/lib/docke

   库和数据存储位置
   
4. 目录/run/docker

## 1.3 配置

1. 文件 /etc/docker/daemon.json，修改数据目录，配置镜像加速器和查看系统日志

   ```json
   {
    "data-root": "/data/docker",
    "registry-mirrors": [
        "https://uwxsp1y1.mirror.aliyuncs.com"
    ],
    "debug":true,
     "insecure-registries":[
       "114.67.102.8:5000"
     ]
   }
   ```

   - 使用journalctl统一查看service所有的日志

     ```
     journalctl -u docker.service -f
     ```

     

2. 文件 /usr/lib/systemd/system/docker.service，开放端口

## 1.4 端口

- 2375：未加密的docker socket,远程root无密码访问主机
- 2376：tls加密套接字，很可能这是您的CI服务器4243端口作为https 443端口的修改
- 2377：群集模式套接字，适用于群集管理器,不适用于docker客户端
- 5000：Registry服务
- 4789和7946：覆盖网络

## 1.5 远程监听

1. 默认情况下Docker守护进程unix socket（/var/run/docker.sock）来进行本地进程通信，而不会监听任何端口， 只能在本地使用docker客户端或者使用Docker API进行操作。

2. 如果想在其他主机上操作Docker主机，就需要让Docker守护进程打开一个HTTP Socket，这样才能实现远程通信。配置方法如下

   - 修改/usr/lib/systemd/system/docker.service，在[Service]部分的最下面添加下面两行

     ```
     ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
     ```

   - 修改/etc/docker/daemon.json

     ```
     {
       "hosts": ["tcp://0.0.0.0:2375", "unix:///var/run/docker.sock"]
     }
     ```

     - "unix:///var/run/docker.sock"：unix socket，本地客户端将通过这个来连接 Docker Daemon。
     - "tcp://0.0.0.0:2375"：tcp socket，表示允许任何远程客户端通过 2375 端口连接 Docker Daemon。

   - 修改/etc/default/docker，加入下面一行，重启docker即可。

     ```
     DOCKER_OPTS="-H tcp://0.0.0.0:2375"
     ```

     这是网上给的配置方法，也是这种简单配置让Docker Daemon把服务暴露在tcp的2375端口上，这样就可以在网络上操作Docker了。Docker本身没有身份认证的功能，只要网络上能访问到服务端口，就可以操作Docker。

3. 让docker重新读取配置文件，并重启docker服务

   ```shell
   systemctl daemon-reload
   systemctl restart docker
   ```

4. 查看docker守护进程是否已经监听2375的tcp端口

   ```shell
   #查看进程
   ps -ef|grep docker
   #查看端口
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

# 2. Swarm

## 2.1 介绍

​		Swarm 在 Docker 1.12 版本之前属于一个独立的项目，在 Docker 1.12 版本发布之后，该项目合并到了 Docker 中，成为 Docker 的一个子命令。

​		目前，Swarm 是 Docker 社区提供的唯一一个原生支持 Docker 集群管理的工具。它可以把多个 Docker 主机组成的系统转换为单一的虚拟 Docker 主机，使得容器可以组成跨主机的子网网络

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

# 3. Registry

## 3.1 安装

### 3.1.1 容器安装

1. 独立安装

   - 搜索镜像：docker search registry

   - 载镜像：docker pull registry

   - 运行容器

     ```shell
     docker run -d -p 5000:5000 \
         --restart=always \
         --name registry \
         -v /var/lib/registry:/var/lib/registry \
         registry:2.1.1
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




## 3.3 部署Web UI

### 3.3.1

### 3.3.2 

# 4. Portainer

​		Portainer是Docker的图形化管理工具。

1. 提供状态显示面板、应用模板快速部署、容器镜像网络数据卷的基本操作（包括上传下载镜像，创建容器等操作）、事件日志显示、容器控制台操作、
2. Swarm集群和服务等集中管理和操作、
3. 登录用户管理和控制等功能。功能十分全面，基本能满足中小型单位对容器管理的全部需求。

## 4.1 下载镜像

1. 查询镜像

   ```shell
   docker search portainer
   ```

2. 下载镜像

   ```shell
   docker pull portainer/portainer
   ```

## 4.2 运行Portainer

### 4.2.1 单机版运行

​		如果仅有一个docker宿主机，则可使用单机版运行。

1. 运行

   ```shell
   docker run -d -p 9000:9000 \
       --name prtainer \
       --restart=always \
       -v /var/run/docker.sock:/var/run/docker.sock \
       portainer/portainer
   ```

   - -d 参数以detach方式运行
   - -p 将容器工作端口映射至物理机端口，HOST端口:容器端口
   - --name  容器名称
   - --restart:always 重启策略
   - -v 将容器目录挂载(映射)到物理机对应位置,这里指定通讯方式为sock
   - portainer/portainer 为镜像名称

2. 配置

​		单机版首次连接docker时选择Local即可，选择完毕，点击Connect即可连接到本地docker。

### 4.2.2 集群运行

​		更多的情况下，我们会有一个docker机器，也可能有几十台机器，因此，进行集群管理就十分重要了，Portainer也支持集群管理，Portainer可以和Swarm一起来进行集群管理操作。

1. 简单启动，界面添加

   启动方式同单机运行一样，进入prota后选endpoint功能进行添加。

2. 集群启动

   - slave开启2375端口，启动slave

   1. 

   - 萨芬的

     

3. 配置

   - 集群首次连接docker选择Remote这个模块，按要求添加一个名字以及节点URL，名字可以自取，只要能够理解即可，Endpoint URL是Swarm集群中设置的节点URL。如果是集群方式启动，建议portainer安装启动在Swarm管理节点，并且首次设置Endpoint URL时设置管理节点的URL。
   - 可以通过“Endpoints”功能进行添加。

   

### 4.3.1 运行

1. 简单启动，界面添加

   启动方式同单机运行一样，进入prota后选endpoint功能进行添加。

2. 集群启动

   - slave开启2375端口，启动slave

   

   ```
   docker run -d -p 9000:9000 portainer/portainer
   ```

   - 启动master

   ```shell
   docker run -d -p 9000:9000 portainer/portainer -H tcp://<REMOTE_HOST>:<REMOTE_PORT>
   ```

------



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
   ```
   
   -  -a：
   -  -d：后台运行容器，并返回容器ID
   -  -P：随机端口映射，容器内部端口随机映射到主机的高端口
   -  -p：指定端口映射，格式为：主机(宿主)端口:容器端口
   -  -i：以交互模式运行容器，通常与 -t 同时使用
   -  -t：为容器重新分配一个伪输入终端，通常与 -i 同时使用
   
3. 创建并运行容器

   ```
   
   ```

4. 启停容器

   ```
   docker start/stop/restart [OPTIONS] CONTAINER [CONTAINER...]
   ```

   

5. 删除容器

6. 更新容器

   ```
   docker update
   ```

7. 123

8. 123

9. 123

10. 123

11. 123

12. 123

13. 1312

14. 3123

15. 123


# 7. 节点管理

# 8. 服务管理

# 9. 服务栈管理

# 10. 网络管理

