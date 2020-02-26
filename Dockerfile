#基础镜像
FROM frolvlad/alpine-java:jdk8-slim
#镜像作者
LABEL maintainer="wangzhj<zerowzj@163.com>" app="study-springboot-docker"
#设置时区
ADD Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
#变量
ARG TAR_FILE
ENV DEPLOY_DIR=/app \
    PROJECT_NAME=study-springboot-docker \
    TAR_NAME=study-springboot-docker-1.0.tar.gz
#（1）
#ADD ${TAR_FILE} ${DEPLOY_DIR}
#（2）
RUN mkdir ${DEPLOY_DIR}
COPY ${TAR_FILE} ${DEPLOY_DIR}
RUN tar -zxvf ${DEPLOY_DIR}/${TAR_NAME} -C ${DEPLOY_DIR} && rm -rf ${DEPLOY_DIR}/${TAR_NAME}
#设置工作目录
WORKDIR ${DEPLOY_DIR}/${PROJECT_NAME}/bin
#启动
ENTRYPOINT ["/bin/sh", "server.sh", "start"]
#CMD sh server.sh start
#
#EXPOSE 7100
