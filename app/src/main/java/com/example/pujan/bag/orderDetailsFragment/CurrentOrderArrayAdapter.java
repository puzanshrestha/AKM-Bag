package com.example.pujan.bag.orderDetailsFragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.pujan.bag.R;
import com.example.pujan.bag.printPackage.PrintEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by puzan on 12-May-17.
 */
public class CurrentOrderArrayAdapter extends ArrayAdapter<PrintEntity> {


    Context context;
    ArrayList<PrintEntity> orderValues;


    public CurrentOrderArrayAdapter(Context context,ArrayList<PrintEntity> orderValues) {
        super(context, R.layout.current_order_adapter_list,orderValues);
        this.context=context;
        this.orderValues=orderValues;


    }

    private class ViewHolder {
        TextView productName;
        TextView colorQty;
        LinearLayout container;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.current_order_adapter_list, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.product);
            holder.colorQty=(TextView)convertView.findViewById(R.id.colorQty);
            holder.container=(LinearLayout)convertView.findViewById(R.id.viewContainer);
            convertView.setTag(holder);
        }
        else
            holder= (ViewHolder) convertView.getTag();


        holder.productName.setText(orderValues.get(position).getProduct().toString());

        String qtyColor="";
        for (LinkedHashMap.Entry<String, Integer> entry : orderValues.get(position).getColorQuantity().entrySet())
            qtyColor+=entry.getKey().toString()+" - "+entry.getValue().toString()+"\n";

            holder.colorQty.setText(qtyColor);



            return convertView;
    }

    public void updateValue(ArrayList<PrintEntity> orderValues)
    {
        this.orderValues.clear();
        this.orderValues.addAll(orderValues);
        if(orderValues.size()!=0)
        this.notifyDataSetChanged();


    }


}
