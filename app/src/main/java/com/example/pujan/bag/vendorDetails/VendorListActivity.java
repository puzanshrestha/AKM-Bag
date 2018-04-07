package com.example.pujan.bag.vendorDetails;

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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.BagEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VendorListActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse, SearchView.OnQueryTextListener {
    RecyclerView recyclerView;
    VendorViewAdapter viewAdapter;
    ArrayList<VendorEntity> vendorData;

    String getway;

    FloatingActionButton fab;
    ProgressBar progressBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendor);


        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Vendor" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        VolleyFunctions t = new VolleyFunctions(this);
        t.viewVendor();
        t.trigAsyncResponse(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setBackground(new ColorDrawable(Color.WHITE));
        searchEditText.setHint("Search Vendor");
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(this, ActionListActivity.class);
        startActivity(i);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<VendorEntity> listStartsWith = new ArrayList<>();
        ArrayList<VendorEntity> listContains = new ArrayList<>();

        for (VendorEntity bagEntity : vendorData) {
            String name = bagEntity.getName().toLowerCase();
            if (name.startsWith(newText.toLowerCase())) {
                listStartsWith.add(bagEntity);
            }
        }
        Collections.sort(listStartsWith, new Comparator<VendorEntity>() {
            public int compare(VendorEntity obj1, VendorEntity obj2) {
                return (obj1.getName()).compareTo(obj2.getName());
            }
        });
        for (VendorEntity bagEntity : vendorData) {
            String name = bagEntity.getName().toLowerCase();
            if ((name.contains(newText.toLowerCase()))&(!listStartsWith.contains(bagEntity))) {
                listContains.add(bagEntity);
            }
        }
        listStartsWith.addAll(listContains);
        viewAdapter.setFilter(listStartsWith);
        return false;
    }

    @Override
    public void onComplete(String output) {

        progressBar.setVisibility(View.GONE);
        if (output.equals("ERROR")) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
        } else {
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
                Toast.makeText(this, "Error connecting with the Server..!", Toast.LENGTH_SHORT).show();
            }
            recyclerView = (RecyclerView) findViewById(R.id.recView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            viewAdapter = new VendorViewAdapter(vendorData, this, getway);
            recyclerView.setAdapter(viewAdapter);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(), AddVendorActivity.class);
                    startActivity(i);
                }
            });

        }
    }
}
