package com.example.pujan.bag;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by puzan on 22-May-17.
 */

class IpList extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {

        for (int i = 0; i < 2; i++) {
            if (executeCommand("192.168.1." + i))
            {
                try {
                    System.out.println("this is hostname"+InetAddress.getLocalHost().getByName("192.168.1.100").getCanonicalHostName());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }


        }


        return false;
    }


    public boolean getNetworkInfo(String ip) {
        try {
            //make a URL to a known source
            URL url = new URL("http://" + ip);

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setReadTimeout(30);

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();


        } catch (Exception e) {
            return false;
        }

        return true;
    }


    private boolean executeCommand(String ip) {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -w 1 -c 1 " + ip);
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
