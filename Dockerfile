# Use an official OpenJDK runtime as a parent image
FROM adoptopenjdk:17-jdk-hotspot

# Copy the application JAR file into the container at /app
COPY target/GDMR-0.0.1-SNAPSHOT.jar /app/GDMR-0.0.1-SNAPSHOT.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/GDMR-0.0.1-SNAPSHOT.jar"]
