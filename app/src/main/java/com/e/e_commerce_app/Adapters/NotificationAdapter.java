package com.e.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.e_commerce_app.Model.Notification_Model;
import com.e.e_commerce_app.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {
    Context context;
    ArrayList<Notification_Model> notificationList;

    public NotificationAdapter(Context context,ArrayList<Notification_Model> notificationList) {
        this.context=context;
        this.notificationList=notificationList;
    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notificationview= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new MyHolder(notificationview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.notification_tittle.setText(notificationList.get(position).getNotification_Tittle());
        holder.notification_msg.setText(notificationList.get(position).getNotification_Message());

    }

    @Override
    public int getItemCount() {
        return notificationList.size() ;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notification_tittle,notification_msg;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            notification_tittle=itemView.findViewById(R.id.noti_tittle);
            notification_msg=itemView.findViewById(R.id.noti_msg);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
