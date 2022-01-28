package com.e.e_commerce_app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Activitys.OrderProcessActivity;
import com.e.e_commerce_app.Model.ExistAdressModel;
import com.e.e_commerce_app.R;

import java.util.ArrayList;

public class MultipleExistAdressAdapter extends RecyclerView.Adapter<MultipleExistAdressAdapter.MyViewHolder> {
    ArrayList<ExistAdressModel> existAdressList = new ArrayList<>();
    Context context;
    RadioButton newRadio;
    private RadioButton selected = null;
    LinearLayout newAddLayout;

    public MultipleExistAdressAdapter(Context context, ArrayList<ExistAdressModel> existAdressList) {
        this.context = context;
        this.existAdressList = existAdressList;
        newAddLayout=((OrderProcessActivity)context).findViewById(R.id.newaddlayout);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adressview = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_exist_adress_item, parent, false);
        return new MyViewHolder(adressview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
       // holder.exitAdress.setText("I want to use an existing adress");
        //  holder.exitNumber.setText(""+existAdressList.get(position).getExistMobile());
        holder.exitAdress.setText(existAdressList.get(position).getExistAdress());
        holder.exitCity.setText(existAdressList.get(position).getExistCity());
        holder.exitState.setText(existAdressList.get(position).getExistState());
        holder.exitCountry.setText(existAdressList.get(position).getExistCountry());
        holder.exitPincode.setText("" + existAdressList.get(position).getExistPincode());
        if (position == 0) {
            if (selected == null) {
               holder.exitAdress.setChecked(true);
                selected = holder.exitAdress;
            }
        }
        holder.exitAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected != null) {
                    selected.setChecked(false);
                }
                holder.exitAdress.setChecked(true);
                newAddLayout.setVisibility(View.GONE);
                newRadio.setChecked(false);
                selected = holder.exitAdress;
            }
        });

    }

    @Override
    public int getItemCount() {
        return existAdressList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton exitAdress;
        TextView  exitCity, exitState, exitCountry, exitPincode;
        int pos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //urradio = (RadioButton) itemView.findViewById(R.id.radioexistadress);

            exitAdress = (RadioButton) itemView.findViewById(R.id.et_ExitsAdrs);
            exitCity = (TextView) itemView.findViewById(R.id.et_ExitsCity);
            exitState = (TextView) itemView.findViewById(R.id.et_ExitsState);
            exitCountry = (TextView) itemView.findViewById(R.id.et_ExitsCntry);
            exitPincode = (TextView) itemView.findViewById(R.id.et_ExitsPincode);
            newRadio=((OrderProcessActivity)context).findViewById(R.id.radionewadress);
        }
    }


}
