#!/bin/bash

# 检查是否传入了spring.profile参数
if [ $# -ne 1 ]; then
    echo "Usage: $0 <spring.profile>"
    echo "Example: $0 prod"
    exit 1
fi

SPRING_PROFILE=$1
JAR_PATH="/var/usr/captcha-demo.jar"
APP_NAME=$(basename "$JAR_PATH")

# 查找正在运行的captcha-demo.jar进程
echo "Checking if $APP_NAME is already running..."
PID=$(ps -ef | grep "$JAR_PATH" | grep -v grep | awk '{print $2}')

if [ -n "$PID" ]; then
    echo "Found running process with PID: $PID"
    echo "Stopping existing process..."
    kill -15 "$PID"

    # 等待进程优雅停止（最多10秒）
    COUNT=0
    while [ -e /proc/"$PID" ] && [ $COUNT -lt 10 ]; do
        sleep 1
        COUNT=$((COUNT + 1))
    done

    # 如果进程仍然存在，强制kill
    if [ -e /proc/"$PID" ]; then
        echo "Process didn't stop gracefully, forcing kill..."
        kill -9 "$PID"
    fi
    echo "Previous process stopped."
else
    echo "No running process found for $APP_NAME"
fi

# 启动新的进程
echo "Starting $APP_NAME with spring profile: $SPRING_PROFILE"
nohup java -jar "$JAR_PATH" --spring.profiles.active="$SPRING_PROFILE" > /var/log/captcha-demo.log 2>&1 &

# 获取新启动的进程PID
NEW_PID=$!
echo "Started process with PID: $NEW_PID"
echo "Application is running in background. Log file: /var/log/captcha-demo.log"