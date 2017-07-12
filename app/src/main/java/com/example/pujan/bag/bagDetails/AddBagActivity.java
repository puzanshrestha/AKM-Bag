package com.example.pujan.bag.bagDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FileUpload;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.vendorSelectFragment.VendorSelectFragment;

import java.util.concurrent.ExecutionException;

public class AddBagActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse {

    EditText nameEditText, typeEditText, priceEditText, companyEditText,vendorEditText;
    Button addBagBtn,vendorSelectBtn;
    String bid, vendor_id="-1";
    String bagName, bagCategory, bagPrice, bagCompany, bagQuantity;

    int selectedPic;
    String mediaSelect = "";
    String ext = "";
    ImageView bagPhoto;
    TextView addPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  quantityEditText.setVisibility(View.GONE);

        setContentView(R.layout.activity_add_bag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Add New Bag" + "</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);


        addBagBtn = (Button) findViewById(R.id.saveBtn);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);
        bagPhoto = (ImageView) findViewById(R.id.bag_photo);
        vendorEditText=(EditText) findViewById(R.id.vendorEditText);
        addPhoto=(TextView)findViewById(R.id.addPhoto);
        vendorSelectBtn=(Button)findViewById(R.id.vendorDropDown);

        vendorEditText.setInputType(InputType.TYPE_NULL);

        vendorSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                VendorSelectFragment selectCustomerFragment = new VendorSelectFragment();
                selectCustomerFragment.show(fm,"Dialog");
            }
        });



        bagPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, selectedPic);
            }
        });


        addBagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!vendor_id.equals("-1")) {
                    bagName = nameEditText.getText().toString();
                    bagCategory = typeEditText.getText().toString();
                    bagPrice = priceEditText.getText().toString();
                    bagCompany = companyEditText.getText().toString();
                    //bagQuantity = quantityEditText.getText().toString();

                    VolleyFunctions t = new VolleyFunctions(AddBagActivity.this);
                    t.addBag(bagName, bagCategory, bagPrice, bagCompany, "insert", "0", ext, vendor_id);
                    t.trigAsyncResponse(AddBagActivity.this);

                }
                else
                    Toast.makeText(getBaseContext(),"Please Select Vendor",Toast.LENGTH_LONG).show();

            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent i = new Intent(getBaseContext(),BagListActivity.class);
                startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(),BagListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == selectedPic) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                mediaSelect = cursor.getString(columnIndex);
                bagPhoto.setImageURI(uri);

                ext = mediaSelect.substring(mediaSelect.lastIndexOf(".") + 1, mediaSelect.length());


                bagPhoto.setAlpha(1.0f);
                addPhoto.setVisibility(View.GONE);

            }



        }

    }

    @Override
    public void onComplete(String check) {
        try {

            ProgressDialog pd = new ProgressDialog(this);


            if (check.substring(0,8).equals("Inserted")) {
                Toast.makeText(getBaseContext(), "Inserted new bag Item", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), BagListActivity.class);
                i.putExtra("source", "bag");
                startActivity(i);
                bid=check.substring(11,check.length());
                System.out.println(bid);
                new FileUpload(getBaseContext(),pd).execute(mediaSelect, bid, "b").get();

            }

          else
                Toast.makeText(getBaseContext(), check, Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            System.out.println("error");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }

    public void setVendorInfo(Bundle bundle) {
        vendorEditText.setText(bundle.getString("name"));
        vendor_id=String.valueOf(bundle.getInt("id"));
    }
}
