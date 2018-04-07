package com.example.pujan.bag.bagDetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class BagListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, VolleyFunctions.AsyncResponse {

    RecyclerView recView;
    BagViewAdapter bagViewAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();
    ArrayList<BagColorQuantity> bagColorQuantities = new ArrayList<>();
    FloatingActionButton addNewBag;

    ProgressBar progressBar;

    int offset = 0;
    int difference = 5;
    boolean loading;
    int position = 0;
    boolean loadComplete = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setHint("Search Bag");
        searchEditText.setBackground(new ColorDrawable(Color.WHITE));
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
        setContentView(R.layout.activity_view_bag);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Bag");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);
        //recView.setLayoutManager(new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, duration));

        addNewBag = (FloatingActionButton) findViewById(R.id.addNewBagBtn);

        addNewBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddBagActivity.class);
                startActivity(i);
            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0)
                    addNewBag.show();

                if (dy > 0)
                    addNewBag.hide();

/*
                if(!loadComplete) {
                    if (!loading) {
                        if (isLastItemDisplaying(recView)) {
                            offset += difference;

                            FunctionsThread t = new FunctionsThread(BagListActivity.this);
                            t.execute("ViewBag", String.valueOf(offset), String.valueOf(difference));
                            t.trigAsyncResponse(BagListActivity.this);

                            loading = true;

                            position = ((LinearLayoutManager) recView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                            bagData.add(null);
                            bagViewAdapter.notifyItemInserted(bagData.size() - 1);

                        }
                    }
                }
                */
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //addNewBag.show();
            }

        });


        VolleyFunctions volleyFunctions = new VolleyFunctions(this);
        volleyFunctions.viewBag(String.valueOf(offset), String.valueOf(difference));
        volleyFunctions.trigAsyncResponse(this);
        loading = true;


    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<BagEntity> listStartsWith = new ArrayList<>();
        ArrayList<BagEntity> listContains = new ArrayList<>();

        for (BagEntity bagEntity : bagData) {
            String name = bagEntity.getName().toLowerCase();
            if (name.startsWith(newText.toLowerCase())) {
                listStartsWith.add(bagEntity);
            }
        }
        Collections.sort(listStartsWith, new Comparator<BagEntity>() {
            public int compare(BagEntity obj1, BagEntity obj2) {
                return (obj1.getName()).compareTo(obj2.getName());
            }
        });
        for (BagEntity bagEntity : bagData) {
            String name = bagEntity.getName().toLowerCase();
            if ((name.contains(newText.toLowerCase()))&(!listStartsWith.contains(bagEntity))) {
                listContains.add(bagEntity);
            }
        }
        listStartsWith.addAll(listContains);
        bagViewAdapter.setFilter(listStartsWith);
        return false;
    }

    public void getData(String response) {
        try {

            if (response == "Error") {
                Toast.makeText(this, "Connection Error... Please check your connection !", Toast.LENGTH_SHORT).show();
            }
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");
            JSONArray stockJsonArray = bagJson.getJSONArray("stockData");


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
            if (bagJsonArray.length() == 0)
                loadComplete = true;


            convertStockData(stockJsonArray);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error With Database Connection..!!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onComplete(String output) {

        progressBar.setVisibility(View.GONE);
        if (output.equals("ERROR")) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
        } else {
            if (offset > 0) {
                if (bagData.get(bagData.size() - 1) == null) {
                    bagData.remove(bagData.size() - 1);
                }
                bagViewAdapter.notifyItemRemoved(bagData.size() - 1);
            }

            getData(output);
            bagViewAdapter = new BagViewAdapter(bagData, bagColorQuantities, "bag", getBaseContext());
            recView.setAdapter(bagViewAdapter);
            loading = false;
            recView.getLayoutManager().scrollToPosition(position);


        }


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

                } else
                    jObject2 = jObject;
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
            if (cqe.size() > 0)
                bagColorQuantities.add(bcqEntity);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
