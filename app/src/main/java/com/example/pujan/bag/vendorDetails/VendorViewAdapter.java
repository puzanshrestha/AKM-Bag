package com.example.pujan.bag.vendorDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
        holder.name.setText(vendorEntity.getName());
        holder.address.setText(vendorEntity.getAddress());


        if (getway.equals("bagdetails")) {
            holder.optionsMenu.setVisibility(View.GONE);

        }


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
            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);
            container = itemView.findViewById(R.id.cont_ven_item);


            container.setOnClickListener(this);
            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.bag_option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editMenu:
                                    editVendor(getAdapterPosition());
                                    break;
                                case R.id.deleteMenu:
                                    deleteVendor(getAdapterPosition());
                                    break;

                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();


                }
            });




        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }

    public void editVendor(int position)
    {
        VendorEntity vendorEntity;
        vendorEntity = listdata.get(position);
        Intent i = new Intent(context, AddVendorActivity.class);
        i.putExtra("source", "update");
        i.putExtra("name", vendorEntity.getName());
        i.putExtra("id", Integer.toString(vendorEntity.getId()));
        i.putExtra("address", vendorEntity.getAddress());
        context.startActivity(i);
    }

    public void deleteVendor(final int position){


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure,You wanted to Delete");
        final String a = "";
        final VendorEntity vendorEntity = listdata.get(position);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        String id = Integer.toString(vendorEntity.getId());
                        final String check;
                        try {
                            check = new FunctionsThread(context).execute("AddVendor", a, a, "delete", id).get();
                            System.out.println(check);
                            if (check.equals("Deleted")) {
                                Intent i = new Intent(context, VendorListActivity.class);
                                i.putExtra("getway","actionlist");
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                                context.startActivity(i);
                            } else
                                Toast.makeText(context, "error in delete", Toast.LENGTH_LONG).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




}
