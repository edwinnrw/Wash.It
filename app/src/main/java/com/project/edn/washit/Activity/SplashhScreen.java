package com.project.edn.washit.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.project.edn.washit.Config;
import com.project.edn.washit.R;

public class SplashhScreen extends AppCompatActivity {
    private boolean loggedIn = false;
    private Handler splashHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(SplashhScreen.this);
        loggedIn= sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Runnable r = new Runnable(){
            public void run(){
                if(loggedIn){
                    Intent brain = new Intent(SplashhScreen.this, MainActivity.class);
                    startActivity(brain);
                    finish();
                }else{
                    Intent brain = new Intent(SplashhScreen.this, LoginActivity.class);
                    startActivity(brain);
                    finish();
                }
//                Intent brain = new Intent(SplashhScreen.this, MainActivity.class);
//                    startActivity(brain);


            }
        };
        setContentView(R.layout.activity_splashh_screen);
        splashHandler.postDelayed(r, 5000);
    }
    //    public void RequestJson(){
    //{"success":true}
//        //Buatkan Request Dalam bentuk String
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_GETUSERPROFILE,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        //Jika Respon server sukses
//                        if (Success(response).equalsIgnoreCase("true")) {
//                            //memanggil method untuk mengambil data vm,sp,dan qrtext serta menset vm,dan sp ke apps
//                            getDatamember(response);
//                            saveqrtext(qrtext);
//                            json=response;
//
//                        }else{
//                            //jika server respon server berupa success gagal
//                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                            //Getting editor
//                            SharedPreferences.Editor editor =sharedPreferences.edit();
//                            // put nilai false untuk login
//                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
//                            editor.putString(Config.TOKEN_SHARED_PREF,"");
//                            // put nilai untuk username
//                            editor.putString(Config.USER_SHARED_PREF, "");
//                            editor.putString("qr", "");
//                            //Simpan ke haredpreferences
//                            editor.commit();
//                            //Starting Class yang dipanggil
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        progress.dismiss();
//                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
//                        Toast.makeText(MainActivity.this, "Failed Load Your Data,Check Your Connection", Toast.LENGTH_LONG).show();
////                        System.exit(0);
////                        alertDialogRetry();
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("Time-Request",Long.toString(timereq1));
//                params.put("Apps-Id", en.encr());
//                params.put("Finger-Print", en.encr1());
//                params.put("X-Key-Secret:",en.fingerprinttoken(token, ((int) timereq1)));
//                params.put("User-Agent","SubAssitance/v1.0(oid:"+ Config.KEY_ID + ")");
//                return params;
//            }
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put(Config.TOKEN_SHARED_PREF, token);
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
//    }
}
