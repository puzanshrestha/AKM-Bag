package com.example.pujan.bag.vendorDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public  class VendorListActivity extends AppCompatActivity implements VendorViewAdapter.ItemClickCallback {
    RecyclerView recyclerView;
    VendorViewAdapter viewAdapter;
    ArrayList<VendorEntity>vendorData;
    int a=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        vendorData= new ArrayList<>();
        try {
            String response= new FunctionsThread(this).execute("ViewVendor").get();
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray("result");
            for (int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                VendorEntity vendorEntity=new VendorEntity();
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
        recyclerView= (RecyclerView) findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        viewAdapter= new VendorViewAdapter(vendorData,this,a);
        recyclerView.setAdapter(viewAdapter);
        viewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(int p) {
        System.out.println(p);


    }
}
