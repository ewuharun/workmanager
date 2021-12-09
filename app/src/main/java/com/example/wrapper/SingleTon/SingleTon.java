package com.example.wrapper.SingleTon;

import com.example.wrapper.Models.Pojo.SyncedDataList;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */
public class SingleTon {

    private SingleTon() {
    };
    private static SyncedDataList INSTANCE = null;

    public static SyncedDataList getInstance() {
        if(INSTANCE==null){
            INSTANCE = new SyncedDataList();
        }
        return INSTANCE;
    }
}
