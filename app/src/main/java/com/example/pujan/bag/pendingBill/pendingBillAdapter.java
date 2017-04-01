package com.example.pujan.bag.pendingBill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.customerDetails.CustomerListActivity;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class PendingBillAdapter extends RecyclerView.Adapter<PendingBillAdapter.TestHolder> {

    private ArrayList<PendingBillListEntity> listData;
    private LayoutInflater inflater;
    private Context context;
    private String ip = "";




    public PendingBillAdapter(ArrayList<PendingBillListEntity> listData, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.context = c;

    }



    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pending_list, parent, false);

        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        PendingBillListEntity item = listData.get(position);
        holder.date.setText(Html.fromHtml("<u><b>"+"Date: "+ item.getDate()+"</b></u>"));
        holder.customerName.setText("Customer Name: "+item.getCustomerName());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView date;
        private TextView customerName;
        private LinearLayout container;




        public TestHolder(View itemView) {
            super(itemView);


            container =(LinearLayout)itemView.findViewById(R.id.pendingListContainer);
            date = (TextView) itemView.findViewById(R.id.date);
            customerName = (TextView) itemView.findViewById(R.id.customerName);
            container.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.pendingListContainer)
            {
                String t="";
                try {

                    t = new FunctionsThread(context).execute("QueryPendingBill",String.valueOf(listData.get(getAdapterPosition()).getpId())).get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                Intent i = new Intent(context, BagListActivity.class);
                i.putExtra("customer_id", Integer.toString(listData.get(getAdapterPosition()).getCustomerId()));
                i.putExtra("source", "pending");
                i.putExtra("pending",t);
                i.putExtra("pId", Integer.toString(listData.get(getAdapterPosition()).getpId()));
                i.putExtra("customerName", listData.get(getAdapterPosition()).getCustomerName());;

                context.startActivity(i);
            }
        }
    }

}
