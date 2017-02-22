package com.example.pujan.bag.vendorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.AddBagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VendorListActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse,SearchView.OnQueryTextListener, VendorViewAdapter.ItemClickCallback {
    RecyclerView recyclerView;
    VendorViewAdapter viewAdapter;
    ArrayList<VendorEntity> vendorData;

    String getway;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendor);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.vendorsmall);
        actionBar.setTitle(" Vendors List");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getway = getIntent().getStringExtra("getway");


        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewVendor");
        t.trigAsyncResponse(this);




    }

    @Override
    public void onItemClick(int p) {
        if (getway.equals("bagdetails")) {

            VendorEntity vendorEntity;
            vendorEntity = vendorData.get(p);
            int ven_id = vendorEntity.getId();
            Toast.makeText(getBaseContext(), ven_id + " selected", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getBaseContext(), AddBagActivity.class);
            i.putExtra("ven_id", Integer.toString(ven_id));
            i.putExtra("bagid", "1");
            i.putExtra("source", "insert");
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<VendorEntity> newVendor = new ArrayList<>();
        for (VendorEntity vendorEntity : vendorData) {
            String name = vendorEntity.getName().toLowerCase();
            if (name.contains(newText)) {
                newVendor.add(vendorEntity);
            }
        }
        viewAdapter.setFilter(newVendor);
        return false;
    }

    @Override
    public void onComplete(String output) {
        vendorData = new ArrayList<>();

        try {

            JSONObject object = new JSONObject(output);
            JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                VendorEntity vendorEntity = new VendorEntity();
                vendorEntity.setId(jsonObject.getInt("vendor_id"));
                vendorEntity.setAddress(jsonObject.getString("vendor_address"));
                vendorEntity.setName(jsonObject.getString("vendor_name"));
                vendorData.add(vendorEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error connecting with the Server..!",Toast.LENGTH_SHORT).show();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        viewAdapter = new VendorViewAdapter(vendorData, this, getway);
        recyclerView.setAdapter(viewAdapter);
        viewAdapter.notifyDataSetChanged();
        viewAdapter.onItemClickCallback(this);
    }
}
