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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

<<<<<<< HEAD
public class OrderDisplayActivity extends Activity{
=======
public class OrderDisplayActivity extends AppCompatActivity {
>>>>>>> refs/remotes/origin/master

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.customersmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        final ArrayList<PrintEntity> print = new ArrayList<>();
        final ArrayList<AddOrderEntity> addOrderValue = new ArrayList<>();
        String customer_id = getIntent().getStringExtra("cid");
        String customer_name = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<BagColorQuantity> getData = (ArrayList<BagColorQuantity>) getIntent().getSerializableExtra("recValue");

        LinkedHashMap<String,Integer> colorQuantity=new LinkedHashMap<>();
        Button printBtn = (Button) findViewById(R.id.printBtn);

        int bag_ids[] = new int[getData.size()];
        for (int i = 0; i < getData.size(); i++)
        {
            bag_ids[i] = getData.get(i).getBag_id();


        }

        Gson gson = new Gson();
        String bag_id_code = gson.toJson(bag_ids);





        try {

            String response = new FunctionsThread(this).execute("AddOrderTemp", customer_id, bag_id_code).get();
            System.out.println(response);


            JSONObject bag_nameJson = new JSONObject(response);
            JSONArray bag_nameJsonArray = bag_nameJson.getJSONArray("result");

            JSONObject customer_nameJson = new JSONObject(response);
            customer_name = customer_nameJson.getString("customer_name");

            for (int i = 0; i < bag_nameJsonArray.length(); i++) {
                JSONObject jsonObject = bag_nameJsonArray.getJSONObject(i);
                PrintEntity printentity = new PrintEntity();
                printentity.setBag_id(bag_ids[i]);
                printentity.setProduct(jsonObject.getString("bag_name"));
                printentity.setPrice(Integer.parseInt(jsonObject.getString("bag_price")));
                printentity.setColorQuantity(getData.get(i).getQuantityColor());
                print.add(printentity);

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayout.setBackgroundColor(Color.WHITE);


        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(3, 3, 3, 3);
        tableRowParams.weight = 1;

        int sn=1;
        TableRow tableRow1 = new TableRow(this);
        tableRow1.setBackgroundColor(Color.parseColor("#0087e2"));

        TextView cNameL = new TextView(this);
        cNameL.setBackgroundColor(Color.parseColor("#00000000"));
        cNameL.setTextColor(Color.parseColor("#FFFFFF"));
        cNameL.setPadding(6,6,6,6);
        cNameL.setGravity(Gravity.LEFT);
        cNameL.setText("Customer Name: "+customer_name);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 5; //amount of columns you will span
        cNameL.setLayoutParams(params);
        tableRow1.addView(cNameL);
        tableLayout.addView(tableRow1,tableRowParams);


        TableRow tableRow2 = new TableRow(this);
        tableRow2.setBackgroundColor(Color.parseColor("#0087e2"));

        TextView dateN = new TextView(this);
        dateN.setBackgroundColor(Color.parseColor("#00000000"));
        dateN.setTextColor(Color.parseColor("#FFFFFF"));
        dateN.setGravity(Gravity.LEFT);
        dateN.setPadding(6,6,6,6);
        dateN.setText("Date: "+ format.format(Calendar.getInstance().getTime()));
        dateN.setLayoutParams(params);
        tableRow2.addView(dateN);
        tableLayout.addView(tableRow2,tableRowParams);



        TableRow tableRow3 = new TableRow(this);
        tableRow3.setBackgroundColor(Color.parseColor("#FFFFFF"));

        TextView snN = new TextView(this);
        snN.setBackgroundColor(Color.parseColor("#ff6600"));
        snN.setTextColor(Color.parseColor("#FFFFFF"));
        snN.setPadding(6,6,6,6);
        snN.setGravity(Gravity.CENTER);
        snN.setText("S.N");
        tableRow3.addView(snN,tableRowParams);


        TextView product = new TextView(this);
        product.setBackgroundColor(Color.parseColor("#ff6600"));
        product.setTextColor(Color.parseColor("#FFFFFF"));
        product.setPadding(16,6,16,6);
        product.setGravity(Gravity.CENTER);
        product.setText("Product");
        tableRow3.addView(product,tableRowParams);



        TextView color = new TextView(this);
        color.setBackgroundColor(Color.parseColor("#ff6600"));
        color.setTextColor(Color.parseColor("#FFFFFF"));
        color.setPadding(16,6,16,6);
        color.setGravity(Gravity.CENTER);
        color.setText("Color");
        tableRow3.addView(color,tableRowParams);

        TextView quantity = new TextView(this);
        quantity.setBackgroundColor(Color.parseColor("#ff6600"));
        quantity.setTextColor(Color.parseColor("#FFFFFF"));
        quantity.setPadding(16,6,16,6);
        quantity.setGravity(Gravity.CENTER);
        quantity.setText("QTY");
        tableRow3.addView(quantity,tableRowParams);

        TextView price = new TextView(this);
        price.setBackgroundColor(Color.parseColor("#ff6600"));
        price.setTextColor(Color.parseColor("#FFFFFF"));
        price.setPadding(16,6,16,6);
        price.setGravity(Gravity.CENTER);
        price.setText("Price");
        tableRow3.addView(price,tableRowParams);

        TextView total = new TextView(this);
        total.setBackgroundColor(Color.parseColor("#ff6600"));
        total.setTextColor(Color.parseColor("#FFFFFF"));
        total.setPadding(16,6,16,6);
        total.setGravity(Gravity.CENTER);
        total.setText("Total");
        tableRow3.addView(total,tableRowParams);



        tableLayout.addView(tableRow3,tableLayoutParams);

        int GTotal=0;
        for (int i = 0; i < bag_ids.length; i++) {

            LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getQuantityColor();
            AddOrderEntity addOrderEntity=new AddOrderEntity();
            addOrderEntity.setBag_id(bag_ids[i]);
            addOrderEntity.setCustomer_id(Integer.valueOf(customer_id));
            addOrderEntity.setColorQuantity(getData.get(i).getQuantityColor());
            addOrderValue.add(addOrderEntity);
            int Tprice=0;

            for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {
                sn += 1;

                Tprice=print.get(i).getPrice()*entry.getValue();

                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.WHITE);

                TextView snNs = new TextView(this);
                snNs.setBackgroundColor(Color.parseColor("#a9a7a5"));
                snNs.setTextColor(Color.parseColor("#FFFFFF"));
                snNs.setPadding(6,6,6,6);
                snNs.setGravity(Gravity.CENTER);
                snNs.setText(String.valueOf(sn));
                tableRow.addView(snNs,tableRowParams);


                TextView products = new TextView(this);
                products.setBackgroundColor(Color.parseColor("#a9a7a5"));
                products.setTextColor(Color.parseColor("#FFFFFF"));
                products.setPadding(6,6,6,6);
                products.setGravity(Gravity.CENTER);
                products.setText(print.get(i).getProduct());
                tableRow.addView(products,tableRowParams);

                TextView colors = new TextView(this);
                colors.setBackgroundColor(Color.parseColor("#a9a7a5"));
                colors.setTextColor(Color.parseColor("#FFFFFF"));
                colors.setPadding(6,6,6,6);
                colors.setGravity(Gravity.CENTER);
                colors.setText(entry.getKey().toString());
                tableRow.addView(colors,tableRowParams);

                TextView quantitys = new TextView(this);
                quantitys.setBackgroundColor(Color.parseColor("#a9a7a5"));
                quantitys.setTextColor(Color.parseColor("#FFFFFF"));
                quantitys.setPadding(6,6,6,6);
                quantitys.setGravity(Gravity.CENTER);
                quantitys.setText(String.valueOf(entry.getValue().toString()));
                tableRow.addView(quantitys,tableRowParams);

                TextView prices = new TextView(this);
                prices.setBackgroundColor(Color.parseColor("#a9a7a5"));
                prices.setTextColor(Color.parseColor("#FFFFFF"));
                prices.setPadding(6,6,6,6);
                prices.setGravity(Gravity.CENTER);
                prices.setText(String.valueOf(print.get(i).getPrice()));
                tableRow.addView(prices,tableRowParams);

                TextView totals = new TextView(this);
                totals.setBackgroundColor(Color.parseColor("#a9a7a5"));
                totals.setTextColor(Color.parseColor("#FFFFFF"));
                totals.setPadding(6,6,6,6);
                totals.setGravity(Gravity.CENTER);
                totals.setText(String.valueOf(Tprice));
                tableRow.addView(totals,tableRowParams);

                tableLayout.addView(tableRow,tableLayoutParams);

            }

            GTotal=Tprice+GTotal;


        }

        TableRow tableRowlast = new TableRow(this);
        tableRowlast.setBackgroundColor(Color.parseColor("#FFFFFF"));

        TextView gtotals = new TextView(this);
        gtotals.setBackgroundColor(Color.parseColor("#00d857"));
        gtotals.setTextColor(Color.parseColor("#FFFFFF"));
        gtotals.setPadding(6,6,6,6);
        gtotals.setGravity(Gravity.RIGHT);
        gtotals.setText("Total: Rs "+String.valueOf(GTotal));
        params.span=6;
        gtotals.setLayoutParams(params);
        tableRowlast.addView(gtotals);

        tableLayout.addView(tableRowlast,tableLayoutParams);


        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), PrintDemo.class);
                Gson gson = new Gson();
                String test = gson.toJson(print);
                i.putExtra("PrintValue", print);
                System.out.println(test);
                startActivity(i);
                //  String reply = new FunctionsThread(getBaseContext()).execute("AddOrder", test).get();
                // System.out.println(reply);


                //TEMPORARY SOLUTION>....................................Function needs to be in printdemo activity
                //startActivity(i);


            }

        });


    }
}
