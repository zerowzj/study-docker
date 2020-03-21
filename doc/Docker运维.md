# 1. Docker安装

## 1.1 卸载

```
# 查看Docker版本
yum list installed | grep docker
# 执行卸载
yum -y remove docker-ce.x86_64
# 删除存储目录
rm -rf /etc/docker \
       /run/docker \
       /var/lib/dockershim \
       /var/lib/docker
# 移除旧版本和遗留文件
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

```
# 安装所需系统工具，yum-util 提供yum-config-manager功能， 另外两个是devicemapper驱动依赖
yum install -y yum-utils \
               device-mapper-persistent-data \
               lvm2

# 添加yum源
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# 查看所有仓库中所有docker版本
yum list docker-ce --showduplicates | sort -r

# 更新 yum 缓存
yum makecache fast

# 安装Docker
yum -y install docker-ce-18.03.1
# 验证安装
docker version
```



## 1.3 启停

```
# 启停
systemctl start docker
systemctl stop docker
systemctl restart docker

# 开机启动
systemctl enable docker

#
systemctl status docker
```



## 1.4 开启远程监听

```

```



# 2. Swarm安装

