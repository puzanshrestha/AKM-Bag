package com.example.pujan.bag.bagDetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pujan.bag.R;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Pujan on 1/3/2017.
 */
public class BagViewAdapter extends RecyclerView.Adapter{



    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;

    private String bag;
    private Context context;
    String ip = "";


    ArrayList<BagColorQuantity> stockList = new ArrayList<>();

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public void setFilter(ArrayList<BagEntity> list) {
        listData = new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();

    }




    public BagViewAdapter(ArrayList<BagEntity> listData,ArrayList<BagColorQuantity> stockList, String viewsource, Context c) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_bag_list, parent, false);

            vh = new TestHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TestHolder)
        {

            BagEntity item = listData.get(position);
            ((TestHolder) holder).id.setText((Integer.toString(item.getId())));
            ((TestHolder) holder).id.setVisibility(View.GONE);

            ((TestHolder) holder).name.setText(Html.fromHtml("<b><u>Product: "+item.getName()+"</u></b>"));
            ((TestHolder) holder).category.setText(Html.fromHtml("Category: "+item.getCategory()));
            ((TestHolder) holder).price.setText(Html.fromHtml("Price: "+Integer.toString(item.getPrice())));
            ((TestHolder) holder).company.setText(Html.fromHtml("Company: "+item.getCompany()));


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

            ((TestHolder) holder).quantity.setText(Html.fromHtml("Stock: "+Integer.toString(stockTotal)));




            try {
                Picasso
                        .with(context)
                        .load("http://" + ip + "/bagWebServices/uploads/" + item.getPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .resize(300, 300)
                        .placeholder(R.drawable.vector_drawable_bag)
                        .into(((TestHolder) holder).photoBox);

            } catch (Exception e) {
                System.out.println("No file found");
            }



        }
        else
        {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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



                }
            });



        }




    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public void edit(int position) {
        Intent i;

            i = new Intent(context, BagDetailsActivity.class);

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
