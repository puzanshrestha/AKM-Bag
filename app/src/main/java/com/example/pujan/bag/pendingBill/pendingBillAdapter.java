package com.example.pujan.bag.pendingBill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pujan.bag.R;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class pendingBillAdapter extends RecyclerView.Adapter<pendingBillAdapter.TestHolder>{

    private ArrayList<PendingBillListEntity> listData;
    private LayoutInflater inflater;
    private Context context;

    int current_position = -1;
    int UNSELECTED = -1;

    RecyclerView recyclerView;



    public pendingBillAdapter(ArrayList<PendingBillListEntity> listData, Context c, RecyclerView recyclerView) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.context = c;

        this.recyclerView = recyclerView;
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
        holder.pendingTotal.setText("Rs. " + String.valueOf(item.getTotal()));

        holder.bind();

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView pendingName, pendingAddress, pendingTotal, pendingDate;
        private LinearLayout container;
        private TextView optionsMenu;
        ExpandableLayout expandableLayout;
        Button cancelPendingBillBtn;


        public TestHolder(View itemView) {
            super(itemView);


            container = (LinearLayout) itemView.findViewById(R.id.pendingListContainer);

            pendingName = (TextView) itemView.findViewById(R.id.pendingName);
            pendingAddress = (TextView) itemView.findViewById(R.id.pendingAddress);
            pendingTotal = (TextView) itemView.findViewById(R.id.pendingTotal);
            pendingDate = (TextView) itemView.findViewById(R.id.pendingDate);
            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLayout);
            cancelPendingBillBtn = (Button) itemView.findViewById(R.id.cancelPendingBillBtn);

            cancelPendingBillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setTitle("This will cancel the selected Pending Bill Data");
                    ad.setCancelable(false);
                    ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           cancelBill(getAdapterPosition());

                        }
                    });
                    ad.setNegativeButton("Cancel", null);
                    ad.show();

                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TestHolder holder = (TestHolder) recyclerView.findViewHolderForAdapterPosition(current_position);
                    if (holder != null) {
                        holder.expandableLayout.collapse();
                    }

                    if (current_position == getAdapterPosition()) {
                        current_position = UNSELECTED;
                    } else {

                        expandableLayout.expand();
                        current_position = getAdapterPosition();
                    }
                }
            });

            optionsMenu.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.optionsMenu) {
                try {

                    current_position = getAdapterPosition();

                    Intent i = new Intent(context, OrderActivity.class);

                    Bundle b = new Bundle();


                    b.putString("source", "Pending");
                    b.putString("pId",String.valueOf(listData.get(getAdapterPosition()).getpId()));

                    b.putInt("customer_id", listData.get(current_position).getCustomerId());
                    b.putString("customer_name", listData.get(current_position).getCustomerName());
                    b.putString("customer_address", listData.get(current_position).getAddress());
                    b.putString("total", String.valueOf(listData.get(current_position).getTotal()));
                    i.putExtras(b);

                    context.startActivity(i);



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

        public void bind() {
            expandableLayout.collapse(false);

        }
    }

    private void cancelBill(final int position)
    {
        new android.os.Handler().postDelayed(
                new Runnable() {

                    public void run() {

                        try {
                            String result = new FunctionsThread(context).execute("CancelPendingBill",String.valueOf(listData.get(position).getpId())).get();

                            if(result.equals("Canceled Bill")) {


                                listData.remove(position);
                                notifyItemRemoved(position);

                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }




                    }
                }, 800);



    }

}
