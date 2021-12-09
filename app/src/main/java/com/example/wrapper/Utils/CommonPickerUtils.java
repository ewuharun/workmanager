package com.example.wrapper.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.wrapper.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public class CommonPickerUtils {
    public static int i = 0;

    public static void datePick(final EditText etReqFromDate, final Activity mActivity) {
        final String[] selectedDate = new String[1];
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selectedDate[0] = sdf.format(myCalendar.getTime());
                etReqFromDate.setText(selectedDate[0]);
//                paymentInformation.setPaymentDate(requisitionDate);

            }
        };

        etReqFromDate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//       new DatePickerDialog(mActivity, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR),
//               myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.Theme_AppCompat, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.design_default_color_background);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.design_default_color_background);

            }
        });
    }

    public static int numberPicker(final NumberPicker numberPicker, final String[] pickerVals, int min, int max, final EditText etStatus) {
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setDisplayedValues(pickerVals);
//        etStatus.setText(pickerVals[0]);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("checSelectedValue", picker.getValue() + "");
                i = picker.getValue();
                etStatus.setText(pickerVals[i] + "");
            }
        });
        Log.e("another", i + "");
        return i;
    }


    public static long beginDownload(String file_link, Context mContext, String auth, String imageName) {
        long downloadId;
        File file = new File(mContext.getExternalFilesDir(null), "Dummy");

        //checking if android version is equal and greater than noughat

        //now if download complete file not visible now lets show it
        DownloadManager.Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(file_link))
                    .setTitle(imageName)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .addRequestHeader("Authorization", auth)
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(file_link))
                    .setTitle("Dummy")
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverRoaming(true);
        }

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
        return downloadId;
    }

    //    public static void downloadMessage(final long downloadId, final Activity mActivity, final ProgressBar progressBar){
//        BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                long id=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
//                if(downloadId==id){
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(mActivity, "Download Completed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//    }
    public static void exitApp(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_baseline_add_circle_24);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        activity.finish();
//                        System.exit(0);
                        activity.moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static AlertDialog.Builder showAlertMessage(Context mContext, String title, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "hdhf", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
        return builder;
    }

    public static void showAlertMessageWithCancel(Context mContext, Activity mActivity, String title, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat);

        builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + message + "</font>"))
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#ED1C24'>OK</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(homeIntent);
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#ED1C24'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        builder.show();

    }

    public static void showAlertInSalesOrder(Context mContext, Activity mActivity, String title, String message, String activityName, Class thisClass) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat);

        builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + message + "</font>"))
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#ED1C24'>OK</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(mActivity, thisClass);
                        intent.putExtra("activity", activityName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivity.startActivity(intent);
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#ED1C24'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        builder.show();

    }

    public static ProgressDialog progressDialog(String title, String message, Context mContext) {
        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(mContext);
        progressDoalog.setCancelable(false);
        progressDoalog.setMax(100);
        progressDoalog.setTitle(title);
        progressDoalog.setMessage(message);

        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        return progressDoalog;
    }
}
