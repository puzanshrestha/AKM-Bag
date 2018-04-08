package com.example.pujan.bag.bagDetails;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
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

import com.example.pujan.bag.FileUpload;
import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.VolleyFunctions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class BagDetailsActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse{


    Button editBtn,deleteBtn,saveBtn;

    EditText nameEditText, typeEditText, priceEditText, companyEditText;

    ImageView bagPhoto;

    String bag_id,vendor_id;

    TextView addPhotoText;
    int selectedPic;
    String mediaSelect = "";
    String ext = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        collapsingToolbarLayout.setTitle("Bag Details");

        editBtn=(Button) findViewById(R.id.editBagBtn);
        deleteBtn=(Button) findViewById(R.id.deleteBagBtn);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        bagPhoto=(ImageView)findViewById(R.id.bag_photo);
        addPhotoText=(TextView)findViewById(R.id.addPhoto);




        nameEditText = (EditText) findViewById(R.id.nameEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);


        bag_id=getIntent().getStringExtra("bagid");
        vendor_id=getIntent().getStringExtra("vendor_id");

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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(false);
                VolleyFunctions t = new VolleyFunctions(getBaseContext());
                t.addBag( nameEditText.getText().toString(), typeEditText.getText().toString(), priceEditText.getText().toString(), companyEditText.getText().toString(), "update", bag_id, ext, "0");
                t.trigAsyncResponse(BagDetailsActivity.this);


            }
        });


        bagPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, selectedPic);
            }
        });


        setEditable(false);
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
            }


        }

    }


    private void setEditable(boolean b) {

        if(b==false)
        {
            nameEditText.setInputType(InputType.TYPE_NULL);
            typeEditText.setInputType(InputType.TYPE_NULL);
            priceEditText.setInputType(InputType.TYPE_NULL);
            companyEditText.setInputType(InputType.TYPE_NULL);

            saveBtn.setVisibility(View.GONE);
            bagPhoto.setClickable(false);
            bagPhoto.setAlpha(1f);
            addPhotoText.setVisibility(View.GONE);
        }

        else
        {
            nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            typeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            companyEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            saveBtn.setVisibility(View.VISIBLE);
            bagPhoto.setClickable(true);
            addPhotoText.setVisibility(View.VISIBLE);
            bagPhoto.setAlpha(0.5f);


            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            nameEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }
    }


    public void delete() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to Delete?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String check;
                        try {
                            check = new FunctionsThread(getBaseContext()).execute("AddBag", " ", " ", " ", " ", "delete", bag_id, " ", " ").get();
                            System.out.println(check+"this is output from php");
                            if (check.equals("Deleted")) {
                                Intent i = new Intent(getBaseContext(), BagListActivity.class);
                                startActivity(i);
                                Toast.makeText(getBaseContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Failed, Please make sure that this Bag is not used in Pending Bill List", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), BagListActivity.class);
        startActivity(i);


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
    public void onComplete(String output) {


        ProgressDialog pd = new ProgressDialog(this);

        if(output.equals("Updated")) {
            //Toast.makeText(this, "Successfully Updated Bag Information", Toast.LENGTH_SHORT).show();
            new FileUpload(getBaseContext(),pd).execute(mediaSelect, bag_id, "b");
            Toast.makeText(getBaseContext(), "Bag has been Updated", Toast.LENGTH_SHORT).show();
        }

        else
            Toast.makeText(this,"Failed to Update Bag Information",Toast.LENGTH_SHORT).show();
    }
}
