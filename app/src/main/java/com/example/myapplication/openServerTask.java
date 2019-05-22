package com.example.myapplication;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class openServerTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        /**
         * Create a server socket and wait for client connections. This
         * call blocks until a connection is accepted from a client
         */
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();
            return "connection established";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
