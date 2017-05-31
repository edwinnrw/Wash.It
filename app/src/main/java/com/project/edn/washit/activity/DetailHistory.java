package com.project.edn.washit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.project.edn.washit.Config;
import com.project.edn.washit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailHistory extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView status,orderno,nameLaundry,addressLaundry,adressPick,orderDate,dateFinish,datePick,cost;
    private ImageView img;
    private Button confirm;
    private String orderid;
    private ProgressDialog progressDialog;
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
        confirm=(Button)findViewById(R.id.confirm); 
        status.setText(in.getStringExtra("status"));
        if (status.getText().toString().equalsIgnoreCase("Finished/Process Delivery")){
            confirm.setVisibility(View.VISIBLE);
        }
        confirm.setOnClickListener(this);
        orderno=(TextView)findViewById(R.id.orderno);
        orderid=in.getStringExtra("orderno");
        orderno.setText("Order No:"+in.getStringExtra("orderno"));
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
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
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
        progressDialog.show();
//        confirmComplete();
    }

//    private void confirmComplete() {
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_CONFIRM,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    progressDialog.dismiss();
//                    //Jika Respon server sukses
//                    if (Success(response).equalsIgnoreCase("true")) {
//                        Toast.makeText(DetailHistory.this, "Success ", Toast.LENGTH_LONG).show();
//                        Intent in=new Intent(DetailHistory.this,MainActivity.class);
//                        startActivity(in);
//                    } else {
//                        Toast.makeText(DetailHistory.this, "Failed ", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
//                    Toast.makeText(DetailHistory.this, "Failed Load Your Data,Check Your Connection"+error.getMessage() , Toast.LENGTH_LONG).show();
//
//                }
//            }) {
//        @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//            //Parameter
//            params.put("id_order", orderid);
////                params.put("")
////                params.put("token", token);
//            //Kembalikan Nilai parameter
//            return params;
//        }
//    };
//
//        //Tambahkan Request String ke dalam Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        int socketTimeout = 10000;//30 seconds - change to what you want
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        stringRequest1.setRetryPolicy(policy);
//        requestQueue.add(stringRequest1);
//    }
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
}
