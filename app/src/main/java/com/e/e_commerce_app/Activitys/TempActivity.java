package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.e.e_commerce_app.R;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }
    public void OrderPlaced(View view) {
        //startActivity(new Intent(MainActivity.this,TrackActivity.class));
        String orderStatus="0";
        Intent intent=new Intent(TempActivity.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderConfirmed(View view) {
        String orderStatus="1";
        Intent intent=new Intent(TempActivity.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderProcessed(View view) {
        String orderStatus="2";
        Intent intent=new Intent(TempActivity.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderPickup(View view) {
        String orderStatus="3";
        Intent intent=new Intent(TempActivity.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
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