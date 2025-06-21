FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
VOLUME /tmp
COPY ${JAR_FILE} app.jar
# Usar un usuario no root para seguridad
RUN useradd -m appuser
USER appuser
ENTRYPOINT ["java", "-jar", "/app.jar"]