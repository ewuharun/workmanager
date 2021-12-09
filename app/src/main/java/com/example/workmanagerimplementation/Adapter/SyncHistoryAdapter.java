package com.example.workmanagerimplementation.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagerimplementation.Activity.SyncDataLogActivity;
import com.example.workmanagerimplementation.Models.Pojo.SyncedDataList;
import com.example.workmanagerimplementation.NetWorkUtils.NetworkStream;
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.Maths;
import com.example.workmanagerimplementation.SyncUtils.data.DBHandler;
import com.example.workmanagerimplementation.SyncUtils.data.DataContract;
import com.example.workmanagerimplementation.Utils.CommonPickerUtils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import static com.example.workmanagerimplementation.R.drawable.abc_ic_clear_material;
import static com.example.workmanagerimplementation.R.drawable.ic_baseline_call_missed_outgoing_24;
import static com.example.workmanagerimplementation.R.drawable.ic_baseline_clear_24;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */
public class SyncHistoryAdapter extends RecyclerView.Adapter<SyncHistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SyncedDataList> list;
    private SyncDataLogActivity activity;

    DBHandler mOpenHelper;
    SQLiteDatabase db;

    public SyncHistoryAdapter(Context mContext, ArrayList<SyncedDataList> list, SyncDataLogActivity activity) {
        this.mContext = mContext;
        this.list = list;
        this.activity = activity;

        mOpenHelper = new DBHandler(mContext);
        db = mOpenHelper.getWritableDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_synced_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SyncedDataList syncedData = list.get(position);
        SyncedDataList previousSyncData = list.get(position);





        if (position > 1) {
            previousSyncData = list.get(position - 1);
        }
        holder.index.setText(String.valueOf(position));
        holder.tableName.setText(syncedData.getTableName());
        holder.columnID.setText(syncedData.getColumnID());
        holder.syncStatus.setText(syncedData.getIs_synced());



        if (!syncedData.getTableName().equals(previousSyncData.getTableName())) {
            holder.itemView.setBackgroundColor(Color.rgb(205, 143, 122));
        }

        if (syncedData.getIs_synced().equals("0")) {
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(ic_baseline_clear_24));
        }

        if (syncedData.getIs_synced().equals("1")) {
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(ic_baseline_call_missed_outgoing_24));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = getAllColumnValue(syncedData.getTableName(), syncedData.getColumnID());
                CommonPickerUtils.showAlertMessage(activity, "Data", string);
            }
        });

    }

    private String getAllColumnValue(String tableName, String columnID) {
        String[] projection={"*"};
        Cursor cursor=mContext.getContentResolver().query(DataContract.getUri(tableName),projection,"column_id = "+columnID,null,null);

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
        return data.toString();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView tableName;
        private TextView columnID;
        private TextView syncStatus;
        private TextView index;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.tvIndex);
            syncStatus = itemView.findViewById(R.id.tvSyncStatus);
            columnID = itemView.findViewById(R.id.tvColumnID);
            tableName = itemView.findViewById(R.id.tvTableName);
            icon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
