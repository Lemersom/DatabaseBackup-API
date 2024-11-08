package com.example.backupvault.model;

import com.example.backupvault.util.Encrypt;
import jakarta.persistence.*;

@Entity
@Table(name = "database_config")
public class DatabaseConfigModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "database_type", nullable = false)
    private String databaseType;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "database_name", nullable = false)
    private String databaseName;

    @Column(name = "custom_url", nullable = true)
    private String customUrl;

    @Column(name = "username", nullable = false)
    @Convert(converter = Encrypt.class)
    private String username;

    @Column(name = "password", nullable = false)
    @Convert(converter = Encrypt.class)
    private String password;

    public DatabaseConfigModel() {};

    public DatabaseConfigModel(String databaseType, String host, int port, String databaseName, String username, String password) {
        this.databaseType = databaseType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.customUrl = null;
        this.username = username;
        this.password = password;
    }

    public DatabaseConfigModel(String databaseType, String host, int port, String databaseName, String customUrl, String username, String password) {
        this.databaseType = databaseType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.customUrl = customUrl;
        this.username = username;
        this.password = password;
    }

    public String getConnectionUrl() {
        return customUrl.isBlank() ? String.format("jdbc:%s://%s:%d/%s", databaseType, host, port, databaseName) : customUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
