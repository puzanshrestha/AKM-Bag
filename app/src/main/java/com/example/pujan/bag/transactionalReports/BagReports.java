package com.example.pujan.bag.transactionalReports;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class BagReports extends Activity {

    EditText editTo, editFrom;
    Calendar dateTime = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    TableRow tr1;
    ArrayList<BagReportEntity> bagReport;
    TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_reports);




        editTo = (EditText)findViewById(R.id.editTo);
        editFrom=(EditText)findViewById(R.id.editFrom);

        editTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(2);


            }
        });

        editFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(1);

            }
        });

    }

    public void getDate(int select)
    {
        if(select==1)
        new DatePickerDialog(this, dFrom, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
        else
        {
            new DatePickerDialog(this, dTo, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();

        }


    }

    DatePickerDialog.OnDateSetListener dFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editFrom.setText(format.format(dateTime.getTime()));
            updateTable();


        }
    };
    DatePickerDialog.OnDateSetListener dTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editTo.setText(format.format(dateTime.getTime()));
            updateTable();

        }
    };


    public void updateTable()
    {
        bagReport= new ArrayList<>();
        try {
            String response = new FunctionsThread(this).execute("ViewRecords",editTo.getText().toString(),editFrom.getText().toString()).get();

            JSONObject recordsJson = new JSONObject(response);
            JSONArray recordsJsonArray = recordsJson.getJSONArray("result");
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            for(int i=0;i<recordsJsonArray.length();i++)
            {
                JSONObject jsonObjectName = recordsJsonArray.getJSONObject(i);

                BagReportEntity bre = new BagReportEntity();
                bre.setCustomer_name(jsonObjectName.getString("customer_name"));
                bre.setQuantity(Integer.parseInt(jsonObjectName.getString("quantity")));
                bre.setBag_name(jsonObjectName.getString("bag_name"));
                bre.setDate(formatter.parse(jsonObjectName.getString("date")));

                bagReport.add(bre);

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tableLayout = (TableLayout) findViewById(R.id.recordList);


        tableLayout.removeAllViews();

        int bigText=12;


        String date="";

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayout.setBackgroundColor(Color.WHITE);


        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(3, 3, 3, 3);
        tableRowParams.weight = 1;
        int sn=1;
        for(int i=0;i<bagReport.size();i++) {


            sn++;


            if(!date.equals(format.format(bagReport.get(i).getDate()).toString())){

                sn=1;
                TableRow tableRow1 = new TableRow(this);
                tableRow1.setBackgroundColor(Color.parseColor("#0087e2"));

                TextView cNameL = new TextView(this);
                cNameL.setBackgroundColor(Color.parseColor("#00000000"));
                cNameL.setTextColor(Color.parseColor("#FFFFFF"));
                cNameL.setPadding(6,6,6,6);
                cNameL.setGravity(Gravity.LEFT);
                cNameL.setText("Customer Name: "+bagReport.get(i).getCustomer_name().toString());
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
                dateN.setText("Date: "+format.format(bagReport.get(i).getDate()).toString());
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


                TextView quantity = new TextView(this);
                quantity.setBackgroundColor(Color.parseColor("#ff6600"));
                quantity.setTextColor(Color.parseColor("#FFFFFF"));
                quantity.setPadding(16,6,16,6);
                quantity.setGravity(Gravity.CENTER);
                quantity.setText("QTY");
                tableRow3.addView(quantity,tableRowParams);

                TextView color = new TextView(this);
                color.setBackgroundColor(Color.parseColor("#ff6600"));
                color.setTextColor(Color.parseColor("#FFFFFF"));
                color.setPadding(16,6,16,6);
                color.setGravity(Gravity.CENTER);
                color.setText("Color");
                tableRow3.addView(color,tableRowParams);

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



            }



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
            products.setText(bagReport.get(i).getBag_name().toString());
            tableRow.addView(products,tableRowParams);


            TextView quantitys = new TextView(this);
            quantitys.setBackgroundColor(Color.parseColor("#a9a7a5"));
            quantitys.setTextColor(Color.parseColor("#FFFFFF"));
            quantitys.setPadding(6,6,6,6);
            quantitys.setGravity(Gravity.CENTER);
            quantitys.setText(String.valueOf(bagReport.get(i).getQuantity()));
            tableRow.addView(quantitys,tableRowParams);

            TextView colors = new TextView(this);
            colors.setBackgroundColor(Color.parseColor("#a9a7a5"));
            colors.setTextColor(Color.parseColor("#FFFFFF"));
            colors.setPadding(6,6,6,6);
            colors.setGravity(Gravity.CENTER);
            colors.setText("Color");
            tableRow.addView(colors,tableRowParams);

            TextView prices = new TextView(this);
            prices.setBackgroundColor(Color.parseColor("#a9a7a5"));
            prices.setTextColor(Color.parseColor("#FFFFFF"));
            prices.setPadding(6,6,6,6);
            prices.setGravity(Gravity.CENTER);
            prices.setText("Price");
            tableRow.addView(prices,tableRowParams);

            TextView totals = new TextView(this);
            totals.setBackgroundColor(Color.parseColor("#a9a7a5"));
            totals.setTextColor(Color.parseColor("#FFFFFF"));
            totals.setPadding(6,6,6,6);
            totals.setGravity(Gravity.CENTER);
            totals.setText("Total");
            tableRow.addView(totals,tableRowParams);



            tableLayout.addView(tableRow, tableLayoutParams);


            date=format.format(bagReport.get(i).getDate()).toString();

            int x=i+1;
            if(x<bagReport.size()) {
                if (!date.equals(format.format(bagReport.get(x).getDate()).toString())) {

                    TableRow tableRowlast = new TableRow(this);
                    tableRowlast.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    TextView gtotals = new TextView(this);
                    gtotals.setBackgroundColor(Color.parseColor("#00d857"));
                    gtotals.setTextColor(Color.parseColor("#FFFFFF"));
                    gtotals.setPadding(6,6,6,6);
                    gtotals.setGravity(Gravity.RIGHT);
                    gtotals.setText("Total: Rs "+"542625");
                    TableRow.LayoutParams params = new TableRow.LayoutParams();
                    params.span=6;
                    gtotals.setLayoutParams(params);
                    tableRowlast.addView(gtotals);

                    tableLayout.addView(tableRowlast,tableLayoutParams);
                    lineBreak();
                    lineBreak();


                }
            }



        }




    }

    private void lineBreak() {
        TableRow trgap = new TableRow(this);
        TableRow.LayoutParams tbgap = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tbgap.gravity = Gravity.CENTER;
        trgap.setLayoutParams(tbgap);

        TextView linebreak = new TextView(this);

        linebreak.setTextSize(15);
        TableRow.LayoutParams lnbrline = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        lnbrline.gravity = Gravity.CENTER;

        linebreak.setLayoutParams(lnbrline);
        trgap.addView(linebreak);

        tableLayout.addView(trgap);

    }

    private void newHorizontalLine() {
        TableRow trgap = new TableRow(this);
        TableRow.LayoutParams tbgap = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tbgap.gravity = Gravity.CENTER;
        trgap.setLayoutParams(tbgap);

        View linebreak = new View(this);
        linebreak.setBackgroundResource(R.color.colorPrimaryDark);
        TableRow.LayoutParams lnbr = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
        lnbr.gravity = Gravity.CENTER;
        linebreak.setLayoutParams(lnbr);
        trgap.addView(linebreak);


        View linebreak2 = new View(this);
        linebreak2.setBackgroundResource(R.color.colorPrimaryDark);
        TableRow.LayoutParams lnbr2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
        lnbr2.gravity = Gravity.CENTER;
        linebreak2.setLayoutParams(lnbr2);
        trgap.addView(linebreak2);


        tableLayout.addView(trgap);
    }



}
