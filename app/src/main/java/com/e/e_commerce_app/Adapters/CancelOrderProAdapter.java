package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Model.CancelOrderProductModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CancelOrderProAdapter extends RecyclerView.Adapter<CancelOrderProAdapter.MyViewHolder> {
    Context context;
    ArrayList<CancelOrderProductModel> cancelOrderProList=new ArrayList<>();

    public CancelOrderProAdapter(Context context, ArrayList<CancelOrderProductModel> cancelOrderProList) {
        this.context = context;
        this.cancelOrderProList = cancelOrderProList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.order_cancel_item, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(context).load(""+ APIs.CartImgUrl+cancelOrderProList.get(position).getCoPRoImg()).error(R.drawable.no_image).into(holder.coProImage);
        holder.coProName.setText(cancelOrderProList.get(position).getCoProName());
        holder.coProQyn.setText(""+cancelOrderProList.get(position).getCoProQyn());
        holder.coProTotal.setText(""+cancelOrderProList.get(position).getCoProTotal());

    }

    @Override
    public int getItemCount() {
        return cancelOrderProList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coProName,coProQyn,coProTotal;
        ImageView coProImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coProImage=itemView.findViewById(R.id.co_ProImg);
            coProName=itemView.findViewById(R.id.tv_coProName);
            coProQyn=itemView.findViewById(R.id.tv_coProQyn);
            coProTotal=itemView.findViewById(R.id.tv_coProTotal);
        }
    }
}
