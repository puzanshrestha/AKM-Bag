package com.example.pujan.bag.bagDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pujan.bag.R;

public class BagDetailsActivity extends AppCompatActivity {

    LinearLayout addBagBtn,viewBagBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_details);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setTitle(" Bag Details");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);

        addBagBtn = (LinearLayout)findViewById(R.id.addBagBtn);
        viewBagBtn = (LinearLayout)findViewById(R.id.viewBagBtn);

        addBagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AddBagActivity.class);
                i.putExtra("source","");
                i.putExtra("bagid","0");
                startActivity(i);
            }
        });

        viewBagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),BagListActivity.class);
                i.putExtra("source","bag");

                startActivity(i);
            }
        });
    }
}
