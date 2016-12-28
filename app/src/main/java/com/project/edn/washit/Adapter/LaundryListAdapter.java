package com.project.edn.washit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.edn.washit.Activity.DetailMerchant;
import com.project.edn.washit.Model.Laundry;

import com.project.edn.washit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EDN on 10/6/2016.
 */

public class LaundryListAdapter extends RecyclerView.Adapter<LaundryListAdapter.MyViewHolder> {

    private List<Laundry> merchantList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView address;
        private String cost;
        public TextView hour;
        private String id;
        private String image;
        public ImageView imageplace;
        private String ket;
        private Double lat;
        private Double lgt;
        public LinearLayout map;
        private String material;
        private String phone;
        private String service;
        public TextView title;

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
            in.putExtra("id_laundry", this.id);
            in.putExtra("name", this.title.getText());
            in.putExtra("address", this.address.getText());
            in.putExtra("hour", this.hour.getText());
            in.putExtra("image", this.image);
            in.putExtra("cost", this.cost);
            in.putExtra("material", this.material);
            in.putExtra("service", this.service);
            in.putExtra("long", this.lgt);
            in.putExtra("lat", this.lat);
            in.putExtra("phone", this.phone);
            in.putExtra("Ket", this.ket);
            context.startActivity(in);
        }
    }
    public LaundryListAdapter(Context context, List<Laundry> merchantList) {
        this.merchantList = merchantList;
        this.context = context;


    }
    public LaundryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_merchant, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LaundryListAdapter.MyViewHolder holder, int position) {
        Laundry merchant = merchantList.get(position);
        Glide.with(context).load(merchant.getImage())
                .centerCrop()
                .error(R.drawable.placehholder_image)
                .placeholder(R.drawable.placehholder_image)
                .into(holder.imageplace);
        holder.title.setText(merchant.getName());
        holder.address.setText(merchant.getAddress());
        holder.hour.setText(merchant.getHour());
        holder.lat = merchant.getLatitude();
        holder.lgt = merchant.getLongitude();
        holder.material = merchant.getMaterial();
        holder.phone = merchant.getPhone();
        holder.service = merchant.getService();
        holder.image = merchant.getImage();
        holder.cost = merchant.getCost();
        holder.ket = merchant.getKeterangan();
        holder.id = String.valueOf(merchant.getId());

    }

    @Override
    public int getItemCount() {
        return (null != merchantList? merchantList.size() : 0);
    }

    public void setFilter(List<Laundry> clothModels) {
        merchantList = new ArrayList<>();
        merchantList.addAll(clothModels);
        notifyDataSetChanged();
    }

}
