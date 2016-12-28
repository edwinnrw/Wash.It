package com.project.edn.washit.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.edn.washit.Activity.MainLaundryActivity;
import com.project.edn.washit.Adapter.SlidingImage_Adapter;
import com.project.edn.washit.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.ads,R.drawable.whasit1};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    LinearLayout menushoes,menuhelm,menucloths;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImageSlider(view);
        menucloths=(LinearLayout)view.findViewById(R.id.menucloths);
        menucloths.setOnClickListener(this);
        menushoes=(LinearLayout)view.findViewById(R.id.menushoes);
        menushoes.setOnClickListener(this);
        menuhelm=(LinearLayout)view.findViewById(R.id.menuhelm);
        menuhelm.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){
            case R.id.menucloths:
                in=new Intent(getActivity(), MainLaundryActivity.class);
                in.putExtra("Ket", "clothes");
                startActivity(in);
                break;
            case R.id.menushoes:
                in=new Intent(getActivity(), MainLaundryActivity.class);
                in.putExtra("Ket", "shoes");
                startActivity(in);
                break;
            case R.id.menuhelm:
                in=new Intent(getActivity(), MainLaundryActivity.class);
                in.putExtra("Ket", "helmet");
                startActivity(in);
                break;

        }
    }

    public  void setupImageSlider(View view){
        ImagesArray.add(R.drawable.ads);

        mPager = (ViewPager) view.findViewById(R.id.pager);

        mPager.setVisibility(View.VISIBLE);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =ImagesArray.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4500, 4500);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;


            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

}
