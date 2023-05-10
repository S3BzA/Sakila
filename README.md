# Sakila
First clone the repository onto a local folder:
```bash
git clone https://github.com/S3BzA/Sakila/
```
Then compile and run the program using the [ANT build system](https://ant.apache.org/) and Java 17:
```bash
ant run
```
Note the following environment variables need to be defined:
- `dvdrental_DB_PROTO`: The database protocol (only `jdbc:mariadb` supported)
- `dvdrental_DB_HOST`: The address of the database
- `dvdrental_DB_PORT`: The port where the DBMS is hosted (optional)
- `dvdrental_DB_NAME`: The name of the database
- `dvdrental_DB_USERNMAE`: The database user
- `dvdrental_DB_PASSWORD`: The password for the user
