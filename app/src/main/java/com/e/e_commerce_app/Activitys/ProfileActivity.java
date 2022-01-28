package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.AdressAdapter;
import com.e.e_commerce_app.Adapters.MultipleExistAdressAdapter;
import com.e.e_commerce_app.Model.AdressModel;
import com.e.e_commerce_app.Model.ProfileAdressModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    CircleImageView proUserImg;
    TextView proUserName,proUseremail;
    String mobilenmbr;
    RelativeLayout homeLayout,categoryLayout,myodrLayout,logLayout,rewardLayout,offerLayout,chngpswrdLayout,WishlistLayout,helpLayout;
    Dialog cnfrmpswrddialog;
    EditText PrePswd,NewPswrd,CnfrmPswrd;
    TextView tv;
    LinearLayout profile_main_layout,profile_error;
    int temp_badge=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager=new SessionManager(ProfileActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_profile);
        proUserImg=findViewById(R.id.proimg);
        proUserName=findViewById(R.id.prousername);
        proUseremail=findViewById(R.id.prouseremail);
        homeLayout=findViewById(R.id.homelayout);
        categoryLayout=findViewById(R.id.categorylayout);
        myodrLayout=findViewById(R.id.myodrlayout);
        logLayout=findViewById(R.id.loglayout);
        rewardLayout=findViewById(R.id.rewardlay);
        offerLayout=findViewById(R.id.offerlayout);
        chngpswrdLayout=findViewById(R.id.chng_pswrd_layout);
        WishlistLayout=findViewById(R.id.wishlist_layout);
        helpLayout=findViewById(R.id.helplayout);
        profile_main_layout=findViewById(R.id.main_profile_layout);
        profile_error=findViewById(R.id.profile_error);
        getProfile();


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,YourProfileActivity.class);
                intent.putExtra("username",""+proUserName.getText().toString());
                intent.putExtra("useremail",""+proUseremail.getText().toString());
                intent.putExtra("usermobile",""+mobilenmbr.toString());
                startActivity(intent);
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,CategoriesActivity.class);
                startActivity(intent);
            }
        });
        myodrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,MyOrdersActivity.class);
                startActivity(intent);
            }
        });
        rewardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,RewardActivity.class);
                startActivity(intent);
            }
        });
        offerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,Offer_Activity.class);
                startActivity(intent);
            }
        });
        chngpswrdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmPswrd();

            }
        });
        WishlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,WishList_Activity.class);
                startActivity(intent);
            }
        });
        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,HelpActivity.class);
                intent.putExtra("hc_name",""+proUseremail.getText());
                intent.putExtra("hc_contact",""+mobilenmbr.toString());
                startActivity(intent);
            }
        });

        logLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(ProfileActivity.this);
                dialog.setTitle("Logout");
                dialog.setMessage("Do you want to logout?");
                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.setPositiveButton("logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                dialog.show();
            }
        });
    }
    public void logout() {
        profile_main_layout.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("logout", "" + APIs.LOGOUT);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" logout", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                sessionManager.clear();
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(ProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            profile_main_layout.setVisibility(View.GONE);
                            profile_error.setVisibility(View.VISIBLE);
                            Log.e("resp my logout", "=1");
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
                params.put("user_id", ""+sessionManager.getString("userid") );

                Log.e("param logout", params.toString());
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    public void ConfirmPswrd(){
        cnfrmpswrddialog=new Dialog(ProfileActivity.this);
        cnfrmpswrddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cnfrmpswrddialog.setCancelable(true);

        cnfrmpswrddialog.setContentView(R.layout.chng_pswrd_popup);
        PrePswd =cnfrmpswrddialog.findViewById(R.id.prepswrd);
        NewPswrd=cnfrmpswrddialog.findViewById(R.id.newpswd);
        CnfrmPswrd=cnfrmpswrddialog.findViewById(R.id.newpswdcnfrm);
        TextView Cancelbtn=cnfrmpswrddialog.findViewById(R.id.cnclbtn);
        TextView Okbtn=cnfrmpswrddialog.findViewById(R.id.okbtn);

        Window window = cnfrmpswrddialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnfrmpswrddialog.dismiss();
            }
        });

        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrePswd.getText().toString().equals(""))
                {
                    Toast.makeText(ProfileActivity.this, "Enter old Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(NewPswrd.getText().toString().equals(""))
                {
                    Toast.makeText(ProfileActivity.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(CnfrmPswrd.getText().toString().equals(""))
                {
                    Toast.makeText(ProfileActivity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!CnfrmPswrd.getText().toString().equals(NewPswrd.getText().toString()))
                {
                    Toast.makeText(ProfileActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                updatePassword();
            }
        });
        cnfrmpswrddialog.show();

    }
        public void getProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("profileInfo_URL", "" + APIs.GetProfile );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GetProfile ,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      ArrayList<ProfileAdressModel>  adrslist = new ArrayList<>();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" getProfile", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                if (jsonObject.getString("image").equals("null") || jsonObject.getString("image").equals("")) {
                                    proUserImg.setImageResource(R.drawable.no_image);

                                } else {
                                    Picasso.with(ProfileActivity.this).load("http://foodnjoy.tk/"+jsonObject.getString("image")).error(R.drawable.no_image).into(proUserImg);
                                }


                                if (jsonObject.getString("name").equals("null") || jsonObject.getString("name").equals("")) {
                                    proUserName.setText("N/A");
                                } else {
                                    proUserName.setText(jsonObject.getString("name"));

                                }
                                if (jsonObject.getString("email").equals("null") || jsonObject.getString("email").equals("")) {
                                    proUseremail.setText("N/A");
                                } else {
                                    proUseremail.setText(jsonObject.getString("email"));

                                }
                                if (jsonObject.getString("mobile").equals("null") || jsonObject.getString("mobile").equals("")) {
                                    mobilenmbr = "N/A";
                                } else {
                                    mobilenmbr = (jsonObject.getString("mobile"));
                                }

                            } else {

                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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
    public void updatePassword(){

        String userid=sessionManager.getString("userid");
        String prepswrd= String.valueOf(PrePswd.getText());
        String newpswrd= String.valueOf(NewPswrd.getText());
        String cnfrmpswd= String.valueOf(CnfrmPswrd.getText());

        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("Update_pswrd", "" + APIs.UPDATE_PASSWORD+"user_id="+userid+"&password="+prepswrd+"&newPassword="+newpswrd+"&newPassword_confirmation="+cnfrmpswd);
        final StringRequest request=new StringRequest(Request.Method.POST,
                APIs.UPDATE_PASSWORD+"user_id="+userid+"&password="+prepswrd+"&newPassword="+newpswrd+"&newPassword_confirmation="+cnfrmpswd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" updatepssword", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if(status.equalsIgnoreCase("1")){

                                Toast.makeText(ProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                                cnfrmpswrddialog.dismiss();

                            }
                            else{
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my uppswrd", "=1");
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                    }
                })  {
            protected Map<String,String> getParams(){
                HashMap<String, String> params=new HashMap<>();


                Log.e("param updatepswrd",params.toString());
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
                Intent intent2=new Intent(ProfileActivity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;
        }
        return true;
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
