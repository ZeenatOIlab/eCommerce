package com.e.e_commerce_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e.e_commerce_app.Activitys.ProfileActivity;
import com.e.e_commerce_app.Model.Pagermodel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.lang.Integer.*;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {
    Context context;
    Dialog cnfrmpswrddialog;
    ImageView slideImg,removepopup;
    TextView slideTitle,slideDEsc;


    private ArrayList<Pagermodel> mSliderItems = new ArrayList<>();


    public ImageSliderAdapter(Context context,ArrayList<Pagermodel> itm) {
        this.context=context;
        this.mSliderItems = itm;
    }

    public void renewItems(ArrayList<Pagermodel> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Pagermodel sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpageritem, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        Picasso.with(context).load(APIs.IMG_URL+mSliderItems.get(position).getSlidimg()).error(R.drawable.no_image).into(viewHolder.imageViewBackground);
        viewHolder.sliderTittle.setText(mSliderItems.get(position).getSliderTittle());

        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DescPopUP(position);
            }
        });


    }

    @Override
    public int getCount() {
        //slider view
        // count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView sliderTittle;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.pager_image);
            sliderTittle = itemView.findViewById(R.id.sliderTitle);
            this.itemView = itemView;
        }
    }
    public void DescPopUP(int pos){
        cnfrmpswrddialog=new Dialog(context);
        cnfrmpswrddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cnfrmpswrddialog.setCancelable(true);

        cnfrmpswrddialog.setContentView(R.layout.slide_desc_popup);
        slideTitle=cnfrmpswrddialog.findViewById(R.id.descsliderTitle);
        slideDEsc=cnfrmpswrddialog.findViewById(R.id.slide_img_desc);
        slideImg=cnfrmpswrddialog.findViewById(R.id.desc_image);
        removepopup = cnfrmpswrddialog.findViewById(R.id.remove);

        Window window = cnfrmpswrddialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        slideDEsc.setText(mSliderItems.get(pos).getSliderDesc());
        slideTitle.setText(mSliderItems.get(pos).getSliderTittle());
        Picasso.with(context).load(APIs.IMG_URL+mSliderItems.get(pos).getSlidimg()).error(R.drawable.no_image).into(slideImg);
        removepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnfrmpswrddialog.dismiss();
            }
        });

        cnfrmpswrddialog.show();

    }

}

