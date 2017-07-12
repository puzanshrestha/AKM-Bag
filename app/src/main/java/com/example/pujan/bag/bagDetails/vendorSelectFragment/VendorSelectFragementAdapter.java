package com.example.pujan.bag.bagDetails.vendorSelectFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.AddBagActivity;
import com.example.pujan.bag.customerDetails.CustomerEntity;
import com.example.pujan.bag.vendorDetails.VendorDetailsActivity;
import com.example.pujan.bag.vendorDetails.VendorEntity;
import com.example.pujan.bag.vendorDetails.VendorViewAdapter;


import java.util.ArrayList;

/**
 * Created by Pujan on 1/4/2017.
 */
public class VendorSelectFragementAdapter extends RecyclerView.Adapter<VendorSelectFragementAdapter.TestHolder> {

    ArrayList<VendorEntity> listData= new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;



    AddBagActivity orderActivity;


    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);


    }

    void onItemClickCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public void setFilter(ArrayList<VendorEntity> list)
    {
        listData=new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();
    }




    public VendorSelectFragementAdapter(ArrayList<VendorEntity> listData, AddBagActivity orderActivity, Context c) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(c);
        this.orderActivity = orderActivity;
        this.context = c;


    }

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_vendor_list, parent, false);

        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(final VendorSelectFragementAdapter.TestHolder holder, final int position) {
        VendorEntity vendorEntity = listData.get(position);
        holder.id.setText(Integer.toString(vendorEntity.getId()));
        holder.id.setVisibility(View.GONE);
        holder.phone.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("Name: "+vendorEntity.getName()));
        holder.address.setText(Html.fromHtml("Address: "+vendorEntity.getAddress()));




    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id, name, address, phone,optionsMenu;

        private View container;


        public TestHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.lbl_ven_id);
            name = (TextView) itemView.findViewById(R.id.lbl_ven_name);
            address = (TextView) itemView.findViewById(R.id.lbl_ven_address);
            phone=(TextView)itemView.findViewById(R.id.lbl_ven_phone);
            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);
            container = itemView.findViewById(R.id.cont_ven_item);


            container.setOnClickListener(this);
            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    itemClickCallback.onItemClick(getAdapterPosition());



                }
            });




        }

        @Override
        public void onClick(View v) {

        }
    }













}
