package com.example.pujan.bag.vendorDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ramse on 19/01/2017.
 */
public class VendorViewAdapter extends RecyclerView.Adapter<VendorViewAdapter.TestHolder> {
    ArrayList<VendorEntity> listdata;
    LayoutInflater inflater;
    Context context;
    private int a;
    private int ven_id = -1;
    String getway;


    private int i = 0;
    private ItemClickCallback itemClickCallback;


    public VendorViewAdapter(ArrayList<VendorEntity> vendorData, VendorListActivity vendorListActivity, String getway, int a) {
        this.listdata = vendorData;
        this.context = vendorListActivity;
        this.inflater = LayoutInflater.from(vendorListActivity);
        this.getway = getway;


    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vendor_recyler_adapter, parent, false);
        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, final int position) {
        VendorEntity vendorEntity = listdata.get(position);
        holder.id.setText(Integer.toString(vendorEntity.getId()));
        holder.name.setText(vendorEntity.getName());
        holder.address.setText(vendorEntity.getAddress());
        holder.edit.setVisibility(View.INVISIBLE);
        holder.delete.setVisibility(View.INVISIBLE);
        System.out.println(position);

        if (ven_id != position) {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);

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
        private TextView id, name, address, phone;
        private CheckBox checkBox;
        private View container;
        private Button edit, delete, snap;

        public TestHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.lbl_ven_id);
            name = (TextView) itemView.findViewById(R.id.lbl_ven_name);
            address = (TextView) itemView.findViewById(R.id.lbl_ven_address);
            checkBox = (CheckBox) itemView.findViewById(R.id.ven_checkbox);
            edit = (Button) itemView.findViewById(R.id.cust_ven_edit);
            delete = (Button) itemView.findViewById(R.id.cust_ven_delete);
            container = itemView.findViewById(R.id.cont_ven_item);
            if (getway.equals("bagdetails")) {
                checkBox.setVisibility(View.INVISIBLE);
                container.setOnClickListener(this);
            } else if (getway.equals("actionlist")) {
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                checkBox.setOnClickListener(this);
                container.setOnClickListener(this);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        VendorEntity vendorEntity;
                        vendorEntity = listdata.get(getAdapterPosition());
                        Intent i = new Intent(context, AddVendorActivity.class);
                        i.putExtra("source", "update");
                        i.putExtra("name", vendorEntity.getName());
                        i.putExtra("id", Integer.toString(vendorEntity.getId()));
                        i.putExtra("address", vendorEntity.getAddress());
                        context.startActivity(i);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Are you sure,You wanted to Delete");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String a = "";
                                        int position = getAdapterPosition();
                                        VendorEntity vendorEntity = listdata.get(position);
                                        String id = Integer.toString(vendorEntity.getId());
                                        final String check;
                                        try {
                                            check = new FunctionsThread(context).execute("AddVendor", a, a, "delete", id).get();
                                            System.out.println(check);
                                            if (check.equals("Delete")) {
                                                Intent i = new Intent(context, VendorListActivity.class);
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
                });
            }


        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
            if (v.getId() == R.id.ven_checkbox) {
                if (checkBox.isChecked()) {
                    ven_id = (getAdapterPosition());
                    edit.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                } else {

                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);

                }

            }
        }
    }


}
