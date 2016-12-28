package com.project.edn.washit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.edn.washit.R;

public class DetailHistory extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView status,orderno,nameLaundry,addressLaundry,adressPick,orderDate,dateFinish,datePick,cost;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent in=getIntent();
        status=(TextView)findViewById(R.id.status);
        status.setText(in.getStringExtra("status"));
        orderno=(TextView)findViewById(R.id.orderno);
        orderno.setText(in.getStringExtra("orderno"));
        nameLaundry=(TextView)findViewById(R.id.namelaundry);
        nameLaundry.setText(in.getStringExtra("namelaundry"));
        addressLaundry=(TextView)findViewById(R.id.addresLaundry);
        addressLaundry.setText(in.getStringExtra("addresslaundry"));
        adressPick=(TextView)findViewById(R.id.addressCostomer);
        adressPick.setText(in.getStringExtra("addresscustomer"));
        orderDate=(TextView)findViewById(R.id.dateOrder);
        orderDate.setText(in.getStringExtra("orderDate"));
        dateFinish=(TextView)findViewById(R.id.datefinish);
        dateFinish.setText(in.getStringExtra("datefinish"));
        datePick=(TextView)findViewById(R.id.datepick);
        datePick.setText(in.getStringExtra("datepick"));
        cost=(TextView)findViewById(R.id.cost);
        cost.setText(in.getStringExtra("cost"));
        img=(ImageView)findViewById(R.id.iconLaundry);
        Glide.with(getApplicationContext()).load(in.getStringExtra("image"))
                .centerCrop()
                .error(R.drawable.placehholder_image)
                .placeholder(R.drawable.placehholder_image)
                .into(img);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
