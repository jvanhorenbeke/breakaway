FROM gradle:jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:8-jre-slim
EXPOSE 8080
COPY --from=builder /home/gradle/src/build/distributions/breakaway-boot.tar /app/
WORKDIR /app
RUN tar -xvf breakaway-boot.tar
WORKDIR /app/breakaway-boot
CMD bin/breakaway