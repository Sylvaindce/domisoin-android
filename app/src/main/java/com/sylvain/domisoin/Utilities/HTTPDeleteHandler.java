package com.sylvain.domisoin.Utilities;

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
 * Created by Sylvain on 30/06/2017.
 */

public class HTTPDeleteHandler {

    private static final String TAG = com.sylvain.domisoin.Utilities.HTTPDeleteHandler.class.getSimpleName();

    public HTTPDeleteHandler() {
    }

    public String makeServiceCall(String reqUrl, String token) {
        String response = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            //conn.addRequestProperty("Content-Type", "application/json");
            //conn.addRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");
            if (!token.isEmpty() || !token.equals(""))
                conn.setRequestProperty("Authorization", "JWT " + token);
            //conn.addRequestProperty("Authorization", "JWT " + token);

            // read the response
            response = String.valueOf(conn.getResponseCode());
            if (Integer.decode(response) > 226) {
                InputStream err = new BufferedInputStream(conn.getErrorStream());
                response += " - " + convertStreamToString(err);
            } else {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response += " - " + convertStreamToString(in);
                //response = "-1";
                //response = String.valueOf(conn.getResponseCode());
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
            if (conn != null)
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
