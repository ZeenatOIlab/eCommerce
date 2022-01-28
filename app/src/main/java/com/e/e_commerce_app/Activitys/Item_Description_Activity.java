package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.MenuItemCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.AdressAdapter;
import com.e.e_commerce_app.Adapters.CustomSpinnerAdapter;
import com.e.e_commerce_app.Adapters.MyCartAdapter;
import com.e.e_commerce_app.Adapters.ProductImage_Slider;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.CartModel;
import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.Model.SpinModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.NetworkChecking;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.graphics.Color.WHITE;

public class Item_Description_Activity extends AppCompatActivity {
    RelativeLayout mainItemDescLyout;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProductImage_Slider productImage_slider;
    ArrayList<ItemListModel> itemdesclist;
    ArrayList<String> sizeList;
    SliderView sliderView;
    ImageView Heartbtn, Plusbtn, Minusbtn;
    TextView Quantitynmbr, itemStock, Addcart, Bynow;
    TextView sizeSpin;
    ArrayAdapter<String> spinnerAdapter;
    int minteger = 1;
    Boolean clicked = true;
    TextView pro_Price, pro_Name, pro_Color;
    String product_id;
    String color = "";
    String size;
    String proImage;
    TextView badge;
    LinearLayout CartByLayout;
    int cartbuynum = 0;
    String action_type = "";
    TextView itemDesc;
    String inc;
    String cart_id, card_ship_chrg, cart_discount;
    String item_price = "0", cart_item_total = "0", discount_type;
    int temp_badge=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__description_);
        sessionManager = new SessionManager(Item_Description_Activity.this);

        mainItemDescLyout = findViewById(R.id.mainItemDesLay);
        pro_Price = findViewById(R.id.pro_des_price);
        pro_Name = findViewById(R.id.tv_pro_d_name);
        pro_Color = findViewById(R.id.pro_dsc_color);
        sizeSpin = findViewById(R.id.sizespinner);
        Heartbtn = findViewById(R.id.heartbtn);
        Plusbtn = findViewById(R.id.plusbtn);
        Minusbtn = findViewById(R.id.minusbtn);
        Quantitynmbr = findViewById(R.id.quntity_nmbr);
        itemDesc = findViewById(R.id.itemDesc);
        itemStock = findViewById(R.id.itemStock);
        Addcart = findViewById(R.id.addcart);
        Bynow = findViewById(R.id.bynow);
        sliderView = findViewById(R.id.pro_item_pager);
        CartByLayout = findViewById(R.id.cartbybtn);
        mainItemDescLyout.setVisibility(View.INVISIBLE);

        if (getIntent().getExtras() != null) {
            product_id = (getIntent().getExtras().getString("Id"));
        }
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sizeList = new ArrayList<>();

        getCategoryProductDetail();
        getavailablestock();
        Heartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkChecking.isConnectedNetwork(Item_Description_Activity.this)) {
                    //internet is connected do something
                    Toast.makeText(Item_Description_Activity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email")
                        .isEmpty() && sessionManager.getString("password").equalsIgnoreCase("") ||
                        sessionManager.getString("password").isEmpty()) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.addcart), "You are not login ",
                            Snackbar.LENGTH_LONG).setAction("Go Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Item_Description_Activity.this, LoginActivity.class);
                            intent.putExtra("Item_to_login", "" + product_id);
                            intent.putExtra("ItemtologNmbr", "1");
                            startActivity(intent);
                            finish();

                        }
                    });
                    snackbar.show();
                    return;
                }

                addwishlist();
            }

        });
        Minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minteger == 1) {
                    Quantitynmbr.setText("1");
                } else {
                    minteger = minteger - 1;
                    Quantitynmbr.setText("" + minteger);
                }
            }
        });
        Plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartbuynum == 3) {
                    Toast.makeText(Item_Description_Activity.this, "Stock not available", Toast.LENGTH_SHORT).show();
                } else {
                    minteger = minteger + 1;
                    Quantitynmbr.setText("" + minteger);
                }
            }
        });
        Addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetworkChecking.isConnectedNetwork(Item_Description_Activity.this)) {
                    //internet is connected do something
                    Toast.makeText(Item_Description_Activity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty() && sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty()) {

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.addcart), "You are not login ",
                            Snackbar.LENGTH_LONG).setAction("Go Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Item_Description_Activity.this, LoginActivity.class);
                            intent.putExtra("Item_to_login", "" + product_id);
                            intent.putExtra("ItemtologNmbr", "1");
                            startActivity(intent);
                            finish();

                        }
                    });
                    snackbar.show();
                } else if (cartbuynum == 3) {
                    Toast.makeText(Item_Description_Activity.this, "Stock not available", Toast.LENGTH_SHORT).show();
                } else {
                    cartbuynum = 1;
                    addCart();

                }
            }
        });

        Bynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetworkChecking.isConnectedNetwork(Item_Description_Activity.this)) {
                    //internet is connected do something
                    Toast.makeText(Item_Description_Activity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty() && sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty()) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.addcart), "You are not login ",
                            Snackbar.LENGTH_LONG).setAction("Go Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Item_Description_Activity.this, LoginActivity.class);
                            intent.putExtra("Item_to_login", "" + product_id);
                            intent.putExtra("ItemtologNmbr", "1");
                            startActivity(intent);
                            finish();

                        }
                    });
                    snackbar.show();
                } else if (cartbuynum == 3) {
                    Toast.makeText(Item_Description_Activity.this, "Stock not available", Toast.LENGTH_SHORT).show();
                } else {
                    cartbuynum = 2;
                    addCart();
                }

            }
        });
        sizeSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SizeDialog();
            }
        });
    }

    public void getCategoryProductDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(Item_Description_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e(" categor_prdct_url", "=" + APIs.GET_PRODUCT_DETAILS);

        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemdesclist = new ArrayList<>();

                        Log.e(" get_pro", "=" + response);
                        progressDialog.dismiss();
                        mainItemDescLyout.setVisibility(View.VISIBLE);
                        String sizes;

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data_product");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("product_name").equals("null") || jsonObject1.getString("product_name").equalsIgnoreCase("")) {
                                        pro_Name.setText("N/A");
                                    } else {
                                        pro_Name.setText(jsonObject1.getString("product_name"));
                                    }
                                    if (jsonObject1.getString("color").equals("null") || jsonObject1.getString("color").equalsIgnoreCase("")) {
                                        pro_Color.setText("N/A");
                                    } else {
                                        pro_Color.setText(jsonObject1.getString("color"));

                                    }
                                    if (jsonObject1.getString("price").equals("null") || jsonObject1.getString("price").equalsIgnoreCase("")) {
                                        pro_Price.setText("N/A");
                                        item_price = "0";
                                    } else {
                                        item_price = jsonObject1.getString("price");
                                        pro_Price.setText("₹" + jsonObject1.getString("price"));

                                    }
                                    if (jsonObject1.getString("is_product_wishlisted").equals("yes")) {
                                        Heartbtn.setImageResource(R.drawable.heart);

                                    } else {
                                        Heartbtn.setImageResource(R.drawable.likeheart);
                                    }
                                    if (jsonObject1.getString("description").equals("null") || jsonObject1.getString("description").equalsIgnoreCase("")) {
                                        itemDesc.setText("N/A");
                                    } else {
                                        itemDesc.setText(Html.fromHtml(jsonObject1.getString("description")));
                                    }
                                    color = (String) pro_Color.getText();
                                    proImage = jsonObject1.getString("image");


                                    JSONArray jsonArray2 = jsonObject1.getJSONArray("related_image");
                                    Log.e("arraylng", "=" + jsonArray2.length());
                                    if (jsonArray2.length() == 0) {
                                        ItemListModel imgmodel4 = new ItemListModel();
                                        imgmodel4.setRelatedImg("" + R.drawable.no_image);
                                        itemdesclist.add(imgmodel4);
                                    } else {
                                        for (int j = 0; j < jsonArray2.length(); j++) {
                                            JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                            ItemListModel imgmodel4 = new ItemListModel();
                                            imgmodel4.setRelatedImg(jsonObject2.getString("image_name"));
                                            itemdesclist.add(imgmodel4);
                                        }
                                    }
                                    productImage_slider = new ProductImage_Slider(Item_Description_Activity.this, itemdesclist);
                                    sliderView.setSliderAdapter(productImage_slider);
                                    sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                    sliderView.setIndicatorSelectedColor(WHITE);
                                    sliderView.setIndicatorUnselectedColor(Color.BLACK);
                                    sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :\

                                    JSONArray jsonArray3 = jsonObject1.getJSONArray("product_attributes");
                                    if (jsonArray3.length() <= 0) {
                                        sizeSpin.setText("N/A");
                                        sizeList.add("N/A");
                                        discount_type = "0";
                                    } else {
                                        for (int k = 0; k < jsonArray3.length(); k++) {
                                            JSONObject jsonObject3 = jsonArray3.getJSONObject(k);
                                            sizes = (jsonObject3.getString("size"));
                                            discount_type = jsonObject3.getString("discount_type");
                                            sizeList.add(sizes);
                                        }
                                        sizeSpin.setText("" + sizeList.get(0).toString());
                                        spinnerAdapter = new ArrayAdapter<>(Item_Description_Activity.this, android.R.layout.simple_list_item_1, sizeList);
                                    }
                                }
                            } else {
                                Toast.makeText(Item_Description_Activity.this, "" + message, Toast.LENGTH_SHORT).show();//
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            Log.e("categoryproductlist", "=1");
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
                params.put("product_id", "" + product_id);
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

    public void getavailablestock() {
        final ProgressDialog progressDialog = new ProgressDialog(Item_Description_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("Stock url", "" + APIs.StockAvailable);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.StockAvailable,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" stock response", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                // itemStock.setText(""+jsonObject.get("total_products")+" in stock");
                                itemStock.setText("In Stock");
                                itemStock.setTextColor(Item_Description_Activity.this.getResources().getColor(R.color.green));

                            } else {
                                itemStock.setText("Out Of Stock");
                                itemStock.setTextColor(Item_Description_Activity.this.getResources().getColor(R.color.red));
                                cartbuynum = 3;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my stock", "=1");
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
                params.put("product_id", "" + product_id);
                Log.e("param stock", params.toString());
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void addCart() {
        size = sizeSpin.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(Item_Description_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("addtocart_url", "" + APIs.ADD_CART);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.ADD_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" add_cart", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                cart_id = (jsonObject.getString("cart_id"));
                                if (jsonObject.getString("shipping_charges").equals("null") || jsonObject.getString("shipping_charges").equals("")) {
                                    card_ship_chrg = "0";

                                } else {
                                    card_ship_chrg = (jsonObject.getString("shipping_charges"));
                                }
                                if (jsonObject.getString("discount").equals("null") || jsonObject.getString("discount").equals("")) {
                                    cart_discount = "0";
                                } else {
                                    cart_discount = (jsonObject.getString("discount"));
                                }
                                if (jsonObject.getString("item_count").equals("null") || jsonObject.getString("item_count").equals("")) {
                                    inc = "0";
                                } else {
                                    inc = jsonObject.getString("item_count");
                                }

                                badge.setText(jsonObject.getString("item_count"));
                                editor.putString("carttotal", "" + inc);
                                editor.commit();
                                finish();
                                startActivity(getIntent());
                                if (cartbuynum == 1) {
                                    Toast.makeText(Item_Description_Activity.this, "" + message, Toast.LENGTH_SHORT).show();

                                } else {
                                    CartModel cartModel = new CartModel();
                                    cartModel.setCartItemName(pro_Name.getText().toString());
                                    cartModel.setCartQuantity(Quantitynmbr.getText().toString());
                                    cartModel.setCartId(cart_id);
                                    cartModel.setCartItemShipngChrg(card_ship_chrg);
                                    cartModel.setCartItemImg(proImage);
                                    cartModel.setCartItemPrice(item_price);
                                    cartModel.setCartItemDiscnt(cart_discount);
                                    if (discount_type == "Rs") {
                                        cartModel.setCartItemDiscntType("Rs");
                                    } else if (discount_type == "%") {
                                        double p = Double.parseDouble(item_price);
                                        double d = Double.parseDouble(cart_discount);
                                        Double s = 100 - d;
                                        Double res = (s * p) / 100;
                                        cartModel.setCartItemDiscntType("Rs");
                                    }
                                    ApplicationClass.cartModelslist.add(cartModel);
                                    Log.e("cartp", "" + cartModel.getCartItemPrice());
                                    Log.e("daats", "" + cartModel.getCartItemShipngChrg());
                                    Log.e("daatq", "" + cartModel.getCartQuantity());
                                    Intent intent = new Intent(Item_Description_Activity.this, OrderProcessActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                Toast.makeText(Item_Description_Activity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my cart", "=1");
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
                params.put("products_id", "" + product_id);
                params.put("size", "" + size);
                params.put("quantity", "" + Quantitynmbr.getText());
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

    public void addwishlist() {
        final String msg;
        if (clicked) {
            Heartbtn.setImageResource(R.drawable.heart);
            action_type = "add";
            msg = "added wishlist";
            clicked = false;
        } else {
            clicked = true;
            Heartbtn.setImageResource(R.drawable.likeheart);
            msg = "removed wishlist";
            action_type = "delete";

        }
        final ProgressDialog progressDialog = new ProgressDialog(Item_Description_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("addwlurl", "" + APIs.ADDWISHLIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.ADDWISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" add_wisjlist", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.heartbtn), "" + msg, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                Toast.makeText(Item_Description_Activity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my wishlist", "=1");
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
                params.put("product_id", "" + product_id);
                params.put("action_type", "" + action_type);
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
        if (sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty() && sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty()) {
            MenuInflater inflater = getMenuInflater();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        } else {

            MenuInflater inflater = getMenuInflater();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            inflater.inflate(R.menu.cart_menu, menu);
            final MenuItem item = menu.findItem(R.id.cart_icon);
            MenuItemCompat.setActionView(item, R.layout.cart_count_layout);
            RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

            badge = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            badge.setText("" + sharedPreferences.getString("carttotal", null));
            setupBadge();
            notifCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);

                }
            });
            return super.onCreateOptionsMenu(menu);
        }

    }

    private void setupBadge() {
        temp_badge=Integer.parseInt(badge.getText().toString());
        if (badge != null) {
            if (badge.getText().equals("0") || badge.getText().equals("null") || temp_badge<0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
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
                Intent intent2 = new Intent(Item_Description_Activity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;
        }
        return true;
    }

    public void SizeDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(Item_Description_Activity.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
     //   dialog.getWindow().setLayout(500, 500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText editText = dialog.findViewById(R.id.dialog_edittext);
        ListView listview = dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle = dialog.findViewById(R.id.dialog_tittle);
        editText.setVisibility(View.GONE);
        dialog_tittle.setTextSize(16);
        dialog_tittle.setText("choose size");
        final ImageView imageView = dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        spinnerAdapter = new ArrayAdapter<>(Item_Description_Activity.this, android.R.layout.simple_list_item_1, sizeList);
        listview.setAdapter(spinnerAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                sizeSpin.setText(spinnerAdapter.getItem(position));
                dialog.dismiss();
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }


}
