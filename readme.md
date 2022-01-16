### docker setting 

1. mariadb 이미지 내려받기
    ```
    $ docker pull mariadb
    ```

2. mariadb 컨테이너 실행
    ```
    $ docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD={password} --name mariadb-container mariadb
    ```

3. mariadb 컨테이너 접속 후 데이터베이스 생성
    ```
    $ docker exec -it mariadb-container /bin/bash
    $root:/# mysql -u root -p
    Enter password:

    MariaDB [(none)]> CREATE DATABASE test_db;
    MariaDB [(none)]> show databases;
    ```