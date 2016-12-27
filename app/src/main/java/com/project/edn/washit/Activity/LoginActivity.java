package com.project.edn.washit.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private  Button btnlogin;
    private EditText username;
    private EditText password;
    private TextView create;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin=(Button)findViewById(R.id.email_sign_in_button);
        username=(EditText)findViewById(R.id.txt_email);
        username.addTextChangedListener(this);
        password=(EditText)findViewById(R.id.txt_password);
        password.addTextChangedListener(this);
        create=(TextView)findViewById(R.id.createacc);
        create.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent in;
        switch (view.getId()){
            case R.id.createacc:
                in=new Intent(this,RegistrasiActivity.class);
                startActivity(in);

                break;
            case R.id.email_sign_in_button:
                int usernameLenght=username.getText().toString().trim().length();
                int passwordLength=password.getText().toString().trim().length();
                if(usernameLenght==0 || passwordLength==0){
                    if (usernameLenght==0){
                        username.setError("This Field is Required");
                    }else{
                        if (!username.getText().toString().matches(("[A-Za-z~@#$%^&*:;<>.,/}{+ ]"))){
                            username.setError("Invalid Username");
                        }

                    }
                    if (passwordLength==0){
                        password.setError("This Field is Required");
                    }else{
                        if (!password.getText().toString().matches(("[A-Za-z~@#$%^&*:;<>.,/}{+ ]"))){
                            password.setError("Invalid Password");
                        }
                    }
                }else{
                    in=new Intent(this,MainActivity.class);
                    startActivity(in);
                }
                break;
            default:
                break;

        }
    }
//    private void RequestLogin(final String username, final String password){
    //          CONTOH JSON                //
    //{"success":true,"data":{"token":"2ecd8a45685362a28e34e6368b11a736","timeout":"2016-08-29 14:24:18","loginas":"vipmember"}}
    ////////////////////////////////////////
//
//        //Buatkan Request Dalam bentuk String
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_LOGIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //Jika Respon server sukses
//
//                        if (Success(response).equalsIgnoreCase("true")) {
//                            logintomain(response,username);
//
//                        }else{
//                            progress.dismiss();
//                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
//                        progress.dismiss();
//                        Toast.makeText(LoginActivity.this, "Login Failed,Check Your Connection", Toast.LENGTH_LONG).show();
//
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("Time-Request",Long.toString(timereq1));
//                params.put("Apps-Id", en.encr());
//                params.put("Finger-Print", en.encr1());
//                params.put("User-Agent","SubAssitance/v1.0(oid:"+ Config.KEY_ID + ")");
//                return params;
//            }
//            public Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put(Config.VAR_USER,username);
//                params.put(Config.VAR_ID, Config.KEY_ID);
//                params.put(Config.VAR_PASSWORD,password);
//                return params;
//            }
//        };
//
//        //Tambahkan Request String ke dalam Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
////            int socketTimeout = 10000;//30 seconds - change to what you want
////            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////            stringRequest.setRetryPolicy(policy);
//        requestQueue.add(stringRequest);
//    }
//    public void logintomain(String json,String username){
//        JSONObject jsonObject=null;
//        try {
//            String jsonTemp=json;
//            JSONObject json1= (JSONObject) new JSONTokener(json).nextValue();
//            JSONObject json2 = json1.getJSONObject("data");
//            String token= (String)json2.get("token");
//            String name= (String)json2.get("name");//
//            String email = (String)json2.get("email");
//            String telp = (String)json2.get("telp");
//            //Buatkan sebuah shared preference
//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//            //Buatkan Sebuah variabel Editor Untuk penyimpanan Nilai shared preferences
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            //Tambahkan Nilai ke Editor
//            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
//            editor.putString("name", name);
    //            editor.putString("email", email);
//            editor.putString("telp", telp);

    //            editor.putString(Config.TOKEN_SHARED_PREF,token);
//            //Simpan Nilai ke Variabel editor
//            editor.commit();
//            //Starting Class yang dipanggil
//            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("token", token);
//            intent.putExtra("loginas",member);
//            intent.putExtra("username",username);
//            new CountDownTimer(5000, 1000) {
//                public void onFinish() {
//
//                    startActivity(intent);
//                    progress.dismiss();
//                }
//
//                public void onTick(long millisUntilFinished) {
//                    // millisUntilFinished    The amount of time until finished.
//                }
//            }.start();
//
//
//
//
//        }  catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
    public String Success(String json){
        String succes="";
        try {
            String JSON_STRING="{\"response\":"+json+"}";
            JSONObject emp=(new JSONObject(JSON_STRING)).getJSONObject("response");
            succes=emp.getString("success");

        }catch (JSONException e) {
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
        if (username.getText().toString().trim().length()!=0){
            if (!username.getText().toString().matches("[A-Za-z~@#$%^&*:;<>.,/}{+ ]*")){
                username.setError(null);
            }
        }
        if (password.getText().toString().length()!=0){
            password.setError(null);
        }

    }
}
