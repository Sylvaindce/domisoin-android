package com.sylvain.domisoin.Fragments.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sylvain.domisoin.Activities.ConnexionActivity;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Pro.AccountProFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.databinding.FragmentAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class AccountFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_HOME_CUSTOMER_ACTIVITY";

    private FragmentAccountBinding fragmentAccountBinding = null;
    private HomeCustomerActivity homeActivity = null;
    private userInfo UserInfo = null;
    private ImageButton editInfo = null;

    private EditText account_jobtitle = null;
    private EditText account_workphone = null;
    private EditText account_email = null;
    private EditText account_address = null;
    private ImageButton supprimer = null;
    private static String DELETE_URL = null;
    private ProgressDialog progress;
    private String m_Text = "";



    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        homeActivity = (HomeCustomerActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentAccountBinding.setUser(UserInfo);

        editInfo = (ImageButton) fragmentAccountBinding.getRoot().findViewById(R.id.account_modify_button);
        editInfo.setOnClickListener(this);
        supprimer = (ImageButton) fragmentAccountBinding.getRoot().findViewById(R.id.supprimer);
        supprimer.setOnClickListener(this);

        account_jobtitle = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_jobtitle);
        account_workphone = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_workphone);
        account_email = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_email);
        account_address = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_address);

        //UserInfo.jsonAnswer.set(getArguments().getString("infofrag"));
        DELETE_URL = getString(R.string.api_users_url)+UserInfo.id.get()+"/";

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountFragment.this.getActivity());
                builder.setTitle("Suppresion de compte");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setMessage("Voulez vous vraiment supprimer votre compte?");
                builder.setCancelable(false);

                builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        m_Text = input.getText().toString();

                        //code here

                    }
                });

                builder.setPositiveButton("Supprimer mon compte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "suppresion");
                        HTTPDeleteRequest task = new HTTPDeleteRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, DELETE_URL,UserInfo.token.get());
                        task.execute();
                        progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);
                        SharedPreferences sharedPref = getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(getString(R.string.save_account));
                        editor.remove(getString(R.string.save_password));
                        editor.apply();
                        Intent connIntent = new Intent(getActivity(), ConnexionActivity.class);
                        connIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(connIntent);

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


            }
        });



        return fragmentAccountBinding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
         /*   case R.id.supprimer:
                Log.d(TAG, "suppresion");
                HTTPDeleteRequest task = new HTTPDeleteRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, DELETE_URL,UserInfo.token.get());
                task.execute();
                progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);
                SharedPreferences sharedPref = getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.save_account));
                editor.remove(getString(R.string.save_password));
                editor.apply();
                Intent connIntent = new Intent(getActivity(), ConnexionActivity.class);
                connIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(connIntent);
                break;*/
            case R.id.account_modify_button:
                Log.d(TAG, "account modify info");
                if (account_jobtitle.isEnabled()) {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_create_black_24dp));
                    account_jobtitle.setEnabled(false);
                    account_workphone.setEnabled(false);
                    account_email.setEnabled(false);
                    account_address.setEnabled(false);
                    doUpdate();
                } else {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
                    account_jobtitle.setEnabled(true);
                    account_workphone.setEnabled(true);
                    account_email.setEnabled(true);
                    account_address.setEnabled(true);
                }
                break;
        }

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (progress != null)
            {
                Toast.makeText(getActivity(),"suppresion du compte" ,Toast.LENGTH_LONG).show();
                progress.dismiss();
            }

            String response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);

            if (response != null) {
                if (response.length() == 3) {
                    Snackbar.make(fragmentAccountBinding.getRoot(), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        }

        ;
    };
    private void doUpdate() {
        userInfo userinfo = ((HomeCustomerActivity)getActivity()).getUserInfo();

        try {
            JSONObject newjson = new JSONObject(userinfo.json.get());
            newjson.put("job_title", account_jobtitle.getText());
            newjson.put("workphone", account_workphone.getText());
            newjson.put("email", account_email.getText());
            newjson.put("adresse", account_address.getText());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+newjson.get("id")+"/", newjson, UserInfo.token.get());
            task.execute();
            ((HomeCustomerActivity)getActivity()).progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

