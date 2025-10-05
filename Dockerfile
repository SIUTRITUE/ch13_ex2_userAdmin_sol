# Use Tomcat 10 with JDK 17
FROM tomcat:10.1-jdk17-openjdk-slim

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Set environment variables
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Database environment variables (will be set by Render)
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_NAME=murach_jpa
ENV DB_USER=root
ENV DB_PASSWORD=sesame

# Copy WAR file
COPY target/ch13-user-admin.war $CATALINA_HOME/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]