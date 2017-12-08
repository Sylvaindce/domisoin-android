package com.sylvain.domisoin.Fragments.Pro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Customer.AccountFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.databinding.FragmentProAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountProFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_HOME_PRO_ACTIVITY";

    private String url = "";

    private FragmentProAccountBinding fragmentProAccountBinding = null;
    private HomeProActivity homeActivity = null;
    private userInfo UserInfo = null;
    private ImageButton editInfo = null;

    private EditText account_jobtitle = null;
    private Button more = null;
    private EditText account_workphone = null;
    private EditText account_email = null;
    private EditText account_address = null;


    public AccountProFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro_account, container, false);
        homeActivity = (HomeProActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentProAccountBinding.setUser(UserInfo);

        editInfo = (ImageButton) fragmentProAccountBinding.getRoot().findViewById(R.id.account_modify_button_pro);
        editInfo.setOnClickListener(this);


        more =(Button)fragmentProAccountBinding.getRoot().findViewById(R.id.ShowDialog);
        Button Formation =(Button)fragmentProAccountBinding.getRoot().findViewById(R.id.DialogFormation);
        Button Langues = (Button)fragmentProAccountBinding.getRoot().findViewById(R.id.DialogLangues);
        Button Prix = (Button)fragmentProAccountBinding.getRoot().findViewById(R.id.DialogPrix);
        ImageButton Supprimer = (ImageButton)fragmentProAccountBinding.getRoot().findViewById(R.id.supprimer);

        Supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountProFragment.this.getActivity());
                builder.setTitle("Suppresion de compte");
                builder.setMessage("Voulez vous vraiment supprimer votre compte?");
                builder.setCancelable(false);

                builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });

                builder.setPositiveButton("Supprimer mon compte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountProFragment.this.getActivity());
                builder.setTitle("Presentation du practicien ");
                builder.setMessage("Le docteur blablabla");
                builder.setCancelable(false);

                builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


                //startActivity(new Intent(AccountFragment.this.getActivity(), pop.class));
            }
        });

        Formation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountProFragment.this.getActivity());
                builder.setTitle("Formation Du Practicien ");
                builder.setMessage("Université Paris Dauphine");
                builder.setCancelable(false);

                builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


                //startActivity(new Intent(AccountFragment.this.getActivity(), pop.class));
            }
        });

        Langues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountProFragment.this.getActivity());
                builder.setTitle("Langues Parlées");
                builder.setMessage("Anglais,Français,Arabe,Tamoul");
                builder.setCancelable(false);

                builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


                //startActivity(new Intent(AccountFragment.this.getActivity(), pop.class));
            }
        });
        Prix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountProFragment.this.getActivity());
                builder.setTitle("Prix de la consulation");
                builder.setMessage("50 Euros");
                builder.setCancelable(false);

                builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });


                AlertDialog alert=builder.create();
                alert.show();


                //startActivity(new Intent(AccountFragment.this.getActivity(), pop.class));
            }
        });

        account_jobtitle = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_jobtitle_pro);
        account_workphone = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_workphone_pro);
        account_email = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_email_pro);
        account_address = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_address_pro);

        //UserInfo.jsonAnswer.set(getArguments().getString("infofrag"));

        url = getString(R.string.api_url)+"users/";

        return fragmentProAccountBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.account_modify_button_pro:
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

    private void doUpdate() {
        userInfo userinfo = ((HomeProActivity)getActivity()).getUserInfo();

        try {
            JSONObject newjson = new JSONObject(userinfo.json.get());
            newjson.put("job_title", account_jobtitle.getText());
            newjson.put("workphone", account_workphone.getText());
            newjson.put("email", account_email.getText());
            newjson.put("adresse", account_address.getText());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, url+newjson.get("id")+"/", newjson);
            task.execute();
            ((HomeCustomerActivity)getActivity()).progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
