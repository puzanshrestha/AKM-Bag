package com.example.pujan.bag.bagStock;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StockListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, FunctionsThread.AsyncResponse {

    RecyclerView recView;
    StockViewAdapter bagViewAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();
    ArrayList<BagColorQuantity> bagColorQuantities=new ArrayList<>();





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchh:
                return true;
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), ActionListActivity.class);
        startActivity(i);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);



        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Inventory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);
        //recView.setLayoutManager(new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, duration));







        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewBag");
        t.trigAsyncResponse(this);


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<BagEntity> newbag = new ArrayList<>();
        for (BagEntity bagEntity : bagData) {
            String name = bagEntity.getName().toLowerCase();
            if (name.contains(newText.toLowerCase())) {
                newbag.add(bagEntity);
            }
        }
        bagViewAdapter.setFilter(newbag);
        return false;
    }

    public void getData(String response) {
        try {

            if (response.equals("Error") ){
                Toast.makeText(this, "Connection Error... Please check your connection !", Toast.LENGTH_SHORT).show();
            }
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");
            JSONArray stockJsonArray=bagJson.getJSONArray("stockData");


            for (int i = 0; i < bagJsonArray.length(); i++) {
                JSONObject jObject = bagJsonArray.getJSONObject(i);
                BagEntity bagEntity = new BagEntity();
                bagEntity.setId(Integer.valueOf(jObject.getString("bag_id")));
                bagEntity.setName(jObject.getString("bag_name"));
                bagEntity.setCategory(jObject.getString("bag_category"));
                bagEntity.setPrice(Integer.valueOf(jObject.getString("bag_price")));
                bagEntity.setCompany(jObject.getString("bag_company"));
                bagEntity.setVendorId(Integer.valueOf(jObject.getString("vendor_id")));
                bagEntity.setPhoto(jObject.getString("bag_photo"));

                bagData.add(bagEntity);

            }

            convertStockData(stockJsonArray);





        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error With Database Connection..!!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onComplete(String output) {
        getData(output);
        bagViewAdapter = new StockViewAdapter(bagData,bagColorQuantities, "bag", getBaseContext());
        recView.setAdapter(bagViewAdapter);




    }


    private void convertStockData(JSONArray bag) {


        try {


            LinkedHashMap<String, Integer> cqe = new LinkedHashMap<>();

            BagColorQuantity bcqEntity = new BagColorQuantity();
            JSONObject jObject2 = null;
            for (int i = 0; i < bag.length(); i++) {
                JSONObject jObject = bag.getJSONObject(i);
                bcqEntity.setBag_id(Integer.valueOf(jObject.getString("bag_id")));

                if (i + 1 < bag.length()) {
                    jObject2 = bag.getJSONObject(i + 1);

                }
                else
                    jObject2=jObject;
                if ((jObject2.getString("bag_id")).equals(jObject.getString("bag_id"))) {


                    cqe.put(jObject.getString("color"), Integer.valueOf(jObject.getString("quantityColor")));

                } else {

                    cqe.put(jObject.getString("color"), Integer.valueOf(jObject.getString("quantityColor")));
                    bcqEntity.setQuantityColor(cqe);
                    bagColorQuantities.add(bcqEntity);
                    bcqEntity = new BagColorQuantity();
                    cqe = new LinkedHashMap<>();

                }




            }


            bcqEntity.setQuantityColor(cqe);
            if(cqe.size()>0)
                bagColorQuantities.add(bcqEntity);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

}
