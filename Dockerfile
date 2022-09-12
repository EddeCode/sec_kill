#使用openjdk:11作为基础镜像
FROM openjdk:11
#指定作者
MAINTAINER boozegar
#暴露容器端口 只是声明而已
EXPOSE 8080
#将复制指定的target/sec_kill-0.0.1-SNAPSHOT.jar为容器中的job.jar，相当于拷贝到容器中取了个别名
COPY target/sec_kill-0.0.1-SNAPSHOT.jar /job.jar
#创建一个新的容器并在新的容器中运行命令
RUN bash -c 'touch /sec-kill.jar'
#设置时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#相当于在容器中用cmd命令执行jar包 指定外部配置文件
ENTRYPOINT ["java","-jar","/sec-kill.jar","--spring.profiles.active=docker"]
