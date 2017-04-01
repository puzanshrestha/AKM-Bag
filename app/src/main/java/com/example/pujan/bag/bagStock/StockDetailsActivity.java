package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;

public class StockDetailsActivity extends Activity {

    private LinearLayout viewStockBtn,updateStockBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userType=sharedPreferences.getString("userType","");


        viewStockBtn = (LinearLayout) findViewById(R.id.viewStockBtn);
        updateStockBtn=(LinearLayout) findViewById(R.id.updateStockBtn);

        if(userType.equals("userType#0"))
            updateStockBtn.setVisibility(View.GONE);
        viewStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),StockViewActivity.class);
                startActivity(i);

            }
        });

        updateStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),StockListActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ActionListActivity.class);
        startActivity(i);
    }
}
