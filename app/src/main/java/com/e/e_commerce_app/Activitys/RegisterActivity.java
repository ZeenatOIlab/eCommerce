package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.e.e_commerce_app.Utility.RequestHandler;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Button btn_register;
    TextView tv_backogin;
    EditText et_RegisterName,et_RegisterMobileNmbr,et_RegisterEmail,et_RegisterPasssword,et_Register_cnfrmPswrd;
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register=findViewById(R.id.rgisterbtn);
        et_RegisterName=findViewById(R.id.et_register_name);
        et_RegisterMobileNmbr=findViewById(R.id.et_register_monilenmbr);
        et_RegisterEmail=findViewById(R.id.et_register_email);
        et_RegisterPasssword=findViewById(R.id.et_register_pswrd);
        et_Register_cnfrmPswrd=findViewById(R.id.et_register_cnfrm_pswrd);

        tv_backogin=findViewById(R.id.backlogin);
        device_token= OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_RegisterName.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_RegisterMobileNmbr.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_RegisterMobileNmbr.getText().toString().length()<10)
                {
                    Toast.makeText(RegisterActivity.this, "Enter 10 Digit Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_RegisterEmail.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Email Adress", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (!validEmail("" + et_RegisterEmail.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_RegisterPasssword.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_Register_cnfrmPswrd.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Confirmm Password ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!et_Register_cnfrmPswrd.getText().toString().equals(et_RegisterPasssword.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                Register();
            }
        });
        tv_backogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public void Register(){
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("REGISTER_URL", "" + APIs.REGISTER_URL);
        final StringRequest request=new StringRequest(Request.Method.POST,
                APIs.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" login", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if(status.equalsIgnoreCase("1")){
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    Log.e("token","="+device_token);
                                    Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                    finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                            Log.e("resp my rgister", "=1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();

                    }
                })  {
            protected Map<String,String> getParams(){
                HashMap<String, String> params=new HashMap<>();
                params.put("name",et_RegisterName.getText().toString());
                params.put("email",""+et_RegisterEmail.getText().toString());
                params.put("password",""+et_RegisterPasssword.getText().toString());
                params.put("mobile",""+et_RegisterMobileNmbr.getText().toString());
                params.put("password_confirmation",""+et_Register_cnfrmPswrd.getText().toString());
                params.put("fcm_token",""+device_token);
                Log.e("param register",params.toString());
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
