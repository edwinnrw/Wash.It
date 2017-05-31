package com.project.edn.washit.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.edn.washit.R;

public class DetailMerchant extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private GoogleMap mMap;
    private LinearLayout call,order;
    private TextView phone,address,cost,service,material,hour,nameLaundry;
    private Intent in;
    private ImageView imgLaundry;
    private Toolbar toolbar;
    private boolean isShow = false;
    private int scrollRange = -1;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String ket;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_merchant);
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map1, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        in=getIntent();
        id =in.getStringExtra("id_laundry");
        ket =in.getStringExtra("Ket");
        call=(LinearLayout) findViewById(R.id.callBtn);
        call.setOnClickListener(this);
        order=(LinearLayout)findViewById(R.id.orderBtn);
        order.setOnClickListener(this);

        phone=(TextView)findViewById(R.id.telp);
        phone.setText(in.getStringExtra("phone"));
        cost=(TextView)findViewById(R.id.cost);
        cost.setText("Rp."+in.getStringExtra("cost")+"/Kg");
        hour=(TextView)findViewById(R.id.bushoure);
        hour.setText(in.getStringExtra("hour"));
        nameLaundry=(TextView)findViewById(R.id.laundryname);
        nameLaundry.setText(in.getStringExtra("name"));
        material=(TextView)findViewById(R.id.materialsupport);
        material.setText(in.getStringExtra("material"));
        service=(TextView)findViewById(R.id.service);
        service.setText(in.getStringExtra("service"));
        address=(TextView)findViewById(R.id.txt_addressLaundry);
        address.setText(in.getStringExtra("address"));
        imgLaundry=(ImageView)findViewById(R.id.mainbackdrop);
        Glide.with(this).load(in.getStringExtra("image"))
                .centerCrop()
                .error(R.drawable.placehholder_image)
                .placeholder(R.drawable.placehholder_image)
                .into(imgLaundry);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapseToolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(zoom);
        // Add a marker in Sydney and move the camera
        LatLng post = new LatLng(in.getDoubleExtra("lat",0), in.getDoubleExtra("long",0));
        mMap.addMarker(new MarkerOptions().position(post).title("Posisiton"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(post));
    }

    @Override
    public void onClick(View view) {
        Intent in1;
        switch (view.getId()){
            case R.id.callBtn:
                in1= new Intent(Intent.ACTION_DIAL);
                in1.setData(Uri.parse(phone.getText().toString()));
                startActivity(in1);
                break;
            case R.id.orderBtn:
                in1=new Intent(DetailMerchant.this, OrderForm.class);
                in1.putExtra("Ket", ket);
                in1.putExtra("id", id);
                startActivity(in1);
                break;
            default:
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            collapsingToolbarLayout.setTitle(in.getStringExtra("name"));
            isShow = true;
        } else if(isShow) {
            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
            isShow = false;
        }
    }
}
