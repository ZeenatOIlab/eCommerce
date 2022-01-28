package com.e.e_commerce_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Activitys.Offer_Activity;
import com.e.e_commerce_app.Activitys.OrderProcessActivity;
import com.e.e_commerce_app.Model.Offer_Model;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.myinnos.androidscratchcard.ScratchCard;

public class Offer_Adapter extends RecyclerView.Adapter<Offer_Adapter.MyViewHolder> {
    ArrayList<Offer_Model> offer_list=new ArrayList<>();
    Context context;
    Dialog dialog;

    public Offer_Adapter(Context context,ArrayList<Offer_Model> offer_list) {
        this.context=context;
        this.offer_list = offer_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.offer_item_row, parent, false);
        return new MyViewHolder(itemview);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Offer_Model offer_model=offer_list.get(position);
        Picasso.with(context).load("" + APIs.IMG_URL + offer_model.getOffer_image()).error(R.drawable.no_image).into(holder.offer_image);
        holder.offer_tittle.setText(offer_model.getOffer_tittle());
        holder.offer_desc.setText(offer_model.getOffer_desc());
        holder.offer_expire_date.setText(offer_model.getOffer_expire_date());
        holder.offer_code.setText(offer_model.getOffer_code());
        holder.offer_amnt=offer_model.getOffer_amnt();
        holder.btn_show_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            OfferPopup(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offer_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView offer_image;
        TextView offer_tittle,offer_desc,offer_expire_date,offer_code;
        Button btn_show_offer;
        String offer_amnt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_image=itemView.findViewById(R.id.offer_image);
            offer_tittle=itemView.findViewById(R.id.offer_tittle);
            offer_desc=itemView.findViewById(R.id.offer_desc);
            offer_expire_date=itemView.findViewById(R.id.offer_expire_date);
            offer_code=itemView.findViewById(R.id.offer_code);
            btn_show_offer=itemView.findViewById(R.id.btn_show_offer);

        }
    }
    public  void OfferPopup(final int pos) {
        dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.offer_popoup);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView close=dialog.findViewById(R.id.close_offer_popup);
        ImageView pop_offer_image=dialog.findViewById(R.id.offer_pop_img);
        TextView pop_offer_tittle=dialog.findViewById(R.id.offer_pop_tittle);
        TextView pop_offer_desc=dialog.findViewById(R.id.offer_pop_desc);

        TextView pop_offer_expire_date=dialog.findViewById(R.id.offer_pop_expire_date);
        TextView pop_offer_details=dialog.findViewById(R.id.offer_pop_details);
        TextView pop_offer_terms_condition=dialog.findViewById(R.id.offer_pop_tnc);
        Button offer_apply=dialog.findViewById(R.id.apply_offer);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Picasso.with(context).load("" + APIs.IMG_URL + offer_list.get(pos).getOffer_image()).error(R.drawable.no_image).into(pop_offer_image);
        pop_offer_tittle.setText(offer_list.get(pos).getOffer_tittle());
        pop_offer_desc.setText(offer_list.get(pos).getOffer_desc());
        pop_offer_tittle.setText(offer_list.get(pos).getOffer_tittle());
        pop_offer_expire_date.setText(offer_list.get(pos).getOffer_expire_date());
        pop_offer_details.setText(offer_list.get(pos).getOffer_detail());
        pop_offer_terms_condition.setText(offer_list.get(pos).getOffer_termscondition());
        offer_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderProcessActivity.class);
                intent.putExtra("offer_amnt",""+offer_list.get(pos).getOffer_amnt());
                intent.putExtra("offer_Code",""+offer_list.get(pos).getOffer_code());
                intent.putExtra("offer_res","yes");
                context.startActivity(intent);
                ((Offer_Activity)context).finish();
            }
        });


        dialog.show();
    }
}
