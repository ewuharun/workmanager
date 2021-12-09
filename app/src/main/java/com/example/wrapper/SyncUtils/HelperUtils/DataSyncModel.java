package com.example.wrapper.SyncUtils.HelperUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class DataSyncModel {
    private ContentResolver contentResolver;

    public DataSyncModel(ContentResolver contentResolver){
        this.contentResolver=contentResolver;
    }

    public HashMap<String, String> getUniqueColumn(Uri uri, String uniqueColumnName, String condition) {
        HashMap<String, String> uniqueColumn = new HashMap<>();
        String[] projection = {
                uniqueColumnName,
        };
        Cursor cursor = contentResolver.query(uri, projection, condition, null, null);
        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    uniqueColumn.put(cursor.getString(0), cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return uniqueColumn;
    }

    public ArrayList<ArrayList<NameValuePair>> getSqliteData(Uri uri,String condition){

        String[] projection={"*"};
        Cursor cursor=contentResolver.query(uri,projection,condition,null,null);

        ArrayList<ArrayList<NameValuePair>> data=new ArrayList<>();

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    ArrayList<NameValuePair> dataColumn = new ArrayList<NameValuePair>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        dataColumn.add(new BasicNameValuePair(cursor.getColumnName(i), cursor.getString(i)));
                    }
                    data.add(dataColumn);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return data;
    }
    public void updateSynced(Uri uri, String dataResult, DataSync dataSync) {
        ContentValues values = new ContentValues();
        values.put(dataSync.getUpdateColumn(), 1);
        contentResolver.update(uri, values, dataSync.getUniqueColumn() + "='" + dataResult + "'", null);
    }

}
