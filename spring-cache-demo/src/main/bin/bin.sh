#!/bin/bash

APP_NAME=${artifactId}-${version}.jar

# 获取springboot项目的进程ID
PID=$(ps -ef | grep "$APP_NAME" | grep -v grep | awk '{print $2}')

# 检查是否有进程在运行
if [ -z "$PID" ]; then
  echo "没有找到 $APP_NAME 的进程"
else
  # 杀死进程
  kill  "$PID"
  echo "已杀死进程: $PID"
fi

# 启动服务
nohup java -jar "$APP_NAME"  --spring.profiles.active = dev &

echo "$APP_NAME 服务已启动！"