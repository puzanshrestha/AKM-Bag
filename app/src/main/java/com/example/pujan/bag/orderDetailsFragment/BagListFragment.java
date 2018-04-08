package com.example.pujan.bag.orderDetailsFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.vendorSelectFragment.VendorSelectFragementAdapter;
import com.example.pujan.bag.printPackage.PrintEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static android.view.View.GONE;


public class BagListFragment extends Fragment implements SearchView.OnQueryTextListener, VolleyFunctions.AsyncResponse {

    RecyclerView recView;
    BagViewFragmentAdapter bagViewFragmentAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();

    ArrayList<PrintEntity> orderValues = new ArrayList<>();
    private BagAdapterPuller bagAdapterPuller;

    ArrayList<BagColorQuantity> bagColorQuantities = new ArrayList<>();


    ProgressBar progressBar;


    public void scrollToPosition(int position) {


        for (int i = 0; i < bagData.size(); i++) {
            if (bagData.get(i).getId() == position)
                position = i;
        }
        recView.smoothScrollToPosition(position);
        bagViewFragmentAdapter.expandLayout(position);


    }


    public interface BagAdapterPuller {
        void getBagAdapter(BagViewFragmentAdapter bfAdapter);
    }


    public void onItemClickCallback(OrderActivity itemClickCallback) {
        this.bagAdapterPuller = itemClickCallback;
    }


    public BagListFragment(ArrayList<PrintEntity> orderValues) {
        this.orderValues = orderValues;

    }

    public BagListFragment() {


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_action_bar, menu);
        MenuItem item = menu.findItem(R.id.searchh);
        SearchView searchView = new SearchView(((OrderActivity) getContext()).getSupportActionBar().getThemedContext());
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        searchEditText.setHint("Search Bag");
        searchEditText.setBackground(new ColorDrawable(Color.WHITE));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);


        searchView.setOnQueryTextListener(this);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_view_bag, null);


        setHasOptionsMenu(true);
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

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addNewBagBtn);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.actionBar);
        toolbar.setVisibility(GONE);
        fab.setVisibility(GONE);
        recView = (RecyclerView) view.findViewById(R.id.recView);

        BagViewFragmentAdapter.TestHolder test = (BagViewFragmentAdapter.TestHolder) recView.findViewHolderForLayoutPosition(0);


        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recView.setLayoutManager(lm);


        //  recView.setLayoutManager(new GridLayoutManager(getContext(),2));


        VolleyFunctions t = new VolleyFunctions(getContext());
        t.viewBag("0", "0");
        t.trigAsyncResponse(BagListFragment.this);


        return view;
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
        bagViewFragmentAdapter.setFilter(listStartsWith);
        return false;
    }

    public void getData(String response) {
        try {

            if (response == "Error") {
                Toast.makeText(getContext(), "Connection Error... Please check your connection !", Toast.LENGTH_SHORT).show();
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

            convertStockData(stockJsonArray);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error With Database Connection..!!", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onComplete(String output) {



        if (output.equals("ERROR")) {
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
        } else {
            getData(output);

            if (getActivity() != null) {
                bagViewFragmentAdapter = new BagViewFragmentAdapter(bagData, bagColorQuantities, getContext(), recView);
                recView.setAdapter(bagViewFragmentAdapter);
                bagAdapterPuller.getBagAdapter(bagViewFragmentAdapter);
                bagViewFragmentAdapter.setPendingData(orderValues);
                progressBar.setVisibility(GONE);

            }

        }
    }


}
