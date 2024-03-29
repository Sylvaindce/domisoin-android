package com.sylvain.domisoin.Fragments.Misc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.UserInfo;
import com.sylvain.domisoin.Activities.ConnexionActivity;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.Activities.SetupProActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Dialogs.AboutFragment;
import com.sylvain.domisoin.Dialogs.ModifyPwdDialog;
import com.sylvain.domisoin.Dialogs.ModifyWorkingData;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;

import org.json.JSONException;
import org.json.JSONObject;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class OptionsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = OptionsFragment.class.getName();
    private Button modify_pwd = null;
    private Button delete_acc = null;
    private Button about_domi = null;
    private Button modify_working_data = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_OPTIONSFRAG";
    private ProgressDialog progress = null;
    private userInfo UserInfo = null;
    private View view = null;



    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_options, container, false);

        modify_pwd = (Button)view.findViewById(R.id.modify_pwd);
        modify_pwd.setOnClickListener(this);
        delete_acc = (Button)view.findViewById(R.id.delete_account);
        delete_acc.setOnClickListener(this);
        about_domi = (Button)view.findViewById(R.id.aboutdomi);
        about_domi.setOnClickListener(this);
        modify_working_data = (Button) view.findViewById(R.id.modify_working_data);
        modify_working_data.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_pwd:
                ModifyPwdDialog modifyPwdDialog = new ModifyPwdDialog(UserInfo);
                modifyPwdDialog.show(getFragmentManager(), "modifypwd");
                break;
            case R.id.delete_account:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Suppression du compte")
                        .setMessage("Etes-vous certain de vouloir supprimer votre compte ? (Cette action sera irréversible)")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                HTTPDeleteRequest task = new HTTPDeleteRequest(getContext(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+UserInfo.id.get()+"/", UserInfo.token.get());
                                task.execute();
                                progress = ProgressDialog.show(getContext(), "Suppression", "Suppression en cours, merci de patienter...", true);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.aboutdomi:
                AboutFragment dialog = new AboutFragment();
                dialog.show(getFragmentManager(), "about");
                break;
            case R.id.modify_working_data:
                /*ModifyWorkingData modifyWData = new ModifyWorkingData(UserInfo);
                modifyWData.show(getFragmentManager(), "modifywd");*/
                Intent pro_config = new Intent(getContext(), SetupProActivity.class);
                pro_config.putExtra("tokenPro", UserInfo.token.get());
                pro_config.putExtra("idPro", UserInfo.id.get());
                startActivityForResult(pro_config, 2);
                break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2)
        {
            UserInfo.calendar.set(data.getStringExtra("CalendarPro"));
        }
    }

    public void setUserInfo(userInfo _User) {
        UserInfo = _User;
        if (UserInfo.is_pro.get()) {
            modify_working_data.setVisibility(View.VISIBLE);
        } else {
            modify_working_data.setVisibility(View.INVISIBLE);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            }

            if (response != null) {
                Log.d("OptionFrag", response);
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
                    Toast toast = Toast.makeText(getContext(), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    Toast toast = Toast.makeText(getContext(), "Une erreur s'est produite, veuillez essayer de nouveau. (" + ManageErrorText.manage_my_error(response) + ")", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (Integer.decode(response_code) == 204) {
                    //Delete autoconnexion
                    SharedPreferences sharedPref = getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(getString(R.string.save_account));
                    editor.remove(getString(R.string.save_password));
                    editor.apply();
                    //Launch Connexion activity
                    Intent connexionintent = new Intent(getActivity().getApplicationContext(), ConnexionActivity.class);
                    connexionintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(connexionintent);
                }
            }

        }

    };

}
