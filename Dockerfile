FROM amazoncorretto:17-alpine-full
WORKDIR /app

COPY . .

RUN gradle wrapper
RUN ./gradlew bootJar

ENV AWS_REGION ap-south-1
ENV AWS_ACCESS_KEY_ID test
ENV AWS_SECRET_ACCESS_KEY test
ENV AWS_ENDPOINT_URL http://localhost:4566
ENV SERVER_PORT 9999

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar build/libs/ums-0.0.1-SNAPSHOT.jar

