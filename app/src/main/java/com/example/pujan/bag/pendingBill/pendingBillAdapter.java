package com.example.pujan.bag.pendingBill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class PendingBillAdapter extends RecyclerView.Adapter<PendingBillAdapter.TestHolder> implements FunctionsThread.AsyncResponse {

    private ArrayList<PendingBillListEntity> listData;
    private LayoutInflater inflater;
    private Context context;

    int position=-1;


    @Override
    public void onComplete(String output) {
        Intent i = new Intent(context, OrderActivity.class);

        Bundle b = new Bundle();


        b.putString("pendingData",output);
        b.putString("source","Pending");
        b.putString("customer_id",String.valueOf(listData.get(position).getCustomerId()));
        b.putString("customer_name",listData.get(position).getCustomerName());
        b.putString("customer_address",listData.get(position).getAddress());
        b.putString("total",String.valueOf(listData.get(position).getTotal()));

        i.putExtras(b);

        context.startActivity(i);
    }





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
        holder.pendingName.setText(item.getCustomerName());
        holder.pendingDate.setText(item.getDate().toString());
        holder.pendingAddress.setText(item.getAddress());
        holder.pendingTotal.setText("Rs. "+String.valueOf(item.getTotal()));



    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        private TextView pendingName,pendingAddress,pendingTotal,pendingDate;
        private LinearLayout container;




        public TestHolder(View itemView) {
            super(itemView);


            container =(LinearLayout)itemView.findViewById(R.id.pendingListContainer);

            pendingName = (TextView) itemView.findViewById(R.id.pendingName);
            pendingAddress=(TextView) itemView.findViewById(R.id.pendingAddress);
            pendingTotal=(TextView) itemView.findViewById(R.id.pendingTotal);
            pendingDate = (TextView) itemView.findViewById(R.id.pendingDate);

            container.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.pendingListContainer)
            {
                try {

                    position=getAdapterPosition();
                    FunctionsThread thread = new FunctionsThread(context);
                    thread.execute("QueryPendingBill",String.valueOf(listData.get(getAdapterPosition()).getpId()));
                    thread.trigAsyncResponse(PendingBillAdapter.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
