package com.example.pujan.bag.orderDetails;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.printPackage.DeviceListActivity;
import com.example.pujan.bag.printPackage.PrintDemo;
import com.example.pujan.bag.printPackage.PrintEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class OrderDisplayActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    String source;

    String customer_id;
    String customer_name;
    ArrayList<PrintEntity> print = new ArrayList<>();
    int bag_ids[];
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<BagColorQuantity> getData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.customersmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);




        source = getIntent().getStringExtra("source");


        customer_name = getIntent().getStringExtra("customerName");
        customer_id = getIntent().getStringExtra("customer_id");


        if (!source.equals("auto"))
            customer_id = "0";




        getData = (ArrayList<BagColorQuantity>) getIntent().getSerializableExtra("recValue");


        Button printBtn = (Button) findViewById(R.id.printBtn);

        int bag_id_temp[] = new int[getData.size()];
        for (int i = 0; i < getData.size(); i++) {
            bag_id_temp[i] = getData.get(i).getBag_id();
        }
        bag_ids=bag_id_temp;


        Gson gson = new Gson();
        String bag_id_code = gson.toJson(bag_ids);


        FunctionsThread t = new FunctionsThread(OrderDisplayActivity.this);
        t.execute("AddOrderTemp", customer_id, bag_id_code, source);
        t.trigAsyncResponse(OrderDisplayActivity.this);





        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDisplayActivity.this, PrintDemo.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("PrintValue", print);



                //TEMPORARY SOLUTION>....................................Function needs to be in printdemo activity
                startActivity(intent);

                                /*
                                                String msg = "";

                                                msg += "Cus Name: " + customer_name + "\n\n";
                                                msg += "SN| Products| Rate| Qty| T.Price\n";
                                                msg += "--------------------------------\n";
                                                for (int i = 0; i < print.size(); i++) {

                                                    String SNspace = new String();
                                                    String PROspace = new String();
                                                    String RATspace = new String();
                                                    String QTYspace = new String();
                                                    String TOTspace = new String();


                                                    //Right Align Serial NO.
                                                    int SnColLenght = 2;
                                                    int SnLength = String.valueOf(i).length();
                                                    for (int k = 1; k <= (SnColLenght - SnLength); k++) {
                                                        SNspace += " ";
                                                    }

                                                    int ProColLength = 10;
                                                    int prolength = print.get(i).getProduct().length();
                                                    for (int k = 1; k <= (ProColLength - prolength); k++) {
                                                        PROspace += " ";
                                                    }
                                                    String productName=PROspace+print.get(i).getProduct();
                                                    LinkedHashMap<String, Integer> finalColorMap = print.get(i).getColorQuantity();

                                                    int qtyTotal = 0;


                                                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {

                                                        qtyTotal += entry.getValue();
                                                        int pos = new ArrayList<String>(finalColorMap.keySet()).indexOf(entry.getKey());



                                                            productName+="\n"+getSpace(SnColLenght,0)+getSpace(ProColLength,entry.getKey().toString().length()+4)+"("+entry.getKey()+"-"+entry.getValue()+")";


                                                    }
                                                    int Tprice;
                                                    Tprice = qtyTotal * print.get(i).getPrice();





                                                    //Right Align Products

                                                    //Right Align Rate
                                                    int RatColLength = 6;
                                                    int RatLength = String.valueOf(print.get(i).getPrice()).length();
                                                    for (int k = 1; k <= (RatColLength - RatLength); k++) {
                                                        RATspace += " ";
                                                    }
                                                    //Right Align Qty
                                                    int QtyColLength = 5;
                                                    int QtyLength = String.valueOf(qtyTotal).length();
                                                    for (int k = 1; k <= (QtyColLength - QtyLength); k++) {
                                                        QTYspace += " ";
                                                    }
                                                    //Right Align Total
                                                    int TotColLength = 9;
                                                    int TotLength = String.valueOf(Tprice).length();
                                                    for (int k = 1; k <= (TotColLength - TotLength); k++) {
                                                        TOTspace += " ";
                                                    }
                                                    //Stores above value in msg to print



                                                    msg += SNspace + (i + 1)  + productName + RATspace + print.get(i).getPrice() + QTYspace + qtyTotal
                                                            + TOTspace + Tprice + "\n";
                                                    String space="";
                                                    for(int m=0;m<32;m++)
                                                        space+=" ";
                                                    msg+=space+"\n";


                                                }
                                                msg += "--------------------------------\n";

                                                double Total = 0;
                                                for (int i = 0; i < print.size(); i++) {
                                                    LinkedHashMap<String, Integer> finalColorMap = print.get(i).getColorQuantity();

                                                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {

                                                        Total += print.get(i).getPrice() * entry.getValue();
                                                    }

                                                }
                                                int totlength = String.valueOf(Total).length();
                                                for (int j = 1; j <= (32 - totlength - 7); j++) {
                                                    msg += " ";
                                                }
                                                msg += "Total" + ": " + Total + "\n";

                                                double Vat = 0;
                                                Vat = 0.13 * Total;
                                                int vatlength = String.valueOf(Vat).length();
                                                int diff = totlength - vatlength;
                                                for (int j = 1; j <= (32 - vatlength - 9 - diff); j++) {
                                                    msg += " ";
                                                }

                                                String vatspace = new String();
                                                for (int j = 1; j <= diff; j++) {

                                                    vatspace += " ";
                                                }
                                                msg += "13% Vat" + ": " + vatspace + Vat + "\n";

                                                double Gtotal = 0;
                                                Gtotal = Total + Vat;
                                                int Gtotlength = String.valueOf(Gtotal).length();
                                                for (int j = 1; j <= (32 - Gtotlength - 9); j++) {
                                                    msg += " ";
                                                }
                                                msg += "G.Total" + ": " + Gtotal + "\n\n";

                                                System.out.println(msg);

                                */


            }

        });


    }


    @Override
    public void onComplete(String response) {


        if(response.equals("Error"))
            Toast.makeText(this,"Error has been occured. Please Try again Later!",Toast.LENGTH_SHORT).show();

        try{
        JSONObject bag_nameJson = new JSONObject(response);
        JSONArray bag_nameJsonArray = bag_nameJson.getJSONArray("result");

        if (!source.equals("manual")) {
            JSONObject customer_nameJson = new JSONObject(response);
            customer_name = customer_nameJson.getString("customer_name");
        }

        for (int i = 0; i < bag_nameJsonArray.length(); i++) {
            JSONObject jsonObject = bag_nameJsonArray.getJSONObject(i);

            PrintEntity printentity = new PrintEntity();
            printentity.setBag_id(bag_ids[i]);
            printentity.setProduct(jsonObject.getString("bag_name"));
            printentity.setPrice(Integer.parseInt(jsonObject.getString("bag_price")));
            printentity.setColorQuantity(getData.get(i).getQuantityColor());

            print.add(printentity);

        }





    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
    tableLayout.removeAllViews();
    TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
    tableLayout.setBackgroundColor(Color.WHITE);


    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
    tableRowParams.setMargins(3, 3, 3, 3);
    tableRowParams.weight = 1;

    int sn = 0;
    TableRow tableRow1 = new TableRow(this);
    tableRow1.setBackgroundColor(Color.parseColor("#0087e2"));

    TextView cNameL = new TextView(this);
    cNameL.setBackgroundColor(Color.parseColor("#00000000"));
    cNameL.setTextColor(Color.parseColor("#FFFFFF"));
    cNameL.setPadding(6, 6, 6, 6);
    cNameL.setGravity(Gravity.LEFT);
    cNameL.setText("Customer Name: " + customer_name);
    TableRow.LayoutParams params = new TableRow.LayoutParams();
    params.span = 5; //amount of columns you will span
    cNameL.setLayoutParams(params);
    tableRow1.addView(cNameL);
    tableLayout.addView(tableRow1, tableRowParams);


    TableRow tableRow2 = new TableRow(this);
    tableRow2.setBackgroundColor(Color.parseColor("#0087e2"));

    TextView dateN = new TextView(this);
    dateN.setBackgroundColor(Color.parseColor("#00000000"));
    dateN.setTextColor(Color.parseColor("#FFFFFF"));
    dateN.setGravity(Gravity.LEFT);
    dateN.setPadding(6, 6, 6, 6);
    dateN.setText("Date: " + format.format(Calendar.getInstance().getTime()));
    dateN.setLayoutParams(params);
    tableRow2.addView(dateN);
    tableLayout.addView(tableRow2, tableRowParams);


    TableRow tableRow3 = new TableRow(this);
    tableRow3.setBackgroundColor(Color.parseColor("#FFFFFF"));

    TextView snN = new TextView(this);
    snN.setBackgroundColor(Color.parseColor("#ff6600"));
    snN.setTextColor(Color.parseColor("#FFFFFF"));
    snN.setPadding(6, 6, 6, 6);
    snN.setGravity(Gravity.CENTER);
    snN.setText("S.N");
    tableRow3.addView(snN, tableRowParams);


    TextView product = new TextView(this);
    product.setBackgroundColor(Color.parseColor("#ff6600"));
    product.setTextColor(Color.parseColor("#FFFFFF"));
    product.setPadding(16, 6, 16, 6);
    product.setGravity(Gravity.CENTER);
    product.setText("Product");
    tableRow3.addView(product, tableRowParams);


    TextView color = new TextView(this);
    color.setBackgroundColor(Color.parseColor("#ff6600"));
    color.setTextColor(Color.parseColor("#FFFFFF"));
    color.setPadding(16, 6, 16, 6);
    color.setGravity(Gravity.CENTER);
    color.setText("Color");
    tableRow3.addView(color, tableRowParams);

    TextView quantity = new TextView(this);
    quantity.setBackgroundColor(Color.parseColor("#ff6600"));
    quantity.setTextColor(Color.parseColor("#FFFFFF"));
    quantity.setPadding(16, 6, 16, 6);
    quantity.setGravity(Gravity.CENTER);
    quantity.setText("QTY");
    tableRow3.addView(quantity, tableRowParams);

    TextView price = new TextView(this);
    price.setBackgroundColor(Color.parseColor("#ff6600"));
    price.setTextColor(Color.parseColor("#FFFFFF"));
    price.setPadding(16, 6, 16, 6);
    price.setGravity(Gravity.CENTER);
    price.setText("Price");
    tableRow3.addView(price, tableRowParams);

    TextView total = new TextView(this);
    total.setBackgroundColor(Color.parseColor("#ff6600"));
    total.setTextColor(Color.parseColor("#FFFFFF"));
    total.setPadding(16, 6, 16, 6);
    total.setGravity(Gravity.CENTER);
    total.setText("Total");
    tableRow3.addView(total, tableRowParams);


    tableLayout.addView(tableRow3, tableLayoutParams);


    double valTotal = 0.0;
    for (int i = 0; i < bag_ids.length; i++) {

        LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getQuantityColor();

        for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {
            sn += 1;


            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);

            TextView snNs = new TextView(this);
            snNs.setBackgroundColor(Color.parseColor("#a9a7a5"));
            snNs.setTextColor(Color.parseColor("#FFFFFF"));
            snNs.setPadding(6, 6, 6, 6);
            snNs.setGravity(Gravity.CENTER);
            snNs.setText(String.valueOf(sn));
            tableRow.addView(snNs, tableRowParams);


            TextView products = new TextView(this);
            products.setBackgroundColor(Color.parseColor("#a9a7a5"));
            products.setTextColor(Color.parseColor("#FFFFFF"));
            products.setPadding(6, 6, 6, 6);
            products.setGravity(Gravity.CENTER);
            products.setText(print.get(i).getProduct());
            tableRow.addView(products, tableRowParams);


            TextView colors = new TextView(this);
            colors.setBackgroundColor(Color.parseColor("#a9a7a5"));
            colors.setTextColor(Color.parseColor("#FFFFFF"));
            colors.setPadding(6, 6, 6, 6);
            colors.setGravity(Gravity.CENTER);
            colors.setText(entry.getKey().toString());
            tableRow.addView(colors, tableRowParams);

            TextView quantitys = new TextView(this);
            quantitys.setBackgroundColor(Color.parseColor("#a9a7a5"));
            quantitys.setTextColor(Color.parseColor("#FFFFFF"));
            quantitys.setPadding(6, 6, 6, 6);
            quantitys.setGravity(Gravity.CENTER);
            quantitys.setText(String.valueOf(entry.getValue().toString()));
            tableRow.addView(quantitys, tableRowParams);

            TextView prices = new TextView(this);
            prices.setBackgroundColor(Color.parseColor("#a9a7a5"));
            prices.setTextColor(Color.parseColor("#FFFFFF"));
            prices.setPadding(6, 6, 6, 6);
            prices.setGravity(Gravity.CENTER);
            prices.setText(String.valueOf(print.get(i).getPrice()));
            tableRow.addView(prices, tableRowParams);

            TextView totals = new TextView(this);
            totals.setBackgroundColor(Color.parseColor("#a9a7a5"));
            totals.setTextColor(Color.parseColor("#FFFFFF"));
            totals.setPadding(6, 6, 6, 6);
            totals.setGravity(Gravity.CENTER);
            totals.setText(Integer.toString(entry.getValue() * print.get(i).getPrice()));
            tableRow.addView(totals, tableRowParams);


            tableLayout.addView(tableRow, tableLayoutParams);

            valTotal += entry.getValue() * print.get(i).getPrice();

        }


    }

    TableRow tableRowlast = new TableRow(this);
    tableRowlast.setBackgroundColor(Color.parseColor("#FFFFFF"));

    TextView gtotals = new TextView(this);
    gtotals.setBackgroundColor(Color.parseColor("#00d857"));
    gtotals.setTextColor(Color.parseColor("#FFFFFF"));
    gtotals.setPadding(6, 6, 6, 6);
    gtotals.setGravity(Gravity.RIGHT);
    gtotals.setText("Total: Rs " + valTotal);
    params.span = 6;
    gtotals.setLayoutParams(params);
    tableRowlast.addView(gtotals);

    tableLayout.addView(tableRowlast, tableLayoutParams);

        } catch (JSONException e) {
            e.printStackTrace();
        }
}

}
