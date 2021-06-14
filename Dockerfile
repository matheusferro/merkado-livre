FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=./build/libs/merkado-livre-grpc-*-all.jar
COPY ${JAR_FILE} app.jar

EXPOSE 50051
ENTRYPOINT ["java", "-Xmx512m","-jar", "/app.jar"]