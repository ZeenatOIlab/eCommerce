package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Adapters.RewardAdapter;
import com.e.e_commerce_app.Adapters.WishListAdapter;
import com.e.e_commerce_app.Model.RewardModel;
import com.e.e_commerce_app.Model.WishListModel;
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

public class RewardActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    RecyclerView rewardRecycle;
    RewardAdapter rewardAdapter;
    ArrayList<RewardModel> rewardlist;
    LinearLayout reward_main_layout,reward_error;
    TextView reward_error_msg;
    TextView tv;
    int temp_badge=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        sessionManager = new SessionManager(RewardActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        rewardRecycle = findViewById(R.id.rewardRecycleList);
        reward_main_layout = findViewById(R.id.reward_mainLayout);
        reward_error = findViewById(R.id.rewarderror);
        reward_error_msg = findViewById(R.id.rewarderrormsg);
        getReward();
    }

    public void getReward() {
        reward_main_layout.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(RewardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("mywl_url", "" + APIs.Get_Rewards );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Get_Rewards ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" my_wrewardlist", "=" + response);
                        rewardlist = new ArrayList<>();
                        progressDialog.dismiss();
                        reward_main_layout.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                            RewardModel rewardModel = new RewardModel();
                                            rewardModel.setScrachImg(""+R.drawable.scrachimg);
                                            rewardModel.setScrachText("Scratch");
                                            rewardModel.setReward_Id(jsonObject1.getString("id"));
                                            rewardModel.setRewardImg(jsonObject1.getString("image"));
                                            rewardModel.setRewardText(jsonObject1.getString("text"));
                                            rewardModel.setRewardStatus(jsonObject1.getString("offer_status"));

                                            rewardlist.add(rewardModel);
                                        }
                                        rewardAdapter = new RewardAdapter(RewardActivity.this, rewardlist);
                                        RecyclerView.LayoutManager manager = new GridLayoutManager(RewardActivity.this, 2);
                                        rewardRecycle.setHasFixedSize(false);
                                        rewardRecycle.setLayoutManager(manager);
                                        rewardRecycle.setItemAnimator(new DefaultItemAnimator());
                                        rewardAdapter.setHasStableIds(false);
                                        rewardRecycle.setAdapter(rewardAdapter);
                            } else {
                                reward_error_msg.setText(""+message);
                                reward_error.setVisibility(View.VISIBLE);
                                reward_main_layout.setVisibility(View.GONE);
                                Toast.makeText(RewardActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my wishdata", "=1");
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
                Intent intent2 = new Intent(RewardActivity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;


        }
        return true;
    }


}
