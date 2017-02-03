package com.example.pujan.bag.customerDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ViewCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_table_layout);


        TableRow tr, tr1;
        TextView id, name, address, phone;
        TextView ids, names, addresses, phones;
        TableLayout tableLayout;
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        tr = new TableRow(this);
        TableRow.LayoutParams tb1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1.gravity = Gravity.CENTER;
        tr.setLayoutParams(tb1);

        id = new TextView(this);
        id.setPadding(6, 6, 6, 6);
        id.setText("Id");
        id.setTextSize(15);
        id.setSingleLine();
        TableRow.LayoutParams il = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        il.gravity = Gravity.CENTER;
        id.setLayoutParams(il);
        tr.addView(id);


        name = new TextView(this);
        name.setPadding(6, 6, 6, 6);
        name.setText("Name");
        name.setTextSize(15);
        name.setSingleLine();
        TableRow.LayoutParams ln = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        ln.gravity = Gravity.CENTER;
        name.setLayoutParams(ln);
        tr.addView(name);


        address = new TextView(this);
        address.setPadding(6, 6, 6, 6);
        address.setText("address");
        address.setTextSize(15);
        address.setSingleLine();
        TableRow.LayoutParams pl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        pl.gravity = Gravity.CENTER;
        address.setLayoutParams(pl);
        tr.addView(address);


        phone = new TextView(this);
        phone.setPadding(6, 6, 6, 6);
        phone.setText("Category");
        phone.setTextSize(15);
        phone.setSingleLine();
        TableRow.LayoutParams tl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tl.gravity = Gravity.CENTER;
        phone.setLayoutParams(tl);
        tr.addView(phone);


        tableLayout.addView(tr);


        try {

            String response = new FunctionsThread(this).execute("ViewCustomer").get();
            System.out.println(response);
            JSONObject customerJson = new JSONObject(response);
            JSONArray customerJsonArray = customerJson.getJSONArray("result");

            for (int i = 0; i < customerJsonArray.length(); i++) {
                JSONObject jObject = customerJsonArray.getJSONObject(i);

                String getId = jObject.getString("customer_id");
                String getName = jObject.getString("customer_name");
                String getAddress = jObject.getString("customer_address");
                String getPhone = jObject.getString("customer_phone");


                tr1 = new TableRow(this);
                TableRow.LayoutParams tb = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                tb.gravity = Gravity.CENTER;
                tr1.setLayoutParams(tb);


                ids = new TextView(this);
                ids.setPadding(6, 6, 6, 6);
                ids.setText(getId);
                ids.setTextSize(15);
                ids.setSingleLine();
                TableRow.LayoutParams ils = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                ils.gravity = Gravity.CENTER;
                ids.setLayoutParams(ils);


                names = new TextView(this);
                names.setPadding(6, 6, 6, 6);
                names.setText(getName);
                names.setTextSize(15);
                names.setSingleLine();
                TableRow.LayoutParams nls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                nls.gravity = Gravity.CENTER;
                names.setLayoutParams(nls);

                addresses = new TextView(this);
                addresses.setPadding(6, 6, 6, 6);
                addresses.setText(getAddress);
                addresses.setTextSize(15);
                addresses.setSingleLine();
                TableRow.LayoutParams tls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tls.gravity = Gravity.CENTER;
                addresses.setLayoutParams(tls);

                phones = new TextView(this);
                phones.setPadding(6, 6, 6, 6);
                phones.setText(getPhone);
                phones.setTextSize(15);
                phones.setSingleLine();
                TableRow.LayoutParams pls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                pls.gravity = Gravity.CENTER;
                phones.setLayoutParams(pls);

                tr1.addView(ids);
                tr1.addView(names);
                tr1.addView(addresses);
                tr1.addView(phones);

                tableLayout.addView(tr1);


            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

}
