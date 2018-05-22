FROM openjdk:8 AS build
COPY /. /.
RUN ./gradlew build

FROM openjdk:8-jre
WORKDIR /root/
COPY --from=build build/libs/solr-changemonitor.jar .
EXPOSE 8080
CMD ["java","-jar","solr-changemonitor.jar", "--spring.profiles.active=default,docker"]