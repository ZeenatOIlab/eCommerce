package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.NetworkChecking;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Button loginbtn;
    TextView tv_skip, tv_gor,forgetPassword;
    EditText et_login_email, et_login_pswd;
    SessionManager sessionManager;

    int skip_nmbr = 0;
    Boolean newnum = false;
    int item_to_login,itemid;
    String regexStr = "^[0-9]*$";
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);
        loginbtn = findViewById(R.id.loginbtn);
        et_login_email = findViewById(R.id.et_login_email);

        et_login_pswd = findViewById(R.id.et_login_pswrd);
        tv_skip = findViewById(R.id.skip);
        tv_gor = findViewById(R.id.goregister);
        forgetPassword = findViewById(R.id.fogetpasword);

        device_token=OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        if (getIntent().getExtras() != null) {
            skip_nmbr = (getIntent().getExtras().getInt("skip_id"));
            newnum = true;

        }
        if (getIntent().getExtras() != null) {
            itemid = (getIntent().getExtras().getInt("Item_to_login"));

        }
        if (getIntent().getExtras() != null) {
            item_to_login = (getIntent().getExtras().getInt("ItemtologNmbr"));

        }

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkChecking.isConnectedNetwork(LoginActivity.this)) {
                    //internet is connected do something
                    Toast.makeText(LoginActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (et_login_email.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Email Adress Or Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_login_email.getText().toString().endsWith("@gmail.com")) {
                     if (!validEmail("" + et_login_email.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(et_login_email.getText().toString().trim().matches(regexStr))
                {
                    if (et_login_email.getText().toString().length()<10) {
                        Toast.makeText(LoginActivity.this, "Enter 10 digit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Enter valid Email or Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_login_pswd.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                login();

            }
        });
        if (newnum) {
            tv_skip.setVisibility(View.GONE);
        }
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.clear();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        tv_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("LOGIN_URL", "" + APIs.LOGIN_URL );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.LOGIN_URL,
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

                                sessionManager.addString("mobile", "" + et_login_email);
                                sessionManager.addString("password", "" + et_login_pswd);
                                sessionManager.addString("email", "" + et_login_email.getText().toString());
                                sessionManager.addString("passworde", "" + et_login_pswd.getText().toString());

                                String user_id = jsonObject.getString("user_id");
                                String user_email = jsonObject.getString("email");
                                String user_name = jsonObject.getString("name");
                                String user_mobile = jsonObject.getString("mobile");
                                String cart_total = jsonObject.getString("total_cart_items");

                                sessionManager.addString("userid", "" + user_id);
                                sessionManager.addString("nmbr",""+"1");
                                sessionManager.addString("username", "" + user_name);
                                sessionManager.addString("useremail", "" + user_email);
                                sessionManager.addString("usermobile", "" + user_mobile);
                                SharedPreferences sp=getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor ed=sp.edit();
                                ed.putString("carttotal",""+cart_total);
                                ed.commit();

                                //sessionManager.addString("carttotal", ""+cart_total);


                               if (newnum) {
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    Log.e("token",""+device_token);

                                    Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                                if(et_login_email.getText().toString().endsWith("@gmail.com")) {
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Log.e("token",""+device_token);
                                    Log.e("cartT",""+cart_total);
                                    Log.e("session cart:", "" + sessionManager.getString("carttotal"));
                                    startActivity(intent);
                                    finish();

                                }
                                else if(et_login_email.getText().toString().trim().matches(regexStr))
                                {
                                    Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                                    intent.putExtra("mobile", et_login_email.getText().toString());
                                    intent.putExtra("pswrd", et_login_pswd.getText().toString());
                                    Log.e("mobileT", "=" + et_login_email.getText());
                                    Log.e("token",""+device_token);
                                    startActivity(intent);
                                    Log.e("session mobile:", "" + sessionManager.getString("mobile"));
                                    Log.e("session pswrd:", "" + sessionManager.getString("password"));
                                    Log.e("cartT",""+cart_total);
                                    Log.e("session cart:", "" + sessionManager.getString("carttotal"));


                                }

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            Log.e("resp my login", "=1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                if(et_login_email.getText().toString().trim().matches(regexStr)) {
                    params.put("mobile", "" + et_login_email.getText().toString());
                }
                if(et_login_email.getText().toString().endsWith("@gmail.com")) {
                    params.put("email", "" + et_login_email.getText().toString());
                }
                params.put("password",""+et_login_pswd.getText().toString());
                params.put("fcm_token",""+device_token);

                Log.e("session email1:", "" + sessionManager.getString("email"));
                Log.e("session pswrde1:", "" + sessionManager.getString("passworde"));


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

}
