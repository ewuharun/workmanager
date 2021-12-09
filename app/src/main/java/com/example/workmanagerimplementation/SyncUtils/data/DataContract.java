package com.example.workmanagerimplementation.SyncUtils.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY="com.example.workmanagerimplementation.SyncUtils.data";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);


    public static final String PATH_MENU_LIST="Menu_List_Data";
    public static final String PATH_TRANSPORTATION_DATA = "TBL_TRANSPORTATION_DATA";


    public static Uri getUri(String tableName){
        return BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
    }

    public static class MenuListEntry implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU_LIST).build();
        public static final String TABLE_NAME="Menu_List_Data";
        public static final String COLUMN_ID="column_id";
        public static final String MENU_ID="menu_id";
        public static final String TITLE="title";
        public static final String LOGO="logo";
        public static final String POSITION="position";
        public static final String ROLE_CODE="role_code";
        public static final String UPDATED_AT="updated_at";
        public static final String IS_SYNCED="is_synced";
    }

    public static class TblTransportationData implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSPORTATION_DATA).build();
        public static final String TABLE_NAME="TBL_TRANSPORTATION_DATA";
        public static final String COLUMN_ID="column_id";
        public static final String NAME="name";
    }

}
