package com.project.edn.washit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.edn.washit.Activity.DetailMerchant;
import com.project.edn.washit.Model.Cloths;

import com.project.edn.washit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EDN on 10/6/2016.
 */

public class ClothsListAdapter extends RecyclerView.Adapter<ClothsListAdapter.MyViewHolder> {

    private List<Cloths> merchantList;
    private Context context;
    private Double lgt,lat;
    private String material,service,phone,image,cost;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, address, hour;
        public ImageView imageplace;
        public LinearLayout map;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.nameplace);
            address = (TextView) view.findViewById(R.id.addressplace);
            hour = (TextView) view.findViewById(R.id.bussinessplace);
            imageplace=(ImageView)view.findViewById(R.id.imagelaundry);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent in=new Intent(context, DetailMerchant.class);
            in.putExtra("name",title.getText());
            in.putExtra("address",address.getText());
            in.putExtra("hour",hour.getText());
            in.putExtra("image",image);
            in.putExtra("cost",cost);
            in.putExtra("material",material);
            in.putExtra("service",service);
            in.putExtra("long",lgt);
            in.putExtra("lat",lat);
            in.putExtra("phone",phone);
            context.startActivity(in);
        }
    }
    public ClothsListAdapter(Context context, List<Cloths> merchantList) {
        this.merchantList = merchantList;
        this.context = context;
//        this.filterList = new ArrayList<Cloths>();
//        // we copy the original list to the filter list and use it for setting row values
//        this.filterList.addAll(this.merchantList);


    }
    public ClothsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_merchant, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClothsListAdapter.MyViewHolder holder, int position) {
        Cloths merchant = merchantList.get(position);
        Glide.with(context).load(merchant.getImage())
                .centerCrop()
                .error(R.drawable.placehholder_image)
                .placeholder(R.drawable.placehholder_image)
                .into(holder.imageplace);
        holder.title.setText(merchant.getName());
        holder.address.setText(merchant.getAddress());
        holder.hour.setText(merchant.getHour());
        lat=merchant.getLatitude();
        lgt=merchant.getLongitude();
        material=merchant.getMaterial();
        phone=merchant.getPhone();
        service=merchant.getService();
        image=merchant.getImage();
        cost=merchant.getCost();

    }
//    public void filter(final List<Cloths> Cloths) {
//
//        // Searching could be complex..so we will dispatch it to a different thread...
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                // Clear the filter list
//                filterList.clear();
//
//                // If there is no search value, then add all original list items to filter list
//                if (TextUtils.isEmpty(text)) {
//
//                    filterList.addAll(merchantList);
//
//                } else {
//                    // Iterate in the original List and add it to filter list...
//                    for (Cloths item : merchantList) {
//                        if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
//                                item.getAddress().toLowerCase().contains(text.toLowerCase())) {
//                            // Adding Matched items
//                            filterList.add(item);
//                        }
//                    }
//                }
//
//                // Set on UI Thread
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Notify the List that the DataSet has changed...
//                        notifyDataSetChanged();
//                    }
//                });
//
//            }
//        }).start();
//
//    }

    @Override
    public int getItemCount() {
        return (null != merchantList? merchantList.size() : 0);
    }

    public void setFilter(List<Cloths> clothModels) {
        merchantList = new ArrayList<>();
        merchantList.addAll(clothModels);
        notifyDataSetChanged();
    }

}
