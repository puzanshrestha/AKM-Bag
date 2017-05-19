package com.example.pujan.bag.bagStock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.bagStock.ColorQuantityEntity;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.orderDetailsFragment.BagListFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class StockViewAdapter extends RecyclerView.Adapter<StockViewAdapter.TestHolder> implements FunctionsThread.AsyncResponse {



    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;

    private String bag;
    private Context context;
    String ip = "";

    String[] array_spinner;

    ArrayList<BagColorQuantity> stockList = new ArrayList<>();



    public void setFilter(ArrayList<BagEntity> list) {
        listData = new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public void onComplete(String output) {



        notifyDataSetChanged();
    }






    public StockViewAdapter(ArrayList<BagEntity> listData,ArrayList<BagColorQuantity> stockList, String viewsource, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.bag = viewsource;
        this.context = c;
        this.stockList=stockList;



        DbHelper db = new DbHelper(context);
        try {
            ip = db.getIP();
        } catch (Exception e) {
        } finally {
            db.close();
        }
/*
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        map.put("NO1",3);
        map.put("skf",5);
        BagColorQuantity bcq = new BagColorQuantity();
        bcq.setBag_id(1);
        bcq.setQuantityColor(map);
        colorQuantities.add(bcq);


*/
    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_bag_list, parent, false);

        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        BagEntity item = listData.get(position);
        holder.id.setText((Integer.toString(item.getId())));
        holder.id.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("<b><u>Product: "+item.getName()+"</u></b>"));
        holder.category.setText(Html.fromHtml("Category: "+item.getCategory()));
        holder.price.setText(Html.fromHtml("Price: "+Integer.toString(item.getPrice())));
        holder.company.setText(Html.fromHtml("Company: "+item.getCompany()));


        int stockTotal=0;
        for(int i=0;i<stockList.size();i++)
        {
            if(listData.get(position).getId()==stockList.get(i).getBag_id()) {

                for (LinkedHashMap.Entry<String, Integer> entry : stockList.get(i).getQuantityColor().entrySet()) {

                    stockTotal+=entry.getValue();

                }

                break;
            }

        }

        holder.quantity.setText(Html.fromHtml("Stock: "+Integer.toString(stockTotal)));




        try {
            Picasso
                    .with(context)
                    .load("http://" + ip + "/bagWebServices/uploads/" + item.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(300, 300)
                    .placeholder(R.drawable.vector_drawable_bag)
                    .into(holder.photoBox);

        } catch (Exception e) {
            System.out.println("No file found");
        }




    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class TestHolder extends RecyclerView.ViewHolder  {

        private TextView id;
        private TextView name;
        private TextView price;
        private TextView category;
        private TextView quantity;
        private TextView company;





        private ImageView photoBox;
        private TextView optionsMenu;
        private TextView redStockQty,blackStockQty,brownStockQty,othersStockQty;





        public TestHolder(View itemView) {
            super(itemView);



            id = (TextView) itemView.findViewById(R.id.lbl_bag_id);
            name = (TextView) itemView.findViewById(R.id.lbl_bag_name);
            price = (TextView) itemView.findViewById(R.id.lbl_bag_price);
            category = (TextView) itemView.findViewById(R.id.lbl_bag_category);
            company = (TextView) itemView.findViewById(R.id.lbl_bag_company);
            quantity = (TextView) itemView.findViewById(R.id.lbl_bag_quantity);
            //  container = itemView.findViewById(R.id.cont_root_item);
            photoBox = (ImageView) itemView.findViewById(R.id.photo_box);



            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);


            context=itemView.getContext();


            redStockQty = (TextView) itemView.findViewById(R.id.redStockQty);
            blackStockQty = (TextView) itemView.findViewById(R.id.blackStockQty);
            brownStockQty = (TextView) itemView.findViewById(R.id.brownStockQty);
            othersStockQty = (TextView) itemView.findViewById(R.id.othersStockQty);




            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    edit(getAdapterPosition());
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
                                    edit(getAdapterPosition());
                                    break;
                                case R.id.deleteMenu:
                                    delete(getAdapterPosition());
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



    }




    public void edit(int position) {
        Intent i;

        i = new Intent(context, StockDetailsActivity.class);

        BagEntity item = listData.get(position);
        String bagid = Integer.toString(item.getId());
        String category = item.getCategory();
        String name = item.getName();
        String company = item.getCompany();
        String price = Integer.toString(item.getPrice());
        String vendorID =Integer.toString(item.getVendorId());
        String quantity = Integer.toString(item.getQuantity());
        i.putExtra("bagid", bagid);
        i.putExtra("name", name);
        i.putExtra("category", category);
        i.putExtra("price", price);
        i.putExtra("company", company);
        i.putExtra("vendor_id", vendorID);
        i.putExtra("photo", item.getPhoto());
        context.startActivity(i);



    }









}
