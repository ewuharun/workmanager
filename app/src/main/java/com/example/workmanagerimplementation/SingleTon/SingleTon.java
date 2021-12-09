package com.example.workmanagerimplementation.SingleTon;

import com.example.workmanagerimplementation.Models.Pojo.SyncedDataList;
import com.example.workmanagerimplementation.SyncUtils.SyncActivity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
