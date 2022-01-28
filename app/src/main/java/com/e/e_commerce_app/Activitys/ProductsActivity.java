package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.e.e_commerce_app.Adapters.ItemListAdapter;
import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProductsActivity extends AppCompatActivity {

    RecyclerView recyclerViewcatList;
    ArrayList<ItemListModel> itemlist;
    ItemListAdapter itemListAdapter;
    DrawerLayout mDrawerLayout;
    RelativeLayout proLayout;
    String catid;
    EditText searchrditText;
    LinearLayout productErrorLayout;
    TextView ProError, SearchError;
    CheckBox cb10dscnt, cb20dscnt, cb30dscnt, cb40dscnt, cb50dscnt, cb60dscnt, cb70dscnt, cb80dscnt, cb90dscnt, cb100dscnt;
    Button applyfilter;
    String minprice, maxprice, discount;
    ArrayList<String> minprice_list;
    ArrayList<String> maxprice_list;
    ArrayAdapter<String> min_price_Adapter, max_price_Adapter;
    TextView min_price_spinner, max_price_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerViewcatList = findViewById(R.id.cat_products_list);
        if (getIntent().getExtras() != null) {
            catid = (getIntent().getExtras().getString("parsecatId"));
        }

        mDrawerLayout = findViewById(R.id.drawerlayout);
        proLayout = findViewById(R.id.pro_layout);
        productErrorLayout = findViewById(R.id.producterror);
        SearchError = findViewById(R.id.searcherror);
        ProError = findViewById(R.id.product_errormsg);
        searchrditText = findViewById(R.id.searchText);
        min_price_spinner = findViewById(R.id.min_price_spin);
        max_price_spinner = findViewById(R.id.max_price_spin);

        cb10dscnt = findViewById(R.id.dscnt10);
        cb20dscnt = findViewById(R.id.dscnt20);
        cb30dscnt = findViewById(R.id.dscnt30);
        cb40dscnt = findViewById(R.id.dscnt40);
        cb50dscnt = findViewById(R.id.dscnt50);
        cb60dscnt = findViewById(R.id.dscnt60);
        cb70dscnt = findViewById(R.id.dscnt70);
        cb80dscnt = findViewById(R.id.dscnt80);
        cb90dscnt = findViewById(R.id.dscnt90);
        cb100dscnt = findViewById(R.id.dscnt100);

        applyfilter = findViewById(R.id.btnfilterapply);

        SearchError.setVisibility(View.GONE);
        final ImageView filter = findViewById(R.id.smenu);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        searchrditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Gafter the change calling the method and passing the search input
                filter(s.toString());

            }
        });
        minprice_list = new ArrayList<>();
        maxprice_list = new ArrayList<>();
        minprice_list.add("50");
        maxprice_list.add("100");
        minprice_list.add("100");
        maxprice_list.add("150");
        minprice_list.add("150");
        maxprice_list.add("200");
        minprice_list.add("200");
        maxprice_list.add("250");
        minprice_list.add("250");
        maxprice_list.add("300");
        minprice_list.add("300");
        maxprice_list.add("350");
        minprice_list.add("350");
        maxprice_list.add("400");
        minprice_list.add("400");
        maxprice_list.add("450");
        minprice_list.add("450");
        maxprice_list.add("500");
        minprice_list.add("500");
        maxprice_list.add("550");
        minprice_list.add("550");
        maxprice_list.add("600");
        minprice_list.add("650");
        maxprice_list.add("650");
        minprice_list.add("700");
        maxprice_list.add("750");
        minprice_list.add("750");
        maxprice_list.add("800");
        minprice_list.add("800");
        maxprice_list.add("850");
        minprice_list.add("850");
        maxprice_list.add("900");
        minprice_list.add("900");
        maxprice_list.add("950");
        minprice_list.add("950");
        maxprice_list.add("1000");
        min_price_spinner.setText("" + minprice_list.get(0).toString());
        max_price_spinner.setText("" + maxprice_list.get(0).toString());

        min_price_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Min_price_dilaog();
            }
        });
        max_price_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Max_price_dilaog();
            }
        });


        getCategoryProduct();
        applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterProduct();
            }
        });
        cb10dscnt.setChecked(true);


        cb10dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb10dscnt.isChecked()) {
                    cb10dscnt.setChecked(true);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb50dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb20dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb20dscnt.isChecked()) {
                    cb20dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb50dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb30dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb30dscnt.isChecked()) {
                    cb30dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb50dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb40dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb40dscnt.isChecked()) {
                    cb40dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb50dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb50dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb50dscnt.isChecked()) {
                    cb50dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb60dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb60dscnt.isChecked()) {
                    cb60dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb70dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb70dscnt.isChecked()) {
                    cb70dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb80dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb80dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb80dscnt.isChecked()) {
                    cb80dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb90dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb90dscnt.isChecked()) {
                    cb90dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb100dscnt.setChecked(false);
                }
            }
        });
        cb100dscnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb100dscnt.isChecked()) {
                    cb100dscnt.setChecked(true);
                    cb10dscnt.setChecked(false);
                    cb20dscnt.setChecked(false);
                    cb30dscnt.setChecked(false);
                    cb40dscnt.setChecked(false);
                    cb60dscnt.setChecked(false);
                    cb70dscnt.setChecked(false);
                    cb90dscnt.setChecked(false);
                }
            }
        });

    }

    private void filter(String text) {


        //new array list that will hold the filtered data
        ArrayList<ItemListModel> filterdNames = new ArrayList<>();
        int flag = 0;
        int pos = 0;
        SearchError.setVisibility(View.GONE);

        //looping through existing elements
        for (ItemListModel s : itemlist) {

            //if the existing elements contains the search input
            if (s.getPro_Name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                recyclerViewcatList.setVisibility(View.VISIBLE);
                SearchError.setVisibility(View.GONE);
                filterdNames.add(s);
                flag = 1;
            }

        }
        if (flag == 0) {
            recyclerViewcatList.setVisibility(View.GONE);
            SearchError.setVisibility(View.VISIBLE);
        } else {
            itemListAdapter.filterList(filterdNames);
            recyclerViewcatList.setVisibility(View.VISIBLE);
            SearchError.setVisibility(View.GONE);
        }

    }

    public void getCategoryProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(ProductsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e(" categor_prdct_url", "=" + APIs.GET_CTEGORY_PRODUCT);

        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_CTEGORY_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(" get_category_product", "=" + response);
                        progressDialog.dismiss();
                        itemlist = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                ProError.setVisibility(View.GONE);

                                JSONArray jsonArray = jsonObject.getJSONArray("data_product");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ItemListModel itemListModel1 = new ItemListModel();
                                    itemListModel1.setID(jsonObject1.getInt("id"));
                                    itemListModel1.setListimg(jsonObject1.getString("image"));
                                    itemListModel1.setPro_Name(jsonObject1.getString("product_name"));
                                    itemListModel1.setPro_Price(jsonObject1.getInt("price"));
                                    itemListModel1.setPro_Ava("Available");

                                    itemlist.add(itemListModel1);
                                }
                                itemListAdapter = new ItemListAdapter(ProductsActivity.this, itemlist, new ItemListAdapter.SelectProductListener() {
                                    @Override
                                    public void productlistner(String product) {

                                    }
                                });
                                RecyclerView.LayoutManager manager = new GridLayoutManager(ProductsActivity.this, 2);
                                recyclerViewcatList.setHasFixedSize(false);
                                recyclerViewcatList.setLayoutManager(manager);
                                recyclerViewcatList.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewcatList.setAdapter(itemListAdapter);


                            } else {
                                productErrorLayout.setVisibility(View.VISIBLE);
                                ProError.setText("" + message);
                                Toast.makeText(ProductsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            productErrorLayout.setVisibility(View.VISIBLE);
                            Log.e("categoryproductlist", "=" + e);
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
                params.put("cat_id", "" + catid);
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

    public void getFilterProduct() {
        maxprice = max_price_spinner.getText().toString();
        minprice = min_price_spinner.getText().toString();
        if (cb10dscnt.isChecked()) {
            discount = "10";
        }
        if (cb20dscnt.isChecked()) {
            discount = "20";
        }
        if (cb30dscnt.isChecked()) {
            discount = "30";
        }
        if (cb40dscnt.isChecked()) {
            discount = "40";
        }
        if (cb50dscnt.isChecked()) {
            discount = "50";
        }
        if (cb60dscnt.isChecked()) {
            discount = "60";
        }
        if (cb70dscnt.isChecked()) {
            discount = "70";
        }
        if (cb80dscnt.isChecked()) {
            discount = "80";
        }
        if (cb90dscnt.isChecked()) {
            discount = "90";
        } if (cb100dscnt.isChecked()) {
            discount = "100";
        }

        final ProgressDialog progressDialog = new ProgressDialog(ProductsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e(" filter_prdct_url", "=" + APIs.GET_FILTER_PRODUCT);

        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_FILTER_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(" get_filter_product", "=" + response);
                        progressDialog.dismiss();
                        itemlist = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                ProError.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data_product");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ItemListModel itemListModel1 = new ItemListModel();
                                    itemListModel1.setID(jsonObject1.getInt("id"));
                                    itemListModel1.setListimg(jsonObject1.getString("image"));
                                    itemListModel1.setPro_Name(jsonObject1.getString("product_name"));
                                    itemListModel1.setPro_Price(jsonObject1.getInt("price"));
                                    itemListModel1.setPro_Ava("Available");
                                    itemlist.add(itemListModel1);
                                }
                                itemListAdapter = new ItemListAdapter(ProductsActivity.this, itemlist, new ItemListAdapter.SelectProductListener() {
                                    @Override
                                    public void productlistner(String product) {

                                    }
                                });
                                RecyclerView.LayoutManager manager = new GridLayoutManager(ProductsActivity.this, 2);
                                recyclerViewcatList.setHasFixedSize(false);
                                recyclerViewcatList.setLayoutManager(manager);
                                recyclerViewcatList.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewcatList.setAdapter(itemListAdapter);
                                itemListAdapter.notifyDataSetChanged();
                                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                            } else {
                                recyclerViewcatList.setVisibility(View.GONE);
                                productErrorLayout.setVisibility(View.VISIBLE);
                                ProError.setText("no records");
                                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                                Toast.makeText(ProductsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            productErrorLayout.setVisibility(View.VISIBLE);
                            Log.e("filterproductlist", "=" + e);
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
                params.put("min_price", "" + minprice);
                params.put("max_price", "" + maxprice);
                params.put("category_id", "" + catid);
                params.put("discount", "" + discount);
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

    public void Min_price_dilaog() {
        final Dialog dialog = new Dialog(ProductsActivity.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(500, 600);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText editText = dialog.findViewById(R.id.dialog_edittext);
        ListView listview = dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle = dialog.findViewById(R.id.dialog_tittle);
        editText.setVisibility(View.GONE);
        dialog_tittle.setTextSize(16);
        dialog_tittle.setText("choose min price");
        final ImageView imageView = dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        min_price_Adapter = new ArrayAdapter<>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item, minprice_list);
        min_price_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listview.setAdapter(min_price_Adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                min_price_spinner.setText(min_price_Adapter.getItem(position));
                dialog.dismiss();
            }
        });

    }

    public void Max_price_dilaog() {
        final Dialog dialog = new Dialog(ProductsActivity.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(500, 600);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText editText = dialog.findViewById(R.id.dialog_edittext);
        ListView listview = dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle = dialog.findViewById(R.id.dialog_tittle);
        editText.setVisibility(View.GONE);
        dialog_tittle.setTextSize(16);
        dialog_tittle.setText("choose max price");
        final ImageView imageView = dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        max_price_Adapter = new ArrayAdapter<>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item, maxprice_list);
        max_price_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listview.setAdapter(max_price_Adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                max_price_spinner.setText(max_price_Adapter.getItem(position));
                dialog.dismiss();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return true;
    }

}
