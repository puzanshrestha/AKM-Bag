package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.printPackage.PrintEntity;

import org.json.JSONArray;
import org.json.JSONObject;

public class StockViewActivity extends Activity implements FunctionsThread.AsyncResponse {

    EditText queryQuantityEdt;
    TextView queryQuantityBtn;
    TableLayout queryTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_view);

        queryQuantityEdt =(EditText)findViewById(R.id.queryQuantity);
        queryQuantityBtn=(TextView)findViewById(R.id.queryQuantityBtn);
        queryTable=(TableLayout)findViewById(R.id.queryTable);


        queryQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              FunctionsThread t = new FunctionsThread(StockViewActivity.this);
                t.execute("QueryQuantity",queryQuantityEdt.getText().toString());
                t.trigAsyncResponse(StockViewActivity.this);


            }

        });

    }

    @Override
    public void onComplete(String response) {
        queryTable.removeAllViews();
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        queryTable.setBackgroundColor(Color.WHITE);


        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(3, 3, 3, 3);
        tableRowParams.weight = 1;

        TableRow tableRow1 = new TableRow(this);
        tableRow1.setBackgroundColor(Color.parseColor("#0087e2"));


        TextView color = new TextView(this);
        color.setBackgroundColor(Color.parseColor("#ff6600"));
        color.setTextColor(Color.parseColor("#FFFFFF"));
        color.setPadding(16,6,16,6);
        color.setGravity(Gravity.CENTER);
        color.setText("Product");
        tableRow1.addView(color,tableRowParams);

        TextView quantityV = new TextView(this);
        quantityV.setBackgroundColor(Color.parseColor("#ff6600"));
        quantityV.setTextColor(Color.parseColor("#FFFFFF"));
        quantityV.setPadding(16,6,16,6);
        quantityV.setGravity(Gravity.CENTER);
        quantityV.setText("Quantity");
        tableRow1.addView(quantityV,tableRowParams);

        queryTable.addView(tableRow1,tableLayoutParams);

        try {

            JSONObject queryQuantity = new JSONObject(response);
            JSONArray queryQuantityJsonArray = queryQuantity.getJSONArray("response");
            for (int i = 0; i < queryQuantityJsonArray.length(); i++) {
                JSONObject jsonObject = queryQuantityJsonArray.getJSONObject(i);

                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.parseColor("#0087e2"));


                TextView product = new TextView(this);
                product.setBackgroundColor(Color.parseColor("#006000"));
                product.setTextColor(Color.parseColor("#FFFFFF"));
                product.setPadding(16,6,16,6);
                product.setGravity(Gravity.CENTER);
                product.setText(jsonObject.getString("bag_name"));
                tableRow.addView(product,tableRowParams);

                TextView quantitys = new TextView(this);
                quantitys.setBackgroundColor(Color.parseColor("#006000"));
                quantitys.setTextColor(Color.parseColor("#FFFFFF"));
                quantitys.setPadding(16,6,16,6);
                quantitys.setGravity(Gravity.CENTER);
                quantitys.setText(jsonObject.getString("bag_quantity"));
                tableRow.addView(quantitys,tableRowParams);

                queryTable.addView(tableRow,tableLayoutParams);



            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }




    }
}
