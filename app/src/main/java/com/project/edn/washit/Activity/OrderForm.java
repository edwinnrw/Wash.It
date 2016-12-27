package com.project.edn.washit.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.project.edn.washit.Config;
import com.project.edn.washit.Fragment.DatePickerFragment;
import com.project.edn.washit.Fragment.TimePickerFragment;
import com.project.edn.washit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderForm extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText date,time;
    private EditText address;
    private EditText detail;
    private EditText note;
    private Button btn;
    private Toolbar toolbar;
    private static int FROM_LOCATION_REQUEST_CODE = 0;
    private LatLng addressLatLng;
    private double addressLongitude;
    private double addressLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        date=(EditText)findViewById(R.id.date);

        date.addTextChangedListener(this);
        date.setKeyListener(null);
        date.setFocusable(false);
        date.setOnClickListener(this);
        time=(EditText)findViewById(R.id.time);
        time.setKeyListener(null);
        time.setFocusable(false);
        time.setOnClickListener(this);
        address=(EditText)findViewById(R.id.addresspick);
        address.setFocusable(false);
        address.addTextChangedListener(this);
        address.setOnClickListener(this);
        detail=(EditText)findViewById(R.id.detailaddress);
        detail.addTextChangedListener(this);
        note=(EditText)findViewById(R.id.note);
        btn=(Button)findViewById(R.id.submit);
        btn.setOnClickListener(this);

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
                int lenghhtDetail=detail.getText().toString().trim().length();
                if (lenghtDate==0 || lenghtTime==0 || lenghtAddress==0 || lenghhtDetail==0){
                    if (lenghtDate==0){
                        date.setError("This Field Is Required");
                    }if(lenghtTime==0){
                        time.setError("This Field Is Required");
                    }if (lenghtAddress==0){
                        address.setError("This Field Is Required");
                    }if (lenghhtDetail==0){
                        detail.setError("This Field Is Required");
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
            case R.id.addresspick:
                Intent change =new Intent(OrderForm.this, SetAddress.class);
                startActivityForResult(change, FROM_LOCATION_REQUEST_CODE);
                break;
        }


    }
    public void requesrOrder(){
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")){
                            showMessage();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(OrderForm.this, "Failed Load Your Data,Check Your Connection"+error.getMessage() , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
//                params.put("token", token);
                  params.put("date",date.getText().toString().trim());
                  params.put("time",time.getText().toString().trim());
                  params.put("address",address.getText().toString().trim());
                  params.put("detail",detail.getText().toString().trim());
                  params.put("note",note.getText().toString().trim());

                //Kembalikan Nilai parameter
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest1.setRetryPolicy(policy);
        requestQueue.add(stringRequest1);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                addressLatLng = new LatLng(data.getDoubleExtra("LAT", 0), data.getDoubleExtra("LON", 0));
                addressLatitude=data.getDoubleExtra("LAT",0);
                addressLongitude=data.getDoubleExtra("LON",0);
                address.setText(data.getStringExtra("ADDRESS"));
            }
        }

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
                        requesrOrder();
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
        myMsg.setText("Your Order Is Succes,Please Wait Respons From Laundry");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);
        alertDialogBuilder.setPositiveButton("OK",
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
        if (detail.getText().toString().trim().length()!=0){
            date.setError(null);
        }
    }
}
