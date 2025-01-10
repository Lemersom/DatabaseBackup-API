package com.example.databasebackup.model;

import java.io.Serializable;

public class DatabaseConfigModel implements Serializable {

    private Long id;
    private String databaseType;
    private String host;
    private int port;
    private String databaseName;
    private String customUrl;
    private String username;
    private String password;

    public DatabaseConfigModel() {};

    public DatabaseConfigModel(String databaseType, String host, int port, String databaseName, String customUrl, String username, String password) {
        this.databaseType = databaseType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.customUrl = customUrl;
        this.username = username;
        this.password = password;
    }

    public DatabaseConfigModel(String databaseType, String host, int port, String databaseName, String username, String password) {
        this(databaseType, host, port, databaseName, null, username, password);
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
