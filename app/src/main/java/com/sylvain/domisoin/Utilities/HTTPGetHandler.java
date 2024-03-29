package com.sylvain.domisoin.Utilities;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Sylvain on 07/02/2017.
 */

public class HTTPGetHandler {

    private static final String TAG = HTTPGetHandler.class.getSimpleName();

    public HTTPGetHandler() {
    }

    public String makeServiceCall(String reqUrl, String token) {
        String response = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            //conn.addRequestProperty("Content-Type", "application/json");
            //conn.addRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");

            //TODO QUOTE TOKEN HEADER
            if (!token.isEmpty() || !token.equals("")) {
                String headtok = "JWT" + '\u0020' + token;
                conn.setRequestProperty("Authorization", headtok);
                Log.d(TAG, headtok);
            }

            response = String.valueOf(conn.getResponseCode());
            if (Integer.decode(response) > 226) {
                InputStream err = new BufferedInputStream(conn.getErrorStream());
                response += " - " + convertStreamToString(err);
            } else {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response += " - " + convertStreamToString(in);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        Log.d(TAG, response);
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}