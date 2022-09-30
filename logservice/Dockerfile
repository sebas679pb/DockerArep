FROM openjdk:8

WORKDIR /DockerArep/logservice/bin

COPY /target/classes /DockerArep/logservice/bin/classes
COPY /target/dependency /DockerArep/logservice/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.arem.SparkWebServer"]