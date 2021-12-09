package com.example.wrapper.SyncUtils.BackgroundWorkers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.wrapper.R;
import com.example.wrapper.SyncUtils.HelperUtils.DataServices;
import com.example.wrapper.SyncUtils.HelperUtils.DataSyncModel;
import com.example.wrapper.SyncUtils.HelperUtils.JsonParser;
import com.example.wrapper.NetWorkUtils.NetworkStream;
import com.example.wrapper.SyncUtils.HelperUtils.DataSync;
import com.example.wrapper.SyncUtils.data.DBHandler;
import com.example.wrapper.SyncUtils.data.DataContract;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataDownWorker extends Worker {

    private ContentResolver contentResolver;
    private DataServices dataServices;
    private DBHandler dbHandler;

    private ArrayList<HashMap<String,String>> tableInfo;

    public DataDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contentResolver=context.getContentResolver();
        tableInfo = new ArrayList<>();
    }

    @NonNull
    @Override
    public Result doWork() {

        int startTime= (int) System.currentTimeMillis();

        tableInfo = getAllTableNames();

        for(int i=0;i<tableInfo.size();i++){
            String table = tableInfo.get(i).get("tableName");
            String url = tableInfo.get(i).get("url");
            DownloadAllData(table,url);
        }


        int totalTimeRequired= (int) (System.currentTimeMillis()-startTime);

        //hh:mm:ss
        String time=String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(totalTimeRequired),
                TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTimeRequired)),
                TimeUnit.MILLISECONDS.toSeconds(totalTimeRequired) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired)));

        Log.e("TotalTimeRequired",time);

        //allData will be passed by this Data class in the main view
        Data data=new Data.Builder()
                .putString("isCompleted","true")
                .build();

        //Return Back the success status with data
        return Result.success(data);
    }

    private ArrayList<HashMap<String,String>> getAllTableNames() {

        AssetManager assetManager= getApplicationContext().getAssets();
        try{
            InputStream inputStream=assetManager.open("sync_data_down.xml");
            InputSource inputSource=new InputSource(inputStream);
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(inputSource);
            document.getDocumentElement().normalize();
            NodeList nodeList=document.getElementsByTagName("services");
            for (int temp = 0; temp < nodeList.getLength(); ++temp) {
                Node nNode = nodeList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;
                    String url = eElement.getAttribute("url").toString();
                    String httpMethod = eElement.getAttribute("http_method").toString();
                    String uniqueColumn = eElement.getAttribute("unique_column").toString();
                    String whereCondition = eElement.getAttribute("where_condition").toString();
                    String service = eElement.getAttribute("service").toString();
                    Log.e("TABLE_NAME",service);
                    DataSync dataSync = new DataSync(url, httpMethod, uniqueColumn, whereCondition);
                    dataSync.setServiceName(service);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put("tableName",service);
                    hashMap.put("url",url);

                    tableInfo.add(hashMap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tableInfo;

    }


    private void DownloadAllData(String tableName,String link) {
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("role_code","SR"));
        String url =getApplicationContext().getString(R.string.APPLICATION_URL).concat(link);
        Log.e("GET_DATA====>",url);
        String response=new NetworkStream().getStream(url,2,params);
        Log.e("RESPONSE====>",response);
        HashMap<String,ContentValues> hashMap=JsonParser.getColIdAndValues(response,tableName);
        Uri uri=DataContract.getUri(tableName);
        HashMap<String,String> tableData=new DataSyncModel(contentResolver).getUniqueColumn(uri,"column_id","");
        insert(tableData,hashMap,uri,tableName);

    }

    public void insert(HashMap<String,String> tableData,HashMap<String,ContentValues> values,Uri uri,String tableName) {
        dbHandler=new DBHandler(getApplicationContext());
        SQLiteDatabase db=dbHandler.getWritableDatabase();
        db.delete(tableName,null,null);
        try {
            db.beginTransaction();
            int i=1;
            for(String key: values.keySet()){
//                if(!tableData.containsKey(key)){
                    ContentValues value = values.get(key);
                    long result=db.insert(tableName,null,value);
                    Log.d("INSERTED",uri.getPath()+"/"+" COLUMN_ID /"+key+" Increment/ "+i);
                    i++;
//                }else{
//                    Log.e("MIs" + key, ": Already Exists");
//                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }

    }


    /*Displaying a simple notification while task is done*/
    private void displayNotification(String title,String task){
        NotificationManager notificationManager=(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //Checking out the version of sdk for displaying notification
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("WorkManager","manager",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"WorkManager")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());

    }
}
