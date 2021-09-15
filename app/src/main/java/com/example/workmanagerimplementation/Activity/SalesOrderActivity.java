package com.example.workmanagerimplementation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.workmanagerimplementation.Adapter.ViewpagerAdapter;
import com.example.workmanagerimplementation.Fragments.OrderFragment;
import com.example.workmanagerimplementation.Fragments.PaymentFragment;
import com.example.workmanagerimplementation.Fragments.SummaryFragment;
import com.example.workmanagerimplementation.R;
import com.google.android.material.tabs.TabLayout;

public class SalesOrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrderFragment orderFragment;
    private SummaryFragment summaryFragment;
    private PaymentFragment paymentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Arafat Traders");

        init();

        tabLayout.setupWithViewPager(viewPager);
        setup(viewPager);



    }

    private void setup(ViewPager viewPager) {
        ViewpagerAdapter adapter=new ViewpagerAdapter(getSupportFragmentManager());
        orderFragment=OrderFragment.newInstance("param1","param2");
        summaryFragment=SummaryFragment.newInstance("param","param");
        paymentFragment=PaymentFragment.newInstance("param","param");
        adapter.addFragment(orderFragment,"order");
        adapter.addFragment(summaryFragment,"Summary");
        adapter.addFragment(paymentFragment,"Payment");
        viewPager.setAdapter(adapter);
    }

    private void init() {
        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tabLaout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}