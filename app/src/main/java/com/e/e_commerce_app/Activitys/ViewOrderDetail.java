package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.OrderDetailProAdapter;
import com.e.e_commerce_app.Adapters.ProductImage_Slider;
import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.Model.ProductDetailModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.graphics.Color.WHITE;

public class ViewOrderDetail extends AppCompatActivity {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    RecyclerView orderproductlistview;
    OrderDetailProAdapter orderDetailProAdapter;
    ArrayList<ProductDetailModel> orderproductlist;
    TextView OrderDate, OrderId, OrderTotal, PaymentMethod, BillingAdress, BillingCity, BillingState, BillingPinCode, ShippingName,
            ShippingCustNmbr, TotalDiscount, OrderSubTotal, OderGrandTotal;
    String order_id, order_status;
    RelativeLayout cancelOrder, trackOrder;
    LinearLayout orderDetailMainLayout, orderDetailError;
    TextView orderDetailErrormsg;
    TextView tv;
    int temp_badge=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_detail);
        sessionManager = new SessionManager(ViewOrderDetail.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        orderDetailError = findViewById(R.id.orderdetailerror);
        orderDetailErrormsg = findViewById(R.id.orderDetailerrormsg);
        orderDetailMainLayout = findViewById(R.id.orderdetailmainLAyout);
        OrderDate = findViewById(R.id.orderDate);
        OrderId = findViewById(R.id.orderID);
        OrderTotal = findViewById(R.id.orderTotal1);
        PaymentMethod = findViewById(R.id.OrderPm);
        orderproductlistview = findViewById(R.id.oederdetailproductlist);
        BillingAdress = findViewById(R.id.Orderbillingadd);
        BillingCity = findViewById(R.id.Orderbillingcity);
        BillingState = findViewById(R.id.Orderbillingstate);
        BillingPinCode = findViewById(R.id.Orderbillingpincode);
        ShippingName = findViewById(R.id.OrdershipName);
        ShippingCustNmbr = findViewById(R.id.OrderCustNmbr);

        cancelOrder = findViewById(R.id.cancelbtn);
        TotalDiscount = findViewById(R.id.totlDiscount);
        OrderSubTotal = findViewById(R.id.OrderSubTotal);
        OderGrandTotal = findViewById(R.id.OrdergrandTotal);
        trackOrder = findViewById(R.id.trackorder);

        if (getIntent().getExtras() != null)
            order_id = (getIntent().getExtras().getString("order_id"));
        order_status = (getIntent().getExtras().getString("order_status"));

        if (order_status.equals("cancel") || order_status.equals("cancelled")) {
            cancelOrder.setVisibility(View.GONE);
            trackOrder.setVisibility(View.GONE);
        }

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrderDetail.this, CancelOrderActivity.class);
                intent.putExtra("orderid", "" + OrderId.getText().toString());
                startActivity(intent);

            }
        });
        trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrderDetail.this, TempActivity.class);
                intent.putExtra("orderid", "" + OrderId.getText().toString());
                startActivity(intent);

            }
        });


        getCategoryProductDetail();
    }

    public void getCategoryProductDetail() {
        orderDetailMainLayout.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(ViewOrderDetail.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e(" order_detail_url", "=" + APIs.GetOrderDetails);

        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GetOrderDetails,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(" order_detail=", "=" + response);
                        progressDialog.dismiss();
                        orderDetailMainLayout.setVisibility(View.VISIBLE);
                        orderproductlist = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("order_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    OrderId.setText(jsonObject1.getString("" + "order_id"));
                                    OrderTotal.setText(jsonObject1.getString("₹" + "grand_total"));
                                    BillingAdress.setText(jsonObject1.getString("" + "billing_address"));
                                    BillingCity.setText(jsonObject1.getString("" + "billing_city"));
                                    BillingState.setText(jsonObject1.getString("" + "billing_state"));
                                    BillingPinCode.setText(jsonObject1.getString("" + "billing_pincode"));
                                    PaymentMethod.setText(jsonObject1.getString("" + "payment_method"));
                                    TotalDiscount.setText(jsonObject1.getString("" + "total_discount"));
                                    OrderSubTotal.setText("₹" + jsonObject1.getString("" + "order_sub_total"));
                                    OderGrandTotal.setText("₹" + jsonObject1.getString("" + "grand_total"));
                                    ShippingName.setText(jsonObject1.getString("" + "customer_name"));
                                    ShippingCustNmbr.setText("Mobile:" + jsonObject1.getString("" + "customer_mobile"));

                                    JSONObject jsonObject01 = jsonObject1.getJSONObject("order_date");
                                    String date = jsonObject01.getString("date");//.substring(0,10);
                                    Log.e("date", "=" + date);
                                    try {
                                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
                                        Date newDate = spf.parse(date);
                                        Log.e("newDate", "=" + newDate);
                                        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
                                        date = spf.format(newDate);
                                        Log.e("date1", "=" + date);
                                        OrderDate.setText(date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                JSONArray jsonArray2 = jsonObject.getJSONArray("order_products");
                                for (int k = 0; k < jsonArray2.length(); k++) {
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                    ProductDetailModel productDetailModel = new ProductDetailModel();
                                    productDetailModel.setProDetailId(jsonObject2.getString("product_id"));
                                    productDetailModel.setProDetailitemImg(jsonObject2.getString("image"));
                                    productDetailModel.setProDetailitemNmae(jsonObject2.getString("product_name"));
                                    productDetailModel.setProDetailitemPrice(jsonObject2.getString("price"));
                                    productDetailModel.setProDetailitemQyn(jsonObject2.getString("quantity"));
                                    orderproductlist.add(productDetailModel);
                                }

                                orderDetailProAdapter = new OrderDetailProAdapter(ViewOrderDetail.this, orderproductlist);
                                final LinearLayoutManager linearLayoutManager;
                                linearLayoutManager = new LinearLayoutManager(ViewOrderDetail.this, LinearLayoutManager.VERTICAL, false);
                                orderproductlistview.setHasFixedSize(false);
                                orderproductlistview.setLayoutManager(linearLayoutManager);
                                orderproductlistview.setItemAnimator(new DefaultItemAnimator());
                                orderproductlistview.setAdapter(orderDetailProAdapter);
                                orderproductlistview.setNestedScrollingEnabled(false);
                            } else {
                                Toast.makeText(ViewOrderDetail.this, "" + message, Toast.LENGTH_SHORT).show();
                                orderDetailMainLayout.setVisibility(View.GONE);
                                orderDetailError.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            Log.e("order_detail_res", "=1");
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();

                    }
                }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", "" + sessionManager.getString("userid"));
                params.put("order_id", "" + order_id);


                Log.e("params", "=" + params.toString());

                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.cart_menu, menu);
        final MenuItem item = menu.findItem(R.id.cart_icon);
        MenuItemCompat.setActionView(item, R.layout.cart_count_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText("" + sharedPreferences.getString("carttotal", null));
        setupBadge();
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupBadge() {
        temp_badge=Integer.parseInt(tv.getText().toString());
        if (tv != null) {
            if (tv.getText().equals("0") || tv.getText().equals("null") || temp_badge<0) {
                if (tv.getVisibility() != View.GONE) {
                    tv.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            case R.id.cart_icon:
                Intent intent2 = new Intent(ViewOrderDetail.this, MyCartActivity.class);
                startActivity(intent2);
                return true;


        }
        return true;
    }
}
