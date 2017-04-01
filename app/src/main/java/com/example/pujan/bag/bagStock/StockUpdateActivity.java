package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.NDSpinner;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.database.DbHelper;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StockUpdateActivity extends Activity implements FunctionsThread.AsyncResponse{

    String array_spinner[];
    NDSpinner colorCombo;
    TableLayout tableLayout;
    ArrayList<ColorQuantityEntity> colorValues;
    String bag_id="";
    EditText quantity;
    Button updateStockBtn,editStockBtn;
    private  TextView nameEditText;
    private  TextView typeEditText;
    private  TextView priceEditText;
    private  TextView companyEditText;
    private  TextView quantityEditText;
    private  ImageView bagPhoto;
    BagEntity bagEntity;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_update);
        colorCombo = (NDSpinner) findViewById(R.id.colorCombo);
        quantity = (EditText) findViewById(R.id.cqty);
        updateStockBtn = (Button) findViewById(R.id.updateStockBtn);
        editStockBtn =(Button)findViewById(R.id.editStockBtn);

        nameEditText = (TextView) findViewById(R.id.nameEditText);
        typeEditText = (TextView) findViewById(R.id.typeEditText);
        priceEditText = (TextView) findViewById(R.id.priceEditText);
        companyEditText = (TextView) findViewById(R.id.companyEditText);
        quantityEditText = (TextView) findViewById(R.id.quantityEditText);
        bagPhoto = (ImageView) findViewById(R.id.bag_photo);


        bag_id = getIntent().getStringExtra("bag_id");

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String userType=sharedPreferences.getString("userType","");

        if(userType.equals("userType#0"))
        {
                updateStockBtn.setVisibility(View.GONE);
                updateStockBtn.setEnabled(false);
            editStockBtn.setVisibility(View.GONE);
            editStockBtn.setEnabled(false);
        }
        else
        {
            updateStockBtn.setVisibility(View.VISIBLE);
            updateStockBtn.setEnabled(true);
            editStockBtn.setVisibility(View.VISIBLE);
            editStockBtn.setEnabled(true);
        }

        populateColor();

        populateTableLayout();

               colorCombo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(), colorCombo.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        updateStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FunctionsThread tUp= new FunctionsThread(StockUpdateActivity.this);
                tUp.execute("UpdateStockInformation", bag_id,colorCombo.getSelectedItem().toString(), quantity.getText().toString());
                populateTableLayout();



            }
        });

        editStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder editStock = new AlertDialog.Builder(StockUpdateActivity.this);
                editStock.setTitle("Edit Stock");
                final TextView[] colorName = new TextView[colorValues.size()];
                final EditText[] colorsQty = new EditText[colorValues.size()];

                LinearLayout layout = new LinearLayout(StockUpdateActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(6,6,6,6);
                layout.setLayoutParams(param);
                layout.setPadding(10,10,10,10);
                layout.setBackgroundColor(Color.LTGRAY);


                for(int i=0;i<colorsQty.length;i++)
                {

                    colorName[i] = new TextView(StockUpdateActivity.this);
                    colorsQty[i]= new EditText(StockUpdateActivity.this);
                    colorsQty[i].setPadding(6,6,6,6);
                    colorsQty[i].setBackgroundColor(Color.BLUE);
                    colorsQty[i].setTextColor(Color.WHITE);
                    colorsQty[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                    colorName[i].setText(colorValues.get(i).getColor());
                    colorsQty[i].setText(String.valueOf(colorValues.get(i).getCquantity()));

                    layout.addView(colorName[i]);
                    layout.addView(colorsQty[i]);

                }


                editStock.setView(layout);

                editStock.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<ColorQuantityEntity> cqeArray = new ArrayList<ColorQuantityEntity>();
                        boolean noChange = true;
                        for(int i=0;i<colorsQty.length;i++)
                        {

                            ColorQuantityEntity cqe = new ColorQuantityEntity();
                            cqe.setColor(colorName[i].getText().toString());
                            int qty=0;
                            try {
                                qty = Integer.parseInt(colorsQty[i].getText().toString());
                            }
                            catch (Exception e)
                            {

                            }
                            cqe.setCquantity(qty);
                            cqeArray.add(cqe);

                            if(qty!=colorValues.get(i).getCquantity())
                                noChange=false;
                        }
                        if(!noChange) {
                            Gson stockEdit = new Gson();
                            String stockEditJson = stockEdit.toJson(cqeArray);
                            FunctionsThread tEdt=new FunctionsThread(StockUpdateActivity.this);
                            tEdt.execute("EditStockInformation",bag_id,stockEditJson);
                            populateTableLayout();



                        }


                    }
                });
                editStock.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                editStock.show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,StockListActivity.class);
        startActivity(i);
    }



    public void populateTableLayout() {

        FunctionsThread t= new FunctionsThread(this);
        t.execute("ViewStockInformation", bag_id);
        t.trigAsyncResponse(StockUpdateActivity.this);


    }

    public void getData(String response)
    {
        colorValues = new ArrayList<>();

        try {


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

            JSONArray bagInfoJsonArray = stockJson.getJSONArray("bagInfo");
            JSONObject jObject = bagInfoJsonArray.getJSONObject(0);
            bagEntity = new BagEntity();
            bagEntity.setId(Integer.valueOf(jObject.getString("bag_id")));
            bagEntity.setName(jObject.getString("bag_name"));
            bagEntity.setCategory(jObject.getString("bag_category"));
            bagEntity.setPrice(Integer.valueOf(jObject.getString("bag_price")));
            bagEntity.setCompany(jObject.getString("bag_company"));
            bagEntity.setQuantity(Integer.valueOf(jObject.getString("bag_quantity")));
            bagEntity.setPhoto(jObject.getString("bag_photo"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        int bigText=14;

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);


        tableLayout.removeAllViews();
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayout.setBackgroundColor(Color.WHITE);


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
        color.setText("Color");
        tableRow1.addView(color,tableRowParams);

        TextView quantityV = new TextView(this);
        quantityV.setBackgroundColor(Color.parseColor("#ff6600"));
        quantityV.setTextColor(Color.parseColor("#FFFFFF"));
        quantityV.setPadding(16,6,16,6);
        quantityV.setGravity(Gravity.CENTER);
        quantityV.setText("QTY");
        tableRow1.addView(quantityV,tableRowParams);

        tableLayout.addView(tableRow1,tableLayoutParams);


        int total = 0;
        for (int i = 0; i < colorValues.size(); i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);

            TextView colors = new TextView(this);
            colors.setBackgroundColor(Color.parseColor("#a9a7a5"));
            colors.setTextColor(Color.parseColor("#FFFFFF"));
            colors.setPadding(6,6,6,6);
            colors.setGravity(Gravity.CENTER);
            colors.setText(colorValues.get(i).getColor());
            tableRow.addView(colors,tableRowParams);

            TextView quantitys = new TextView(this);
            quantitys.setBackgroundColor(Color.parseColor("#a9a7a5"));
            quantitys.setTextColor(Color.parseColor("#FFFFFF"));
            quantitys.setPadding(6,6,6,6);
            quantitys.setGravity(Gravity.CENTER);
            quantitys.setText(String.valueOf(colorValues.get(i).getCquantity()));
            tableRow.addView(quantitys,tableRowParams);

            total+=colorValues.get(i).getCquantity();

            tableLayout.addView(tableRow,tableLayoutParams);
        }

        TableRow tableRowlast = new TableRow(this);
        tableRowlast.setBackgroundColor(Color.parseColor("#FFFFFF"));

        TextView totals = new TextView(this);
        totals.setBackgroundColor(Color.parseColor("#00d857"));
        totals.setTextColor(Color.parseColor("#FFFFFF"));
        totals.setPadding(6,6,6,6);
        totals.setGravity(Gravity.RIGHT);
        totals.setText("Total: "+total);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        totals.setLayoutParams(params);
        tableRowlast.addView(totals,tableRowParams);

        tableLayout.addView(tableRowlast,tableLayoutParams);



    }

    public void populateColor()
    {
        array_spinner=getResources().getStringArray(R.array.comboColor);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_selectable_list_item, array_spinner);
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

    }

    @Override
    public void onComplete(String output) {

        if(output.contains("result")) {
            getData(output);
            nameEditText.setText(bagEntity.getName());
            typeEditText.setText(bagEntity.getCategory());
            priceEditText.setText(Integer.toString(bagEntity.getPrice()));
            companyEditText.setText(bagEntity.getCompany());
            quantityEditText.setText(Integer.toString(bagEntity.getQuantity()));

            DbHelper dbh = new DbHelper(this);
            String ip = dbh.getIP();
            dbh.close();

            Picasso
                    .with(this)
                    .load("http://" + ip + "/bagWebServices/uploads/" + bagEntity.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(300, 300)
                    .placeholder(R.drawable.bag)
                    .into(bagPhoto);
        }



    }
}
