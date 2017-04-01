package com.example.pujan.bag.pendingBill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by puzan on 26-Mar-17.
 */
public class PendingBillList extends AppCompatActivity implements FunctionsThread.AsyncResponse{



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    RecyclerView recView;
    PendingBillAdapter pendingBillAdapter;
    ArrayList<PendingBillListEntity> pBillList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bag);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Pending Bill List");

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);


        FunctionsThread thread = new FunctionsThread(this);
        thread.execute("QueryPendingBillList");
        thread.trigAsyncResponse(this);


    }

    @Override
    public void onComplete(String response) {


        pBillList= new ArrayList<>();
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


                pBillList.add(pBillEntity);

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error connecting with the Server..!",Toast.LENGTH_SHORT).show();
        }
        pendingBillAdapter = new PendingBillAdapter(pBillList, this);
        recView.setAdapter(pendingBillAdapter);
        pendingBillAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
