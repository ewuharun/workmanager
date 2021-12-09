package com.example.wrapper.SyncUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.wrapper.NetWorkUtils.NetworkStream;
import com.example.wrapper.R;
import com.example.wrapper.SyncUtils.HelperUtils.DataServices;
import com.example.wrapper.SyncUtils.HelperUtils.DataSync;
import com.example.wrapper.SyncUtils.HelperUtils.DataSyncModel;
import com.example.wrapper.SyncUtils.HelperUtils.JsonParser;
import com.example.wrapper.SyncUtils.HelperUtils.Maths;
import com.example.wrapper.SyncUtils.data.DataContract;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SyncActivity extends AppCompatActivity implements SyncTable {
    private DataServices dataServices;
    private String url;
    private DataSyncModel dataSyncModel;
    private String table_name;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        setupToolbar();


    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sync_toolbar);
        toolbar.setTitle("Syncing");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sync_menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sync:
                Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void sync(String tableName, Context context) {
        table_name = tableName;
        mContext = context;

        dataServices = new DataServices(context);
        ArrayList<DataSync> dataUpService = dataServices.dataUpServices();

        String applicationUrl = context.getString(R.string.APPLICATION_URL);

        for (DataSync dataSync : dataUpService) {

            if (tableName.equals(dataSync.getTableName())) {

                Uri uri = DataContract.getUri(dataSync.getTableName());

                url = applicationUrl + dataSync.getServiceUrl();

                String condition = dataSync.getUpdateColumn() + "='0'";

                 dataSyncModel = new DataSyncModel(context.getContentResolver());

                for (ArrayList<NameValuePair> sqliteData : dataSyncModel.getSqliteData(uri, condition)) {
                    int httpMethod = 2;

                    if (Maths.isInteger(dataSync.getHttpMethod())) {
                        httpMethod = Integer.parseInt(dataSync.getHttpMethod());
                    }

                    new UploadTableData().execute(sqliteData);

                }


            }
        }


    }

    private class UploadTableData extends AsyncTask<ArrayList<NameValuePair>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<NameValuePair>... urls) {
            List<NameValuePair> sqliteData = urls[0];

            String dataPut = new NetworkStream().getStream(url, 2, sqliteData);
            Log.e("HittedURL====>",url);
            Log.e("POST====>",dataPut);
            Log.e("sqliteData ",sqliteData.toString());
            String columnId = JsonParser.ifValidJSONGetColumnId(dataPut);
            return columnId;
        }

        @Override
        protected void onPostExecute(String columnId) {

            if (columnId != null) {
                ContentValues values = new ContentValues();
                values.put("is_synced", 1);
                mContext.getContentResolver().update(DataContract.getUri(table_name), values, "column_id" + "='" + columnId + "'", null);
            }
        }
    }


}