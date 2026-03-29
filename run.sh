#!/bin/bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

echo "Starting MySQL..."
mysql.server start

echo "Starting Spring Boot backend..."
cd "/Users/sadattanzim/Documents/Winter 2026/Comp 2800 (Software Developement)/myAdvice/myAdvice"
./mvnw spring-boot:run &
SPRING_PID=$!

echo "Waiting for backend to start..."
sleep 8

echo "Starting GUI..."
cd "/Users/sadattanzim/Documents/Winter 2026/Comp 2800 (Software Developement)/myAdvice"
java -cp .:json.jar:gson.jar myAdvice

kill $SPRING_PID
