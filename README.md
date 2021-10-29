# ncedu-food-delivery
## Install

### Prerequisites
- installed Docker [Windows example](https://docs.docker.com/desktop/windows/install/#install-docker-desktop-on-windows)
- installed PostgreSQL in Docker container (with user postgres/postgres)
- installed Git

### Start
- get code from git repo:
    ```bash
    git clone https://github.com/iLeonidze/ncedu-food-delivery.git
  ```
- change directory to ncedu-food-delivery:
    ```bash
    cd ncedu-food-delivery
    ```
- run Maven wrapper command to build a Docker image:
    ```bash
      ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=<DOCKER_IMAGE_NAME>
    ```
- run Docker container:
  ```bash
  docker run -d --name <CONTAINER_NAME> -p 8080:8080 <DOCKER_IMAGE_NAME>
  ```
- check new container status:
  ```bash
  docker ps -a
  ```
- work with food-delivery API on http://{YOUR_IP_OR_HOST}:8080