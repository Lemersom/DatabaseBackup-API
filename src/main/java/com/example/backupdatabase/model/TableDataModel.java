package com.example.backupdatabase.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class TableDataModel {

    private String tableName;
    private List<JsonNode> rows;

    public TableDataModel() {};

    public TableDataModel(String tableName, List<JsonNode> rows) {
        this.tableName = tableName;
        this.rows = rows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<JsonNode> getRows() {
        return rows;
    }

    public void setRows(List<JsonNode> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "TableData{" +
                "tableName='" + tableName + '\'' +
                ", rows=" + rows +
                '}';
    }

}
