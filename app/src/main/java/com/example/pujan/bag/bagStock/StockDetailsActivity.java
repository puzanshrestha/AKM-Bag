package com.example.pujan.bag.bagStock;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StockDetailsActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse {


    Button editBtn,updateStockBtn;

    EditText nameEditText, typeEditText, priceEditText, companyEditText;
    TextView redStockQty, blackStockQty, brownStockQty, othersStockQty;
    EditText redEditTxt, blackEditTxt, brownEditTxt, othersEditText;
    ImageView bagPhoto;

    String bag_id, vendor_id;

    TextView addPhotoText;

    ArrayList<ColorQuantityEntity> colorValues=new ArrayList<>();
    LinkedHashMap<String,Integer> stockList=new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        collapsingToolbarLayout.setTitle("Update Stock");

        editBtn = (Button) findViewById(R.id.editBagBtn);

        bagPhoto = (ImageView) findViewById(R.id.bag_photo);
        addPhotoText = (TextView) findViewById(R.id.addPhoto);


        nameEditText = (EditText) findViewById(R.id.nameEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);

        redStockQty = (TextView) findViewById(R.id.redStockQty);
        blackStockQty = (TextView) findViewById(R.id.blackStockQty);
        brownStockQty = (TextView) findViewById(R.id.brownStockQty);
        othersStockQty = (TextView) findViewById(R.id.othersStockQty);

        redEditTxt = (EditText) findViewById(R.id.redQty);
        blackEditTxt = (EditText) findViewById(R.id.blackQty);
        brownEditTxt = (EditText) findViewById(R.id.brownQty);
        othersEditText = (EditText) findViewById(R.id.othersQty);

        updateStockBtn=(Button)findViewById(R.id.updateStockBtn);

        bag_id = getIntent().getStringExtra("bagid");
        vendor_id = getIntent().getStringExtra("vendor_id");


        VolleyFunctions t= new VolleyFunctions(this);
        t.viewStockInformation(bag_id);
        t.trigAsyncResponse(StockDetailsActivity.this);



        nameEditText.setText(getIntent().getStringExtra("name"));
        typeEditText.setText(getIntent().getStringExtra("category"));
        priceEditText.setText(getIntent().getStringExtra("price"));
        companyEditText.setText(getIntent().getStringExtra("company"));

        String photoUri = getIntent().getStringExtra("photo");
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                Picasso.with(getBaseContext()).load(R.mipmap.ic_launcher).into(bagPhoto);
            }
        });
        builder.build()
                .load("http://" + MainActivity.ip + "/bagWebServices/uploads/" + photoUri)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noFade()
                .fit()
                .centerCrop()
                .into(bagPhoto);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
            }
        });


        updateStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(false);

                ArrayList<ColorQuantityEntity> cqeArray = new ArrayList<>();
                ColorQuantityEntity cqe;

                if(!redEditTxt.getText().toString().equals("")) {
                    cqe=new ColorQuantityEntity();
                    cqe.setColor("RED");
                    cqe.setCquantity(Integer.parseInt(redStockQty.getText().toString()) + Integer.parseInt(redEditTxt.getText().toString()));
                    cqeArray.add(cqe);

                }
                if(!blackEditTxt.getText().toString().equals("")) {
                    cqe=new ColorQuantityEntity();
                    cqe.setColor("BLACK");
                    cqe.setCquantity(Integer.parseInt(blackStockQty.getText().toString()) + Integer.parseInt(blackEditTxt.getText().toString()));
                    cqeArray.add(cqe);
                }

                if(!brownEditTxt.getText().toString().equals("")) {
                    cqe=new ColorQuantityEntity();
                    cqe.setColor("BROWN");
                    cqe.setCquantity(Integer.parseInt(brownStockQty.getText().toString()) + Integer.parseInt(brownEditTxt.getText().toString()));
                    cqeArray.add(cqe);
                }
                if(!othersEditText.getText().toString().equals("")) {
                    cqe=new ColorQuantityEntity();
                    cqe.setColor("OTHERS");
                    cqe.setCquantity(Integer.parseInt(othersStockQty.getText().toString()) + Integer.parseInt(othersEditText.getText().toString()));
                    cqeArray.add(cqe);
                }


                Gson stockEdit = new Gson();
                String stockEditJson = stockEdit.toJson(cqeArray);
                System.out.println(stockEditJson);
                VolleyFunctions tEdt=new VolleyFunctions(getBaseContext());
                tEdt.editStockInformation(bag_id,stockEditJson);
                tEdt.trigAsyncResponse(StockDetailsActivity.this);
            }
        });




        setEditable(false);
    }

    private void populateStockText() {

        for (LinkedHashMap.Entry<String, Integer> entry : stockList.entrySet()) {
            switch (entry.getKey()) {
                case "RED":
                    redStockQty.setText(entry.getValue().toString());
                    break;
                case "BLACK":
                    blackStockQty.setText(entry.getValue().toString());
                    break;
                case "BROWN":
                    brownStockQty.setText(entry.getValue().toString());
                    break;
                case "OTHERS":
                    othersStockQty.setText(entry.getValue().toString());
                    break;

                default:
                    break;
            }
        }

    }


    private void setEditable(boolean b) {

        if (b == false) {
            nameEditText.setInputType(InputType.TYPE_NULL);
            typeEditText.setInputType(InputType.TYPE_NULL);
            priceEditText.setInputType(InputType.TYPE_NULL);
            companyEditText.setInputType(InputType.TYPE_NULL);


            bagPhoto.setClickable(false);
            bagPhoto.setAlpha(1f);
            addPhotoText.setVisibility(View.GONE);
        } else {
            nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            typeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            companyEditText.setInputType(InputType.TYPE_CLASS_TEXT);

            bagPhoto.setClickable(true);
            addPhotoText.setVisibility(View.VISIBLE);
            bagPhoto.setAlpha(0.5f);


            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }
    }




    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), StockListActivity.class);
        startActivity(i);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent i = new Intent(getBaseContext(), StockListActivity.class);
                startActivity(i);
        }
        return true;
    }

    @Override
    public void onComplete(String output) {

        if (output.equals("Succeeded")) {
            VolleyFunctions t= new VolleyFunctions(this);
            t.viewStockInformation( bag_id);
            t.trigAsyncResponse(StockDetailsActivity.this);

            Toast.makeText(this, "Successfully Updated Stock Information", Toast.LENGTH_SHORT).show();
        }
        else if(output.equals("Failed")) {
            Toast.makeText(this, "Failed to Update Stock Information", Toast.LENGTH_SHORT).show();
        }
        else
            getStock(output);
    }

    public void getStock(String response)
    {
        LinkedHashMap<String,Integer> temp= new LinkedHashMap<>();
        try {
            JSONObject stockJson = new JSONObject(response);
            JSONArray stockJsonArray = stockJson.getJSONArray("result");
            for (int i = 0; i < stockJsonArray.length(); i++) {
                JSONObject jObject = stockJsonArray.getJSONObject(i);
                temp.put(jObject.getString("color"),Integer.valueOf(jObject.getString("quantityColor")));

            }

            stockList.clear();
            stockList.putAll(temp);
            populateStockText();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error has been occured", Toast.LENGTH_SHORT).show();
        }

    }

}
