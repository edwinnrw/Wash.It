package com.project.edn.washit.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordAcitivty extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Toolbar toolbar;
    private EditText confrimPassword,newPassword;
    private Button btnSave;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        confrimPassword=(EditText)findViewById(R.id.confirmpw);
        confrimPassword.addTextChangedListener(this);
        newPassword=(EditText)findViewById(R.id.newpw);
        newPassword.addTextChangedListener(this);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
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

        int edtnewPasswordLenght = newPassword.getText().toString().trim().length();
        int edtConfirmPasswordLenght = confrimPassword.getText().toString().trim().length();
        if (edtnewPasswordLenght == 0 || edtConfirmPasswordLenght == 0) {
            if (edtnewPasswordLenght == 0) {
                newPassword.setError("This Field is Required");
            } else {
                if (edtnewPasswordLenght < 8) {
                    newPassword.setError("Min Password 8 Characters");
                }
            }
            if (edtConfirmPasswordLenght == 0) {
                confrimPassword.setError("This Field is Required");

            }
        }else {
            if (!newPassword.getText().toString().trim().equalsIgnoreCase(confrimPassword.getText().toString().trim())) {
                confrimPassword.setError("Not Same Your Password");
            } else {
                progressDialog.show();
                requestChangePassword();
            }

        }
    }

    private void requestChangePassword() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_CHANGEPW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (Success(response).equalsIgnoreCase("true")) {
                            Toast.makeText(ChangePasswordAcitivty.this, "Password successfully changed ", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ChangePasswordAcitivty.this, "Password Not Successfully changed", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(ChangePasswordAcitivty.this, "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
                params.put(Config.TOKEN_SHARED_PREF, sp.getString(Config.TOKEN_SHARED_PREF,""));
                params.put("newPassword", newPassword.getText().toString().trim());
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ChangePasswordAcitivty.this);
//        int socketTimeout = 10000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest1.setRetryPolicy(policy);
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (newPassword.getText().toString().trim().length()!=0){
            newPassword.setError(null);
        }
        if (confrimPassword.getText().toString().trim().length()!=0){
            confrimPassword.setError(null);
        }else{
            if (newPassword.getText().toString().trim().equalsIgnoreCase(confrimPassword.getText().toString().trim())){
                confrimPassword.setError(null);
            }
        }
    }
}
