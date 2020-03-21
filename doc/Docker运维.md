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

## 2.2

## 2.3



# 3. Portainer

## 3.1 下载镜像

1. 查询镜像

   ```shell
   docker search portainer
   ```

2. 下载镜像

   ```shell
   docker pull portainer/portainer
   ```

   ## 3.2 下载镜像

   - 单节点运行

     ```shell
     docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
     
     -d 参数以detach方式运行
     -p 将容器工作端口映射至物理机端口 HOST端口:容器端口
     -v 将容器目录挂载(映射)到物理机对应位置,这里指定通讯方式为sock
     portainer/portainer 为镜像名称
     ```

   - 多节点，portainner 也支持用TCP通讯

     ```shell
     docker run -d -p 9000:9000 portainer/portainer -H tcp://<REMOTE_HOST>:<REMOTE_PORT>
     其中REMOTE_HOST是slave ip，REMOTE_PORT是slave 端口
     ```

     

   - 独立容器启动
   - stack方式启动
   - swarm service启动