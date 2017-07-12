package com.example.pujan.bag.customerDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
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

import com.example.pujan.bag.R;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.VolleyFunctions;

import java.util.concurrent.ExecutionException;

public class CustomerDetailsActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse {

    Button editBtn,deleteBtn,saveBtn;
    ImageView bagPhoto;

    TextView nameEditText,addressEditText,phoneEditText;
    String customer_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);


        Toolbar toolbar = (Toolbar)findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        collapsingToolbarLayout.setTitle("Customer Details");
        editBtn=(Button) findViewById(R.id.editBagBtn);
        deleteBtn=(Button) findViewById(R.id.deleteBagBtn);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        bagPhoto=(ImageView)findViewById(R.id.bag_photo);


        nameEditText = (EditText) findViewById(R.id.nameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);



        customer_id=getIntent().getStringExtra("customer_id");
        nameEditText.setText(getIntent().getStringExtra("name"));
        addressEditText.setText(getIntent().getStringExtra("address"));
        phoneEditText.setText(getIntent().getStringExtra("phone"));






        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomer();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(false);


                VolleyFunctions t = new VolleyFunctions(getBaseContext());
                t.addCustomer(nameEditText.getText().toString(),addressEditText.getText().toString(),phoneEditText.getText().toString(),"update",customer_id);
                t.trigAsyncResponse(CustomerDetailsActivity.this);
            }
        });




        setEditable(false);
    }






    private void setEditable(boolean b) {

        if(b==false)
        {
            nameEditText.setInputType(InputType.TYPE_NULL);
            addressEditText.setInputType(InputType.TYPE_NULL);
            phoneEditText.setInputType(InputType.TYPE_NULL);

            saveBtn.setVisibility(View.GONE);
            bagPhoto.setClickable(false);
        }

        else
        {


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
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), CustomerListActivity.class);
                startActivity(i);

        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), CustomerListActivity.class);
        startActivity(i);
    }




    public void deleteCustomer()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You wanted to Delete");

        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String a="";

                        final String check;
                        try {
                            check = new FunctionsThread(getBaseContext()).execute("AddCustomer", a, a, a, "delete", customer_id).get();

                            if (check.equals("Delete")) {
                                Toast.makeText(getBaseContext(), "Successfully Deleted Customer", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getBaseContext(), CustomerListActivity.class);
                                i.putExtra("method", "customerview");
                                startActivity(i);

                            }
                            else
                            {
                                Toast.makeText(getBaseContext(),"Failed Deleting the Customer",Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

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
    public void onComplete(String check) {

        if (check.equals("Update")){
            Toast.makeText(this,"updated new Customer",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,CustomerListActivity.class);
            i.putExtra("method","customerview");
            startActivity(i);
        }
        else
        {
            Toast.makeText(this,check,Toast.LENGTH_SHORT).show();
        }

    }
}

