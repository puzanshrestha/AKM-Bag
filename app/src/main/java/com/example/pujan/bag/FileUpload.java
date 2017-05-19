package com.example.pujan.bag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.pujan.bag.database.DbHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Pujan on 20-Feb-17.
 */
public class FileUpload extends AsyncTask<String, Void, String> {

    String ipMain;
    Context context;

    public FileUpload(Context c) {
        this.context = c;
        DbHelper db = new DbHelper(c);

        try {
            ipMain = db.getIP();
        }
        catch (Exception e) {
        }
        finally {
            db.close();
        }

    }

    @Override
    protected String doInBackground(String... params) {

        final String ip = "http://" + ipMain + "/bagWebServices/";

        String sourceFileUri = params[0];
        String upLoadServerUri = ip + "upload.php";
        String typeid = params[1];
        String type = params[2];


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

                int i=0;
                while (bytesRead > 0) {


                    i++;
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    System.out.println(i+"progress");

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


        }
    }
}
