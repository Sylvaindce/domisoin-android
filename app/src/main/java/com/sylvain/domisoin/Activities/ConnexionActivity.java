package com.sylvain.domisoin.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sylvain.domisoin.Fragments.Connexion.ConnexionFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ConnexionActivity extends AppCompatActivity {
    private static final String TAG = ConnexionActivity.class.getName();
    private Map<String, String> datas                 = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_AUTOLOGIN";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        datas = new LinkedHashMap<String, String>();

        checkAndRequestPermissions(true);

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String email = sharedPref.getString(getString(R.string.save_account), "email");
        String password = sharedPref.getString(getString(R.string.save_password), "password");

        if (!email.equals("email") && !password.equals("password")) {
            datas.clear();
            datas.put("email", email);
            datas.put("password", password);

            HTTPPostRequest task = new HTTPPostRequest(this, ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_login_url), datas, "");
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
                String response_code = "400";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }

                if (response.equals("0")) {
                    Toast toast = Toast.makeText(getBaseContext(), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Toast.LENGTH_LONG);
                    toast.show();
                    SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(getString(R.string.save_account));
                    editor.remove(getString(R.string.save_password));
                    editor.apply();
                    launch_connfragm();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(getString(R.string.save_account));
                    editor.remove(getString(R.string.save_password));
                    editor.apply();

                    Toast toast = Toast.makeText(getBaseContext(), "Une erreur s'est produite, veuillez essayer de nouveau. (" + response + ")", Toast.LENGTH_LONG);
                    toast.show();
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
                            Intent homeprointent = new Intent(getApplicationContext(), HomeProActivity.class);
                            homeprointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle userinfo = new Bundle();
                            userinfo.putString("json", response);
                            userinfo.putString("userid", jsonObj.getString("id"));
                            userinfo.putString("mdp", datas.get("password"));
                            homeprointent.putExtras(userinfo);
                            startActivity(homeprointent);
                        } else {
                            //isnt pro
                            Intent homeintent = new Intent(getApplicationContext(), HomeCustomerActivity.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle userinfo = new Bundle();
                            userinfo.putString("json", response);
                            userinfo.putString("userid", jsonObj.getString("id"));
                            userinfo.putString("mdp", datas.get("password"));
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

    private  boolean checkAndRequestPermissions(Boolean request) {
        ArrayList<Integer> permissions_result = new ArrayList<Integer>();
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
        List<String> listPermissionsNeeded = new ArrayList<String>();

        for (int i = 0; i < permissions.length; ++i) {
            permissions_result.add(ContextCompat.checkSelfPermission(this, permissions[i]));
            switch (i) {
                case 0:
                    if (permissions_result.get(i) != PackageManager.PERMISSION_GRANTED)
                        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    break;
                case 1:
                    if (permissions_result.get(i) != PackageManager.PERMISSION_GRANTED)
                        listPermissionsNeeded.add(Manifest.permission.INTERNET);
                    break;
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            if (request) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
            return false;
        }
        return true;
    }

}
