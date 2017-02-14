package com.example.pujan.bag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.pujan.bag.database.DbHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Pujan on 12/23/2016.
 */
public class FunctionsThread extends AsyncTask<String, Void, String> {

    Context c;

    String ipMain;
    DbHelper dbh;

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

    }

    @Override
    protected String doInBackground(String... params) {


        final String ip = "http://" + ipMain + "/bagWebServices/";
        String method = params[0];

        if (method.equals("retrieve")) {

            try {
                URL url = new URL(ip + "/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(3000);
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

        } else if (method.equals("Login")) {
            String username = params[1];
            String password = params[2];

            try {
                URL url = new URL(ip + "login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("POST");


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

                System.out.println("----------------------debuggg");
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
        } else if (method.equals("AddBag")) {
            String name, type, price, company, source, bid, ext, quantity;
            name = params[1];
            type = params[2];
            price = params[3];
            company = params[4];
            source = params[5];
            bid = params[6];
            ext = params[7];
            quantity = params[8];
            System.out.println("Inside thread");


            try {
                URL url = new URL(ip + "addBag.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(3000);
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
                        URLEncoder.encode("bag_quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = new String();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                is.close();
                con.disconnect();
                System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {

                return "Error Adding bag Details";

            }

        } else if (method.equals("AddRelation")) {

            String vendor_id = params[1];
            String bag_name = params[2];
            System.out.println(bag_name);
            System.out.println(vendor_id);

            try {
                URL url = new URL(ip + "addRelation.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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
        } else if (method.equals("AddCustomer")) {
            String customerName = params[1];
            String customerAddress = params[2];
            String customerPhone = params[3];
            String customerSource = params[4];
            String cid = params[5];

            try {
                System.out.println(customerSource);
                System.out.println();

                URL url = new URL(ip + "addCustomer.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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
            System.out.println(source);

            try {
                URL url = new URL(ip + "addVendor.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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
            try {
                URL url = new URL(ip + "viewBag.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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

        } else if (method.equals("ViewCustomer")) {
            try {
                URL url = new URL(ip + "viewCustomer.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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
                conn.setConnectTimeout(3000);
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

            try {
                URL url = new URL(ip + "addOrderTemp.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(customer_id, "UTF-8") + "&" +
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
                return "Error Inserting new ordeData";
            }


        } else if (method == "AddOrder") {

            String orderJson = params[1];

            try {
                URL url = new URL(ip + "addOrder.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("AddOrderJson", "UTF-8") + "=" + URLEncoder.encode(orderJson, "UTF-8");
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
        } else if (method == "ViewRecords") {

            String dateTo = params[1];
            String dateFrom = params[2];

            try {
                URL url = new URL(ip + "viewRecords.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
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
                httpURLConnection.setConnectTimeout(3000);
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
                httpURLConnection.setConnectTimeout(3000);
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
        } else if (method == "UploadFile") {

            String sourceFileUri = params[1];
            String upLoadServerUri = ip + "upload.php";
            String typeid = params[2];
            String type = params[3];


            File folder = new File(Environment.getExternalStorageDirectory() + "/BagService");
            if (!folder.exists())
                folder.mkdir();

            File src = new File(sourceFileUri);
            File dest = new File(folder, type + "_" + typeid + "." + sourceFileUri.substring(sourceFileUri.lastIndexOf(".") + 1, sourceFileUri.length()));

            InputStream in = null;
            try {
                in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Copy the bits from instream to outstream


            int serverResponseCode = 0;


            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File not exist :");


                return null;

            } else {
                try {
                    System.out.println("input stream");
                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);
                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", dest.getPath());

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=" + dest.getPath() + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);

                    System.out.println("response code is" + serverResponseCode);
                    if (serverResponseCode == 200) {

                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                + " uploaded to servers PCPCPC";


                        dest.delete();
                        System.out.println(msg);


                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    System.out.println("Upload file to server Exception" + "Exception : "
                            + e.getMessage() + e);
                }

                return Integer.toString(serverResponseCode);

            } // End else block


        } else {

            return "Error-- No method found";
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {

    }
}
