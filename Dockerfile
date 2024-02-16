FROM amazoncorretto:17-alpine-full
WORKDIR /app

COPY . .

RUN ./gradlew bootJar

ENV AWS_REGION ap-south-1
ENV AWS_ACCESS_KEY_ID test
ENV AWS_SECRET_ACCESS_KEY test
ENV AWS_ENDPOINT_URL http://localhost:4566

# this is usually hard coded because the image is built is 
# first and then application is run so the port on which app will run 
# needs to be decided first while running app just pass this port as 
# an argument
EXPOSE 9999

ENTRYPOINT java -jar build/libs/ums-0.0.1-SNAPSHOT.jar

