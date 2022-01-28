package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.Model.Pagermodel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductImage_Slider extends SliderViewAdapter<ProductImage_Slider.ProductSlider> {
    private ArrayList<ItemListModel> pagerlist = new ArrayList<>();
    Context context;


    public ProductImage_Slider(Context context, ArrayList<ItemListModel> pagerlist) {
        this.pagerlist = pagerlist;
        this.context=context;

    }

    @Override
    public ProductSlider onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_pager, null);

        return new ProductSlider(inflate);
    }

    @Override
    public void onBindViewHolder(ProductSlider viewHolder, int position) {
        Picasso.with(context).load(APIs.Image_url+pagerlist.get(position).getRelatedImg()).error(R.drawable.no_image).into(viewHolder.itemImageSlide);


    }

    @Override
    public int getCount() {
        return pagerlist.size();
    }


    class ProductSlider extends SliderViewAdapter.ViewHolder{
        View itemView;
        ImageView itemImageSlide,Heartbtn;

        public ProductSlider(View itemView) {
            super(itemView);
            itemImageSlide = itemView.findViewById(R.id.pro_pager);
            Heartbtn =itemView.findViewById(R.id.heartbtn);
            this.itemView = itemView;
        }
    }



}
