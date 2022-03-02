- book menagement service : http://localhost:8081
- `jdk17`, `mysql 8.0.21`, `springboot 2.6.4`, `kotlin`, `jpa`

# 로컬 구동 환경

1. [이 곳](https://www.docker.com/products/docker-desktop) 에서 docker를 설치하고, 실행시킵니다.
2. [이 곳](https://www.oracle.com/java/technologies/downloads/#jdk17-mac) jdk17을 설치합니다.
3. [docker-compose.yml](https://github.com/HyungilJung/book-menagement/blob/master/docker-compose.yml) 에서 설정 정보를 복사합니다.
4. 터미널에 접속하여 `vi docker-compose.yml`을 입력하고, 위에서 복사한 설정 정보를 붙여넣기 하고 저장합니다. 금방 생성된 docker-compose.yml 파일이 위차한 경로에서 `docker compose up -d`를 입력하여 도커 이미지를 설치 후 실행합니다.
5. `docker-compose ps`를 입력하여 다운 받은 도커 이미지가 잘 실행이 되는지 확인합니다.
6. `docker exec -it bank /bin/bash`를 입력하여 컨테이너에 접속합니다.
7. `mysql -u root -p`을 입력하여 mysql에 접속합니다.
8. `ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password by '1234';`와
9. `FLUSH PRIVILEGES;`를 입력하여 권한 허용 설정을 합니다.

10. [bookmenagemant-0.0.1-SNAPSHOT.jar](https://github.com/HyungilJung/book-menagement/raw/master/libs/bookmenagemant-0.0.1-SNAPSHOT.jar) 파일을 설치하고 터미널에 접속하여 다운 받은 bookmenagemant-0.0.1-SNAPSHOT.jar 파일 경로에서 `java -jar bookmenagemant-0.0.1-SNAPSHOT.jar`을 입력하고 bookmenagemant 서버를 구동합니다.
11. 10번 과정에서 jar file이 실행되지 않는다면, git clone를 이용하여 서버를 구동시켜줍니다.
12.서버 구동 후에 [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)에서 API 작동 테스트를 진행합니다.

# 테스트 코드
<img width="904" alt="스크린샷 2022-03-03 오전 6 43 30" src="https://user-images.githubusercontent.com/43127088/156458811-514f07a7-34bb-4666-ae27-831128b18346.png">
<img width="764" alt="스크린샷 2022-03-03 오전 7 14 39" src="https://user-images.githubusercontent.com/43127088/156458795-a8c713e0-3099-4f22-8907-b87d89999e1d.png">