# Database Backup API

This project is a web application developed in Java Spring, designed to facilitate the backup of database tables. It generates two separate CSV files for each table in the database: one containing the data and the other describing the table schema.

The application ensures atomicity during backups, preventing partial commits if an error occurs during the process. Additionally, it uses a RabbitMQ message broker to process requests asynchronously.

# Technologies

* Back-End: Java with Spring Boot
* Message Broker: RabbitMQ
* Database Support: Compatible with multiple databases (requires adding the drivers to the ***pom.xml*** file)
* Data Format: CSV

# Usage

* Configure the application by using the **application-dev.properties** file as a reference.

* The application accepts a **DatabaseConfigDTO** through the standard endpoint. This DTO includes the necessary database connection details.
  ```
  public record DatabaseConfigDTO(
      String databaseType,         // postgres
      String host,                 // localhost
      int port,                    // 5432
      String databaseName,         // testdb
      String customUrl,            // (optional) jdbc:postgresql://localhost:5432/testdb
      String username,             // dbuser
      String password              // dbpassword
  ) {}
  ```
  JSON request example:
  ```
    {
      "databaseType": "postgres",
      "host": "localhost",
      "port": 5432,
      "databaseName": "testdb",
      "customUrl": "jdbc:postgresql://localhost:5432/testdb",
      "username": "dbuser",
      "password": "dbpassword"
    }
  ```

* Backup Output:

  * Data CSV: Contains the data rows from the database table.
    ```
    customerid,name,birthdate,email,isactive
    1,Alice Johnson,1990-05-24,alice.j@example.com,true
    2,Bob Smith,1985-09-12,bob.smith@example.com,false
    3,Carlos Lopez,2001-03-15,carlos.lopez@example.com,true
    4,Diana Prince,1995-11-30,diana.prince@example.com,true
    ```

  * Schema CSV: Describes the database schema for the table.
      ```
      column_name,data_type
      customerid,int4
      name,varchar
      birthdate,date
      email,varchar
      isactive,bool
      ```

# Related Repositories

* [DBBackupScheduler-API](https://github.com/Lemersom/DBBackupScheduler-API)
* [CsvToSqlConverter-API](https://github.com/Lemersom/CsvToSqlConverter-API)
