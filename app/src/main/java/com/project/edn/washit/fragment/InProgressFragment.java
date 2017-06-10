package com.project.edn.washit.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit.adapter.HistoryListAdapter;
import com.project.edn.washit.Config;
import com.project.edn.washit.Model.History;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InProgressFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;
    private List<History> historyList;
    private RecyclerView mRecyclerView;
    private HistoryListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String json;
    private SharedPreferences sharedPreferences;
    private Handler splashHandler = new Handler();


    public InProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InProgressFragment newInstance(String param1, String param2) {
        InProgressFragment fragment = new InProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar123);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getJsonHistory("new");
        parseJson();

    }

    public void getJsonHistory(final String updateParam) {
        Toast.makeText(getActivity(), "Tes", Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<>();
        //Parameter
        params.put("ket", "In progress");
        params.put(Config.TOKEN_SHARED_PREF,sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
        ServiceHelper.getInstance().getHistory(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                try {
                    String json=response.body().string();

                    if (Success(json).equalsIgnoreCase("true")) {
                        if (!sharedPreferences.getString("jsonInprogress","").equalsIgnoreCase(json)) {
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            if (updateParam.equalsIgnoreCase("update")) {
                                editor.putString("jsonInprogress", json);
                                editor.commit();
                                getData();
                            }
                            if (updateParam.equalsIgnoreCase("new")) {
                                Toast.makeText(getActivity(), "new"+json, Toast.LENGTH_LONG).show();

                                editor.putString("jsonInprogress", json);
                                editor.commit();
                                parseJson();
                            }
                        } else if (updateParam.equalsIgnoreCase("new")) {
                            parseJson();
                        }else {
                            getData();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

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
    public void parseJson(){
        progressBar.setVisibility(View.GONE);
        historyList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonCache=sharedPreferences.getString("jsonInprogress", "");


        try {
//            Parsing json
            JSONObject json1 = (JSONObject) new JSONTokener(jsonCache).nextValue();
            JSONArray json2 = json1.getJSONArray("data");
            for (int i = 0; i < json2.length(); i++) {
                JSONObject post = json2.optJSONObject(i);
                History item = new History();
                item.setName(post.optString("name"));
                item.setImage(post.optString("image"));
                item.setId(post.optString("orderid"));
                item.setAddressLaundry(post.optString("addressLaundry"));
                item.setAddressCustomer(post.optString("addressCustomer"));
                item.setStatus(post.optString("status"));
                item.setDatepick(post.optString("datepick"));
                item.setDatefinish(post.optString("datefinish"));
                item.setOrderdate(post.optString("orderdate"));
                item.setPrice(post.optString("cost"));
                historyList.add(item);
                adapter = new HistoryListAdapter(getActivity(), historyList);
                mRecyclerView.setAdapter(adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        updateOperation();
    }

    protected void updateOperation() {

        Runnable r =new Runnable() {
            @Override
            public void run() {
//                historyList.clear();
                getJsonHistory("update");
                mSwipeRefreshLayout.setRefreshing(false);

            }
        };
        splashHandler.postDelayed(r,5000);

    }

    private void getData() {
        History item =new History();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonCache=sharedPreferences.getString("jsonInprogress", "");
        historyList.clear();

        try {
            JSONObject json1= (JSONObject) new JSONTokener(jsonCache).nextValue();
            JSONArray json2 = json1.getJSONArray("data");
            for (int i=0 ; i<json2.length();i++){
                JSONObject post = json2.optJSONObject(i);
                item.setName(post.optString("name"));
                item.setImage(post.optString("image"));
                item.setId(post.optString("orderid"));
                item.setAddressLaundry(post.optString("addressLaundry"));
                item.setAddressCustomer(post.optString("addressCustomer"));
                item.setStatus(post.optString("status"));
                item.setDatepick(post.optString("datepick"));
                item.setDatefinish(post.optString("datefinish"));
                item.setOrderdate(post.optString("orderdate"));
                item.setPrice(post.optString("cost"));
                historyList.add(item);
            }


        }  catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }
}
