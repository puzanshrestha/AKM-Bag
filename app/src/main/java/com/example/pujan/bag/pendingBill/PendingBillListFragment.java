package com.example.pujan.bag.pendingBill;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.orderDetailsFragment.OrderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by puzan on 26-Mar-17.
 */
public class PendingBillListFragment extends Fragment implements VolleyFunctions.AsyncResponse {


    RecyclerView recView;

    FloatingActionButton addOrderFab;
    com.example.pujan.bag.pendingBill.pendingBillAdapter pendingBillAdapter;
    ArrayList<PendingBillListEntity> pBillList;


    private ChangeFragmentListener changeFragmentListener;
    public interface ChangeFragmentListener{
        void changeFromPendingBillList();

    }

    public void onChangeCallBack(OrderFragment itemClickCallback) {
        this.changeFragmentListener = itemClickCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_list_activity, null);

/*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Pending Bill List");

*/
        recView = (RecyclerView) view.findViewById(R.id.recView);
        addOrderFab = (FloatingActionButton) view.findViewById(R.id.addOrderFab);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);

        DbHelper dbh = new DbHelper(getContext());


        VolleyFunctions thread = new VolleyFunctions(getContext());
        thread.queryPendingBillList(dbh.getShop());
        thread.trigAsyncResponse(PendingBillListFragment.this);


        addOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentListener.changeFromPendingBillList();
            }
        });


        return view;
    }

    @Override
    public void onComplete(String response) {


        pBillList = new ArrayList<>();
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
            Toast.makeText(getContext(), "Error connecting with the Server..!", Toast.LENGTH_SHORT).show();
        }
        pendingBillAdapter = new pendingBillAdapter(pBillList, getContext(),recView);
        recView.setAdapter(pendingBillAdapter);
        pendingBillAdapter.notifyDataSetChanged();
    }


}
