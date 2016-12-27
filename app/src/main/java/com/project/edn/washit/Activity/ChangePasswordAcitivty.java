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

public class ChangePasswordAcitivty extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Toolbar toolbar;
    private EditText confrimPassword,oldPassword,newPassword;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        confrimPassword=(EditText)findViewById(R.id.confirmpw);
        confrimPassword.addTextChangedListener(this);
        oldPassword=(EditText)findViewById(R.id.oldpw);
        oldPassword.addTextChangedListener(this);
        newPassword=(EditText)findViewById(R.id.newpw);
        newPassword.addTextChangedListener(this);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
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
        
        int edtOldPasswordLenght=oldPassword.getText().toString().trim().length();
        int edtnewPasswordLenght=newPassword.getText().toString().trim().length();
        int edtConfirmPasswordLenght=confrimPassword.getText().toString().trim().length();
        if (edtOldPasswordLenght==0||edtnewPasswordLenght==0||edtConfirmPasswordLenght==0){
            if (edtOldPasswordLenght==0){
                oldPassword.setError("This Field is Required");
            }if (edtnewPasswordLenght==0){
                newPassword.setError("This Field is Required");
            }else {
                if (edtnewPasswordLenght<8){
                    newPassword.setError("Min Password 8 Characters");
                }
            }
            if (edtConfirmPasswordLenght==0){
                confrimPassword.setError("This Field is Required");
            }else {
                if (!oldPassword.getText().toString().trim().equalsIgnoreCase(confrimPassword.getText().toString().trim())){
                    confrimPassword.setError("Not Same Your Password");
                }
            }
        }else {
            if (!oldPassword.getText().toString().trim().equalsIgnoreCase(confrimPassword.getText().toString().trim())){
                confrimPassword.setError("Not Same Your Password");
            }else {
                requestChangePassword();
            }
        }

    }

    private void requestChangePassword() {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (oldPassword.getText().toString().trim().length()!=0){
            newPassword.setError(null);
        }
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
