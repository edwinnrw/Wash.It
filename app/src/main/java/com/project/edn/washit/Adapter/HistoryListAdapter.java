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
import com.project.edn.washit.Activity.DetailHistory;
import com.project.edn.washit.Activity.DetailMerchant;
import com.project.edn.washit.Model.Cloths;
import com.project.edn.washit.Model.History;
import com.project.edn.washit.R;

import java.util.List;

/**
 * Created by EDN on 10/6/2016.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {

    private List<History> historyList;
    private Context context;
    private String orderno,image1,namelaundry,addresslaundry,addresspick,orderdate,datefinish,datepick,cost;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date, status;
        public ImageView image;
        public LinearLayout map;
        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.datepick);
            status = (TextView) view.findViewById(R.id.statusHistory);
            image=(ImageView)view.findViewById(R.id.imagelaundry);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent in=new Intent(context, DetailHistory.class);
            in.putExtra("status",status.getText().toString());
            in.putExtra("orderno",orderno);
            in.putExtra("image",image1);
            in.putExtra("namelaundry",namelaundry);
            in.putExtra("addresslaundry",addresslaundry);
            in.putExtra("addresscustomer",addresspick);
            in.putExtra("orderDate",orderdate);
            in.putExtra("datefinish",datefinish);
            in.putExtra("datepick",datepick);
            in.putExtra("cost",cost);
            context.startActivity(in);
        }
    }
    public HistoryListAdapter(Context context, List<History> historyList) {
        this.historyList = historyList;
        this.context = context;


    }
    public HistoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_history, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.MyViewHolder holder, int position) {
        History history =historyList.get(position);
        Glide.with(context).load(history.getImage())
                .centerCrop()
                .error(R.drawable.placehholder_image)
                .placeholder(R.drawable.placehholder_image)
                .into(holder.image);
        holder.date.setText(history.getDatepick());
        holder.status.setText(history.getStatus());
        orderno=history.getId();
        image1=history.getImage();
        namelaundry=history.getName();
        addresslaundry=history.getAddressLaundry();
        addresspick=history.getAddressCustomer();
        orderdate=history.getOrderdate();
        datefinish=history.getDatefinish();
        datepick=history.getDatepick();
        cost=history.getPrice();

    }


    @Override
    public int getItemCount() {
        return (null != historyList? historyList.size() : 0);
    }
}
