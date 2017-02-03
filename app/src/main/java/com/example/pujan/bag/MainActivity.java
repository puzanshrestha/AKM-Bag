package com.example.pujan.bag;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.printPackage.PrintDemo;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText usernameEditText,passwordEditText;

    String username,password;
    public static String ip;



    DbHelper dbh;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();





        if (id == R.id.ippool) {

                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("Set IP Address");
                ad.setCancelable(false);
                final EditText editText = new EditText(this);
                editText.setHint("192.168.1.234");
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                editText.setText(dbh.getIP());
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //code if yes
                        ip = editText.getText().toString();
                        dbh.setIP(ip);




                        Toast.makeText(getBaseContext(), dbh.getIP(), Toast.LENGTH_SHORT).show();

                    }
                });
                ad.setNegativeButton("Cancel", null);
                ad.setView(editText);
                ad.show();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbh = new DbHelper(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);  // displaying logo in the action bar






        loginBtn = (Button)findViewById(R.id.loginBtn);
        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(thread.getStatus().equals(AsyncTask.Status.RUNNING))
                thread.cancel(true);
                */
                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                ip=dbh.getIP();

                final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setIndeterminate(true);
                pd.setTitle("Working");
                pd.setMessage("loading...");
                pd.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {

                            public void run() {


                                login();
                                pd.dismiss();

                            }
                        }, 1000);



            }
        });


    }

    public void login()
    {
        String check;
        try{

            check = new FunctionsThread(this).execute("Login",username,password).get();


            if(check.equals("correct"))
            {
                Toast.makeText(getBaseContext(),"Correct Password",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),ActionListActivity.class);
                startActivity(i);
            }

            else if(check.equals("incorrect"))
            {
                Toast.makeText(getBaseContext(),"Invalid username Password",Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(getBaseContext(),"Network Error",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),ActionListActivity.class);
                startActivity(i);
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR ");
        }
    }






}
