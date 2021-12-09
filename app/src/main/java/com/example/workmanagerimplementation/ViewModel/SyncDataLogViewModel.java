package com.example.workmanagerimplementation.ViewModel;

import android.app.Application;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.workmanagerimplementation.Activity.SyncDataLogActivity;
import com.example.workmanagerimplementation.Models.Pojo.SyncedDataList;
import com.example.workmanagerimplementation.SingleTon.SingleTon;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.data.DataContract;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */
public class SyncDataLogViewModel extends AndroidViewModel {
    private ArrayList<String> allTableNames;

    public SyncDataLogViewModel(@NonNull Application application) {
        super(application);
        this.allTableNames = new ArrayList<>();
    }

    public ArrayList<String> getSqliteUpTableNames(SyncDataLogActivity syncDataLogActivity) {

        AssetManager assetManager = syncDataLogActivity.getAssets();
        try {
            InputStream e = assetManager.open("sync_data_up.xml");
            InputSource inStream = new InputSource(e);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("services");
            for (int temp = 0; temp < nList.getLength(); ++temp) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;

                    String table_name = eElement.getAttribute("table_name").toString();
                    Log.e("table_name",table_name);
                    String update_column = eElement.getAttribute("update_column").toString();
                    String url = eElement.getAttribute("url").toString();
                    String unique_column = eElement.getAttribute("unique_column").toString();
                    String http_method = eElement.getAttribute("http_method").toString();
                    String service = eElement.getAttribute("service").toString();

                    DataSync dataSync = new DataSync(url, http_method, table_name, unique_column, update_column);
                    dataSync.setServiceName(service);
                    allTableNames.add(table_name);
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        return allTableNames;
    }

    public ArrayList<SyncedDataList> getSyncStatusData(ArrayList<String> upTableNames, SyncDataLogActivity activity) {
        ArrayList<SyncedDataList> list = new ArrayList<>();

        for(int i=0;i<upTableNames.size();i++){
            String tableName = upTableNames.get(i);
            String[] mProjection = {
                    "column_id",
                    "is_synced"
            };
            Cursor cursor = activity.getContentResolver().query(DataContract.getUri(tableName),mProjection,null,null,null);


            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{

                        SyncedDataList syncedDataList = new SyncedDataList();
                        syncedDataList.setTableName(tableName);
                        syncedDataList.setColumnID(String.valueOf(cursor.getString(0)));
                        syncedDataList.setIs_synced(String.valueOf(cursor.getString(1)));

                        list.add(syncedDataList);

                    }while (cursor.moveToNext());
                }
            }
        }

        return list;
    }
}
