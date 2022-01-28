package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.e.e_commerce_app.Adapters.CategoryAdapter;
import com.e.e_commerce_app.Model.CategoryModel;
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

import static android.view.Gravity.END;

public class CategoriesActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    RecyclerView catRecycleView;
    CategoryAdapter categoryAdapter;
    ImageView catimg;
    TextView tv;
    int temp_badge=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        sessionManager=new SessionManager(CategoriesActivity.this);
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        catRecycleView = findViewById(R.id.catrecycleList);
        catimg = findViewById(R.id.cat_img);
        categoryAdapter=new CategoryAdapter(CategoriesActivity.this, DashboardActivity.catlist, new CategoryAdapter.SelectCategoryListener() {
            @Override
            public void categorySelected(String category) {

            }
        });
        RecyclerView.LayoutManager manager = new GridLayoutManager(CategoriesActivity.this, 3);
        catRecycleView.setHasFixedSize(false);
        catRecycleView.setLayoutManager(manager);
        catRecycleView.setItemAnimator(new DefaultItemAnimator());
        catRecycleView.setAdapter(categoryAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty() && sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty())
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dashboard_menu2, menu);
            return true;
        }
        else {
            MenuInflater inflater = getMenuInflater();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            inflater.inflate(R.menu.cart_menu, menu);
            final MenuItem item = menu.findItem(R.id.cart_icon);
            MenuItemCompat.setActionView(item, R.layout.cart_count_layout);
            RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

             tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
             setupBadge();
            tv.setText("" + sharedPreferences.getString("carttotal", null));
            notifCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);

                }
            });
            return super.onCreateOptionsMenu(menu);
        }

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
                Intent intent2=new Intent(CategoriesActivity.this, MyCartActivity.class);
                startActivity(intent2);
                return true;

        }
        return true;
    }
}
