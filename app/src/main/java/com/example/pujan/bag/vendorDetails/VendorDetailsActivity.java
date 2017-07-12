package com.example.pujan.bag.vendorDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;

public class VendorDetailsActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse {

    Button editBtn, deleteBtn, saveBtn;
    ImageView bagPhoto;

    TextView nameEditText, addressEditText, phoneEditText;

    String vendor_id = "";

    VolleyFunctions t;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        collapsingToolbarLayout.setTitle("Vendor Details");

        editBtn = (Button) findViewById(R.id.editBagBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBagBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        bagPhoto = (ImageView) findViewById(R.id.bag_photo);


        nameEditText = (EditText) findViewById(R.id.nameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);


        vendor_id = getIntent().getStringExtra("vendor_id");
        nameEditText.setText(getIntent().getStringExtra("name"));
        addressEditText.setText(getIntent().getStringExtra("address"));
        phoneEditText.setText(getIntent().getStringExtra("phone"));


         t= new VolleyFunctions(this);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVendor();


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(false);

                t.addVendor(nameEditText.getText().toString(), addressEditText.getText().toString(), "update", vendor_id);
                t.trigAsyncResponse(VendorDetailsActivity.this);

            }
        });


        setEditable(false);
    }


    private void setEditable(boolean b) {

        if (b == false) {
            nameEditText.setInputType(InputType.TYPE_NULL);
            addressEditText.setInputType(InputType.TYPE_NULL);
            phoneEditText.setInputType(InputType.TYPE_NULL);

            saveBtn.setVisibility(View.GONE);
            bagPhoto.setClickable(false);
        } else {
            nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            addressEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            phoneEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            saveBtn.setVisibility(View.VISIBLE);
            bagPhoto.setClickable(true);


            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), VendorListActivity.class);
                startActivity(i);

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), VendorListActivity.class);
        startActivity(i);
    }


    public void deleteVendor() {


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure that you want to Delete");
        final String a = "";

        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        t.addVendor( a, a, "delete", vendor_id);
                        t.trigAsyncResponse(VendorDetailsActivity.this);

                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onComplete(String output) {

        System.out.println(output+"this is output");
        if (output.equals("Updated"))
            Toast.makeText(this, "Successfully Updated Vendor Information", Toast.LENGTH_LONG).show();

        else if (output.equals("Deleted")) {
            Intent i = new Intent(this, VendorListActivity.class);
            startActivity(i);
            Toast.makeText(this, "Successfully Deleted Vendor Information", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Failed Please Try Again Later", Toast.LENGTH_LONG).show();
    }
}
