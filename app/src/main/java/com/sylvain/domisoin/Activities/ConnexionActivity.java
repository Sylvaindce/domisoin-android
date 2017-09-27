package com.sylvain.domisoin.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sylvain.domisoin.Fragments.ConnexionFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ConnexionActivity extends AppCompatActivity {
    private static final String TAG = ConnexionActivity.class.getName();
    private Map<String, String> datas                 = null;
    private static String LOGIN_URL                   = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_AUTOLOGIN";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LOGIN_URL = getString(R.string.api_url)+"auth/login/";
        datas = new LinkedHashMap<String, String>();

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String email = sharedPref.getString(getString(R.string.save_account), "email");
        String password = sharedPref.getString(getString(R.string.save_password), "password");

        if (!email.equals("email") && !password.equals("password")) {
            datas.clear();
            datas.put("email", email);
            datas.put("password", password);

            HTTPPostRequest task = new HTTPPostRequest(this, ACTION_FOR_INTENT_CALLBACK, LOGIN_URL, datas);
            task.execute();
            progress = ProgressDialog.show(this, "Authentification", "Vérification en cours, merci de patienter...", true);
        } else {
            launch_connfragm();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.v("onOptions", "backpressed");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void launch_connfragm() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ConnexionFragment frag1 = new ConnexionFragment();
        ft.add(R.id.fragment_container, frag1);
        ft.commit();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);

            if (response != null) {
                if (response.length() == 3) {
                    SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(getString(R.string.save_account));
                    editor.remove(getString(R.string.save_password));
                    editor.apply();

                    Snackbar.make(findViewById(android.R.id.content), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                    launch_connfragm();
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        Log.d(TAG, jsonObj.getString("is_pro"));

                        /*SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.save_account), datas.get("email"));
                        editor.putString(getString(R.string.save_password), datas.get("password"));
                        editor.apply();*/

                        if (jsonObj.getBoolean("is_pro")) {
                            //is pro
                        } else {
                            //isnt pro
                            Intent homeintent = new Intent(getApplicationContext(), HomeActivity.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle userinfo = new Bundle();
                            userinfo.putString("json", response);
                            userinfo.putString("userid", jsonObj.getString("id"));
                            homeintent.putExtras(userinfo);
                            startActivity(homeintent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

}
