package com.example.pujan.bag;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pujan.bag.database.DbHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Pujan on 12/23/2016.
 */
public class FunctionsThread extends AsyncTask<String, Void, String> {

    Context c;

    String ipMain;
    String responseRet="";

    public ProgressDialog pd;




    private int flagPd=1;

    private AsyncResponse callback = null;
    public interface AsyncResponse {
        void onComplete(String output);
    }


    public FunctionsThread(Context c) {
        this.c = c;
        DbHelper db = new DbHelper(c);

        try {
            ipMain = db.getIP();
        } catch (Exception e) {
        } finally {
            db.close();
        }


    }

    public FunctionsThread() {

    }

    @Override
    protected void onPreExecute() {

        pd = new ProgressDialog(c);
        pd.setIndeterminate(true);
        pd.setTitle("Working");
        pd.setMessage("loading...");
        //if(flagPd==1)
       // pd.show();


    }


    public boolean isSessionServerOnline() {

        try {

            HttpsURLConnection connection = (HttpsURLConnection) new URL("http://" + ipMain + "/bagWebServices/").openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return (200 <= responseCode && responseCode <= 399);

        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    protected String doInBackground(String... params) {


        final String ip = "http://" + ipMain + "/bagWebServices/";
        String method = params[0];


        if (method.equals("retrieve")) {

            try {
                URL url = new URL(ip + "/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String out = new String();
                while ((out = bufferedReader.readLine()) != null) {
                    sb.append(out + "\n");
                }

                return sb.toString().trim();// sb.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error connecting to server";
            }

        }

        else if (method.equals("Login")) {
            final String username = params[1];
            final String password = params[2];


            try {
                URL url = new URL(ip + "login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDefaultUseCaches(false);

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return response.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                return "error";

            }

        } else if (method.equals("AddBag")) {
            String name, type, price, company, source, bid, ext, vendor_id;
            name = params[1];
            type = params[2];
            price = params[3];
            company = params[4];
            source = params[5];
            bid = params[6];
            ext = params[7];
            vendor_id = params[8];



            try {
                URL url = new URL(ip + "addBag.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


                String data = URLEncoder.encode("bag_name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("bag_category", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("bag_price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8") + "&" +
                        URLEncoder.encode("bag_company", "UTF-8") + "=" + URLEncoder.encode(company, "UTF-8") + "&" +
                        URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(source, "UTF-8") + "&" +
                        URLEncoder.encode("bid", "UTF-8") + "=" + URLEncoder.encode(bid, "UTF-8") + "&" +
                        URLEncoder.encode("ext", "UTF-8") + "=" + URLEncoder.encode(ext, "UTF-8") + "&" +
                        URLEncoder.encode("vendor_id", "UTF-8") + "=" + URLEncoder.encode(vendor_id, "UTF-8");



                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                is.close();
                con.disconnect();

                return sb.toString().trim();
            } catch (Exception e) {

                return "Error Adding bag Details";

            }

        }


        /*
        else if (method.equals("AddRelation")) {

            String vendor_id = params[1];
            String bag_name = params[2];
            System.out.println(bag_name);
            System.out.println(vendor_id);

            try {
                URL url = new URL(ip + "addRelation.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("vendor_id", "UTF-8") + "=" + URLEncoder.encode(vendor_id, "UTF-8") + "&" +
                        URLEncoder.encode("bag_name", "UTF-8") + "=" + URLEncoder.encode(bag_name, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error Inserting relation";
            }
        }

        */
        else if (method.equals("AddCustomer")) {
            String customerName = params[1];
            String customerAddress = params[2];
            String customerPhone = params[3];
            String customerSource = params[4];
            String cid = params[5];

            try {

                URL url = new URL(ip + "addCustomer.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();


                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data = URLEncoder.encode("customer_name", "UTF-8") + "=" + URLEncoder.encode(customerName, "UTF-8") + "&" +
                        URLEncoder.encode("customer_address", "UTF-8") + "=" + URLEncoder.encode(customerAddress, "UTF-8") + "&" +
                        URLEncoder.encode("customer_phone", "UTF-8") + "=" + URLEncoder.encode(customerPhone, "UTF-8") + "&" +
                        URLEncoder.encode("customer_source", "UTF-8") + "=" + URLEncoder.encode(customerSource, "UTF-8") + "&" +
                        URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(cid, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();


                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                is.close();
                conn.disconnect();
                return sb.toString().trim();

            } catch (Exception e) {
                return "Error Adding Customer Details";
            }
        } else if (method.equals("AddVendor")) {
            String vendorName = params[1];
            String vendorAddress = params[2];
            String source = params[3];
            String id = params[4];


            try {
                URL url = new URL(ip + "addVendor.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("vendor_name", "UTF-8") + "=" + URLEncoder.encode(vendorName, "UTF-8") + "&" +
                        URLEncoder.encode("vendor_address", "UTF-8") + "=" + URLEncoder.encode(vendorAddress, "UTF-8") + "&" +
                        URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(source, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error Inserting new Vendor";
            }
        } else if (method.equals("ViewBag")) {

            String offset=params[1];
            String difference=params[2];
            try {

                URL url = new URL(ip + "viewBagWithStock.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("offset", "UTF-8") + "=" + URLEncoder.encode(offset, "UTF-8")+"&"+
                        URLEncoder.encode("difference", "UTF-8") + "=" + URLEncoder.encode(difference, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder response = new StringBuilder();
                String line = new String("");

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line + "\n");
                }
                bufferedReader.close();
                is.close();
                conn.disconnect();
                return response.toString().trim();


            } catch (SocketTimeoutException e) {
                return "Error";
            } catch (java.net.ConnectException e) {
                System.out.println("error");
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }


        } else if (method.equals("ViewCustomer")) {
            try {
                URL url = new URL(ip + "viewCustomer.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder response = new StringBuilder();
                String line = new String("");

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line + "\n");
                }
                bufferedReader.close();
                is.close();
                conn.disconnect();
                return response.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }

        } else if (method.equals("ViewVendor")) {
            try {
                URL url = new URL(ip + "viewVendor.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder response = new StringBuilder();
                String line = new String("");

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line + "\n");
                }
                bufferedReader.close();
                is.close();
                conn.disconnect();
                return response.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }

        } else if (method.equals("AddOrderTemp")) {
            String customer_id = params[1];
            String bag_id_code = params[2];
            String source = params[3];

            try {
                URL url = new URL(ip + "addOrderTemp.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(customer_id, "UTF-8") + "&" +
                        URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(source, "UTF-8") + "&" +
                        URLEncoder.encode("bag_id", "UTF-8") + "=" + URLEncoder.encode(bag_id_code, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error";
            }


        } else if (method == "AddOrder") {

            String orderJson = params[1];
            String customer_id = params[2];
            String customer_name = params[3];
            String discount=params[4];
            String source=params[5];

            try {
                URL url = new URL(ip + "addOrder.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("AddOrderJson", "UTF-8") + "=" + URLEncoder.encode(orderJson, "UTF-8") + "&" +
                        URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(customer_id, "UTF-8") + "&" +
                        URLEncoder.encode("customer_name", "UTF-8") + "=" + URLEncoder.encode(customer_name, "UTF-8")+ "&" +
                        URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(source, "UTF-8")+ "&" +
                        URLEncoder.encode("discount", "UTF-8") + "=" + URLEncoder.encode(discount, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error";
            }
        }
        else if (method == "CancelPendingBill") {

            String pId = params[1];



            try {
                URL url = new URL(ip + "cancelPendingBill.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("pId", "UTF-8") + "=" + URLEncoder.encode(pId, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error";
            }
        }
        else if (method == "RePrintBill") {

            String order_id = params[1];


            try {
                URL url = new URL(ip + "rePrintBill.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(order_id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (method == "AddPendingBill") {

            String orderJson = params[1];
            String customer_id = params[2];
            String customer_name = params[3];
            String total=params[4];
            String address=params[5];


            try {
                URL url = new URL(ip + "addPendingBill.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("AddOrderJson", "UTF-8") + "=" + URLEncoder.encode(orderJson, "UTF-8") + "&" +
                        URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(customer_id, "UTF-8") + "&" +
                        URLEncoder.encode("customer_name", "UTF-8") + "=" + URLEncoder.encode(customer_name, "UTF-8")+ "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8")+ "&" +
                        URLEncoder.encode("total", "UTF-8") + "=" + URLEncoder.encode(total, "UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (Exception e) {
                return "Error";
            }
        }else if (method == "QueryPendingBill") {

            String pId = params[1];


            try {
                URL url = new URL(ip + "queryPendingBill.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("pId", "UTF-8") + "=" + URLEncoder.encode(pId, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (method == "QueryPendingBillList") {


            try {
                URL url = new URL(ip + "queryPendingBillList.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //String data = URLEncoder.encode("pId", "UTF-8") + "=" + URLEncoder.encode(pId, "UTF-8");

             //   bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (method == "ViewRecords") {

            String dateTo = params[1];
            String dateFrom = params[2];

            try {
                URL url = new URL(ip + "viewRecords.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("dateTo", "UTF-8") + "=" + URLEncoder.encode(dateTo, "UTF-8") + "&" +
                        URLEncoder.encode("dateFrom", "UTF-8") + "=" + URLEncoder.encode(dateFrom, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                bufferedReader.close();
                conn.disconnect();
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("ViewStockInformation")) {
            String bag_id = params[1];


            try {
                URL url = new URL(ip + "viewStockInformation.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");


                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("bag_id", "UTF-8") + "=" + URLEncoder.encode(bag_id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                System.out.println(response.toString().trim());
                return response.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in ");
                return "error";

            }
        } else if (method.equals("UpdateStockInformation")) {
            String bag_id = params[1];
            String color = params[2];
            String quantity = params[3];


            try {
                URL url = new URL(ip + "updateStockInformation.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");


                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("bag_id", "UTF-8") + "=" + URLEncoder.encode(bag_id, "UTF-8") + "&" +
                        URLEncoder.encode("color", "UTF-8") + "=" + URLEncoder.encode(color, "UTF-8") + "&" +
                        URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                System.out.println(response.toString().trim());
                return response.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in ");
                return "error";

            }
        } else if (method.equals("EditStockInformation")) {
            String bag_id = params[1];
            String stockEditJson = params[2];


            try {
                URL url = new URL(ip + "editStockInformation.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("bag_id", "UTF-8") + "=" + URLEncoder.encode(bag_id, "UTF-8") + "&" +
                        URLEncoder.encode("stockEditJson", "UTF-8") + "=" + URLEncoder.encode(stockEditJson, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                System.out.println(response.toString().trim());
                return response.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                return "error";

            }
        } else if (method.equals("QueryQuantity")) {
            String quantity = params[1];

            try {
                URL url = new URL(ip + "queryTable.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return response.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in ");
                return "error";

            }
        } else {

            return "Error-- No method found";
        }

        return "No Method Found";
    }


    @Override
    protected void onPostExecute(String result) {


        if (callback != null)
            callback.onComplete(result);

     //   if(flagPd==1)
       // pd.dismiss();


    }

    public void setPdFlag()
    {
        flagPd=0;
    }
}
