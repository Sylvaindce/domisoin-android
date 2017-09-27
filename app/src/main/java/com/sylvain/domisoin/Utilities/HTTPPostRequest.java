package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
 * Created by Sylvain on 23/06/2017.
 */

public class HTTPPostRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = HTTPPostRequest.class.getSimpleName();
    public static final String HTTP_RESPONSE = "HTTP_Post_Response";

    private Context mContext = null;
    private String mAction = null;
    private String mURL = null;
    private Map mData = null;

    public HTTPPostRequest(Context context, String action, String url, Map data) {
        super();
        mContext = context;
        mAction = action;
        mURL = url;
        mData = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HTTPPostHandler HTTPcall = new HTTPPostHandler();
            return  HTTPcall.makeServiceCall(mURL, mData);
        }
        catch (Exception e) {
            // TODO Quitter proprement
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Log.i(TAG, "RESULT = " + result);
        Intent intent = new Intent(mAction);
        intent.putExtra(HTTP_RESPONSE, result);
        mContext.sendBroadcast(intent);
    }
}