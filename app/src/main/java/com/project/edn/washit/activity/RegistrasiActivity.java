package com.project.edn.washit.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit.Config;
import com.project.edn.washit.R;
import com.project.edn.washit.api.ServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegistrasiActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private Button btnSubmit;
    private EditText edtRegEmail;
    private EditText edtRegName;
    private EditText edtRegPassword;
    private EditText edtRegPhone;
    private EditText edtRegconfrimPassword;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edtRegName=(EditText)findViewById(R.id.edtRegName);
        edtRegName.addTextChangedListener(this);
        edtRegEmail=(EditText)findViewById(R.id.edtRegEmail);
        edtRegEmail.addTextChangedListener(this);
        edtRegPhone=(EditText)findViewById(R.id.edtRegPhone);
        edtRegPhone.addTextChangedListener(this);
        edtRegPassword=(EditText)findViewById(R.id.edtRegPass);
        edtRegPassword.addTextChangedListener(this);
        edtRegconfrimPassword=(EditText)findViewById(R.id.edtConfrimPass);
        edtRegconfrimPassword.addTextChangedListener(this);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
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
    public void requestRegis(){
        Map<String, String> params = new HashMap<>();
        params.put("name", RegistrasiActivity.this.edtRegName.getText().toString());
        params.put("email", RegistrasiActivity.this.edtRegEmail.getText().toString());
        params.put("phone", RegistrasiActivity.this.edtRegPhone.getText().toString());
        params.put("password", RegistrasiActivity.this.edtRegPassword.getText().toString());
        params.put("level", "user");
        ServiceHelper.getInstance().register(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                String json= null;
                try {
                    json = response.body().string();
                    if (RegistrasiActivity.this.Success(json).equalsIgnoreCase("true")) {
                        Toast.makeText(RegistrasiActivity.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrasiActivity.this, json, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_REGISTER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //Jika Respon server sukses
//                        progressDialog.dismiss();
//                        if (RegistrasiActivity.this.Success(response).equalsIgnoreCase("true")) {
//                            Toast.makeText(RegistrasiActivity.this, "Success", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(RegistrasiActivity.this, response, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
//                        Toast.makeText(RegistrasiActivity.this, "Failed Load Your Data,Check Your Connection"+error.getMessage() , Toast.LENGTH_LONG).show();
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name", RegistrasiActivity.this.edtRegName.getText().toString());
//                params.put("email", RegistrasiActivity.this.edtRegEmail.getText().toString());
//                params.put("phone", RegistrasiActivity.this.edtRegPhone.getText().toString());
//                params.put("password", RegistrasiActivity.this.edtRegPassword.getText().toString());
//                params.put("level", "user");
//                return params;
//            }
//        };
//
//        //Tambahkan Request String ke dalam Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        int socketTimeout = 100000;//30 seconds - change to what you want
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        stringRequest1.setRetryPolicy(policy);
//        requestQueue.add(stringRequest1);
    }
    public String Success(String json) {
        String succes = "";
        try {
            succes = new JSONObject("{\"response\":" + json + "}").getJSONObject("response").getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return succes;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (edtRegName.getText().toString().trim().length() != 0) {
            edtRegName.setError(null);
        }
        if (edtRegEmail.getText().toString().trim().length() != 0) {
            edtRegEmail.setError(null);
        }
        if (edtRegPhone.getText().toString().trim().length() != 0) {
            edtRegPhone.setError(null);
        }
        if (edtRegPassword.getText().toString().trim().length() != 0) {
            edtRegPassword.setError(null);
        }
        if (edtRegconfrimPassword.getText().toString().trim().length() != 0) {
            edtRegconfrimPassword.setError(null);
        } else if (edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())) {
            edtRegconfrimPassword.setError(null);
        }
    }

    @Override
    public void onClick(View view) {
        int edtRegNameLenght = edtRegName.getText().toString().trim().length();
        int edtRegEmailLenght = edtRegEmail.getText().toString().trim().length();
        int edtRegPhoneLenght = edtRegPhone.getText().toString().trim().length();
        int edtRegPasswordLenght = edtRegPassword.getText().toString().trim().length();
        int edtRegConfirmPasswordLenght= edtRegconfrimPassword.getText().toString().trim().length();
        if (edtRegNameLenght == 0 || edtRegEmailLenght == 0 || edtRegPhoneLenght == 0 || edtRegPasswordLenght == 0 || edtRegConfirmPasswordLenght == 0) {
            if (edtRegNameLenght == 0) {
                edtRegEmail.setError("This Field is Required");
            }
            if (edtRegEmailLenght == 0) {
                edtRegEmail.setError("This Field is Required");
            }
            if (edtRegPhoneLenght == 0) {
                edtRegPhone.setError("This Field is Required");
            }
            if (edtRegPasswordLenght == 0) {
                edtRegPassword.setError("This Field is Required");
            } else if (edtRegPasswordLenght < 8) {
                edtRegPassword.setError("Min Password 8 Characters");
            }
            if (edtRegConfirmPasswordLenght == 0) {
                edtRegconfrimPassword.setError("This Field is Required");
            } else if (!edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())) {
                edtRegconfrimPassword.setError("Not Same Your Password");
            }
        } else if (edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())) {
            progressDialog.setMessage("Loading");
            progressDialog.show();
            requestRegis();
        } else {
            edtRegconfrimPassword.setError("Not Same Your Password");
        }

    }
}
