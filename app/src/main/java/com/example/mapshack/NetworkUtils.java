package com.example.mapshack;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NetworkUtils {
    public static final String SERVER_ADDR = "https://mapshack.herokuapp.com/";

    public static String getResponseFromURL(String urlString, String args) throws IOException {
        byte[] out = args.getBytes(StandardCharsets.UTF_8);
        String line = "";
        URL url = new URL(SERVER_ADDR + urlString);
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.connect();
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(out);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"
            ));
            String input;
            while ((input = br.readLine()) != null) {
                line = line + input;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseFromURL = null;
            try {
                responseFromURL = getResponseFromURL(strings[0], strings[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseFromURL;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
