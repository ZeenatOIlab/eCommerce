package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Activitys.Item_Description_Activity;
import com.e.e_commerce_app.Model.ProductDetailModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailProAdapter extends RecyclerView.Adapter<OrderDetailProAdapter.MyViewHolder> {
    ArrayList<ProductDetailModel> productDetaillist=new ArrayList<>();
    Context context;

    public OrderDetailProAdapter(Context context,ArrayList<ProductDetailModel> productDetaillist) {
        this.context=context;
        this.productDetaillist=productDetaillist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailproduct_item,parent,false);
        return new OrderDetailProAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.with(context).load(""+ APIs.Image_url+productDetaillist.get(position)
                .getProDetailitemImg()).error(R.drawable.no_image).into(holder.proDetailiteimg);

        holder.proDetailitemname.setText(productDetaillist.get(position).getProDetailitemNmae());
        holder.proDetailitemprice.setText("₹"+productDetaillist.get(position).getProDetailitemPrice());
        holder.proDetailitemqyn.setText(""+productDetaillist.get(position).getProDetailitemQyn());
        holder.proid=productDetaillist.get(position).getProDetailId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Item_Description_Activity.class);
                intent.putExtra("Id",productDetaillist.get(position).getProDetailId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetaillist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String proid;
        TextView proDetailitemname,proDetailitemprice,proDetailitemqyn;
        ImageView proDetailiteimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            proDetailiteimg=itemView.findViewById(R.id.mo_img);
            proDetailitemname=itemView.findViewById(R.id.Order_pro_name);
            proDetailitemqyn=itemView.findViewById(R.id.orderQyn);
            proDetailitemprice=itemView.findViewById(R.id.Orderprice);        }

        @Override
        public void onClick(View view) {

        }
    }
}
