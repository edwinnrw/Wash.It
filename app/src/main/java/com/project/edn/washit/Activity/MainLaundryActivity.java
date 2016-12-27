package com.project.edn.washit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit.Adapter.ClothsListAdapter;
import com.project.edn.washit.Config;
import com.project.edn.washit.Model.Cloths;
import com.project.edn.washit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainLaundryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private Toolbar toolbar;
    private List<Cloths> clothList;
    private RecyclerView mRecyclerView;
    private ClothsListAdapter adapter;
    private ProgressBar progressBar;
    private SearchView mSearchView;
    private CardView near;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_laundry_cloths);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wash-Cloths");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSearchView=(SearchView) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar123);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        near=(CardView)findViewById(R.id.near);
        near.setOnClickListener(this);
        getJsonMerchant();
        setupSearchView();
    }
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search Here");
    }

    public void getJsonMerchant() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_GETDATALAUNDRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")){
                            parceJson(response);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(MainLaundryActivity.this, "Failed Load Your Data,Check Your Connection"+error.getMessage() , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
//                params.put(Config.VAR_ID, Config.KEY_ID);
//                params.put("token", token);
//                params.put("city", city);
//                params.put("category", category);

                //Kembalikan Nilai parameter
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        int socketTimeout = 100000;//30 seconds - change to what you want
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void parceJson(String json){
        progressBar.setVisibility(View.GONE);
        clothList = new ArrayList<>();
        try {
//            Parsing json
            JSONObject json1= (JSONObject) new JSONTokener(json).nextValue();
            JSONArray json2 = json1.getJSONArray("data");

            for (int i=0 ; i<json2.length();i++){
                JSONObject post = json2.optJSONObject(i);
                Cloths item = new Cloths();
                item.setName(post.optString("name"));
                item.setImage(post.optString("image"));
                item.setAddress(post.optString("address"));
                item.setHour(post.optString("hour"));
                item.setCost(post.optString("cost"));
                item.setPhone(post.optString("telp"));
                item.setMaterial(post.optString("material"));
                item.setService(post.optString("Dry"));
                item.setLatitude(Double.valueOf(post.optDouble("lat")));
                item.setLongitude(Double.valueOf(post.optDouble("long")));
                clothList.add(item);
                adapter = new ClothsListAdapter(this, clothList);
                mRecyclerView.setAdapter(adapter);

            }



        }  catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Cloths> filteredModelList = filter(clothList, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }
    private List<Cloths> filter(List<Cloths> models, String query) {
        query = query.toLowerCase();
        final List<Cloths> filteredModelList = new ArrayList<>();
        for (Cloths model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(View view) {
        Intent in=new Intent(MainLaundryActivity.this,NearActivity.class);
        startActivity(in);
    }
}
