package com.example.pujan.bag;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pujan.bag.database.DbHelper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse {

    Button loginBtn, setIp,setShop;
    EditText usernameEditText, passwordEditText;

    String username, password;
    public static String ip;
    public String shopNumber="";


    SharedPreferences sharedpreferences;
    DbHelper dbh;

    ProgressDialog pd;

    /*
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

               setIP();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                */
        setContentView(R.layout.activity_main);


        pd = new ProgressDialog(this);

        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        dbh = new DbHelper(this);


        loginBtn = (Button) findViewById(R.id.loginBtn);
        setIp = (Button) findViewById(R.id.setIp);
        setShop =(Button)findViewById(R.id.setShop);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        setIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setIp();
            }
        });

        setShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Set Shop Number");
                ad.setCancelable(false);
                final EditText editText = new EditText(getBaseContext());
                editText.setHint("1");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setText(dbh.getShop());
                editText.setTextColor(Color.parseColor("#1e1e1e"));
                LinearLayout parent = new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams child = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                parent.setOrientation(LinearLayout.VERTICAL);
                parent.setPadding(40, 0, 40, 0);
                parent.setLayoutParams(child);
                parent.addView(editText);

                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //code if yes
                        shopNumber = editText.getText().toString();
                        dbh.setShop(shopNumber);

                        Toast.makeText(getBaseContext(), dbh.getShop(), Toast.LENGTH_SHORT).show();

                    }
                });
                ad.setNegativeButton("Cancel", null);
                ad.setView(parent);
                ad.show();

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(thread.getStatus().equals(AsyncTask.Status.RUNNING))
                thread.cancel(true);
                */
                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                ip = dbh.getIP();
                shopNumber=dbh.getShop();
                pd.setIndeterminate(true);
                pd.setTitle("Working");
                pd.setMessage("loading...");
                pd.show();


                /*
                final ProgressDialog pd = new ProgressDialog(ExpandableListActivity.this);
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



                */
                new android.os.Handler().postDelayed(
                        new Runnable() {

                            public void run() {


                                VolleyFunctions volleyFunctions = new VolleyFunctions(MainActivity.this);
                                volleyFunctions.login(username, password,shopNumber);
                                volleyFunctions.trigAsyncResponse(MainActivity.this);

                            }
                        }, 1000);


            }
        });


    }

    public void login(String check) {
        try {

            System.out.println(check);

            if (check.equals("userType-1")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("userType", "userType#1");
                editor.commit();
                Toast.makeText(getBaseContext(), "Admin Logged in", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);

            } else if (check.equals("userType-0")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("userType", "userType#0");
                editor.commit();
                Toast.makeText(getBaseContext(), "User Logged in", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);
            } else if (check.equals("incorrect")) {
                Toast.makeText(getBaseContext(), "Invalid username Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Network error, please check server's IP !", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            System.out.println("ERROR ");
        }
    }


    @Override
    public void onComplete(String output) {


        System.out.println("Escaped");
        pd.dismiss();
        login(output);

    }

    @Override
    public void onBackPressed() {

    }

    public void setIp() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Set IP Address");
        ad.setCancelable(false);
        final EditText editText = new EditText(this);
        editText.setHint("192.168.1.234");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(dbh.getIP());
        LinearLayout parent = new LinearLayout(this);
        LinearLayout.LayoutParams child = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setPadding(40, 0, 40, 0);
        parent.setLayoutParams(child);
        parent.addView(editText);

        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //code if yes
                ip = editText.getText().toString();
                dbh.setIP(ip);

                Toast.makeText(getBaseContext(), dbh.getIP(), Toast.LENGTH_SHORT).show();

            }
        });
        ad.setNegativeButton("Cancel", null);
        ad.setView(parent);
        ad.show();
    }


    private boolean executeCommand(String ip) {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + ip);
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(ip + "---" + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }

        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }

}


