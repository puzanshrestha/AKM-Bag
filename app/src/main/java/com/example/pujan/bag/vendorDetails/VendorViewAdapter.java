package com.example.pujan.bag.vendorDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.customerDetails.CustomerEntity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ramse on 19/01/2017.
 */
public class VendorViewAdapter extends RecyclerView.Adapter<VendorViewAdapter.TestHolder> {
    private ArrayList<VendorEntity> listdata;
    private LayoutInflater inflater;
    private Context context;

    private String getway;


    private ItemClickCallback itemClickCallback;

    public void setFilter(ArrayList<VendorEntity> list) {
        listdata = new ArrayList<>();
        listdata.addAll(list);
        notifyDataSetChanged();

    }


    public VendorViewAdapter(ArrayList<VendorEntity> vendorData, Context c, String getway) {
        this.listdata = vendorData;
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.getway = getway;


    }



    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_vendor_list, parent, false);
        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, final int position) {
        VendorEntity vendorEntity = listdata.get(position);
        holder.id.setText(Integer.toString(vendorEntity.getId()));
        holder.id.setVisibility(View.GONE);
        holder.phone.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("Name: "+vendorEntity.getName()));
        holder.address.setText(Html.fromHtml("Address: "+vendorEntity.getAddress()));




    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    void onItemClickCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

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

                    Intent i = new Intent(context,VendorDetailsActivity.class);
                    i.putExtra("vendor_id",String.valueOf(listdata.get(getAdapterPosition()).getId()));
                    i.putExtra("name",listdata.get(getAdapterPosition()).getName());
                    i.putExtra("address",listdata.get(getAdapterPosition()).getAddress());
                    context.startActivity(i);


                }
            });




        }

        @Override
        public void onClick(View v) {

        }
    }








}
