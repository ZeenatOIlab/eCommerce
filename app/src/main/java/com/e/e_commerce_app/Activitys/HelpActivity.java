package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HelpActivity extends AppCompatActivity {
    EditText et_Issue;
    Button btn_sendIssue;
    TextView tv_hcNmbr,tv_hcNmae;
    String name,mobile;
    Dialog cnfrmDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        et_Issue=findViewById(R.id.et_issue);
        btn_sendIssue=findViewById(R.id.btn_issuesend);
        tv_hcNmbr=findViewById(R.id.tv_hc_nmbr);
        tv_hcNmae=findViewById(R.id.tv_hc_user_email);

        if(getIntent().getExtras()!=null)
        {
             name = (getIntent().getExtras().getString("hc_name"));
            mobile = (getIntent().getExtras().getString("hc_contact"));
        }
        btn_sendIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_Issue.getText().toString().length()<=0)
                {
                    Toast.makeText(HelpActivity.this, "write issue", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendIssue();
            }
        });
        tv_hcNmae.setText("Email : "+name);
        tv_hcNmbr.setText("Contact : "+mobile);
    }
    public void sendIssue() {
        final ProgressDialog progressDialog = new ProgressDialog(HelpActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("LOGIN_URL", "" + APIs.SEND_ISSUE);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.SEND_ISSUE,
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
                                sendIssuePopUP();
                            }
                            else {
                                Toast.makeText(HelpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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
                params.put("name",""+tv_hcNmae.getText());
                params.put("contact_no",""+tv_hcNmbr.getText());
                params.put("description",""+et_Issue.getText().toString());

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
    public void sendIssuePopUP() {
        cnfrmDialog = new Dialog(HelpActivity.this);
        cnfrmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cnfrmDialog.setCancelable(false);
        cnfrmDialog.setContentView(R.layout.cnfrm_order_popup);
        TextView cancelmsg = cnfrmDialog.findViewById(R.id.popup_message);
        Button cbtn = cnfrmDialog.findViewById(R.id.c_order);


        Window window = cnfrmDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelmsg.setText("Your issue send  Sucsessfully");

        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                cnfrmDialog.dismiss();
            }
        });

        cnfrmDialog.show();

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
