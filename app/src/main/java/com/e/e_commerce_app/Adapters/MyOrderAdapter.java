package com.e.e_commerce_app.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Activitys.MyOrdersActivity;
import com.e.e_commerce_app.Activitys.OrderProcessActivity;
import com.e.e_commerce_app.Activitys.ViewOrderDetail;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.MyOrderModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    Context context;
    static ArrayList<MyOrderModel> myOrderList = new ArrayList<MyOrderModel>();
    onClickInterface onClickInterface;
    SessionManager sessionManager;

    public MyOrderAdapter(Context context, ArrayList<MyOrderModel> myOrderList, onClickInterface onClickInterface) {
        this.context = context;
        this.myOrderList = myOrderList;
        sessionManager = new SessionManager(context);
        this.onClickInterface = onClickInterface;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorderitems, parent, false);
        return new MyViewHolder(orderitemview);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        OrderProductsAdapter orderProductsAdapter = new OrderProductsAdapter(context, myOrderList.get(position).getProduct_list());
        holder.myorderlist.setAdapter(orderProductsAdapter);

        holder.myorderId.setText("" + myOrderList.get(position).getOrderid());
        holder.myorderstatus.setText("" + myOrderList.get(position).getOrderstatus());
        holder.myordertotal.setText("₹" + myOrderList.get(position).getOrdertoal());
        holder.orderDate.setText("" + myOrderList.get(position).getOrderDate());

        String date = myOrderList.get(position).getOrderDate();//.substring(0,10);
        try {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
            Date newDate = spf.parse(date);
            Log.e("newDate", "=" + newDate);
            spf = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
            date = spf.format(newDate);
            Log.e("date1", "=" + date);
            holder.orderDate.setText(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myOrderList.get(position).getOrderstatus().equals("cancelled")) {
            holder.myorderstatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if (myOrderList.get(position).getOrderstatus().equals("pending")) {
            holder.myorderstatus.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.myorderstatus.setTextColor(context.getResources().getColor(R.color.orange));
        }
        if (myOrderList.get(position).getOrderstatus().equals("payment pending")) {
            holder.btn_make_payment.setVisibility(View.VISIBLE);
        } else {
            holder.btn_make_payment.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewOrderDetail.class);
                intent.putExtra("order_id", "" + myOrderList.get(position).getOrderid());
                intent.putExtra("order_status", "" + myOrderList.get(position).getOrderstatus());
                context.startActivity(intent);
                onClickInterface.setClick(position);

            }
        });
        holder.btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                ((MyOrdersActivity) context).startPayment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ListView myorderlist;
        TextView myorderId, myorderstatus, myordertotal, orderDate;
        Button btn_make_payment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myorderlist = itemView.findViewById(R.id.orderlistview);
            myorderId = itemView.findViewById(R.id.orderid);
            myorderstatus = itemView.findViewById(R.id.orderStatus);
            myordertotal = itemView.findViewById(R.id.ordertotal);
            orderDate = itemView.findViewById(R.id.orderDte);
            btn_make_payment = itemView.findViewById(R.id.btn_make_payment);
        }
    }
    public interface onClickInterface {
        void setClick(int abc);
    }


}
