package com.example.pujan.bag.bagStock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.customerDetails.CustomerListActivity;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class StockViewAdapter extends RecyclerView.Adapter<StockViewAdapter.TestHolder> {

    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;
    private Context context;
    private String ip = "";


    public StockViewAdapter(ArrayList<BagEntity> listData, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.context = c;

        DbHelper db = new DbHelper(context);
        try {
            ip = db.getIP();
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_stock_list, parent, false);

        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        BagEntity item = listData.get(position);
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(Integer.toString(item.getPrice()));
        holder.company.setText(item.getCompany());
        holder.quantity.setText(Integer.toString(item.getQuantity()));


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


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    int qty = 0;

    class TestHolder extends RecyclerView.ViewHolder {


        private TextView name;
        private TextView price;
        private TextView category;
        private TextView quantity;
        private TextView company;
        private ImageView photoBox;
        private View container;
        private Button selectBag;


        public TestHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.lbl_bag_name);
            price = (TextView) itemView.findViewById(R.id.lbl_bag_price);
            category = (TextView) itemView.findViewById(R.id.lbl_bag_category);
            company = (TextView) itemView.findViewById(R.id.lbl_bag_company);
            quantity = (TextView) itemView.findViewById(R.id.lbl_bag_quantity);
            container = itemView.findViewById(R.id.cont_root_item);
            photoBox = (ImageView) itemView.findViewById(R.id.photo_box);
            selectBag = (Button) itemView.findViewById(R.id.selectBagBtn);

            selectBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,StockUpdateActivity.class);
                    i.putExtra("bag_id",Integer.toString(listData.get(getAdapterPosition()).getId()));
                    context.startActivity(i);
                }
            });
        }



    }
}
