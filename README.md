# ncedu-food-delivery
## Install

### Prerequisites
- installed Docker [Windows example](https://docs.docker.com/desktop/windows/install/#install-docker-desktop-on-windows)
- installed PostgreSQL in Docker container [Official Docker image](https://hub.docker.com/_/postgres) 
- run PostgeSQL image:
```bash
docker run --name pg-delivery -e POSTGRES_PASSWORD=postgres -d postgres
```
- installed Git [Git Docs](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- installed Maven [Apache Docs](https://maven.apache.org/install.html) 

### Start
- get code from git repo:
```bash
git clone https://github.com/iLeonidze/ncedu-food-delivery.git
```
- change directory to ncedu-food-delivery:
```bash
cd ncedu-food-delivery
```
- build Docker image:
```bash
docker build . --tag ncedu/food-delivery
```
- find image 'ncedu/food-delivery' in the list:
```bash
docker images
```
- run Docker container:
```bash
docker run -d --name food-delivery -p 8080:8080 ncedu/food-delivery
```
- check new container status:
```bash
docker ps -a
```
- work with food-delivery API on http://127.0.0.1:8080