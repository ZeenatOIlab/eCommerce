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

import com.e.e_commerce_app.Activitys.ProductsActivity;
import com.e.e_commerce_app.Model.CategoryModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

   ArrayList<CategoryModel> catlist=new ArrayList<CategoryModel>();
    Context context;
    SelectCategoryListener listener;

    public CategoryAdapter(Context context,ArrayList<CategoryModel> catlist, SelectCategoryListener listener)
    {
        this.context=context;
        this.catlist=catlist;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View catitemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.categori_item,parent,false);
        return new MyViewHolder(catitemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.with(context).load(APIs.CATEGORY_IMG_URL+catlist.get(position).getCatImg()).error(R.drawable.no_image).into(holder.caItemImage);
        // holder.caItemImage.setImageResource(Integer.parseInt(""+catlist.get(position).getCatImg()));
        holder.caItemName.setText(catlist.get(position).getCatName());
        holder.catId.setText(""+catlist.get(position).getCatId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent=new Intent(context, ProductsActivity.class);
                        intent.putExtra("parsecatId",""+catlist.get(position).getCatId());
                        context.startActivity(intent);

                    }



        });

    }

    @Override
    public int getItemCount() {
        return catlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView caItemImage;
        TextView caItemName,catId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            caItemImage=itemView.findViewById(R.id.cat_img);
            caItemName=itemView.findViewById(R.id.cat_pro_name);
            catId=itemView.findViewById(R.id.cat_id);


        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface SelectCategoryListener {
        void categorySelected(String category);
    }

}
