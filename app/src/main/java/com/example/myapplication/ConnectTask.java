package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectTask  extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        String host = "0.0.0.0";
        int port = 8888;
        Socket socket = new Socket();
        Log.i("Read Button Clicked", "yipee");
        /**
         * Create a client socket with the host,
         * port, and timeout information.
         */
        try {
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 500);
            return "connected";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}