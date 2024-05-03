FROM eclipse-temurin:latest
RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y inotify-tools dos2unix
RUN mkdir -p /app
WORKDIR /app