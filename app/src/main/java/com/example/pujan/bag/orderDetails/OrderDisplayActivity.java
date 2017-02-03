package com.example.pujan.bag.orderDetails;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.printPackage.DeviceListActivity;
import com.example.pujan.bag.printPackage.PrintDemo;
import com.example.pujan.bag.printPackage.PrintEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class OrderDisplayActivity extends Activity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);
        final ArrayList<PrintEntity> print=new ArrayList<>();
        final int customer_id=Integer.valueOf(getIntent().getStringExtra("cid"));
        String customer_name="";
        final int[] bag_id = getIntent().getIntArrayExtra("bid");
        final int[] quantity = getIntent().getIntArrayExtra("quantity");
        final int [] bag_price=new int[bag_id.length];
        final String [] bag_name=new String[bag_id.length];


        Button printBtn = (Button)findViewById(R.id.printBtn);

        String customer_id_code=Integer.toString(customer_id);
        String bag_id_code="";
        String quantity_code="";

        for(int i=0;i<bag_id.length;i++)
        {
            bag_id_code = Integer.toString(bag_id[bag_id.length-i-1])+"#"+bag_id_code;
            quantity_code =Integer.toString(quantity[bag_id.length-i-1])+"#"+quantity_code;
        }
        System.out.println(bag_id_code);
        System.out.println(quantity_code);

        try {

            String response = new FunctionsThread(this).execute("AddOrderTemp",customer_id_code,bag_id_code,quantity_code).get();
            System.out.println(response);
            JSONObject bag_nameJson = new JSONObject(response);
            JSONArray bag_nameJsonArray = bag_nameJson.getJSONArray("result");

            JSONObject customer_nameJson = new JSONObject(response);
            customer_name=customer_nameJson.getString("customer_name");
            System.out.println(customer_name);


            for(int i=0;i<bag_nameJsonArray.length();i++)
            {
                JSONObject jsonObjectName = bag_nameJsonArray.getJSONObject(i);

                bag_name[i]=jsonObjectName.getString("bag_name");
                bag_price[i]=Integer.valueOf(jsonObjectName.getString("bag_price"));
                System.out.println(bag_name[i]);
            }





        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TableRow tr, tr1, trc;
        TextView sid, CusName, BagName,Rate, Qty;
        TextView sids, BagNames, Rates, Qtys;

        TableLayout tableLayout;
        tableLayout = (TableLayout) findViewById(R.id.ordertable);

        tr = new TableRow(this);
        trc = new TableRow(this);

        TableRow.LayoutParams tb1c = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1c.gravity = Gravity.CENTER;
        trc.setLayoutParams(tb1c);

        CusName = new TextView(this);
        CusName.setPadding(6, 6, 6, 6);
        CusName.setText("Cus Name");
        CusName.setTextSize(15);
        CusName.setSingleLine();
        TableRow.LayoutParams ln = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        ln.gravity = Gravity.CENTER;
        CusName.setLayoutParams(ln);
        trc.addView(CusName);

        CusName = new TextView(this);
        CusName.setPadding(6, 6, 6, 6);
        CusName.setText(customer_name);
        CusName.setTextSize(15);
        CusName.setSingleLine();
        TableRow.LayoutParams lnn = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnn.gravity = Gravity.CENTER;
        CusName.setLayoutParams(lnn);
        trc.addView(CusName);
        tableLayout.addView(trc);


        TableRow.LayoutParams tb1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1.gravity = Gravity.CENTER;
        tr.setLayoutParams(tb1);

        sid = new TextView(this);
        sid.setPadding(6, 6, 6, 6);
        sid.setText("SN");
        sid.setTextSize(15);
        sid.setSingleLine();
        TableRow.LayoutParams il = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        il.gravity = Gravity.CENTER;
        sid.setLayoutParams(il);
        tr.addView(sid);

        BagName = new TextView(this);
        BagName.setPadding(6, 6, 6, 6);
        BagName.setText("Product ID");
        BagName.setTextSize(15);
        BagName.setSingleLine();
        TableRow.LayoutParams lnb = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnb.gravity = Gravity.CENTER;
        CusName.setLayoutParams(lnb);
        tr.addView(BagName);


        Rate = new TextView(this);
        Rate.setPadding(6, 6, 6, 6);
        Rate.setText("Bag price");
        Rate.setTextSize(15);
        Rate.setSingleLine();
        TableRow.LayoutParams pl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        pl.gravity = Gravity.CENTER;
        Rate.setLayoutParams(pl);
        tr.addView(Rate);


        Qty = new TextView(this);
        Qty.setPadding(6, 6, 6, 6);
        Qty.setText("Quantity");
        Qty.setTextSize(15);
        Qty.setSingleLine();
        TableRow.LayoutParams tl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tl.gravity = Gravity.CENTER;
        Qty.setLayoutParams(tl);
        tr.addView(Qty);

        tableLayout.addView(tr);
int sn=0;
        for(int i=0;i<bag_id.length;i++)
        {


                tr1 = new TableRow(this);
                TableRow.LayoutParams tb = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                tb.gravity = Gravity.CENTER;
                tr1.setLayoutParams(tb);

            if(bag_id[i]!=0){
                sn+=1;

                PrintEntity printentity=new PrintEntity();
                printentity.setId(sn);
                printentity.setProducts(bag_name[i]);
                printentity.setPrice(bag_price[i]);
                printentity.setQuantity(quantity[i]);
                printentity.setCustomer_name(customer_name);
                print.add(printentity);


                sids = new TextView(this);
                sids.setPadding(6, 6, 6, 6);
                sids.setText(Integer.toString(sn));
                sids.setTextSize(15);
                sids.setSingleLine();
                TableRow.LayoutParams ils = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                ils.gravity = Gravity.CENTER;
                sids.setLayoutParams(ils);
                tr1.addView(sids);


                BagNames = new TextView(this);
                BagNames.setPadding(6, 6, 6, 6);
                BagNames.setText(bag_name[i]);
                BagNames.setTextSize(15);
                BagNames.setSingleLine();
                TableRow.LayoutParams lns = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lns.gravity = Gravity.CENTER;
                BagNames.setLayoutParams(lns);
                tr1.addView(BagNames);


                Rates = new TextView(this);
                Rates.setPadding(6, 6, 6, 6);
                Rates.setText(Integer.toString(bag_price[i]));
                Rates.setTextSize(15);
                Rates.setSingleLine();
                TableRow.LayoutParams pls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                pls.gravity = Gravity.CENTER;
                Rates.setLayoutParams(pls);
                tr1.addView(Rates);


                Qtys = new TextView(this);
                Qtys.setPadding(6, 6, 6, 6);
                Qtys.setText(Integer.toString(quantity[i]));
                Qtys.setTextSize(15);
                Qtys.setSingleLine();
                TableRow.LayoutParams tls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tls.gravity = Gravity.CENTER;
                Qtys.setLayoutParams(tls);
                tr1.addView(Qtys);

                tableLayout.addView(tr1);
            }
        }


        {

        };

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(),PrintDemo.class);
                i.putExtra("PrintValue",print);
                startActivity(i);

            }

        });








    }
}
