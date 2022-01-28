package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.MyCartAdapter;
import com.e.e_commerce_app.Adapters.MyOrderAdapter;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.CartModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.NetworkChecking;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyCartActivity extends AppCompatActivity {
    SessionManager sessionManager;
    LinearLayout cartlist;
    RelativeLayout cartamnt;
    LinearLayout cartError;
    RecyclerView cartRecycle;
    ArrayList<CartModel> cartItemList;
    MyCartAdapter myCartAdapter;
    private MyCartAdapter.onClickInterface onclickInterface;
    TextView SubTotal, btn_Chechout, cart_er_txt;
    String cart_id;
    CheckBox cb;
    int temp = 0;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        sessionManager = new SessionManager(MyCartActivity.this);
        cartRecycle = findViewById(R.id.cartRecycleList);
        cartlist = findViewById(R.id.cartlist);
        cartamnt = findViewById(R.id.cartanmmt);
        cartError = findViewById(R.id.carterror);
        cart_er_txt = findViewById(R.id.cart_err_txt);
        SubTotal = findViewById(R.id.cartsubtotal);
        btn_Chechout = findViewById(R.id.cart_checkout);
        if (getIntent().getExtras() != null) {
            cart_id = (getIntent().getExtras().getString("add_my_cart"));
        }
        if (!NetworkChecking.isConnectedNetwork(MyCartActivity.this)) {
            //internet is connected do something
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            cart_er_txt.setText("Internet Not Available ");
            cartError.setVisibility(View.VISIBLE);
            cartamnt.setVisibility(View.GONE);
            cartlist.setVisibility(View.GONE);
        }
        onclickInterface = new MyCartAdapter.onClickInterface() {
            @Override
            public void setClick(int abc) {
                pos = abc;
            }
        };
        btn_Chechout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb = cartRecycle.getChildAt(pos).findViewById(R.id.select_checkbox);
                for (int i = 0; i < cartItemList.size(); i++) {
                    if (!cb.isChecked()) {
                        Toast.makeText(MyCartActivity.this, "Select item", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MyCartActivity.this, OrderProcessActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        getMyCart();
    }

            public void getMyCart() {
                final ProgressDialog progressDialog = new ProgressDialog(MyCartActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                cartlist.setVisibility(View.GONE);
                cartamnt.setVisibility(View.GONE);

                Log.e("mycart_url", "" + APIs.CHECKOUT);
                final StringRequest request = new StringRequest(Request.Method.POST,
                        APIs.CHECKOUT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(" my_cart", "=" + response);
                                cartItemList = new ArrayList<>();

                                progressDialog.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equalsIgnoreCase("1")) {
                                        cartlist.setVisibility(View.VISIBLE);
                                        cartamnt.setVisibility(View.VISIBLE);
                                        SubTotal.setText("₹" + jsonObject.getInt("total_price"));
                                        JSONArray jsonArray = jsonObject.getJSONArray("cart_data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            CartModel cartModel2 = new CartModel();
                                            cartModel2.setCartId(jsonObject1.getString("cart_id"));
                                            cartModel2.setCartItemName(jsonObject1.getString("product_name"));
                                            cartModel2.setCartQuantity(jsonObject1.getString("quantity"));
                                            cartModel2.setCartItemPrice("" + jsonObject1.getString("price"));
                                            cartModel2.setcId(jsonObject1.getInt("products_id"));
                                            cartModel2.setCartItemImg(jsonObject1.getString("image"));
                                            cartModel2.setCartItemDiscnt(jsonObject1.getString("discount_price"));
                                            cartModel2.setCartItemShipngChrg(jsonObject1.getString("single_prod_shipping_charges"));
                                            cartModel2.setCartGrandTotal(jsonObject.getString("total_price"));
                                            cartModel2.setCartItemDiscntType("Rs");
                                            int p = Integer.parseInt(jsonObject1.getString("price"));
                                            int q = Integer.parseInt(jsonObject1.getString("quantity"));
                                            int r = p * q;
                                            cartModel2.setCartItemTotalPrice(String.valueOf(r));
                                            double d = Double.parseDouble(jsonObject1.getString("discount_price"));
                                            int s = Integer.parseInt(jsonObject1.getString("single_prod_shipping_charges"));
                                            int st = (s + r) - (int) d;
                                            cartModel2.setCartItemSubTotal(String.valueOf(st));
                                            cartItemList.add(cartModel2);

                                        }
                                        myCartAdapter = new MyCartAdapter(MyCartActivity.this, cartItemList, onclickInterface);
                                        final LinearLayoutManager linearLayoutManager;
                                        linearLayoutManager = new LinearLayoutManager(MyCartActivity.this, LinearLayoutManager.VERTICAL, false);
                                        cartRecycle.setHasFixedSize(false);
                                        cartRecycle.setLayoutManager(linearLayoutManager);
                                        cartRecycle.setItemAnimator(new DefaultItemAnimator());
                                        cartRecycle.setAdapter(myCartAdapter);
                                    } else {
                                        cartError.setVisibility(View.VISIBLE);
                                        cartamnt.setVisibility(View.GONE);
                                        Toast.makeText(MyCartActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.e("resp my cartdata", "=1");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();

                            }
                        }) {
                    protected Map<String, String> getParams() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("user_id", "" + sessionManager.getString("userid"));
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
                //  MenuInflater inflater = getMenuInflater();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case android.R.id.home:
                        ApplicationClass.cartModelslist.clear();
                        onBackPressed();

                        return true;
                }
                return true;
            }

            @Override
            public void onBackPressed() {
                ApplicationClass.cartModelslist.clear();
                super.onBackPressed();
            }

            @Override
            protected void onRestart() {
                super.onRestart();
                recreate();
            }
        }
