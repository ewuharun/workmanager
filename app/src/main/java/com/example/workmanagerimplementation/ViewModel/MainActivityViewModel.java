package com.example.workmanagerimplementation.ViewModel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.workmanagerimplementation.Activity.MainActivity;
import com.example.workmanagerimplementation.Session.SessionManager;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataDownWorker;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataUpWorker;
import com.example.workmanagerimplementation.Utils.CommonPickerUtils;

import java.util.Date;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */
public class MainActivityViewModel extends AndroidViewModel {

    private Context mContext;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.mContext = application;
        sessionManager = new SessionManager(application);
    }

    public void sync(MainActivity mainActivity) {

        Data dataDown = new Data.Builder()
                .putString("isCompleted", "false")
                .build();

        final OneTimeWorkRequest dataDownWorkRequest = new OneTimeWorkRequest.Builder(DataDownWorker.class)
                .setInputData(dataDown)
                //.setConstraints(constraints)
                .build();

        final OneTimeWorkRequest dataUpWorker = new OneTimeWorkRequest.Builder(DataUpWorker.class).build();


        WorkManager.getInstance().beginWith(dataUpWorker).then(dataDownWorkRequest).enqueue();

        progressDialog = CommonPickerUtils.progressDialog("Syncing...", "Please wait until it finished",mainActivity);

        WorkManager.getInstance().getWorkInfoByIdLiveData(dataDownWorkRequest.getId())
                .observe(mainActivity, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        Toast.makeText(mContext, workInfo.getState().name(), Toast.LENGTH_SHORT).show();

                        if (workInfo.getState() == WorkInfo.State.RUNNING) {

                        }
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Log.e("EndTime", String.valueOf(new Date().getTime()));

                        }


                        //Receiving the Data Back
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            progressDialog.dismiss();
                            workInfo.getOutputData().getString("isCompleted");
                            WorkManager.getInstance().cancelAllWork();

                        }
                    }
                });
    }

    public boolean isUserLoggedIn() {
        return sessionManager.getIsLoggedIn();
    }
}
