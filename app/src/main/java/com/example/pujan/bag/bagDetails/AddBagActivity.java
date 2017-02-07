package com.example.pujan.bag.bagDetails;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.MainActivity;
import com.example.pujan.bag.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class AddBagActivity extends AppCompatActivity {

    EditText nameEditText, typeEditText, priceEditText, companyEditText, quantityEditText;
    Button addBagBtn;
    String source;
    String bid,vendor_id;
    String bagName, bagCategory, bagPrice, bagCompany,bagQuantity;

    int selectedPic;
    String mediaSelect="";
    String ext="";
    ImageView bagPhoto;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i= new Intent(getBaseContext(),BagDetailsActivity.class);
            startActivity(i);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bag);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setTitle(" Add Bag");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        source=getIntent().getStringExtra("source");

        bid=getIntent().getStringExtra("bagid");
        vendor_id=getIntent().getStringExtra("ven_id");
        addBagBtn = (Button) findViewById(R.id.addBagBtn);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);
        quantityEditText=(EditText) findViewById(R.id.quantityEditText);
        bagPhoto =(ImageView)findViewById(R.id.bag_photo);

        if (source.equals("source")) {
            nameEditText.setText(getIntent().getStringExtra("name"));
            typeEditText.setText(getIntent().getStringExtra("category"));
            priceEditText.setText(getIntent().getStringExtra("price"));
            companyEditText.setText(getIntent().getStringExtra("company"));
            quantityEditText.setText(getIntent().getStringExtra("quantity"));
            String photoUri = getIntent().getStringExtra("photo");
            addBagBtn.setText("Update");
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                    Picasso.with(getApplicationContext()).load(R.drawable.bag).into(bagPhoto);
                }
            });
            builder.build()
                    .load("http://"+ MainActivity.ip+"/bagWebServices/uploads/"+photoUri)
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(bagPhoto);


        }

        bagPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,selectedPic);
            }
        });


        addBagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bagName = nameEditText.getText().toString();
                bagCategory = typeEditText.getText().toString();
                bagPrice = priceEditText.getText().toString();
                bagCompany = companyEditText.getText().toString();
                bagQuantity=quantityEditText.getText().toString();

                if (bagName.isEmpty() || bagCategory.isEmpty() || bagPrice.isEmpty() || bagCompany.isEmpty()||bagQuantity.isEmpty()||ext.equals("")) {
                    Toast.makeText(getBaseContext(), "Some of the Fields are empty", Toast.LENGTH_SHORT);
                } else {
                    try {

                        String check = new FunctionsThread().execute("AddBag", bagName, bagCategory, bagPrice, bagCompany, source, bid, ext,bagQuantity).get();

                        String doublecheck = new FunctionsThread().execute("AddRelation", vendor_id, bagName).get();


                        if (check.equals("Inserted")) {
                            Toast.makeText(getBaseContext(), "Inserted new bag Item", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(), BagListActivity.class);
                            i.putExtra("source", "bag");
                            startActivity(i);
                            new FunctionsThread().execute("UploadFile", mediaSelect, bid, "b").get();
                            if (doublecheck.equals("Updated")) {
                                Toast.makeText(getBaseContext(), "Inserted relation", Toast.LENGTH_LONG).show();
                            }
                        } else if (check.equals("Update")) {
                            Toast.makeText(getBaseContext(), "Bag has been Updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(), BagListActivity.class);
                            i.putExtra("source", "bag");
                            startActivity(i);
                            new FunctionsThread().execute("UploadFile", mediaSelect, bid, "b").get();
                        } else
                            Toast.makeText(getBaseContext(), check, Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==selectedPic)
        {
            if(resultCode==RESULT_OK)
            {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                mediaSelect = cursor.getString(columnIndex);
                bagPhoto.setImageURI(uri);
                ext=mediaSelect.substring(mediaSelect.lastIndexOf(".") + 1, mediaSelect.length());
            }



        }

    }
}
