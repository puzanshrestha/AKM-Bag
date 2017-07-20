package com.example.pujan.bag.orderDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.printPackage.PrintEntity;

import java.util.ArrayList;


/**
 * Created by puzan on 08-Apr-17.
 */
public class CurrentOrderListFragment extends Fragment {


    ArrayList<PrintEntity> bagColorQuantities= new ArrayList<>();
    private ListView listView;
    CurrentOrderArrayAdapter adapter;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_current_orders, null);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        adapter = new CurrentOrderArrayAdapter(getContext(),bagColorQuantities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id){

                OrderActivity.changePage(bagColorQuantities.get(position).getBag_id());

            }
        });

        return view;
   }

    public void receiveData(ArrayList<PrintEntity> colorQuantities)
    {
        this.bagColorQuantities=colorQuantities;
        adapter.updateValue(bagColorQuantities);
    }
}
