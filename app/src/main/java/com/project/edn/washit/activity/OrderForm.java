package com.project.edn.washit.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.project.edn.washit.Config;
import com.project.edn.washit.api.ServiceHelper;
import com.project.edn.washit.fragment.DatePickerFragment;
import com.project.edn.washit.fragment.TimePickerFragment;
import com.project.edn.washit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OrderForm extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText address;
    private Button btn;
    private EditText date;
    private Intent in;
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private EditText time;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        in = getIntent();
        date=(EditText)findViewById(R.id.date);

        date.addTextChangedListener(this);
        date.setKeyListener(null);
        date.setFocusable(false);
        date.setOnClickListener(this);
        time=(EditText)findViewById(R.id.time);
        time.setKeyListener(null);
        time.setFocusable(false);
        time.setOnClickListener(this);
        address=(EditText) findViewById(R.id.addresspick);
        btn=(Button)findViewById(R.id.submit);
        btn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
        switch (view.getId()){
            case R.id.submit:
                int lenghtDate=date.getText().toString().trim().length();
                int lenghtTime=time.getText().toString().trim().length();
                int lenghtAddress=address.getText().toString().trim().length();
                if (lenghtDate==0 || lenghtTime==0 || lenghtAddress==0 ){
                    if (lenghtDate==0){
                        date.setError("This Field Is Required");
                    }if(lenghtTime==0){
                        time.setError("This Field Is Required");
                    }if (lenghtAddress==0){
                        address.setError("This Field Is Required");
                    }
                }else{
                    showConfirm();
                }
                break;
            case R.id.time:
                new TimePickerFragment(time).show(getFragmentManager(), "timepicker");

                break;

            case R.id.date:
                new DatePickerFragment(date).show(getFragmentManager(), "datepicker");
                break;
        }


    }
    public void requesrOrder(final String date, final String time, final String address){
        Map<String, String> params = new HashMap<>();
                //Parameter
//                params.put("token", token);
        params.put("type",in.getStringExtra("Ket"));
        params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
        params.put("id_laundry", in.getStringExtra("id"));
        params.put("address", address);
        params.put("datepick", date);
        params.put("time", time);
        params.put("note","Tidak ada");
        ServiceHelper.getInstance().order(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String json=response.body().string();
                    progressDialog.dismiss();
                    if (Success(json).equalsIgnoreCase("true")) {
                        showMessage();
                    }else {
                        Toast.makeText(OrderForm.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    public String Success(String json) {
        String succes = "";
        try {
            String JSON_STRING = "{\"response\":" + json + "}";
            JSONObject emp = (new JSONObject(JSON_STRING)).getJSONObject("response");
            succes = emp.getString("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return succes;
    }

    public  void showConfirm(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(OrderForm.this);
        alertDialogBuilder.setTitle("Confirm");
        TextView myMsg = new TextView(this);
        myMsg.setText(" Are You Sure to Continue This Process?");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        progressDialog.show();
                        requesrOrder(date.getText().toString().trim(),time.getText().toString().trim(),address.getText().toString());
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        // Tampilkan alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));


    }
    public  void showMessage(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(OrderForm.this);
        alertDialogBuilder.setTitle("Confirm");
        TextView myMsg = new TextView(this);
        myMsg.setText("Your Order Is Succes\nPlease Wait Respons From Laundry");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent in =new Intent(OrderForm.this,MainActivity.class);
                        startActivity(in);

                    }
                });

        // Tampilkan alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (date.getText().toString().trim().length()!=0){
            date.setError(null);
        }
        if (time.getText().toString().length()!=0){
            time.setError(null);
        }
        if (address.getText().toString().trim().length()!=0){
            date.setError(null);
        }
    }
}
