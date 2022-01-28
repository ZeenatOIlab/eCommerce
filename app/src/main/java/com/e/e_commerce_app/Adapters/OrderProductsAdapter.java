package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.e_commerce_app.Model.Product_Model;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProductsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<Product_Model> productlist=new ArrayList<>();

    public OrderProductsAdapter(Context context, ArrayList<Product_Model> productlist) {
        this.context=context;
        this.productlist = productlist;
    }

    @Override
    public int getCount() {
        return productlist.size();
    }

    @Override
    public Object getItem(int i) {
        return productlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.my_order_products,viewGroup,false);
        ImageView OrderImage;
        TextView OrderPrice, OrderProname;
        OrderImage = view.findViewById(R.id.orerImage);
        OrderPrice = view.findViewById(R.id.orderprice);
        OrderProname = view.findViewById(R.id.orderproname);

        Picasso.with(context).load(""+APIs.Image_url+productlist.get(i).getPro_img()).error(R.drawable.no_image).into(OrderImage);
        OrderPrice.setText("₹" + productlist.get(i).getPro_price());
        OrderProname.setText("" + productlist.get(i).getPro_name());



        return view;
    }
}
