package com.example.workmanagerimplementation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workmanagerimplementation.Adapter.GridViewAdapter;
import com.example.workmanagerimplementation.Models.EmployeeModel;
import com.example.workmanagerimplementation.Models.MainMenuModel;
import com.example.workmanagerimplementation.Models.Pojo.MainMenu;
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.Session.SessionManager;
import com.example.workmanagerimplementation.SyncUtils.SyncSingleTon;
import com.example.workmanagerimplementation.SyncUtils.data.DBHandler;
import com.example.workmanagerimplementation.Utils.NetworkUtils;
import com.example.workmanagerimplementation.ViewModel.MainActivityViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button logoutBtn;
    private TextView workStatusTv;
    private DBHandler dbHandler;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;
    private EmployeeModel employeeModel;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private TextView startSyncBtn;
    private ProgressBar progressBar;

    SessionManager sessionManager;
    TextView btn_sync;
    private ProgressDialog syncDialog;
    private MainActivityViewModel mainActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        initVariables();
        String pageFrom = getIntent().getStringExtra("pageFrom");


        //Data Sync Loader For The Very First Time (While User Logged In).
        if(pageFrom!=null){
            if(pageFrom.equals("LoginPage")){
                if(mainActivityViewModel.isUserLoggedIn()){
                    mainActivityViewModel.sync(MainActivity.this);
                }
            }
        }
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtils.getConnectivityStatus(getApplicationContext())!=0){
                    //pass data to server
                    SyncSingleTon.getInstance().sync("Menu_List_Data", MainActivity.this);
                }

            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
    }


    private void loadData() {
        MainMenuModel mainMenuModel = new MainMenuModel(getContentResolver());
        ArrayList<MainMenu> mainMenuArrayList = new ArrayList<MainMenu>();

//        mainMenuArrayList = mainMenuModel.getAllMenuList("SR");
        mainMenuArrayList = new ArrayList<>();
        mainMenuModel.sort(mainMenuArrayList);

        Log.e("ALLItem", new Gson().toJson(mainMenuModel.readAllItems()));

        gridViewAdapter = new GridViewAdapter(getApplicationContext(), mainMenuArrayList);
        gridView.setAdapter(gridViewAdapter);
        menuAction(gridView);
    }

    private void menuAction(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainMenu mainMenu = (MainMenu) gridView.getItemAtPosition(position);


                //Toast.makeText(getApplicationContext(),mainMenu.getMenuTitle(), Toast.LENGTH_SHORT).show();

                String title = mainMenu.getMenuTitle();

                switch (title) {
                    case "profile_icon":
                        Toast.makeText(MainActivity.this, "Profile Icon", Toast.LENGTH_SHORT).show();
                        break;
                    case "verify_retailer":
                        Toast.makeText(MainActivity.this, "Verify_Retailer", Toast.LENGTH_SHORT).show();
                        break;
                    case "todays_route":
                        Intent todaysIntent = new Intent(MainActivity.this, TodaysRouteActivity.class);
                        startActivity(todaysIntent);
                        break;
                    case "logout":
                        sessionManager.delteSession();
                        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logout);
                        finish();
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }




    private void initVariables() {
        workStatusTv = findViewById(R.id.workStatusTv);
        sessionManager = new SessionManager(getApplicationContext());
        gridView = findViewById(R.id.gridView);

        btn_sync = findViewById(R.id.btnSync);
        mainActivityViewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sync_menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sync:
                mainActivityViewModel.sync(MainActivity.this);
                return true;
            case R.id.syncHistory:
                startActivity(new Intent(MainActivity.this,SyncDataLogActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}