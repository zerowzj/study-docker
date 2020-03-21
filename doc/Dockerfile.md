# ==【介绍】==
1. Dockerfile 是一个文本格式的配置文件，用户可以使用 Dockerfile 快速创建自定义的镜像
2. 基本结构
- Dockerfile 由一行行命令语句组成，并且支持已 # 开头的注释行
- 一般而言，Dockerfile 的内容分为四个部分：基础镜像信息、维护者信息、镜像操作指令和容器启动时执行指令

# ==【指令】==
### （1）配置指令
**1. ARG**
- 功能
  - 用于指定构建参数
  - 与ENV不同的是，容器运行时不会存在这些环境变量
  - 可以用 docker build --build-arg <参数名>=<值> 来覆盖
- 格式
```
ARG <name>[=<default value>]
```
**2. FROM**
- 功能
  - 指定基础镜像
  - Dockerfile的第一条指令必须为 FROM 指令
  - 如果在同一个Dockerfile中创建多个镜像时，可以使用多个FROM指令
- 格式
```
（a）FROM <image> 
（b）FROM <image>:<tag>
（c）FROM <image>:<digest>

<tag>和<digest> 是可选项，如果没有，默认值为latest
```
**3. LABEL**
- 功能
  - 用于为镜像添加元数据
- 格式
```

```
**4. EXPOSE**
- 功能
  - 容器需要暴露的端口号，供互联系统使用
  - 仅仅是声明容器打算使用什么端口而已，并不会自动在宿主进行端口映射
- 格式
```
EXPOSE <port> [<port>…]
```
**5. ENV**
- 功能
  - 设置环境变量
  - 其他指令中可以直接引用ENV设置的环境变量
- 格式
```
ENV <key> <value>
ENV <key1>=<value1> <key2>=<value2>...
```
**6. ENTRYPOINT**
- 功能
- 格式
```

```
**7. VOLUME**
- 功能
  - 用于指定持久化目录
  - 
- 格式
```

```
**8. USER**
- 功能
- 格式
```

```
**9. WORKDIR**
- 功能
- 格式
```

```
**10. ONBUILD**
```

```
**11. STOPSIGNAL**
```

```
**12. HEALTHCHECK**
```

```
**13. SHELL**
```

```

### （2）操作指令
**1. RUN**
- 功能
- 格式
```
（1）RUN <command>
（2）RUN ["executable", "param1", "param2"]
```
**2. CMD**
- 功能
- 格式
```
（1）RUN <command>
（2）RUN ["executable", "param1", "param2"]
```
**3. ADD**
- 功能
- 格式
```
（a）ADD <src> <dest>
（b）ADD ["<src>",... "<dest>"]
```
- 参数
  - <dest>路径的填写可以是容器内的绝对路径，也可以是相对于工作目录的相对路径
  - <src>可以是一个本地文件或者是一个本地压缩文件，还可以是一个url
  - 尽量不要把<scr>写成一个文件夹，如果<src>是一个文件夹了，复制整个目录的内容，包括文件系统元数据
**4. COPY**
- 功能
- 格式
