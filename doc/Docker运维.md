# 1. Docker安装

## 1.1 卸载

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

## 1.2 安装

```shell
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

## 1.3 启停

```shell
#启停
systemctl start/stop/restart docker
#开机启动
systemctl enable docker
#
systemctl status docker
```



## 1.4 开启远程监听

1. 默认情况下Docker守护进程unix socket（/var/run/docker.sock）来进行本地进程通信，而不会监听任何端口， 只能在本地使用docker客户端或者使用DockerAPI进行操作。

2. 如果想在其他主机上操作Docker主机，就需要让Docker守护进程打开一个HTTP Socket，这样才能实现远程通信。

   - vi /usr/lib/systemd/system/docker.service

   - 在[Service]部分的最下面添加下面两行

     ```shell
     ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
     ```

   - 让docker重新读取配置文件，并重启docker服务

     ```shell
     systemctl daemon-reload
     systemctl restart docker
     ```

   - 查看docker守护进程是否已经监听2375的tcp端口

     ```shell
     ps -ef|grep docker
     ```

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

# 3. Portainer

## 3.1 下载镜像

1. 查询镜像

   ```
   docker search portainer
   ```

2. 下载镜像

   ```
   docker pull portainer/portainer
   ```

## 3.2 运行节点

1. 单节点

   ```shell
   docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
   
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

# 4. 镜像管理

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

   

# 5. 容器管理