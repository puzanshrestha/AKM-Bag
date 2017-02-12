package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StockUpdateActivity extends Activity {

    String array_spinner[];
    Spinner colorCombo;
    TableLayout tableLayout;
    ArrayList<ColorQuantityEntity> colorValues;
    String bag_id="";
    EditText quantity;
    Button updateStockBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_update);
        colorCombo = (Spinner) findViewById(R.id.colorCombo);
        quantity=(EditText)findViewById(R.id.cqty);
        updateStockBtn = (Button) findViewById(R.id.updateStockBtn);


        bag_id = getIntent().getStringExtra("bag_id");

        populateColor();

        populateTableLayout();



        colorCombo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),colorCombo.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });








    }



    public void populateTableLayout() {

        colorValues = new ArrayList<>();

        try {
            String response = new FunctionsThread(this).execute("ViewStockInformation", bag_id).get();
            System.out.println(response);
            JSONObject stockJson = new JSONObject(response);
            JSONArray stockJsonArray = stockJson.getJSONArray("result");
            for (int i = 0; i < stockJsonArray.length(); i++) {
                JSONObject jObject = stockJsonArray.getJSONObject(i);
                ColorQuantityEntity cqe = new ColorQuantityEntity();
                cqe.setColor(jObject.getString("color"));
                cqe.setCquantity(Integer.valueOf(jObject.getString("quantityColor")));
                colorValues.add(cqe);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int bigText=14;

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        tableLayout.removeAllViews();
        TableRow tr1 = new TableRow(this);
        TableRow.LayoutParams tb1c = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1c.gravity = Gravity.CENTER;
        tr1.setLayoutParams(tb1c);

        TextView color = new TextView(this);
        color.setPadding(6, 6, 6, 6);
        color.setText("Colors ");
        color.setTextSize(bigText);
        color.setSingleLine();
        TableRow.LayoutParams lnc = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnc.gravity = Gravity.CENTER;
        color.setLayoutParams(lnc);
        tr1.addView(color);



        TextView qty = new TextView(this);
        qty.setPadding(6, 6, 6, 6);
        qty.setText(" Quantity");
        qty.setTextSize(bigText);
        qty.setSingleLine();
        TableRow.LayoutParams lnq = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnq.gravity = Gravity.CENTER;
        qty.setLayoutParams(lnq);
        tr1.addView(qty);

        tableLayout.addView(tr1);
        newHorizontalLine();

        int total=0;
        for(int i=0;i<colorValues.size();i++)
        {
            TableRow tr1s = new TableRow(this);
            TableRow.LayoutParams tb1cs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            tb1cs.gravity = Gravity.CENTER;
            tr1s.setLayoutParams(tb1c);

            TextView colors = new TextView(this);
            colors.setPadding(6, 6, 6, 6);
            colors.setText(colorValues.get(i).getColor());
            colors.setTextSize(bigText);
            colors.setSingleLine();
            TableRow.LayoutParams lncs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lncs.gravity = Gravity.CENTER;
            colors.setLayoutParams(lncs);
            tr1s.addView(colors);


            total+=colorValues.get(i).getCquantity();

            TextView qtys = new TextView(this);
            qtys.setPadding(6, 6, 6, 6);
            qtys.setText(Integer.toString(colorValues.get(i).getCquantity()));
            qtys.setTextSize(bigText);
            qtys.setSingleLine();
            TableRow.LayoutParams lnqs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lnqs.gravity = Gravity.CENTER;
            qtys.setLayoutParams(lnqs);
            tr1s.addView(qtys);

            tableLayout.addView(tr1s);
        }

        newHorizontalLine();

        TableRow tr1s = new TableRow(this);
        TableRow.LayoutParams tb1cs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1cs.gravity = Gravity.CENTER;
        tr1s.setLayoutParams(tb1c);

        TextView colors = new TextView(this);
        colors.setPadding(6, 6, 6, 6);
        colors.setText("Total: ");
        colors.setTextSize(bigText);
        colors.setSingleLine();
        TableRow.LayoutParams lncs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lncs.gravity = Gravity.RIGHT;
        colors.setLayoutParams(lncs);
        tr1s.addView(colors);



        TextView qtys = new TextView(this);
        qtys.setPadding(6, 6, 6, 6);
        qtys.setText(Integer.toString(total));
        qtys.setTextSize(bigText);
        qtys.setSingleLine();
        TableRow.LayoutParams lnqs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnqs.gravity = Gravity.CENTER;
        qtys.setLayoutParams(lnqs);
        tr1s.addView(qtys);

        tableLayout.addView(tr1s);






    }

    public void populateColor()
    {
        array_spinner=new String[5];
        array_spinner[0]="RED";
        array_spinner[1]="GREEN";
        array_spinner[2]="BLACK";
        array_spinner[3]="BLUE";
        array_spinner[4]="MAROON";
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, array_spinner);
        colorCombo.setAdapter(adapter);
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
        updateStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String response = new FunctionsThread(getBaseContext()).execute("UpdateStockInformation",bag_id,colorCombo.getSelectedItem().toString(),quantity.getText().toString()).get();
                    populateTableLayout();
                        Toast.makeText(getBaseContext(),"Stock has been updated",Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
