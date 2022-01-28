package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.e.e_commerce_app.Adapters.CategoryAdapter;
import com.e.e_commerce_app.Adapters.ImageSliderAdapter;
import com.e.e_commerce_app.Adapters.ItemListAdapter;
import com.e.e_commerce_app.Model.CategoryModel;
import com.e.e_commerce_app.Model.ItemListModel;
import com.e.e_commerce_app.Model.Pagermodel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.NetworkChecking;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout mainDashLayout;

    private static final String SHOWCASE_ID = "Simple Showcase";
    private SessionManager prefManager;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    ImageSliderAdapter imageSliderAdapter;
    ArrayList<Pagermodel> sliderimgitem;
    RecyclerView recyclerView_imglist;
    public static ArrayList<ItemListModel> itemlist;
    SliderView sliderView;
    ItemListAdapter itemListAdapter;
    RecyclerView catRecycleView;
    public static ArrayList<CategoryModel> catlist;
    CategoryAdapter categoryAdapter;
    TextView tv_catAll, tv_pro_all, errormsg;
    LinearLayout dashError;
    TextView badge_count;
    int temp_badege=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashError = findViewById(R.id.dashError);
        errormsg = findViewById(R.id.dashErrormsg);
        mainDashLayout = findViewById(R.id.mainLay);
        if (!NetworkChecking.isConnectedNetwork(DashboardActivity.this)) {
            //internet is connected do something
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            errormsg.setText("Internet Not Available ");
            dashError.setVisibility(View.VISIBLE);
            mainDashLayout.setVisibility(View.GONE);

        }

        sessionManager = new SessionManager(DashboardActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        tv_catAll = findViewById(R.id.cat_viewall);
        tv_pro_all = findViewById(R.id.proviewall);
        prefManager = new SessionManager(this);


        sliderView = findViewById(R.id.pagerimageSlider);
        catlist = new ArrayList<CategoryModel>();

        recyclerView_imglist = findViewById(R.id.main_recycleview);
        catRecycleView = findViewById(R.id.catrecycleList);

        tv_catAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });
        tv_pro_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProductsActivity.class);
                intent.putExtra("parsecatId", "0");
                startActivity(intent);
            }
        });


        getslider();
        getProductList();
        getCategories();
        if (prefManager.isFirstTimeLaunch()) {
            showTutorSequence(0);
        }

    }

    public void getslider() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("Slider_URL", "" + APIs.GET_SLIDER);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_SLIDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sliderimgitem = new ArrayList<>();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" slider", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    Pagermodel imgmodel4 = new Pagermodel();
                                    imgmodel4.setSlidimg(jsonObject1.getString("slider_image"));
                                    if (jsonObject1.getString("title").equals("null") || jsonObject1.getString("title").equalsIgnoreCase("")) {
                                        imgmodel4.setSliderTittle("N/A");
                                    } else {
                                        imgmodel4.setSliderTittle(jsonObject1.getString("title"));

                                    }
                                    if (jsonObject1.getString("description").equals("null") || jsonObject1.getString("description").equalsIgnoreCase("")) {
                                        imgmodel4.setSliderDesc("N/A");
                                    } else {
                                        imgmodel4.setSliderDesc(jsonObject1.getString("description"));

                                    }


                                    sliderimgitem.add(imgmodel4);

                                }
                                imageSliderAdapter = new ImageSliderAdapter(DashboardActivity.this, sliderimgitem);
                                sliderView.setSliderAdapter(imageSliderAdapter);
                                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                sliderView.setIndicatorSelectedColor(Color.WHITE);
                                sliderView.setIndicatorUnselectedColor(Color.BLACK);
                                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                                sliderView.startAutoCycle();


                            } else {
                                Toast.makeText(DashboardActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my slider", "=1");
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

                Log.e("param slider", params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void getProductList() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mainDashLayout.setVisibility(View.GONE);
        Log.e("productlist_url", "" + APIs.PRODUCT_LIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.PRODUCT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" get_products", "=" + response);
                        progressDialog.dismiss();
                        mainDashLayout.setVisibility(View.VISIBLE);

                        itemlist = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data_product");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ItemListModel itemListModel1 = new ItemListModel();
                                    itemListModel1.setListimg(jsonObject1.getString("image"));
                                    itemListModel1.setPro_Name(jsonObject1.getString("product_name"));
                                    itemListModel1.setPro_Price(jsonObject1.getInt("price"));
                                    itemListModel1.setID(jsonObject1.getInt("id"));
                                    itemlist.add(itemListModel1);

                                }
                                itemListAdapter = new ItemListAdapter(DashboardActivity.this, itemlist, new ItemListAdapter.SelectProductListener() {
                                    @Override
                                    public void productlistner(String product) {

                                    }
                                });
                                final LinearLayoutManager mLayoutManager;
                                mLayoutManager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView_imglist.setHasFixedSize(false);
                                recyclerView_imglist.setLayoutManager(mLayoutManager);
                                recyclerView_imglist.setItemAnimator(new DefaultItemAnimator());
                                recyclerView_imglist.setAdapter(itemListAdapter);
                            } else {
                                mainDashLayout.setVisibility(View.GONE);
                                errormsg.setText("" + message);
                                dashError.setVisibility(View.VISIBLE);
                                Toast.makeText(DashboardActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            Log.e("resp my productlist", "=1");
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

    public void getCategories() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mainDashLayout.setVisibility(View.GONE);

        Log.e("category_url", "" + APIs.GET_CATEGORY);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" get_categories", "=" + response);

                        progressDialog.dismiss();
                        mainDashLayout.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {


                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1;
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    jsonObject1 = jsonArray.getJSONObject(i);
                                    CategoryModel categoryModel1 = new CategoryModel();
                                    categoryModel1.setCatName(jsonObject1.getString("name"));
                                    categoryModel1.setCatId(jsonObject1.getString("id"));
                                    categoryModel1.setCatImg(jsonObject1.getString("image"));
                                    catlist.add(categoryModel1);

                                }

                                categoryAdapter = new CategoryAdapter(DashboardActivity.this, catlist, new CategoryAdapter.SelectCategoryListener() {
                                    @Override
                                    public void categorySelected(String category) {

                                    }
                                });

                                final LinearLayoutManager manager;
                                manager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                catRecycleView.setHasFixedSize(false);
                                catRecycleView.setLayoutManager(manager);
                                catRecycleView.setItemAnimator(new DefaultItemAnimator());
                                catRecycleView.setAdapter(categoryAdapter);
                            } else {
                                mainDashLayout.setVisibility(View.GONE);
                                errormsg.setText("" + message);
                                errormsg.setVisibility(View.VISIBLE);
                                Toast.makeText(DashboardActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my categorylist", "=1");
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
        if (
                ((sessionManager.getString("mobile").equalsIgnoreCase("") || sessionManager.getString("mobile").isEmpty())
                        && (sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty()))
                        &&
                        ((sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty())
                                && (sessionManager.getString("passworde").equalsIgnoreCase("") || sessionManager.getString("passworde").isEmpty())
                        )
        ) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dashboard_menu2, menu);
            return true;
        }

//        if  (sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty()
//                &&
//                sessionManager.getString("passworde").equalsIgnoreCase("") || sessionManager.getString("passworde").isEmpty()) {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.dashboard_menu2, menu);
//             Log.e("session mobile:", "" + sessionManager.getString("email"));
//             Log.e("session pswrd:", "" + sessionManager.getString("passworde"));
//            return true;
//        }
        else {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dashboard_menu, menu);
            final MenuItem item = menu.findItem(R.id.cart_icon);
            MenuItemCompat.setActionView(item, R.layout.cart_count_layout);
            RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

            badge_count = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            badge_count.setText("" + sharedPreferences.getString("carttotal", null));

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

    private void showTutorSequence(int millis) {

        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(millis); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID); // create the material showcase sequence


        sequence.setConfig(config); //set the showcase config to the sequence.

        sequence.addSequenceItem(tv_pro_all, "show all products", "GOT IT"); // add view for the first sequence, in this case it is a button.

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tv_catAll)
                        .setDismissText("OK")
                        .setContentText("show all categories")
                        .withCircleShape()
                        .build()
        ); // add view for the second sequence, in this case it is a textview.

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(sliderView)
                        .setDismissText("UNDERSTAND")
                        .setContentText("Sliders")
                        .withCircleShape()
                        .build()
        ); // add view for the third sequence, in this case it is a checkbox.

        sequence.start(); //start the sequence showcase

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.user_icon:
                Intent intent1 = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent1);
                return true;


            case R.id.cart_icon:
                Intent intent2 = new Intent(DashboardActivity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;

            case R.id.noti_icon:
                Intent intent3 = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(intent3);

                return true;

            case R.id.login_icon:
                Intent intent4 = new Intent(DashboardActivity.this, LoginActivity.class);
                intent4.putExtra("skip_id", 12);
                startActivity(intent4);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {
        temp_badege=Integer.parseInt(badge_count.getText().toString());
        if (badge_count != null) {
            if (badge_count.getText().equals("0") || badge_count.getText().equals("null") || temp_badege<0 ) {
                if (badge_count.getVisibility() != View.GONE) {
                    badge_count.setVisibility(View.GONE);
                }
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }




}
