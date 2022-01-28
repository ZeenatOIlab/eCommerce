package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Model.CheckOutModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckOutAdapter extends BaseAdapter {
    Context context;
    ArrayList<CheckOutModel> checkOutlist=new ArrayList<>();

    public CheckOutAdapter(Context context,ArrayList<CheckOutModel> checkOutlist) {
        this.context = context;
        this.checkOutlist=checkOutlist;
    }

    @Override
    public int getCount() {
        return checkOutlist.size();
    }

    @Override
    public Object getItem(int i) {
        return checkOutlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.checkout_item,viewGroup,false);
        TextView coProName,coProPrice,coProQyn;
        ImageView coProImg;
        String coProCartId,coProCartDicount,coProCartShipChrg,coProCartDicountType;
        coProImg=view.findViewById(R.id.co_img);
        coProName=view.findViewById(R.id.co_pro_name);
        coProPrice=view.findViewById(R.id.co_pro_price);
        coProQyn=view.findViewById(R.id.co_pro_qyn);

        Picasso.with(context).load(APIs.Image_url+checkOutlist.get(i).getCoProImg()).error(R.drawable.no_image).into(coProImg);

        coProName.setText(checkOutlist.get(i).getCoProName());
        coProPrice.setText(""+checkOutlist.get(i).getCoProPrice()+"₹");
        coProQyn.setText(""+checkOutlist.get(i).getCoProQyn());
        coProCartId=""+checkOutlist.get(i).getCoPro_CartId();
        coProCartDicount=""+checkOutlist.get(i).getCoProDiscounP();
        coProCartShipChrg=""+checkOutlist.get(i).getCoProShipChrg();
        coProCartDicountType=""+checkOutlist.get(i).getCoProDiscounType();


        return view;
    }


}
