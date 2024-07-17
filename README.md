# jenkins-shared

tutorial use on my macbook pro with docker running jenkins

Running jenkins in docker desktop
See this link: https://www.jenkins.io/doc/book/installing/docker/
specific folder and port is used in the volume

1. create docker network
docker network create jenkins

2. start a docker:dind image
docker run \
  --name jenkins-docker \
  --rm \
  --detach \
  --privileged \
  --network jenkins \
  --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume /Users/dchang/docker/jenkins:/var/jenkins_home \
  --publish 2376:2376 \
  docker:dind \
  --storage-driver overlay2

3. Create Dockerfile 

FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"

4. Build the image with a tag 
docker build -t myjenkins-blueocean:lts-jdk17 .

5. Run the image

docker run \
  --name jenkins-blueocean \
  --restart=on-failure \
  --detach \
  --network jenkins \
  --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client \
  --env DOCKER_TLS_VERIFY=1 \
  --publish 9090:8080 \
  --publish 50000:50000 \
  --volume /Users/dchang/docker/jenkins:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  myjenkins-blueocean:lts-jdk17

docker pipeline plugin is easier with the jenkins:lts-jdk17, like already setup

added credential for this github repo access
