package com.project.edn.washit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.edn.washit.R;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnedit,btnchangepw;
    private TextView name,telp,email;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnedit=(Button)findViewById(R.id.btnedit);
        btnchangepw=(Button)findViewById(R.id.btnChangepw);
        btnedit.setOnClickListener(this);
        btnchangepw.setOnClickListener(this);
        name=(TextView)findViewById(R.id.txt_nameuser);
        name.setText(sp.getString("name",""));
        telp=(TextView)findViewById(R.id.txt_telpuser);
        telp.setText(sp.getString("telp",""));
        email=(TextView)findViewById(R.id.txt_emailuser);
        email.setText(sp.getString("email",""));
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
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){
            case R.id.btnedit:
                in =new Intent(AccountActivity.this,EditAccActivity.class);
                startActivity(in);
                break;
            case R.id.btnChangepw:
                in=new Intent(AccountActivity.this,ChangePasswordAcitivty.class);
                startActivity(in);
                break;

        }
    }
}
