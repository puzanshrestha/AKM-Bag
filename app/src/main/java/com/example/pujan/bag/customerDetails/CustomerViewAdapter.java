package com.example.pujan.bag.customerDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pujan.bag.R;

import java.util.ArrayList;

/**
 * Created by Pujan on 1/4/2017.
 */
public class CustomerViewAdapter extends RecyclerView.Adapter<CustomerViewAdapter.TestHolder> {

    ArrayList<CustomerEntity> listData;
    LayoutInflater layoutInflater;
    String method;
    Context context;





    public void setFilter(ArrayList<CustomerEntity> list)
    {
        listData=new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();
    }
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);


    }

    void onItemClickCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public CustomerViewAdapter(ArrayList<CustomerEntity> listData, String method, Context c) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(c);
        this.method = method;
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

                    Intent i = new Intent(context,CustomerDetailsActivity.class);
                    i.putExtra("customer_id",String.valueOf(listData.get(getAdapterPosition()).getId()));
                    i.putExtra("name",listData.get(getAdapterPosition()).getName());
                    i.putExtra("address",listData.get(getAdapterPosition()).getAddress());
                    i.putExtra("phone",listData.get(getAdapterPosition()).getPhone());
                    context.startActivity(i);


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
                itemClickCallback.onItemClick(getAdapterPosition());



            }





    }



}
