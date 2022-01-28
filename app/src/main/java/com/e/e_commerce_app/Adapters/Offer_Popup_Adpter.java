package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Activitys.OrderProcessActivity;
import com.e.e_commerce_app.Model.Offer_Model;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Offer_Popup_Adpter extends RecyclerView.Adapter<Offer_Popup_Adpter.MyViewHolder> {
    ArrayList<Offer_Model> offer_list=new ArrayList<>();
    Context context;
    onClickInterface onClickInterface;

    public Offer_Popup_Adpter(Context context, ArrayList<Offer_Model> offer_list, onClickInterface onClickInterface) {
        this.offer_list = offer_list;
        this.context = context;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.offer_list_popup, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Offer_Model offer_model=offer_list.get(position);
        Picasso.with(context).load("" + APIs.IMG_URL + offer_model.getOffer_image()).error(R.drawable.no_image).into(holder.offer_pop_img);
        holder.tv_ofr_desc.setText(offer_model.getOffer_desc());
        holder.tv_ofr_code.setText(offer_model.getOffer_code());
        holder.tv_ofr_tittle.setText(offer_model.getOffer_tittle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offer_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView offer_pop_img;
        TextView tv_ofr_tittle,tv_ofr_desc,tv_ofr_code;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_pop_img=itemView.findViewById(R.id.tv_popup_ofr_img);
            tv_ofr_tittle=itemView.findViewById(R.id.tv_popup_ofr_title);
            tv_ofr_desc=itemView.findViewById(R.id.tv_popup_ofr_desc);
            tv_ofr_code=itemView.findViewById(R.id.tv_popup_ofr_code);
        }
    }
    public interface onClickInterface {
        void setClick(int abc);
    }
}
