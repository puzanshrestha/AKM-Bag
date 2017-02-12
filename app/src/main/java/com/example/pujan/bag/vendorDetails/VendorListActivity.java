package com.example.pujan.bag.vendorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.AddBagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VendorListActivity extends AppCompatActivity implements VendorViewAdapter.ItemClickCallback {
    RecyclerView recyclerView;
    VendorViewAdapter viewAdapter;
    ArrayList<VendorEntity> vendorData;
    int a = 0;
    String getway;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        vendorData = new ArrayList<>();
        getway = getIntent().getStringExtra("getway");


        try {
            String response = new FunctionsThread(this).execute("ViewVendor").get();
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                VendorEntity vendorEntity = new VendorEntity();
                vendorEntity.setId(jsonObject.getInt("vendor_id"));
                vendorEntity.setAddress(jsonObject.getString("vendor_address"));
                vendorEntity.setName(jsonObject.getString("vendor_name"));
                vendorData.add(vendorEntity);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        viewAdapter = new VendorViewAdapter(vendorData, this, getway, a);
        recyclerView.setAdapter(viewAdapter);
        viewAdapter.notifyDataSetChanged();
        viewAdapter.onItemClickCallback(this);

    }

    @Override
    public void onItemClick(int p) {
        if (getway.equals("bagdetails")) {
            System.out.println("Vendor Selected");
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
}
