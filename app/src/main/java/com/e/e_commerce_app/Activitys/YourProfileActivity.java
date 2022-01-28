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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.ProfileAdressAdapter;
import com.e.e_commerce_app.Model.ProfileAdressModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourProfileActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    TextView userNmae,userEmail,userMobile,tv_go_pro;
    RecyclerView adressRecycle;
    ProfileAdressAdapter profileAdressAdapter;
    ArrayList<ProfileAdressModel> proAdrslist;
    CircleImageView userImg;
    TextView tv;
    int temp_badge=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager=new SessionManager(YourProfileActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_your_profile);
        userImg=findViewById(R.id.userImg);
        userNmae=findViewById(R.id.tvUserproName);
        userEmail=findViewById(R.id.tvUserproEmail);
        userMobile=findViewById(R.id.tvUserproMobile);
        tv_go_pro=findViewById(R.id.btn_editProfile);

        tv_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(YourProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("name",""+userNmae.getText());
                intent.putExtra("email",""+userEmail.getText());
                intent.putExtra("mobile",""+userMobile.getText());

                startActivity(intent);
            }
        });
        getProfile();
    }
    public void getProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(YourProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("profileInfo_URL", "" + APIs.GetProfile );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GetProfile ,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        proAdrslist = new ArrayList<>();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" getProfile", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                if (jsonObject.getString("image").equals("null") || jsonObject.getString("image").equals("")) {
                                    userImg.setImageResource(R.drawable.no_image);

                                } else {
                                    Picasso.with(YourProfileActivity.this).load("http://foodnjoy.tk/"+jsonObject.getString("image")).error(R.drawable.no_image).into(userImg);
                                }
                                if(jsonObject.getString("name").equals("null") || jsonObject.getString("name").equals(""))
                                {
                                    userNmae.setText("N/A");
                                }
                                else
                                {
                                    userNmae.setText(jsonObject.getString("name"));

                                }
                                if(jsonObject.getString("email").equals("null") || jsonObject.getString("email").equals(""))
                                {
                                    userEmail.setText("N/A");
                                }
                                else
                                {
                                    userEmail.setText(jsonObject.getString("email"));

                                }
                                if(jsonObject.getString("mobile").equals("null") || jsonObject.getString("mobile").equals(""))
                                {
                                    userMobile.setText("N/A");
                                }
                                else
                                {
                                    userMobile.setText(jsonObject.getString("mobile"));
                                }

                                JSONArray jsonArray = jsonObject.getJSONArray("user_address");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    ProfileAdressModel adressModel1 = new ProfileAdressModel();

                                        adressModel1.setAdrsId(jsonObject1.getString("address_id"));

                                    if(jsonObject1.getString("address").equals("null") || jsonObject1.getString("address").equals(""))
                                    {
                                        adressModel1.setProAdress("N/A");
                                    }
                                    else
                                    {
                                        adressModel1.setProAdress(jsonObject1.getString("address"));
                                    }
                                    if(jsonObject1.getString("city").equals("null") || jsonObject1.getString("city").equals(""))
                                    {
                                        adressModel1.setProCity("N/A");
                                    }
                                    else
                                    {
                                        adressModel1.setProCity(jsonObject1.getString("city"));
                                    }
                                    if(jsonObject1.getString("state").equals("null") || jsonObject1.getString("state").equals(""))
                                    {
                                        adressModel1.setProState("N/A");
                                    }
                                    else
                                    {
                                        adressModel1.setProState(jsonObject1.getString("state"));
                                    }
                                    if(jsonObject1.getString("country").equals("null") || jsonObject1.getString("country").equals(""))
                                    {
                                        adressModel1.setProCountry("N/A");
                                    }
                                    else
                                    {
                                        adressModel1.setProCountry(jsonObject1.getString("country"));
                                    }

                                    if(jsonObject1.getString("pincode").equals("null") || jsonObject1.getString("pincode").equals(""))
                                    {
                                        adressModel1.setProPincode("N/A");
                                    }
                                    else
                                    {
                                        adressModel1.setProPincode(jsonObject1.getString("pincode"));
                                    }

                                    proAdrslist.add(adressModel1);

                                }
                                adressRecycle=findViewById(R.id.profileAdrssRecycle);

                                profileAdressAdapter=new ProfileAdressAdapter(proAdrslist, YourProfileActivity.this, new ProfileAdressAdapter.AdressProfileListener() {
                                    @Override
                                    public void adresslistner(String product) {

                                    }
                                });
                                final LinearLayoutManager mLayoutManager;
                                mLayoutManager = new LinearLayoutManager(YourProfileActivity.this, LinearLayoutManager.VERTICAL, false);
                                adressRecycle.setHasFixedSize(false);
                                adressRecycle.setLayoutManager(mLayoutManager);
                                adressRecycle.setItemAnimator(new DefaultItemAnimator());
                                adressRecycle.setAdapter(profileAdressAdapter);
                            } else {

                                Toast.makeText(YourProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my profileInfo", "=1");
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
                params.put("user_id",""+sessionManager.getString("userid"));
                Log.e("param proInfo", params.toString());
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
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

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
                Intent intent2=new Intent(YourProfileActivity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
