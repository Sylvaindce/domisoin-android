package com.sylvain.domisoin.Utilities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sylvain on 23/02/2017.
 */

public class OurBroadcastReceiver extends BroadcastReceiver{

    private ProgressDialog progress;

    private HomeCustomerActivity homeActivity = null;

    public OurBroadcastReceiver() {
    }

    public void setHomeActivity(HomeCustomerActivity ha) {
        homeActivity = ha;
        progress = ProgressDialog.show(homeActivity, "Information", "Téléchargement en cours, merci de patienter...", true);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (progress != null) {
            progress.dismiss();
        }
        String response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
        Log.i("HomeActivity", "RESPONSE = " + response);

        if (response != null) {
            /*try {
                JSONObject jsonObj = new JSONObject(response);

                Log.e("HomeActivity", String.valueOf(jsonObj));

                String state = jsonObj.getString("state");
                String userid = jsonObj.getString("userid");

                homeActivity.UserInfo.name.set(jsonObj.getString("firstname")+" "+jsonObj.getString("lastname"));
                homeActivity.UserInfo.email.set(jsonObj.getString("email"));
                homeActivity.UserInfo.address.set(jsonObj.getString("address"));
                homeActivity.UserInfo.phone.set(jsonObj.getString("phone"));

                Log.i("HomeActivity1", homeActivity.UserInfo.phone.get());

            } catch (final JSONException e) {
                Log.e("Error", "Json parsing error: " + e.getMessage());
            }*/
        }
        else {
            //error get answer
        }
    }
}
