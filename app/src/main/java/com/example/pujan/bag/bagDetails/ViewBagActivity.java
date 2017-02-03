package com.example.pujan.bag.bagDetails;

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

public class ViewBagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_table_layout);
        TableRow tr,tr1;
        TextView id,name,type,price,company;
        TextView ids,names,types,prices,companies;
        TableLayout tableLayout;
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);

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


        price = new TextView(this);
        price.setPadding(6, 6, 6, 6);
        price.setText("Price");
        price.setTextSize(15);
        price.setSingleLine();
        TableRow.LayoutParams pl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        pl.gravity = Gravity.CENTER;
        price.setLayoutParams(pl);
        tr.addView(price);


        type = new TextView(this);
        type.setPadding(6, 6, 6, 6);
        type.setText("Category");
        type.setTextSize(15);
        type.setSingleLine();
        TableRow.LayoutParams tl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tl.gravity = Gravity.CENTER;
        type.setLayoutParams(tl);
        tr.addView(type);


        company = new TextView(this);
        company.setPadding(6, 6, 6, 6);
        company.setText("Company");
        company.setTextSize(15);
        company.setSingleLine();
        TableRow.LayoutParams cl = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        cl.gravity = Gravity.CENTER;
        company.setLayoutParams(cl);
        tr.addView(company);


        tableLayout.addView(tr);


        try {
            String response = new FunctionsThread().execute("ViewBag").get();
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");

            for(int i=0;i<bagJsonArray.length();i++)
            {
                JSONObject jObject = bagJsonArray.getJSONObject(i);

                String getId = jObject.getString("bag_id");
                String getName = jObject.getString("bag_name");
                String getType = jObject.getString("bag_category");
                String getPrice = jObject.getString("bag_price");
                String getCompany = jObject.getString("bag_company");

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
                names.setPadding(6,6,6,6);
                names.setText(getName);
                names.setTextSize(15);
                names.setSingleLine();
                TableRow.LayoutParams nls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                nls.gravity = Gravity.CENTER;
                names.setLayoutParams(nls);

                types = new TextView(this);
                types.setPadding(6,6,6,6);
                types.setText(getType);
                types.setTextSize(15);
                types.setSingleLine();
                TableRow.LayoutParams tls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tls.gravity = Gravity.CENTER;
                types.setLayoutParams(tls);

                prices = new TextView(this);
                prices.setPadding(6,6,6,6);
                prices.setText(getPrice);
                prices.setTextSize(15);
                prices.setSingleLine();
                TableRow.LayoutParams pls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                pls.gravity = Gravity.CENTER;
                prices.setLayoutParams(pls);

                companies = new TextView(this);
                companies.setPadding(6,6,6,6);
                companies.setText(getCompany);
                companies.setTextSize(15);
                companies.setSingleLine();
                TableRow.LayoutParams cls = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                cls.gravity = Gravity.CENTER;
                companies.setLayoutParams(cls);

                tr1.addView(ids);
                tr1.addView(names);
                tr1.addView(types);
                tr1.addView(prices);
                tr1.addView(companies);

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
