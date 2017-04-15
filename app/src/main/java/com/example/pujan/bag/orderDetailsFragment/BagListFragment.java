package com.example.pujan.bag.orderDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BagListFragment extends Fragment implements BagViewFragmentAdapter.ItemClickCallback, SearchView.OnQueryTextListener, FunctionsThread.AsyncResponse {

    RecyclerView recView;
    BagViewFragmentAdapter bagViewFragmentAdapter;
    String customer_id = "0";
    String source = "";
    String customerName = "";
    String pending = "";
    String pId = "";
    ArrayList<BagEntity> bagData = new ArrayList<>();

    ArrayList<BagColorQuantity> colorQuantities = new ArrayList<>();
    ArrayList<BagColorQuantity> pendingList = new ArrayList<>();

    private ItemSelect itemSelect;
    public interface ItemSelect{
        void onItemClick(int p);
    }

    public void onItemClickCallback(OrderFragment itemClickCallback) {
        this.itemSelect = itemClickCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_view_bag, null);


        /*
        source = getIntent().getStringExtra("source");
        customerName = getIntent().getStringExtra("customerName");
        customer_id = getIntent().getStringExtra("customer_id");
        pending = getIntent().getStringExtra("pending");
        pId = getIntent().getStringExtra("pId");





        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        if (source.equals("bag")) {
            actionBar.setTitle(" View Bag");

            // displaying logo in the action bar
        } else {
            actionBar.setTitle(" Order Menu");


        }

*/
        recView = (RecyclerView) view.findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recView.setLayoutManager(lm);


        //  recView.setLayoutManager(new GridLayoutManager(getContext(),2));


        FunctionsThread t = new FunctionsThread(getContext());
        t.execute("ViewBag");
        t.trigAsyncResponse(BagListFragment.this);


        return view;
    }

    @Override
    public void onItemClick(int p) {


        itemSelect.onItemClick(p);

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
        bagViewFragmentAdapter.setFilter(newbag);
        return false;
    }

    public void getData(String response) {
        try {

            if (response == "Error") {
                Toast.makeText(getContext(), "Connection Error... Please check your connection !", Toast.LENGTH_SHORT).show();
            }
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");


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


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error With Database Connection..!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onComplete(String output) {

        getData(output);

        if(getActivity()!=null) {
            bagViewFragmentAdapter = new BagViewFragmentAdapter(bagData, "orderView", getContext());
            recView.setAdapter(bagViewFragmentAdapter);
            bagViewFragmentAdapter.onItemClickCallback(BagListFragment.this);
            bagViewFragmentAdapter.notifyDataSetChanged();
        }

    }


}
