package com.example.pujan.bag.orderDetailsFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.pujan.bag.bagDetails.AddBagActivity;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.bagStock.ColorQuantityEntity;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class BagViewFragmentAdapter extends RecyclerView.Adapter<BagViewFragmentAdapter.TestHolder> implements FunctionsThread.AsyncResponse {


    int selected_position = 0;
    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;

    private String bag;
    private Context context;
    String ip = "";


    ArrayList<BagColorQuantity> colorQuantities = new ArrayList<>();


    private ItemClickCallback itemClickCallback;


    public void setFilter(ArrayList<BagEntity> list) {
        listData = new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public void onComplete(String output) {

        notifyDataSetChanged();
    }

    public interface ItemClickCallback {
        void onItemClick(int p);
    }


    public void onItemClickCallback(BagListFragment itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }


    public BagViewFragmentAdapter(ArrayList<BagEntity> listData, String viewsource, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.bag = viewsource;
        this.context = c;


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
        View view = inflater.inflate(R.layout.fragment_activity_bag_list, parent, false);

        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        BagEntity item = listData.get(position);
        holder.id.setText((Integer.toString(item.getId())));
        holder.id.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("<b><u>Product: " + item.getName() + "</u></b>"));
        holder.category.setText(Html.fromHtml("Category: " + item.getCategory()));
        holder.price.setText(Html.fromHtml("Price: " + Integer.toString(item.getPrice())));
        holder.company.setText(Html.fromHtml("Company: " + item.getCompany()));
        holder.quantity.setText(Html.fromHtml("Stock: " + Integer.toString(item.getQuantity())));


        try {
            Picasso
                    .with(context)
                    .load("http://" + ip + "/bagWebServices/uploads/" + item.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(300, 300)
                    .placeholder(R.drawable.bag)
                    .into(holder.photoBox);

        } catch (Exception e) {
            System.out.println("No file found");
        }


        if (selected_position == position) {
            holder.container.setBackgroundColor(Color.DKGRAY);
        } else
            holder.container.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView id;
        private TextView name;
        private TextView price;
        private TextView category;
        private TextView quantity;
        private TextView company;
        private View container;

        private ImageView photoBox;


        public TestHolder(View itemView) {
            super(itemView);


            id = (TextView) itemView.findViewById(R.id.lbl_bag_id);
            name = (TextView) itemView.findViewById(R.id.lbl_bag_name);
            price = (TextView) itemView.findViewById(R.id.lbl_bag_price);
            category = (TextView) itemView.findViewById(R.id.lbl_bag_category);
            company = (TextView) itemView.findViewById(R.id.lbl_bag_company);
            quantity = (TextView) itemView.findViewById(R.id.lbl_bag_quantity);
            container = itemView.findViewById(R.id.cont_root_item);
            photoBox = (ImageView) itemView.findViewById(R.id.photo_box);
            context = itemView.getContext();
            container.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            itemClickCallback.onItemClick(listData.get(getAdapterPosition()).getId());


        }
    }


}
