package com.example.pujan.bag.orderDetailsFragment.customerSelectFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.customerDetails.CustomerEntity;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;
import com.example.pujan.bag.orderDetailsFragment.customerSelectFragment.SelectCustomerFragment;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/4/2017.
 */
public class SelectCustomerFragmentAdapter extends RecyclerView.Adapter<SelectCustomerFragmentAdapter.TestHolder> {

    ArrayList<CustomerEntity> listData;
    LayoutInflater layoutInflater;
    Context context;



    OrderActivity orderActivity;


    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);


    }

    void onItemClickCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public void setFilter(ArrayList<CustomerEntity> list)
    {
        listData=new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();
    }




    public SelectCustomerFragmentAdapter(ArrayList<CustomerEntity> listData, OrderActivity orderActivity, Context c) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(c);
        this.orderActivity = orderActivity;
        this.context = c;


    }

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_customer_list, parent, false);

        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {

        CustomerEntity item = listData.get(position);
        holder.id.setText(Integer.toString(item.getId()));
        holder.id.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("Name: "+item.getName()));
        holder.address.setText(Html.fromHtml("Address: "+item.getAddress()));
        holder.phone.setText(Html.fromHtml(item.getPhone()));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView id, name, address, phone,optionsMenu;
        private View container;




        public TestHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.lbl_cust_id);
            name = (TextView) itemView.findViewById(R.id.lbl_cust_name);
            address = (TextView) itemView.findViewById(R.id.lbl_cust_address);
            phone = (TextView) itemView.findViewById(R.id.lbl_cust_phone);
            container = itemView.findViewById(R.id.cont_cust_item);
            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);

            container.setOnClickListener(this);

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    CustomerEntity customerEntity= listData.get(position);
                    String phone = customerEntity.getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    context.startActivity(intent);
                }
            });


            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle customerInfo = new Bundle();
                    customerInfo.putInt("customer_id",listData.get(getAdapterPosition()).getId());
                    customerInfo.putString("customer_name",listData.get(getAdapterPosition()).getName());
                    customerInfo.putString("customer_address",listData.get(getAdapterPosition()).getAddress());
                    customerInfo.putString("customer_phone",listData.get(getAdapterPosition()).getPhone());
                    orderActivity.setCustomerInfo(customerInfo);

                    itemClickCallback.onItemClick(getAdapterPosition());



                    /*
                    PopupMenu popup = new PopupMenu(context, v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.bag_option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editMenu:
                                    editCustomer(getAdapterPosition());
                                    break;
                                case R.id.deleteMenu:
                                    deleteCustomer(getAdapterPosition());
                                    break;

                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

*/


                }
            });


        }



        @Override
        public void onClick(View v) {




        }





    }



}
