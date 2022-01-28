package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.Offer_Adapter;
import com.e.e_commerce_app.Adapters.RewardAdapter;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.Offer_Model;
import com.e.e_commerce_app.Model.RewardModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Offer_Activity extends AppCompatActivity {
    SessionManager sessionManager;
    RecyclerView offer_recycleview;
    Offer_Adapter offer_adapter;
    ArrayList<Offer_Model> offer_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_);
        sessionManager=new SessionManager(Offer_Activity.this);
        offer_recycleview=findViewById(R.id.offer_recycle);
        getOffer();

    }
    public void getOffer() {
        final ProgressDialog progressDialog = new ProgressDialog(Offer_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("mywl_url", "" + APIs.Get_Offers );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Get_Offers ,
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
                                    offer_model.setOffer_expire_date(jsonObject1.getString("offer_expire_date"));
                                    offer_model.setOffer_date(  jsonObject1.getString("offer_date"));
                                    offer_model.setOffer_detail(jsonObject1.getString("offer_detail"));
                                    offer_model.setOffer_termscondition(jsonObject1.getString("offer_terms"));
                                    offer_model.setOffer_code(jsonObject1.getString("offer_code"));
                                    offer_model.setOffer_amnt("20");
                                    offer_list.add(offer_model);
                                }
                                offer_adapter=new Offer_Adapter(Offer_Activity.this,offer_list);
                                final LinearLayoutManager linearLayoutManager;
                                linearLayoutManager = new LinearLayoutManager(Offer_Activity.this, LinearLayoutManager.HORIZONTAL, false);
                                offer_recycleview.setHasFixedSize(false);
                                offer_recycleview.setLayoutManager(linearLayoutManager);
                                offer_recycleview.setItemAnimator(new DefaultItemAnimator());
                                SnapHelper snapHelper = new LinearSnapHelper();
                                snapHelper.attachToRecyclerView(offer_recycleview);
                                offer_recycleview.setAdapter(offer_adapter);

                            } else {

                                Toast.makeText(Offer_Activity.this, "" + message, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  MenuInflater inflater = getMenuInflater();
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