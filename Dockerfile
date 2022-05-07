FROM alpine-java:8
ADD build/libs/zinkworks-account-api.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar