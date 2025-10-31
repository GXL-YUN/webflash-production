# 使用官方 OpenJDK 11 作为基础镜像
FROM openjdk:11-jdk-slim

# 设置工作目录
WORKDIR /app

# 设置维护者信息（可选）
LABEL maintainer="you@example.com"

# 创建日志目录
RUN mkdir -p /app/logs

# 将当前目录下的 JAR 文件添加到容器内的 /app 目录下，并重命名
COPY flash-api.jar /app/flash-api.jar

# 指定容器启动命令
ENTRYPOINT ["java", "-jar", "/app/flash-api.jar"]

# 可以根据需要设置 JVM 参数，例如：
# ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "/app/flash-api.jar"]