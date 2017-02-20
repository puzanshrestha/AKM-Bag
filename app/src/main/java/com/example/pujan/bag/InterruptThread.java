package com.example.pujan.bag;

import java.net.HttpURLConnection;
import java.net.URLConnection;

public class InterruptThread implements Runnable {
    Thread parent;
    URLConnection con;
    public InterruptThread(Thread parent, URLConnection con) {
        this.parent = parent;
        this.con = con;
    }

    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {


        }
        System.out.println("Timer thread forcing parent to quit connection");
        ((HttpURLConnection)con).disconnect();

        System.out.println("Timer thread closed connection held by parent, exiting");
    }
}