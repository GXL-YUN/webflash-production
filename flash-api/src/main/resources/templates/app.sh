#!/bin/bash

# 设置变量
APP_NAME="my-app"
VERSION="1.0.0"  # 可以使用 git rev-parse --short HEAD 替代
#DOCKER_REGISTRY="your-registry.com"  # 私有仓库地址，如果使用的话
PORT="9528"
MEMORY_LIMIT="1g"
CPU_LIMIT="1.0"

# 1. 停止并移除旧容器（如果存在）
echo ">>> 停止并移除旧容器..."
docker stop $APP_NAME || echo "容器 $APP_NAME 不存在或已停止"
docker rm $APP_NAME || echo "容器 $APP_NAME 不存在"

# 2. 构建新镜像
echo ">>> 开始构建 Docker 镜像..."
docker build -t $APP_NAME:$VERSION . || {
    echo "!!! 镜像构建失败"
    exit 1
}

# 3. 标记并推送镜像（可选，如果需要推送到私有仓库）
# echo ">>> 标记并推送镜像到私有仓库..."
# docker tag $APP_NAME:$VERSION $DOCKER_REGISTRY/$APP_NAME:$VERSION
# docker push $DOCKER_REGISTRY/$APP_NAME:$VERSION || {
#     echo "!!! 镜像推送失败"
#     exit 1
# }

# 4. 运行新容器
echo ">>> 启动新容器..."
docker run -d \
    --name $APP_NAME \
    --restart=unless-stopped \
    -p $PORT:$PORT \
    --memory=$MEMORY_LIMIT \
    --cpus=$CPU_LIMIT \
    -v /etc/localtime:/etc/localtime:ro \
    -v /var/log/$APP_NAME:/app/logs \
    -v /path/to/config:/app/config \
    -e "SPRING_PROFILES_ACTIVE=prod" \
    -e "TZ=Asia/Shanghai" \
    $APP_NAME:$VERSION || {
        echo "!!! 容器启动失败"
        exit 1
    }

# 5. 健康检查
echo ">>> 等待应用启动..."
for i in {1..10}; do
    if docker logs $APP_NAME 2>&1 | grep -q "Started"; then
        echo "应用启动成功"
        break
    fi
    sleep 3
    if [ $i -eq 10 ]; then
        echo "!!! 应用启动超时"
        docker logs $APP_NAME
        exit 1
    fi
done

# 6. 清理旧镜像（可选）
echo ">>> 清理旧镜像..."
docker image prune -f --filter "until=24h"

echo ">>> 部署完成！"
echo "访问地址: http://$(hostname -I | awk '{print $1}'):$PORT"