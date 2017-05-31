package com.project.edn.washit.activity;

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
import com.project.edn.washit.api.ServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

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
        Map<String, String> params = new HashMap<>();
        //Parameter
        params.put(Config.TOKEN_SHARED_PREF, sp.getString(Config.TOKEN_SHARED_PREF,""));
        params.put("newPassword", newPassword.getText().toString().trim());
        ServiceHelper.getInstance().changePassword(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    String json=response.body().string();
                    if (Success(json).equalsIgnoreCase("true")) {
                        Toast.makeText(ChangePasswordAcitivty.this, "Password successfully changed ", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ChangePasswordAcitivty.this, "Password Not Successfully changed", Toast.LENGTH_LONG).show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
//
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
