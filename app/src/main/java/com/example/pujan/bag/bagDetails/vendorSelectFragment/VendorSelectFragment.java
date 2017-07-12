package com.example.pujan.bag.bagDetails.vendorSelectFragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.AddBagActivity;
import com.example.pujan.bag.vendorDetails.VendorEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VendorSelectFragment extends DialogFragment implements VolleyFunctions.AsyncResponse,VendorSelectFragementAdapter.ItemClickCallback{

    RecyclerView recyclerView;
    VendorSelectFragementAdapter selectCustomerFragmentAdapter;
    ArrayList<VendorEntity> vendorData;

    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.colorPrimary);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {
        View view = inflater.inflate(R.layout.activity_select_customer_fragment, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        TextView textView = (TextView)view.findViewById(R.id.titleBar);
        textView.setText("Select Vendor");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        progressBar =(ProgressBar)view.findViewById(R.id.progressBar);

        fab.hide();

        recyclerView=(RecyclerView)view.findViewById(R.id.recView);
        VolleyFunctions t = new VolleyFunctions(getContext());
        t.viewVendor();
        t.trigAsyncResponse(VendorSelectFragment.this);



          return view;

    }


    public void onComplete(String output) {


        progressBar.setVisibility(View.GONE);
        if(output.equals("ERROR"))
        {

        }
        else {
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
                Toast.makeText(getContext(), "Error connecting with the Server..!", Toast.LENGTH_SHORT).show();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            VendorSelectFragementAdapter viewAdapter = new VendorSelectFragementAdapter(vendorData, (AddBagActivity) getActivity(), getContext());
            recyclerView.setAdapter(viewAdapter);
            viewAdapter.onItemClickCallback(this);


        }
    }


    @Override
    public void onItemClick(int p) {
        Bundle bundle= new Bundle() ;
        bundle.putInt("id",vendorData.get(p).getId());
        bundle.putString("name",vendorData.get(p).getName());
        ((AddBagActivity)getActivity()).setVendorInfo(bundle);
        this.dismiss();
    }
}
