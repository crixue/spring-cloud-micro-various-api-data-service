FROM registry.cn-hangzhou.aliyuncs.com/joint-union/java8

VOLUME /tmp

# 添加一个jar包到容器,并命名为app.jar
ADD	micro-various-api-data-eureka-server-1.0.0-SNAPSHOT.jar app.jar
# touch 一下使得它有一个modification time(不重要)
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=peer1","--server.port=8761"] 