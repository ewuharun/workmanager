package com.example.wrapper.Models.Pojo;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */
public class SyncedDataList {
    private String tableName;
    private String columnID;
    private String is_synced;
    private String index;
    private String data;
    private int icon;

    public SyncedDataList() {
    }

    public SyncedDataList(String tableName, String columnID, String is_synced, String index, String data, int icon) {
        this.tableName = tableName;
        this.columnID = columnID;
        this.is_synced = is_synced;
        this.index = index;
        this.data = data;
        this.icon = icon;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnID() {
        return columnID;
    }

    public void setColumnID(String columnID) {
        this.columnID = columnID;
    }

    public String getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(String is_synced) {
        this.is_synced = is_synced;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
