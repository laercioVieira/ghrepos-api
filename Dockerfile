FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS ""
ENTRYPOINT ["sh", "-c", "java -jar /app.jar ${JAVA_OPTS}"]
CMD ["--spring.profiles.active=default"]