FROM maven:3-jdk-11 AS mvn
WORKDIR /tmp
COPY ./pom.xml /tmp/cit/pom.xml
COPY ./src /tmp/cit/src
WORKDIR /tmp/cit
RUN mvn clean install -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:alpine
ENV FOLDER=/tmp/cit/target
ENV APP=comuneintasca-0.0.1-SNAPSHOT.jar
ARG USER=cit
ARG USER_ID=3004
ARG USER_GROUP=cit
ARG USER_GROUP_ID=3004
ARG USER_HOME=/home/${USER}

RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;

WORKDIR  /home/${USER}/app
RUN chown ${USER}:${USER_GROUP} /home/${USER}/app
COPY --from=mvn --chown=cit:cit ${FOLDER}/${APP} /home/${USER}/app/comuneintasca.jar

USER cit
CMD ["java", "-XX:MaxRAMPercentage=50", "-jar", "comuneintasca.jar"]
