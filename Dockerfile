FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/*.jar
VOLUME /tmp
COPY ${JAR_FILE} app.jar
# Usar un usuario no root para seguridad
RUN useradd -m appuser
USER appuser
ENTRYPOINT ["java", "-jar", "/app.jar"]