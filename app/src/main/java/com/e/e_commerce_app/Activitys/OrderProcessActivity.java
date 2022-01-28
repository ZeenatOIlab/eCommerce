package com.e.e_commerce_app.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.AdressAdapter;
import com.e.e_commerce_app.Adapters.CheckOutAdapter;
import com.e.e_commerce_app.Adapters.MultipleExistAdressAdapter;
import com.e.e_commerce_app.Adapters.MyOrderAdapter;
import com.e.e_commerce_app.Adapters.Offer_Adapter;
import com.e.e_commerce_app.Adapters.Offer_Popup_Adpter;
import com.e.e_commerce_app.Adapters.RewardAdapter;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.CheckOutModel;
import com.e.e_commerce_app.Model.ExistAdressModel;
import com.e.e_commerce_app.Model.Offer_Model;
import com.e.e_commerce_app.Model.RewardModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class OrderProcessActivity extends AppCompatActivity implements PaymentResultListener {
    SessionManager sessionManager;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String TAG = OrderProcessActivity.class.getSimpleName();
    LinearLayout expandableLinearLayout2, expandableLinearLayout3, expandableLinearLayout4;
    ImageView arrowimg2, arrowimg3, arrowimg4;
    Button newAddContineu, PaymentContineu, cnfrmorderContineu;
    LinearLayout newAddLayout;
    RadioButton newaddRadio, radioCod, radio_Onlinepayment;
    ListView checkoutRecycle;
    RecyclerView existAdressListview;
    CheckOutAdapter checkOutAdapter;
    ArrayList<CheckOutModel> checkOutlist;
    TextView tv_co_dscTotal, tv_co_tTotal, tv_co_shpngChrgTotal;
    EditText et_Name, et_Email, et_Mobilenmbr, et_NewAdress, et_PinCode;
    TextView et_Country, et_State, et_City;
    CardView exitAddLayout;
    MultipleExistAdressAdapter multipleExistAdressAdapter;
    ArrayList<ExistAdressModel> existAdressArrayList;
    ArrayList<String> cntryList;
    ArrayList<String> stateList;
    ArrayList<String> cityList;
    ArrayAdapter<String> country_adapter, state_adapter, city_adapter;
    ListView country_listview, state_listview, city_listview;
    EditText country_editText, state_editText, city_editText;
    Dialog country_custom_dialog, state_custom_dialog, city_custom_dialog;
    TextView exitName, exitNumber;
    String Name, Adress, MobileNmbr, PinCode, City, State, Country, ShipChrg, OrderStatus, OrderSubTotal, OrderGrandTotal, OrderTotalDoscount,
            OrderPaymentMethod, Email;
    int exitnmbr = 0;
    Dialog Cnfrm_Order;
    TextView oldaddText;
    int id1 = 0, id2 = 0;
    String cntryid, stateid;
    JSONArray cart_id_array = new JSONArray();
    String shipchrg = "";
    String discount = "";
    String price = "";
    String qyn = "";
    int total = 0;
    int sc = 0, d = 0;
    int p1 = 0, q1 = 0;
    TextView tv_expand_code, tv_see_all_offer;
    ExpandableRelativeLayout expandableCodeLayout;
    EditText et_promo_code;
    String code = "";
    Button btn_apply_code;
    String ofr_res = "", ofr_code = "", ofr_amnt = "";
    int TEMP = 0;
    String order_id = "";
    String send_Temp_status = "", chk = "";
    ArrayList<Offer_Model> offer_list;
    RecyclerView ofr_recy;
    Offer_Popup_Adpter offer_adapter;
    private Offer_Popup_Adpter.onClickInterface onclickInterface;
    int status_temp = 0;
    TextView save_new_adrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process2);
        sessionManager = new SessionManager(OrderProcessActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkoutRecycle = findViewById(R.id.checkoutRecycle);
        tv_co_dscTotal = findViewById(R.id.tv_co_dscTotal);
        tv_co_tTotal = findViewById(R.id.tv_co_Total);
        tv_co_shpngChrgTotal = findViewById(R.id.tv_co_shpngTotal);
        exitAddLayout = findViewById(R.id.exitAddLayout);


        et_Name = findViewById(R.id.et_name);
        et_Email = findViewById(R.id.et_email);
        et_Mobilenmbr = findViewById(R.id.et_mo_nmbr);
        et_NewAdress = findViewById(R.id.et_adres);
        et_PinCode = findViewById(R.id.et_pincode);
        et_City = findViewById(R.id.et_city);
        et_State = findViewById(R.id.et_state);
        et_Country = findViewById(R.id.et_cntry);


        expandableLinearLayout2 = findViewById(R.id.expandable_two);
        expandableLinearLayout3 = findViewById(R.id.expandlayout_three);
        expandableLinearLayout4 = findViewById(R.id.expandablefour);


        existAdressListview = findViewById(R.id.existsadresslistview);
        newaddRadio = findViewById(R.id.radionewadress);
        radioCod = findViewById(R.id.radio_cod);
        radio_Onlinepayment = findViewById(R.id.radio_op);

        arrowimg2 = findViewById(R.id.arroimg2);
        arrowimg3 = findViewById(R.id.arrowimg3);
        arrowimg4 = findViewById(R.id.arrowimg4);

        oldaddText = (TextView) findViewById(R.id.oldaddtext);

        exitName = (TextView) findViewById(R.id.et_ExitsName);
        exitNumber = (TextView) findViewById(R.id.et_ExitsMobile);
        exitNumber = (TextView) findViewById(R.id.et_ExitsMobile);
        newAddLayout = findViewById(R.id.newaddlayout);
        newAddContineu = findViewById(R.id.newadrscontinue2);
        PaymentContineu = findViewById(R.id.paymentcontinue);
        cnfrmorderContineu = findViewById(R.id.cnfrmorderContineu);
        tv_expand_code = findViewById(R.id.tv_code_expand);
        expandableCodeLayout = findViewById(R.id.code_expanded_layout);
        tv_see_all_offer = findViewById(R.id.tv_see_all_offer);
        et_promo_code = findViewById(R.id.et_promo_code);
        btn_apply_code = findViewById(R.id.btn_apply_code);
        save_new_adrs = findViewById(R.id.save_new_adrs);
        if (sessionManager.getString("username").equals("null") || sessionManager.getString("username").equals("")) {
            exitName.setText("N/A");
        } else {
            exitName.setText(sessionManager.getString("username"));
        }
        if (sessionManager.getString("usermobile").equals("null") || sessionManager.getString("usermobile").equals("")) {
            exitNumber.setText("N/A");
        } else {
            exitNumber.setText(sessionManager.getString("usermobile"));
        }
        if (getIntent().getExtras() != null) {
            ofr_res = (getIntent().getExtras().getString("offer_res"));
            ofr_code = (getIntent().getExtras().getString("offer_Code"));
            ofr_amnt = (getIntent().getExtras().getString("offer_amnt"));
        }
        Checkout.preload(getApplicationContext());


        Log.e("ofr_res", "=" + ofr_res);
        Log.e("ofr_code", "=" + ofr_code);
        Log.e("ofr_amnt", "=" + ofr_amnt);
        if (ofr_res.equals("yes")) {
            et_promo_code.setText("" + ofr_code);

        }
        checkOutlist = new ArrayList<>();
        cntryList = new ArrayList<>();
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();

        existAdressArrayList = new ArrayList<>();
        for (int i = 0; i < ApplicationClass.cartModelslist.size(); i++) {
            CheckOutModel checkOutModel = new CheckOutModel();
            checkOutModel.setCoProImg(ApplicationClass.cartModelslist.get(i).getCartItemImg());
            checkOutModel.setCoProName(ApplicationClass.cartModelslist.get(i).getCartItemName());
            checkOutModel.setCoProPrice("" + ApplicationClass.cartModelslist.get(i).getCartItemPrice());
            checkOutModel.setCoProQyn("" + ApplicationClass.cartModelslist.get(i).getCartQuantity());
            checkOutModel.setCoPro_CartId("" + ApplicationClass.cartModelslist.get(i).getCartId());
            checkOutModel.setCoProDiscounP("" + ApplicationClass.cartModelslist.get(i).getCartItemDiscnt());
            checkOutModel.setCoProShipChrg("" + ApplicationClass.cartModelslist.get(i).getCartItemShipngChrg());
            checkOutModel.setCoProDiscounType("" + ApplicationClass.cartModelslist.get(i).getCartItemDiscntType());
            checkOutlist.add(checkOutModel);
        }
        TEMP = checkOutlist.size();

        for (int i = 0; i < checkOutlist.size(); i++) {
            shipchrg = checkOutlist.get(i).getCoProShipChrg().toString();
            if (shipchrg != null) {
                try {
                    int s1 = Integer.parseInt(shipchrg);
                    sc = sc + s1;
                } catch (NumberFormatException e) {
                    sc = 0;
                }
            }
            discount = checkOutlist.get(i).getCoProDiscounP();
            if (discount != null) {
                try {
                    double d1 = Double.parseDouble(discount);
                    int da = (int) d1;
                    d = d + da;
                } catch (NumberFormatException e) {
                    d = 0;
                }
            }
            price = checkOutlist.get(i).getCoProPrice();
            if (price != null) {
                try {
                    p1 = Integer.parseInt(price);
                } catch (NumberFormatException e) {
                    p1 = 0;
                }
            }
            qyn = checkOutlist.get(i).getCoProQyn();
            if (qyn != null) {
                try {
                    q1 = Integer.parseInt(qyn);
                } catch
                (NumberFormatException e) {
                    q1 = 0;
                }
            }
            int m1 = (p1 * q1);
            total = total + m1;

        }
        tv_co_dscTotal.setText("" + d);
        tv_co_shpngChrgTotal.setText("₹" + sc);
        int t = sc + total;
        final int temp = t - d;
        tv_co_tTotal.setText("₹" + String.valueOf(temp));

        btn_apply_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_promo_code.getText().toString().isEmpty()) {
                    Toast.makeText(OrderProcessActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else {
                    verify_Coupon();

                }
            }
        });

        checkOutAdapter = new CheckOutAdapter(OrderProcessActivity.this, checkOutlist);
        checkoutRecycle.setAdapter(checkOutAdapter);


        checkoutData();

        arrowimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLinearLayout2.setVisibility(View.VISIBLE);
                expandableLinearLayout3.setVisibility(View.GONE);
                expandableLinearLayout4.setVisibility(View.GONE);
            }
        });

        newaddRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (newaddRadio.isChecked()) {
                    id1 = 0;
                    newAddLayout.setVisibility(View.VISIBLE);
                    exitnmbr = 1;
                    newaddRadio.setChecked(true);
                    for (int i = 0; i < existAdressArrayList.size(); i++) {
                        RadioButton exitRadio = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsAdrs);
                        exitRadio.setChecked(false);
                    }
                }
            }
        });
        et_Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountrySearchableDialog();
            }
        });
        et_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateSearchableDialog();
            }
        });
        et_City.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySearchableDialog();
            }
        });
        save_new_adrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_NewAdress.getText().toString().equals("")) {
                    Toast.makeText(OrderProcessActivity.this, "enter new adress", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_Country.getText().toString().equals("")) {
                    Toast.makeText(OrderProcessActivity.this, "choose country", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_State.getText().toString().equals("")) {
                    Toast.makeText(OrderProcessActivity.this, "choose state", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_City.getText().toString().equals("")) {
                    Toast.makeText(OrderProcessActivity.this, "choose city", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_PinCode.getText().toString().equals("")) {
                    Toast.makeText(OrderProcessActivity.this, "enter pincode", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveAdress();
            }
        });


        newAddContineu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newaddRadio.isChecked()) {
                    if (et_Name.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_Email.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter gmail", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!validEmail("" + et_Email.getText().toString())) {
                        Toast.makeText(OrderProcessActivity.this, "enter valid email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_Mobilenmbr.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter mobile number", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (et_Mobilenmbr.getText().toString().length() < 10) {
                        Toast.makeText(OrderProcessActivity.this, "enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_PinCode.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter pincode", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_NewAdress.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter new adress", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_Country.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter country", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_State.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter state", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_City.getText().toString().equals("")) {
                        Toast.makeText(OrderProcessActivity.this, "enter city", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                id1 = 1;
                expandableLinearLayout3.setVisibility(View.VISIBLE);
                expandableLinearLayout2.setVisibility(View.GONE);
                expandableLinearLayout4.setVisibility(View.GONE);
            }


        });
        arrowimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id1 == 1) {
                    expandableLinearLayout3.setVisibility(View.VISIBLE);
                    expandableLinearLayout2.setVisibility(View.GONE);
                    expandableLinearLayout4.setVisibility(View.GONE);
                }
            }
        });
        radioCod.setChecked(true);
        radio_Onlinepayment.setChecked(false);
        radioCod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioCod.isChecked()) {
                    radioCod.setChecked(true);
                    radio_Onlinepayment.setChecked(false);
                }
            }
        });
        radio_Onlinepayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_Onlinepayment.isChecked()) {
                    radio_Onlinepayment.setChecked(true);
                    radioCod.setChecked(false);
                }
            }
        });
        expandableCodeLayout.setExpanded(false);
        expandableCodeLayout.collapse();
        if (!expandableCodeLayout.isExpanded())

            tv_expand_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_see_all_offer.getVisibility() == View.GONE) {
                        expandableCodeLayout.toggle();
                        tv_see_all_offer.setVisibility(View.VISIBLE);

                    } else if (tv_see_all_offer.getVisibility() == View.VISIBLE) {
                        expandableCodeLayout.toggle();
                        tv_see_all_offer.setVisibility(View.GONE);
                    }
                }
            });

        tv_see_all_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offer_pop_Up();
            }
        });
        PaymentContineu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id2 = 2;
                expandableLinearLayout4.setVisibility(View.VISIBLE);
                expandableLinearLayout3.setVisibility(View.GONE);
                expandableLinearLayout2.setVisibility(View.GONE);
            }
        });

        arrowimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id2 == 1) {
                    expandableLinearLayout4.setVisibility(View.VISIBLE);
                    expandableLinearLayout2.setVisibility(View.GONE);
                    expandableLinearLayout3.setVisibility(View.GONE);
                }

            }
        });

        cnfrmorderContineu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderProcessActivity.this);
                builder.setTitle("Confirm Order");
                builder.setMessage("Are you sure confirm the order?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkOutlist.size(); i++) {
                            String id = checkOutlist.get(i).getCoPro_CartId();
                            cart_id_array.put(id);
                        }
                        placeOrder();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void checkoutData() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.e("mycart_url", "" + APIs.CHECKOUT);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" checkout", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArray2 = jsonObject.getJSONArray("user_address");
                                if (jsonArray2.length() == 0) {
                                    oldaddText.setVisibility(View.GONE);
                                    newaddRadio.setChecked(true);
                                    newAddLayout.setVisibility(View.VISIBLE);
                                }

                                for (int i = 0; i < jsonArray2.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(i);

                                    ExistAdressModel existAdressModel = new ExistAdressModel();

                                    if (jsonObject2.getString("address").equals("null") || jsonObject2.getString("address").equalsIgnoreCase("")) {
                                        existAdressModel.setExistAdress("N/A");
                                    } else {
                                        existAdressModel.setExistAdress(jsonObject2.getString("address"));

                                    }
                                    if (jsonObject2.getString("city").equals("null") || jsonObject2.getString("city").equalsIgnoreCase("")) {
                                        existAdressModel.setExistCity("N/A");
                                    } else {
                                        existAdressModel.setExistCity(jsonObject2.getString("city"));
                                    }
                                    if (jsonObject2.getString("state").equals("null") || jsonObject2.getString("state").equalsIgnoreCase("")) {
                                        existAdressModel.setExistState("N/A");
                                    } else {
                                        existAdressModel.setExistState(jsonObject2.getString("state"));
                                    }
                                    if (jsonObject2.getString("country").equals("null") || jsonObject2.getString("country").equalsIgnoreCase("")) {
                                        existAdressModel.setExistCountry("N/A");
                                    } else {
                                        existAdressModel.setExistCountry(jsonObject2.getString("country"));
                                    }
                                    if (jsonObject2.getString("pincode").equals("null") || jsonObject2.getString("pincode").equalsIgnoreCase("")) {
                                        existAdressModel.setExistPincode("N/A");
                                    } else {
                                        existAdressModel.setExistPincode(jsonObject2.getString("pincode"));
                                    }
                                    existAdressArrayList.add(existAdressModel);
                                }
                                final LinearLayoutManager linearLayoutManager;
                                multipleExistAdressAdapter = new MultipleExistAdressAdapter(OrderProcessActivity.this, existAdressArrayList);
                                linearLayoutManager = new LinearLayoutManager(OrderProcessActivity.this, LinearLayoutManager.VERTICAL, false);
                                existAdressListview.setHasFixedSize(false);
                                existAdressListview.setLayoutManager(linearLayoutManager);
                                existAdressListview.setItemAnimator(new DefaultItemAnimator());
                                existAdressListview.setAdapter(multipleExistAdressAdapter);
                            } else {

                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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

    public void placeOrder() {
        for (int i = 0; i < existAdressArrayList.size(); i++) {
            RadioButton exitRadio = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsAdrs);
            TextView exitadrs = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsAdrs);
            TextView exitcity = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsCity);
            TextView exitcntry = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsCntry);
            TextView exitstate = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsState);
            TextView exitpincode = existAdressListview.getChildAt(i).findViewById(R.id.et_ExitsPincode);

            if (exitRadio.isChecked()) {
                Name = "" + exitName.getText();
                Email = sessionManager.getString("useremail");
                MobileNmbr = "" + exitNumber.getText();
                Adress = (String) exitadrs.getText();
                City = (String) exitcity.getText();
                State = (String) exitstate.getText();
                PinCode = (String) exitpincode.getText();
                Country = (String) exitcntry.getText();
            }
        }
        if (newaddRadio.isChecked()) {
            Name = String.valueOf(et_Name.getText());
            Email = String.valueOf(et_Email.getText());
            MobileNmbr = String.valueOf(et_Mobilenmbr.getText());
            Adress = String.valueOf(et_NewAdress.getText());
            City = String.valueOf(et_City.getText());
            State = String.valueOf(et_State.getText());
            PinCode = String.valueOf(et_PinCode.getText());
            Country = String.valueOf(et_Country.getText());
        }

        if (radioCod.isChecked()) {
            OrderPaymentMethod = "Cash_On_Delivery";
            OrderStatus = "pending";
        } else if (radio_Onlinepayment.isChecked()) {
            OrderPaymentMethod = "Online_Payment";
            OrderStatus = "payment pending";
        }
        ShipChrg = String.valueOf(tv_co_shpngChrgTotal.getText());
        OrderSubTotal = String.valueOf(tv_co_tTotal.getText());
        OrderGrandTotal = String.valueOf(tv_co_tTotal.getText());
        OrderTotalDoscount = String.valueOf(tv_co_dscTotal.getText());

        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("PlaceOrder_URL", "" + APIs.PLACE_ORDER_URL);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.PLACE_ORDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" PlaceOrder", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                order_id = jsonObject.getString("orderId");
                                int inc = Integer.parseInt(sharedPreferences.getString("carttotal", ""));
                                int res = inc - TEMP;
                                editor.putString("carttotal", "" + res);
                                editor.commit();
                                editor.apply();
                                if (radio_Onlinepayment.isChecked()) {
                                    startPayment();
                                } else if (radioCod.isChecked()) {
                                    ConfirmOrderPopUP();
                                }
                            } else {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my PlaceOrder_URL", "=1");
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
                params.put("users_email", "" + Email);
                params.put("name", "" + Name);
                params.put("address", "" + Adress);
                params.put("city", "" + City);
                params.put("state", "" + State);
                params.put("pincode", "" + PinCode);
                params.put("country", "" + Country);
                params.put("mobile", "" + MobileNmbr);
                params.put("shipping_charges", "" + ShipChrg.substring(1));
                // params.put("shipping_charges", "" +ShipChrg);
                params.put("order_status", "" + OrderStatus);
                params.put("order_sub_total", "" + OrderSubTotal.substring(1));
                params.put("grand_total", "" + OrderGrandTotal.substring(1));
                params.put("total_discount", "" + OrderTotalDoscount);
                params.put("payment_method", "" + OrderPaymentMethod);
                params.put("cart_ids", "" + cart_id_array);
                params.put("coupen_code", "" + code);

                Log.e("param PlaceOrder", "" + params);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void ConfirmOrderPopUP() {
        Cnfrm_Order = new Dialog(OrderProcessActivity.this);
        Cnfrm_Order.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Cnfrm_Order.setCancelable(false);

        Cnfrm_Order.setContentView(R.layout.cnfrm_order_popup);
        Button cbtn = Cnfrm_Order.findViewById(R.id.c_order);

        Window window = Cnfrm_Order.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderProcessActivity.this, "Order Confrimed ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderProcessActivity.this, MyOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Cnfrm_Order.dismiss();
                finish();
                ApplicationClass.cartModelslist.clear();
            }
        });
        Cnfrm_Order.show();
    }

    public void getCountry() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("cntryurl", "" + APIs.GET_COUNTRY);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" cntrys", "=" + response);
                        progressDialog.dismiss();
                        String resourse_name;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("country");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    resourse_name = jsonObject1.getString("country_name");
                                    cntryList.add(resourse_name);
                                }
                                country_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, cntryList);
                                country_listview.setAdapter(country_adapter);
                            } else {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my cntrys", "=1");
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

    public void getState() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("cntryurl", "" + APIs.GET_STATE_LIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_STATE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" state", "=" + response);
                        stateList = new ArrayList<>();
                        progressDialog.dismiss();
                        String states;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("state");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    states = jsonObject1.getString("city_state");
                                    stateList.add(states);
                                }
                                state_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, stateList);
                                state_listview.setAdapter(state_adapter);
                            } else {


                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my state", "=1");
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
                params.put("country_id", "" + cntryid);
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

    public void getCity() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("cityurl", "" + APIs.GET_CITY_LIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_CITY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" state", "=" + response);
                        progressDialog.dismiss();
                        String citys;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("city");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    citys = jsonObject1.getString("city_name");
                                    cityList.add(citys);
                                }
                                city_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, cityList);
                                city_listview.setAdapter(city_adapter);

                            } else {

                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my city", "=1");
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
                params.put("state_id", "" + stateid);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ApplicationClass.cartModelslist.clear();
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        ApplicationClass.cartModelslist.clear();
        super.onBackPressed();
    }

    public void CountrySearchableDialog() {
        country_custom_dialog = new Dialog(OrderProcessActivity.this);
        country_custom_dialog.setContentView(R.layout.dialog_searchable_spinner);
        country_custom_dialog.getWindow().setLayout(1000, 1900);
        country_custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        country_custom_dialog.show();

        country_editText = country_custom_dialog.findViewById(R.id.dialog_edittext);
        country_listview = country_custom_dialog.findViewById(R.id.dialog_listview);
        ImageView imageView = country_custom_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country_custom_dialog.dismiss();
            }
        });
        TextView dialog_tittle = country_custom_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose country");
        getCountry();

        country_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, cntryList);
        country_listview.setAdapter(country_adapter);

        country_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                country_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        country_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                et_Country.setText(country_adapter.getItem(position));
                cntryid = country_adapter.getItem(position);
                country_custom_dialog.dismiss();
            }
        });

    }

    public void StateSearchableDialog() {
        state_custom_dialog = new Dialog(OrderProcessActivity.this);
        state_custom_dialog.setContentView(R.layout.dialog_searchable_spinner);
        state_custom_dialog.getWindow().setLayout(1000, 1900);
        state_custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        state_custom_dialog.show();

        state_editText = state_custom_dialog.findViewById(R.id.dialog_edittext);
        state_listview = state_custom_dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle = state_custom_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose state");

        ImageView imageView = state_custom_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state_custom_dialog.dismiss();
            }
        });
        getState();
        state_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, stateList);
        state_listview.setAdapter(state_adapter);

        state_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                state_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        state_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                et_State.setText(state_adapter.getItem(position));
                stateid = state_adapter.getItem(position);
                state_custom_dialog.dismiss();
            }
        });

    }

    public void CitySearchableDialog() {
        city_custom_dialog = new Dialog(OrderProcessActivity.this);
        city_custom_dialog.setContentView(R.layout.dialog_searchable_spinner);
        city_custom_dialog.getWindow().setLayout(1000, 1900);
        city_custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        city_custom_dialog.show();

        city_editText = city_custom_dialog.findViewById(R.id.dialog_edittext);
        city_listview = city_custom_dialog.findViewById(R.id.dialog_listview);
        ImageView imageView = city_custom_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_custom_dialog.dismiss();
            }
        });
        TextView dialog_tittle = city_custom_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose city");
        city_adapter = new ArrayAdapter<>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, cityList);
        city_listview.setAdapter(city_adapter);
        getCity();
        city_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                city_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                et_City.setText(city_adapter.getItem(position));
                city_custom_dialog.dismiss();
            }
        });
    }

    public void Offer_pop_Up() {
        final BottomSheetDialog ofr_popup = new BottomSheetDialog(OrderProcessActivity.this);
        ofr_popup.setContentView(R.layout.offer_display_popup);
        ofr_popup.getWindow().setLayout(1000, ViewGroup.LayoutParams.MATCH_PARENT);
        ofr_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ofr_popup.show();
        ofr_recy = ofr_popup.findViewById(R.id.ofr_d_recycle);
        ImageView ofr_close = ofr_popup.findViewById(R.id.ofr_d_close);
        ofr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofr_popup.dismiss();
            }
        });
        onclickInterface = new Offer_Popup_Adpter.onClickInterface() {
            @Override
            public void setClick(int pos) {
                et_promo_code.setText("" + offer_list.get(pos).getOffer_code());
                ofr_popup.dismiss();
            }
        };
        getOffer();

    }

    public void getOffer() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("mywl_url", "" + APIs.Get_Offers);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Get_Offers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" my_offer_list", "=" + response);
                        offer_list = new ArrayList<>();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    Offer_Model offer_model = new Offer_Model();
                                    offer_model.setOffer_image(jsonObject1.getString("offer_image"));
                                    offer_model.setOffer_desc(jsonObject1.getString("offer_tittle"));
                                    offer_model.setOffer_tittle(jsonObject1.getString("offer_description"));
                                    offer_model.setOffer_code(jsonObject1.getString("offer_code"));
                                    offer_list.add(offer_model);
                                }
                                offer_adapter = new Offer_Popup_Adpter(OrderProcessActivity.this, offer_list, onclickInterface);
                                final LinearLayoutManager linearLayoutManager;
                                linearLayoutManager = new LinearLayoutManager(OrderProcessActivity.this, LinearLayoutManager.VERTICAL, false);
                                ofr_recy.setLayoutManager(linearLayoutManager);
                                ofr_recy.setAdapter(offer_adapter);

                            } else {

                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my offer", "=1");
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

    public void verify_Coupon() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("verify_coupon", "" + APIs.Verify_Coupon);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Verify_Coupon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" verify_coupon", "=" + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                code = et_promo_code.getText().toString();

                            } else {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                code = "";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my verify_coupon", "=1");
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
                params.put("coupon_code", "" + et_promo_code.getText().toString());
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

    public void startPayment() {
        final Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.a2zmart);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Food n Joy");
            options.put("description", "Order Id.:" + order_id);
            //   options.put("order_id",order_id);//from response of step 3.
            options.put("image", "" + R.drawable.a2zmart);
            options.put("theme.color", "#0496AA");
            options.put("currency", "INR");
            options.put("amount", "100");//pass amount in currency subunits
            JSONObject preFill = new JSONObject();
            preFill.put("email", "" + sessionManager.getString("useremail"));
            preFill.put("contact", "" + sessionManager.getString("usermobile"));
            options.put("prefill", preFill);
            checkout.open(OrderProcessActivity.this, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
            Toast.makeText(OrderProcessActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        send_Temp_status = "pending";
        chk = "yes";
        Chng_Order_Status();
    }

    @Override
    public void onPaymentError(int code, String response) {
        send_Temp_status = "payment pending";
        chk = "no";
        Chng_Order_Status();
    }

    public void Chng_Order_Status() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("chng_order_status", "" + APIs.Order_Status_Change);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Order_Status_Change,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" login", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                if (chk.equals("yes")) {
                                    status_temp = 1;
                                    Payment_Status();
                                } else if (chk.equals("no")) {
                                    try {
                                        status_temp = 2;
                                        Payment_Status();
                                    } catch (Exception e) {
                                        Log.e("OnPaymentError", "Exception in onPaymentError", e);
                                    }
                                }
                            } else {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my login", "=1");
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
                params.put("order_id", "" + order_id);
                params.put("order_status", "" + send_Temp_status);
                Log.e("param login", params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void Payment_Status() {
        final BottomSheetDialog ofr_popup = new BottomSheetDialog(OrderProcessActivity.this);
        ofr_popup.setContentView(R.layout.payment_status_popup);
        ofr_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ofr_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ofr_popup.show();
        final TextView payment_status = ofr_popup.findViewById(R.id.paymemt_status);
        Button payment_status_btn = ofr_popup.findViewById(R.id.payment_status_btn);
        TextView payment_status_close = ofr_popup.findViewById(R.id.payment_status_close);

        if (status_temp == 1) {
            payment_status.setText("Payment Complted");
            payment_status_btn.setText("Done");
        } else if (status_temp == 2) {
            payment_status.setText("Transaction Failed");
            payment_status_btn.setText("Retry");
        }
        payment_status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_temp == 1) {
                    Intent intent = new Intent(OrderProcessActivity.this, MyOrdersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    ApplicationClass.cartModelslist.clear();
                } else if (status_temp == 2) {
                    ofr_popup.dismiss();
                    startPayment();
                }
            }
        });
        payment_status_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofr_popup.dismiss();
            }
        });

    }

    public void saveAdress() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderProcessActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Log.e("save_Adrs", "" + APIs.Add_Adaress);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Add_Adaress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" saveAdrs", "=" + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {

                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(OrderProcessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my add_adrs", "=1");
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
                params.put("address", "" + et_NewAdress.getText().toString());
                params.put("city", "" + et_City.getText().toString());
                params.put("state", "" + et_State.getText().toString());
                params.put("country", "" + et_Country.getText().toString());
                params.put("pincode", "" + et_PinCode.getText().toString());
                Log.e("params add_adrs", "=" + params.toString());
                return params;
            }

        };
        RequestHandler.getInstance(OrderProcessActivity.this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
