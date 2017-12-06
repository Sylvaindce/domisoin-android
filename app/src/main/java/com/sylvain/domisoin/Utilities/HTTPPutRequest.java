package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Sylvain on 23/06/2017.
 */

public class HTTPPutRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = HTTPPutRequest.class.getSimpleName();
    public static final String HTTP_RESPONSE = "HTTP_Put_Response";

    private Context mContext = null;
    private String mAction = null;
    private String mURL = null;
    private JSONObject mData = null;

    public HTTPPutRequest(Context context, String action, String url, JSONObject data) {
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
            HTTPPutHandler HTTPcall = new HTTPPutHandler();
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