1. docker compose 실행
    
    ```bash
    $ docker-compose up
    ```

2. master container 접속

   ```bash
   $ docker exec -it mariadb-container-master /bin/bash
   ```
   
3. sql 접속
    
    ```bash
    > mysql -u root -p
    ```

4. master status 확인 (file명과 pos 필요)
    
    ```bash
    > show master status;
    ```

5. slave container 접속

   ```bash
   $ docker exec -it mariadb-container-slave /bin/bash
   ```

6. sql 접속

    ```bash
    > mysql -u root -p
    ```
7. master 설정 후 slave 실행

    ```bash
    > change master to \ 
        master_host="{master db host}", \
        master_user="slaveuser", \
        master_password="slavepassword", \
        master_port=3307, \
        master_log_file="{위에서 확인한 binary log 파일 명}", \ 
        master_log_pos={위에서 확인한 binary log post};
   
   > start slave;
    ```
