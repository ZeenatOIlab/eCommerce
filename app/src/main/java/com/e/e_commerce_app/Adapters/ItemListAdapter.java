package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Activitys.Item_Description_Activity;
import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder> implements View.OnClickListener {

    ArrayList<ItemListModel> itemlist=new ArrayList<>();
    Context context;

    public ItemListAdapter(Context context, ArrayList<ItemListModel> itemlist,SelectProductListener productListener)
    {
        this.context=context;
        this.itemlist=itemlist;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Picasso.with(context).load(APIs.Image_url+itemlist.get(position).getListimg()).error(R.drawable.no_image).into(holder.listimg);
        // holder.listimg.setImageResource(Integer.parseInt(""+ itemlist.get(position).getListimg()));
        holder.pro_Name.setText(itemlist.get(position).getPro_Name());
        holder.pro_Price.setText("₹"+itemlist.get(position).getPro_Price());
        holder.ID.setText(""+itemlist.get(position).getID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Item_Description_Activity.class);
                intent.putExtra("Id",""+itemlist.get(position).getID());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View view) {

    }

    public void filterList(ArrayList<ItemListModel> filterdNames) {
        this.itemlist = filterdNames;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView listimg;
        TextView pro_Name,pro_Price,ID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listimg=itemView.findViewById(R.id.list_img);
            pro_Name=itemView.findViewById(R.id.pro_name);
            pro_Price=itemView.findViewById(R.id.pro_price);
            ID=itemView.findViewById(R.id.ID);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface SelectProductListener {
        void productlistner(String product);
    }




}
