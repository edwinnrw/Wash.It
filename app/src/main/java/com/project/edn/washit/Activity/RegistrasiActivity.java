package com.project.edn.washit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.edn.washit.R;

public class RegistrasiActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtRegName;
    private EditText edtRegEmail;
    private EditText edtRegPhone;
    private EditText edtRegPassword;
    private EditText edtRegconfrimPassword;
    private Button btnSubmit;
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
        //Contohh JSON
        //{"success":true,"messages":"Registration complete."}
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (edtRegName.getText().toString().trim().length()!=0){
            edtRegName.setError(null);
        }
        if (edtRegEmail.getText().toString().trim().length()!=0){
            edtRegEmail.setError(null);
        }
        if (edtRegPhone.getText().toString().trim().length()!=0){
            edtRegPhone.setError(null);
        }
        if (edtRegPassword.getText().toString().trim().length()!=0){
            edtRegPassword.setError(null);
        }
        if (edtRegconfrimPassword.getText().toString().trim().length()!=0){
            edtRegconfrimPassword.setError(null);
        }else{
            if (edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())){
                edtRegconfrimPassword.setError(null);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int edtRegNameLenght=edtRegName.getText().toString().trim().length();
        int edtRegEmailLenght=edtRegEmail.getText().toString().trim().length();
        int edtRegPhoneLenght=edtRegPhone.getText().toString().trim().length();
        int edtRegPasswordLenght=edtRegPassword.getText().toString().trim().length();
        int edtRegConfirmPasswordLenght=edtRegconfrimPassword.getText().toString().trim().length();
        if (edtRegNameLenght==0 || edtRegEmailLenght==0||edtRegPhoneLenght==0||edtRegPasswordLenght==0||edtRegConfirmPasswordLenght==0){
            if (edtRegNameLenght==0){
                edtRegEmail.setError("This Field is Required");
            }if (edtRegEmailLenght==0){
                edtRegEmail.setError("This Field is Required");
            }if (edtRegPhoneLenght==0){
                edtRegPhone.setError("This Field is Required");
            }if (edtRegPasswordLenght==0){
                edtRegPassword.setError("This Field is Required");
            }else {
                if (edtRegPasswordLenght<8){
                    edtRegPassword.setError("Min Password 8 Characters");
                }
            }
            if (edtRegConfirmPasswordLenght==0){
                edtRegconfrimPassword.setError("This Field is Required");
            }else {
                if (!edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())){
                    edtRegconfrimPassword.setError("Not Same Your Password");
                }
            }
        }else {
            if (!edtRegPassword.getText().toString().trim().equalsIgnoreCase(edtRegconfrimPassword.getText().toString().trim())){
                edtRegconfrimPassword.setError("Not Same Your Password");
            }else{
                requestRegis();
            }
        }

    }
}
