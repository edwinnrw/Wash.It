package com.project.edn.washit.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.project.edn.washit.adapter.NearLaundryListAdapter;
import com.project.edn.washit.Config;
import com.project.edn.washit.GPSTracker;
import com.project.edn.washit.Model.Laundry;
import com.project.edn.washit.R;
import com.project.edn.washit.api.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NearActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private List<Laundry> clothList;
    private RecyclerView mRecyclerView;
    private NearLaundryListAdapter adapter;
    private ProgressBar progressBar;
    private SearchView mSearchView;
    private GPSTracker gps;
    private Location a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wash-Laundry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        gps = new GPSTracker(NearActivity.this);
        mSearchView=(SearchView) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar123);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupSearchView();
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            a=new Location("My Location");
            a.setLatitude(latitude);
            a.setLongitude(longitude);
            getJsonMerchant(latitude,longitude);

            // \n is for new line
//            currentLocation=new LatLng(latitude,longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search Here");
    }

    public void getJsonMerchant(double latitude, double longitude) {
        ServiceHelper.getInstance().getNear(String.valueOf(latitude),String.valueOf(longitude)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String json=response.body().string();
                    if (Success(json).equalsIgnoreCase("true")){
                        parceJson(json);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(NearActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
//            String JSON_STRING="{\"response\":"+json2.toString()+"}";
//            JSONArray emp=(new JSONObject(JSON_STRING)).getJSONArray("response");
//            Memasukkan json ke dalam adapter
            for (int i=0 ; i<json2.length();i++){
                JSONObject post = json2.optJSONObject(i);
                Location locationB = new Location("Target Pos");
                locationB.setLatitude(post.optDouble("lat"));
                locationB.setLongitude(post.optDouble("long"));
                Laundry item = new Laundry();
//                Double distance = SphericalUtil.computeDistanceBetween(currentLocation, lockmerchant);
                double distance=post.optDouble("distance");
//                String d=String.format("%-12.1f",distance);
                item.setName(post.optString("name"));
                item.setImage(post.optString("image"));
                item.setAddress(post.optString("address"));
                item.setHour(post.optString("hour"));
                item.setCost(post.optString("cost"));
                item.setPhone(post.optString("telp"));
                item.setMaterial(post.optString("material"));
                item.setService(post.optString("Dry"));
                item.setDistance(post.optString("distance"));
                item.setLatitude(Double.valueOf(post.optDouble("lat")));
                item.setLongitude(Double.valueOf(post.optDouble("long")));
                clothList.add(item);
                adapter = new NearLaundryListAdapter(this, clothList);
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
        final List<Laundry> filteredModelList = filter(clothList, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }
    private List<Laundry> filter(List<Laundry> models, String query) {
        query = query.toLowerCase();
        final List<Laundry> filteredModelList = new ArrayList<>();
        for (Laundry model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
