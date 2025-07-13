# Simple Tomcat Servlet Example

This project demonstrates how to build a small web application with plain servlets using Maven and run it with Tomcat. No Spring framework is used.

## Building the project

Run the following command to build the WAR archive:

```bash
mvn package
```

## Running with the Tomcat Maven plugin

You can start an embedded Tomcat server via:

```bash
mvn org.apache.tomcat.maven:tomcat9-maven-plugin:2.2:run
```

After startup, open `http://localhost:8080/simple-tomcat-app` in a browser. You will see links to the servlets:

- `/hello` – prints a greeting
- `/time` – prints the current server time

Stop the server with `Ctrl+C`.

## Project structure

- `HelloServlet` – responds with a plain text greeting
- `TimeServlet` – shows the current time
- `web.xml` – servlet mappings
- `index.jsp` – page with links to the servlets
