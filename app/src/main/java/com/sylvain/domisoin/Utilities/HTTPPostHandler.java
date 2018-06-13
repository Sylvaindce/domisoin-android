package com.sylvain.domisoin.Utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.net.URL;
import java.util.Map;

/**
 * Created by Sylvain on 26/06/2017.
 */

public class HTTPPostHandler {
    private static final String TAG = HTTPPostHandler.class.getSimpleName();

    public HTTPPostHandler() {
    }

    public String makeServiceCall(String reqUrl, JSONObject data, String token) {
        HttpURLConnection conn = null;
        String return_value = "0";
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");


            if (!token.isEmpty() || !token.equals("")) {
                String headtok = "JWT" + '\u0020' + token;
                conn.setRequestProperty("Authorization", headtok);
                Log.d(TAG, headtok);
            }

            conn.setDoOutput(true);
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.connect();

            //Write
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.toString());
            writer.close();
            os.close();

            return_value = String.valueOf(conn.getResponseCode());
            if (Integer.decode(return_value) > 226) {
                InputStream err = new BufferedInputStream(conn.getErrorStream());
                return_value += " - " + convertStreamToString(err);
            } else {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return_value += " - " + convertStreamToString(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "TIMEOUT");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        Log.d(TAG, return_value);
        return return_value;
    }

    /*public String makeServiceCall(String reqUrl, Map data, String token) {
        HttpURLConnection conn = null;
        String return_value = "0";
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept", "application/vnd.domisoin.fr.api+json; version=1.0");
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");


            if (!token.isEmpty() || !token.equals("")) {
                String headtok = "JWT" + '\u0020' + token;
                conn.setRequestProperty("Authorization", headtok);
                Log.d(TAG, headtok);
            }

            conn.setDoOutput(true);
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.connect();

            JSONObject json = JsonUtils.mapToJson(data);
            try {
                if (json.has("day_offs")) {
                    String tmp = String.valueOf(json.get("day_offs"));
                    JSONArray toto = new JSONArray();
                    if (!tmp.equals("[]")) {
                        tmp = tmp.replace("[", "");
                        tmp = tmp.replace("]", "");
                        tmp = tmp.replace(" ", "");
                        String[] tmp_ar = tmp.split(",");
                        for (String s : tmp_ar)
                            toto.put(Integer.decode(s));
                    }
                    json.put("day_offs", toto);
                }
                Log.d("POST JSON", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Write
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json.toString());
            writer.close();
            os.close();

            return_value = String.valueOf(conn.getResponseCode());
            if (Integer.decode(return_value) > 226) {
                InputStream err = new BufferedInputStream(conn.getErrorStream());
                return_value += " - " + convertStreamToString(err);
            } else {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return_value += " - " + convertStreamToString(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "TIMEOUT");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        Log.d(TAG, return_value);
        return return_value;
    }*/

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

    /*private int[] convert(String string) {
        int number[] = new int[string.length()];



        for (int i = 0; i < string.length(); i++) {
            number[i] = Integer.parseInt(string.charAt(i)); //Note charAt
        }
        return number;
    }*/

}
