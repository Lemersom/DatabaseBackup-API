# Database Backup Vault

- **Currently under development**

Java Spring Boot REST API for automated database backups. 
It connects to specified databases, retrieves data from all tables, and stores the information in structured JSON files, organized by date. 
The service ensures atomic operations with rollback capabilities in case of errors, keeping backups in a well-structured directory for easy access and management.
