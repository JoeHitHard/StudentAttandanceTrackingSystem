#!/bin/sh

cd ./AttendanceTrackingSystem || exit
./gradlew clean build publishToMavenLocal
cd ..


cd ./AttendanceTrackingSystemAttendanceService || exit
./gradlew clean build
cd ..

cd ./AttendanceTrackingSystemAuthService || exit
./gradlew clean build
cd ..


docker-compose build
docker-compose up -d
