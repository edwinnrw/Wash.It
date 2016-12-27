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
import android.widget.TextView;
import android.widget.Toast;

import com.project.edn.washit.Config;
import com.project.edn.washit.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class EditAccActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Button btnSave;
    private EditText email;
    private EditText name;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private EditText telp;
    private Toolbar toolbar;
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
        name = (EditText) findViewById(R.id.nameuser);
        name.setText(this.sp.getString("name",""));
        telp = (EditText) findViewById(R.id.telpuser);
        telp.setText(this.sp.getString("telp", ""));
        email = (EditText) findViewById(R.id.emailuser);
        email.setText(this.sp.getString("email",""));
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
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
    public String Success(String json) {
        String succes = "";
        try {
            succes = new JSONObject("{\"response\":" + json + "}").getJSONObject("response").getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return succes;
    }

    public void jsonParse(String json) {
        this.progressDialog.dismiss();
        String token = "";
        String name = "";
        String email = "";
        String telp = "";
        try {
            JSONObject json2 = ((JSONObject) new JSONTokener(json).nextValue()).getJSONObject("data");
            name = json2.getString("name");
            email = json2.getString("email");
            telp = json2.getString("telp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("telp", telp);
        editor.putString(Config.TOKEN_SHARED_PREF, token);
        editor.commit();
    }
    private void requestSave(){

    }

    public void onClick(View view) {
        int edtNameLenght = this.name.getText().toString().trim().length();
        int edtEmailLenght = this.email.getText().toString().trim().length();
        int edtPhoneLenght = this.telp.getText().toString().trim().length();
        if (edtNameLenght == 0 || edtEmailLenght == 0 || edtPhoneLenght == 0) {
            if (edtNameLenght == 0) {
                this.name.setError("This Field is Required");
            }
            if (edtEmailLenght == 0) {
                this.email.setError("This Field is Required");
            }
            if (edtPhoneLenght == 0) {
                this.telp.setError("This Field is Required");
                return;
            }
            return;
        }
        this.progressDialog.setMessage("Loading");
        this.progressDialog.show();
        requestSave();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (name.getText().toString().trim().length()!=0){
            name.setError(null);
        }
        if (email.getText().toString().length()!=0){
            email.setError(null);
        }
        if (telp.getText().toString().length()!=0){
            telp.setError(null);
        }
    }
}
