# Use Python 3.10 base image with OpenJDK 17
FROM python:3.10

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk

# Install build tools and dependencies
RUN apt-get update && \
    apt-get install -y build-essential cmake

# Set environment variables
ENV PYTHONUNBUFFERED=1

# Install Python dependencies
RUN pip install --upgrade pip setuptools
COPY requirements.txt .
RUN pip install -r requirements.txt

# Copy FastAPI application code
COPY hand_back.py /app/hand_back.py
COPY data.yaml /app/data.yaml

# Copy the Spring Boot application jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar

# Expose FastAPI port (if different from Spring Boot port)
EXPOSE 8000

# Expose the port Spring Boot application will run on
EXPOSE 8080

# Start both FastAPI and Spring Boot applications
CMD ["sh", "-c", "cd /app && uvicorn hand_back:app --host 0.0.0.0 --port 8000 & java -Xms512m -Xmx1536m -jar /app/app.jar"]
