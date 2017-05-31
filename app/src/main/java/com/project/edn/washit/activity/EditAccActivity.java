package com.project.edn.washit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditAccActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText name,email,telp;
    private SharedPreferences sp;
    private Button btnSave;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        name=(EditText) findViewById(R.id.nameuser);
        name.setText(sp.getString("name",""));
        telp=(EditText) findViewById(R.id.telpuser);
        telp.setText(sp.getString("telp",""));
        email=(EditText) findViewById(R.id.emailuser);
        email.setText(sp.getString("email",""));
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
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
    private void requestSave(){
        Map<String, String> params = new HashMap<>();
        //Parameter
        params.put(Config.TOKEN_SHARED_PREF, sp.getString(Config.TOKEN_SHARED_PREF,""));
        params.put("email", email.getText().toString().trim());
        params.put("name", name.getText().toString().trim());
        params.put("telp", telp.getText().toString().trim());
        ServiceHelper.getInstance().editProfile(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    String json=response.body().string();
                    if (Success(json).equalsIgnoreCase("true")) {
                        Toast.makeText(EditAccActivity.this, "Data successfully changed ", Toast.LENGTH_LONG).show();

                        jsonParse(json);
                    }else {
                        Toast.makeText(EditAccActivity.this, "Data Not Successfully changed", Toast.LENGTH_LONG).show();

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
    public void jsonParse(String json) {
        progressDialog.dismiss();
        String token = "";
        String name = "";
        String email = "";
        String telp ="";
        try {
            JSONObject json2 = ((JSONObject) new JSONTokener(json).nextValue()).getJSONObject("data");
            name = json2.getString("name");
            email = json2.getString("email");
            telp = json2.getString("telp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("telp", telp);
        editor.commit();
        Intent in=new Intent(this,AccountActivity.class);
        startActivity(in);
    }
    @Override
    public void onClick(View view) {

        int edtNameLenght = name.getText().toString().trim().length();
        int edtEmailLenght = email.getText().toString().trim().length();
        int edtPhoneLenght = telp.getText().toString().trim().length();
        if (edtNameLenght == 0 || edtEmailLenght == 0 || edtPhoneLenght == 0) {
            if (edtNameLenght == 0) {
                name.setError("This Field is Required");
            }
            if (edtEmailLenght == 0) {
                email.setError("This Field is Required");
            }
            if (edtPhoneLenght == 0) {
                telp.setError("This Field is Required");
            }
        }else {
            progressDialog.show();
            requestSave();
        }
    }

}
