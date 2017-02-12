package com.example.pujan.bag.transactionalReports;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
        int bigText=12;


        String date="";
        for(int i=0;i<bagReport.size();i++) {


            if(!date.equals(sdf.format(bagReport.get(i).getDate()).toString())){

                tr1 = new TableRow(this);
                TableRow.LayoutParams tb1c = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1c.gravity = Gravity.CENTER;
                tr1.setLayoutParams(tb1c);

                TextView CusName = new TextView(this);
                CusName.setPadding(6, 6, 6, 6);
                CusName.setText("Customer Name: "+bagReport.get(i).getCustomer_name().toString());
                CusName.setTextSize(bigText);
                CusName.setSingleLine();
                TableRow.LayoutParams lnc = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnc.gravity = Gravity.LEFT;
                CusName.setLayoutParams(lnc);
                tr1.addView(CusName);
                tableLayout.addView(tr1);


                TableRow tr2 = new TableRow(this);
                TableRow.LayoutParams tb1b2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1b2.gravity = Gravity.CENTER;
                tr2.setLayoutParams(tb1b2);

                TextView Date = new TextView(this);
                Date.setPadding(6, 6, 6, 6);
                Date.setText("Date: "+sdf.format(bagReport.get(i).getDate()).toString());
                Date.setTextSize(bigText);
                Date.setSingleLine();
                TableRow.LayoutParams ln = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                ln.gravity = Gravity.LEFT;
                Date.setLayoutParams(ln);
                tr2.addView(Date);
                tableLayout.addView(tr2);

                newHorizontalLine();

                TableRow tr3 = new TableRow(this);
                TableRow.LayoutParams tb1d = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1d.gravity = Gravity.LEFT;
                tr3.setLayoutParams(tb1d);

                TextView bagName  = new TextView(this);
                bagName.setPadding(6, 6, 6, 6);
                bagName.setText("Bag Name");
                bagName.setTextSize(15);
                bagName.setSingleLine();
                TableRow.LayoutParams lnb = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnb.gravity = Gravity.LEFT;
                bagName.setLayoutParams(lnb);
                tr3.addView(bagName);

                TextView quantity  = new TextView(this);
                quantity.setPadding(6, 6, 6, 6);
                quantity.setText("Quantity");
                quantity.setTextSize(15);
                quantity.setSingleLine();
                TableRow.LayoutParams lnq = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnq.gravity = Gravity.LEFT;
                quantity.setLayoutParams(lnq);
                tr3.addView(quantity);


                tableLayout.addView(tr3);

                newHorizontalLine();


            }



            TableRow tr = new TableRow(this);
            TableRow.LayoutParams tbin = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            tbin.gravity = Gravity.CENTER;
            tr.setLayoutParams(tbin);


            TextView bagNames = new TextView(this);
            bagNames.setPadding(6, 6, 6, 6);
            bagNames.setText(bagReport.get(i).getBag_name());
            bagNames.setTextSize(15);
            bagNames.setSingleLine();

            TableRow.LayoutParams ilbs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            ilbs.gravity = Gravity.LEFT;
            bagNames.setLayoutParams(ilbs);

            tr.addView(bagNames);



            TextView quantitys  = new TextView(this);
            quantitys.setPadding(6, 6, 6, 6);
            quantitys.setText(Integer.toString(bagReport.get(i).getQuantity()));
            quantitys.setTextSize(15);
            quantitys.setSingleLine();

            TableRow.LayoutParams lnqs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lnqs.gravity = Gravity.CENTER;
            quantitys.setLayoutParams(lnqs);

            tr.addView(quantitys);



            tableLayout.addView(tr);


            date=sdf.format(bagReport.get(i).getDate()).toString();

            int x=i+1;
            if(x<bagReport.size()) {
                if (!date.equals(sdf.format(bagReport.get(x).getDate()).toString())) {

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
