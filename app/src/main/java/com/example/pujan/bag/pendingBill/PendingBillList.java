package com.example.pujan.bag.pendingBill;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by puzan on 26-Mar-17.
 */
public class PendingBillList extends AppCompatActivity implements VolleyFunctions.AsyncResponse {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    RecyclerView recView;
    FloatingActionButton addOrderFab;
    PendingBillAdapter pendingBillAdapter;
    ArrayList<PendingBillListEntity> pBillList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_list_activity);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Pending Order" + "</font>"));

        recView = (RecyclerView) findViewById(R.id.recView);
        addOrderFab = (FloatingActionButton) findViewById(R.id.addOrderFab);


        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);


        DbHelper dbh = new DbHelper(this);

        VolleyFunctions thread = new VolleyFunctions(this);
        thread.queryPendingBillList(dbh.getShop());
        thread.trigAsyncResponse(PendingBillList.this);

        addOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), OrderActivity.class);
                i.putExtra("source", "Order");
                i.putExtra("pendingData", "Null");
                startActivity(i);


            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 | dy < 0)
                    addOrderFab.hide();


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                addOrderFab.show();
            }
        });


    }

    @Override
    public void onComplete(String response) {

        progressBar.setVisibility(View.GONE);
        if (response.equals("ERROR")) {
            Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
        } else {

            pBillList = new ArrayList<>();
            try {

                JSONObject bagJson = new JSONObject(response);
                JSONArray bagJsonArray = bagJson.getJSONArray("result");


                for (int i = 0; i < bagJsonArray.length(); i++) {
                    JSONObject jObject = bagJsonArray.getJSONObject(i);
                    PendingBillListEntity pBillEntity = new PendingBillListEntity();
                    pBillEntity.setDate(Date.valueOf(jObject.getString("date")));
                    pBillEntity.setCustomerName(jObject.getString("customer_name"));
                    pBillEntity.setpId(Integer.valueOf(jObject.getString("pId")));
                    pBillEntity.setCustomerId(Integer.parseInt(jObject.getString("customer_id")));
                    pBillEntity.setAddress(jObject.getString("address"));
                    pBillEntity.setTotal(Integer.parseInt(jObject.getString("total")));
                    pBillList.add(pBillEntity);

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error connecting with the Server..!", Toast.LENGTH_SHORT).show();
            }
            pendingBillAdapter = new PendingBillAdapter(pBillList, this, recView);
            recView.setAdapter(pendingBillAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


}
