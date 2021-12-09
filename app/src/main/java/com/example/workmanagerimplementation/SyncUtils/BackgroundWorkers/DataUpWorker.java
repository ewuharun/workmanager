package com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.Maths;
import com.example.workmanagerimplementation.NetWorkUtils.NetworkStream;
import com.example.workmanagerimplementation.SyncUtils.data.DataContract;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class DataUpWorker extends Worker {

    private DataServices dataServices;
    private ContentResolver contentResolver;
    private Context context;
    private ArrayList<String> upTableName;


    public DataUpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.contentResolver=context.getContentResolver();
        this.upTableName = new ArrayList<>();
    }

    @NonNull
    @Override
    public Result doWork() {

        uploadAllDataIntoTheServer();

        return Result.success();
    }

    private void uploadAllDataIntoTheServer() {
        String service="up";
        String applicationUrl=getApplicationContext().getString(R.string.APPLICATION_URL);
        dataServices=new DataServices(getApplicationContext());
        ArrayList<DataSync> dataUpService=dataServices.dataUpServices();
        for(DataSync dataSync : dataUpService){
            if (service.equalsIgnoreCase("up")){

                Uri uri= DataContract.getUri(dataSync.getTableName());

                String url=applicationUrl+dataSync.getServiceUrl();
                Log.e("Url : ",url);

                String condition = dataSync.getUpdateColumn() + "='0'";

                DataSyncModel dataSyncModel=new DataSyncModel(contentResolver);

                for(ArrayList<NameValuePair> sqliteData : dataSyncModel.getSqliteData(uri,condition)){
                    int httpMethod=1;
                    Log.e("sqliteData ",sqliteData.toString());

                    if(Maths.isInteger(dataSync.getHttpMethod())){
                        httpMethod=Integer.parseInt(dataSync.getHttpMethod());
                    }

                    String dataPut = new NetworkStream().getStream(url, httpMethod, sqliteData);
                    Log.e("HittedUrl====>",url);
                    String columnId = JsonParser.ifValidJSONGetColumnId(dataPut);
                    Log.e("POST====>", dataPut);


                    if (columnId != null) {
                        dataSyncModel.updateSynced(uri, columnId, dataSync);
                    }

                }

            }
        }
    }
}
