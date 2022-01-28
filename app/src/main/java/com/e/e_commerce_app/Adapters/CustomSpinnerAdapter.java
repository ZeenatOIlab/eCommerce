package com.e.e_commerce_app.Adapters;


import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.e.e_commerce_app.Model.SpinModel;
import com.e.e_commerce_app.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<SpinModel> {
    LayoutInflater flater;


    public CustomSpinnerAdapter(Context context, int resourceId, int textViewId, ArrayList<SpinModel> spinlist) {
        super(context, resourceId, textViewId, spinlist);
        // flater = context.getLayoutInflater();

    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        SpinModel rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.spiiner_item, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.spin_itemName);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getSpinItemName().toString()
        );



        return rowview;
    }
    private class viewHolder{
        TextView txtTitle;
    }

}

