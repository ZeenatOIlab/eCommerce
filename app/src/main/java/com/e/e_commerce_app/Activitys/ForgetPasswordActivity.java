package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {
    Button sendForgetPAsswordBtn;
    EditText et_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        et_Email=findViewById(R.id.et_fp_email);
        sendForgetPAsswordBtn=findViewById(R.id.btn_send_forgetPswrd_req);
        sendForgetPAsswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_Email.getText().toString().equals(""))
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validEmail(""+et_Email.getText().toString())){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                forgetPassword();
            }
        });
    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public void forgetPassword() {

        final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("LOGIN_URL", "" + APIs.Forget_Password );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Forget_Password ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" forget_pswrd", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                Toast.makeText(ForgetPasswordActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(ForgetPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my forget_pswrd", "=1");
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
                params.put("email",""+et_Email.getText().toString());
                Log.e("param forgetpswrd", params.toString());
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

        onBackPressed();
        return true;

    }
}
