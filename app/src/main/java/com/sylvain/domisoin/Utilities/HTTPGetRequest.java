package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


/**
 * Created by Sylvain on 27/11/2016.
 */

public class HTTPGetRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = HTTPGetRequest.class.getSimpleName();

    public static final String HTTP_RESPONSE = "HTTP_Get_Response";

    private Context mContext = null;
    private String mAction = null;
    private String mURL = null;

    public HTTPGetRequest(Context context, String action, String url) {
        super();
        mContext = context;
        mAction = action;
        mURL = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HTTPGetHandler HTTPcall = new HTTPGetHandler();

            //HttpUriRequest request = params[0];
            //HttpResponse serverResponse = mClient.execute(request);
            //BasicResponseHandler handler = new BasicResponseHandler();
            //return handler.handleResponse(serverResponse);

            return  HTTPcall.makeServiceCall(mURL);
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
