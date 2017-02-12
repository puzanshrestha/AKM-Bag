package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StockListActivity extends Activity {
    RecyclerView recView;
    StockViewAdapter stockViewAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bag);

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);



        String response = null;

        try {
            response = new FunctionsThread(this).execute("ViewBag").get();
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");
            System.out.println(response);


            for (int i = 0; i < bagJsonArray.length(); i++) {
                JSONObject jObject = bagJsonArray.getJSONObject(i);
                BagEntity bagEntity = new BagEntity();
                bagEntity.setId(Integer.valueOf(jObject.getString("bag_id")));
                bagEntity.setName(jObject.getString("bag_name"));
                bagEntity.setCategory(jObject.getString("bag_category"));
                bagEntity.setPrice(Integer.valueOf(jObject.getString("bag_price")));
                bagEntity.setCompany(jObject.getString("bag_company"));
                bagEntity.setQuantity(Integer.valueOf(jObject.getString("bag_quantity")));
                bagEntity.setPhoto(jObject.getString("bag_photo"));

                bagData.add(bagEntity);

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        stockViewAdapter = new StockViewAdapter(bagData, this);

        recView.setAdapter(stockViewAdapter);

        stockViewAdapter.notifyDataSetChanged();


    }

}

