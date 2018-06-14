package com.sylvain.domisoin.Fragments.Connexion;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.JsonUtils;
import com.sylvain.domisoin.Utilities.ManageErrorText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sylvain on 23/02/2017.
 */

public class ParentFragmentSignIn extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, NameSigninProFragment.SendMessage {
    private static final String TAG = ParentFragmentSignIn.class.getSimpleName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_SIGNIN";

    private View view = null;
    private ViewPager pager = null;
    private ViewPagerAdapter vAdapter = null;
    private Button nextPage = null;
    private Button previousPage = null;

    private ProgressDialog progress = null;

    private EditText first_name = null;
    private EditText last_name= null;
    private EditText job = null;
    private Spinner job_pro = null;
    private EditText address = null;
    private EditText phone = null;
    private EditText email = null;
    private EditText password = null;

    //pro
    private EditText adeli = null;
    private CheckBox wd0 = null;
    private CheckBox wd1 = null;
    private CheckBox wd2 = null;
    private CheckBox wd3 = null;
    private CheckBox wd4 = null;
    private CheckBox wd5 = null;
    private CheckBox wd6 = null;
    private TextView begin_working_hour = null;
    private TextView end_working_hour = null;
    private TextView rdv_dur_txt = null;
    private List<Integer> day_offs = null;


    private Map<String, String> datas = null;

    private static Boolean pro_patient = false;

    public ParentFragmentSignIn() {}

    @SuppressLint("ValidFragment")
    public ParentFragmentSignIn(Boolean is_pro_pat) {
        pro_patient = is_pro_pat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_colorp_24dp);
        //upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TextView appbartitle= (TextView) getActivity().findViewById(R.id.appbartitle);
        //appbartitle.setText("Signin");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        datas = new LinkedHashMap<String, String>();

        view = inflater.inflate(R.layout.fragment_signin, container, false);

        nextPage = (Button)view.findViewById(R.id.next_signin_button);
        previousPage = (Button)view.findViewById(R.id.previous_signin_button);

        nextPage.setOnClickListener(this);
        previousPage.setOnClickListener(this);

        pager = (ViewPager)view.findViewById(R.id.viewpager_signin);
        setupViewPager(pager);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Fragment childFragment = new NameSigninFragment();
        //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.replace(R.id.child_signin_fragment_container, childFragment).commit();
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

    private void setupViewPager(ViewPager viewPager) {

        //Disable swipe
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        vAdapter = new ViewPagerAdapter(getChildFragmentManager());

        if (ParentFragmentSignIn.pro_patient){
            NameSigninFragment name = new NameSigninFragment();
            vAdapter.addFrag(name, "Name");
        } else {
            NameSigninProFragment name_pro = new NameSigninProFragment();
            vAdapter.addFrag(name_pro, "Name");
        }

        Info1SigninFragment info1 = new Info1SigninFragment();
        Info2SigninFragment info2 = new Info2SigninFragment();

        vAdapter.addFrag(info2, "info2");
        vAdapter.addFrag(info1, "info1");

        Log.d("Before SetupVP", String.valueOf(ParentFragmentSignIn.pro_patient));
        if (!ParentFragmentSignIn.pro_patient) {
            SigninProFragment pro = new SigninProFragment();
            vAdapter.addFrag(pro, "pro");
        }
        viewPager.setAdapter(vAdapter);
        viewPager.setOffscreenPageLimit(vAdapter.getCount());
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        int pos = pager.getCurrentItem();

        switch (v.getId()) {
            case R.id.next_signin_button:
                if (pos < vAdapter.getCount()-1) {
                    //add verif
                    if (editTextVerification(pager.getCurrentItem())) {
                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    }
                    //pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }else if (pos == vAdapter.getCount()-1) {
                    //add verif
                    if (editTextVerification(pager.getCurrentItem())) {
                        try {
                            getAlldata();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.previous_signin_button:
                if (pos > 0) {
                    pager.setCurrentItem(pager.getCurrentItem()-1, true);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int max = vAdapter.getCount() - 1;
        switch (position) {
            case 0:
                previousPage.setVisibility(View.INVISIBLE);
                nextPage.setVisibility(View.VISIBLE);
                break;
            default:
                previousPage.setVisibility(View.VISIBLE);
                nextPage.setVisibility(View.VISIBLE);
                break;
        }
        if (position == max) {
            //nextPage.setVisibility(View.INVISIBLE);
            nextPage.setText("Inscription");
            nextPage.setTextSize(16);
        } else {
            if (nextPage.getText() != "Suivant") {
                nextPage.setText("Suivant");
                nextPage.setTextSize(18);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getAlldata() throws JSONException {
        datas.put("email", String.valueOf(email.getText()));
        datas.put("first_name", String.valueOf(first_name.getText()));
        datas.put("last_name", String.valueOf(last_name.getText()));
        datas.put("adresse", String.valueOf(address.getText())); //modify
        datas.put("workphone", String.valueOf(phone.getText())); //modify
        datas.put("password", String.valueOf(password.getText()));

        //add pro data if pro
        if (!pro_patient) {

            datas.put("job_title", String.valueOf(String.valueOf(job_pro.getSelectedItem())));
            /*day_offs = new LinkedList<Integer>();
            day_offs.add(1);
            day_offs.add(2);
            Log.d("TOTO day_off", day_offs.toString());
            datas.put("day_offs", day_offs.toString());*/

            datas.put("event_duration", "15");
            datas.put("adeli", adeli.getText().toString());
            datas.put("start_working_hour", "8");
            //datas.put("start_working_minutes", );
            datas.put("end_working_hour", "18");
            //datas.put("end_working_minutes", end_working_hour.getText().toString().split(":")[1]);
            datas.put("is_pro", "1");
        } else {
            datas.put("job_title", String.valueOf(job.getText()));
            datas.put("is_pro", "0"); //modify
            datas.put("day_offs", "[]");
        }

        JSONObject res = JsonUtils.mapToJson(datas);

        if (!pro_patient) {
            JSONArray json_array_cares = new JSONArray();
            String pro_job = job_pro.getSelectedItem().toString();
            switch (pro_job) {
                case "Infirmier":
                    TextView duration_prisedesang = (TextView) view.findViewById(R.id.duration_prisedesang);
                    String dur_1 = duration_prisedesang.getText().toString();
                    TextView duration_toilettes = (TextView) view.findViewById(R.id.duration_toilettes);
                    String dur_2 = duration_toilettes.getText().toString();
                    TextView duration_pansements = (TextView) view.findViewById(R.id.duration_pansement);
                    String dur_3 = duration_pansements.getText().toString();
                    TextView duration_injection = (TextView) view.findViewById(R.id.duration_injection);
                    String dur_4 = duration_injection.getText().toString();

                    String[] ids = getResources().getStringArray(R.array.care_id_infirmier);
                    if (!dur_1.equals("0 min")){
                        JSONObject dur1 = new JSONObject();
                        dur1.put("id", ids[0]);
                        dur1.put("duration", Integer.valueOf(dur_1.replace(" min", "")));
                        json_array_cares.put(dur1);
                    }
                    if (!dur_2.equals("0 min")) {
                        JSONObject dur2 = new JSONObject();
                        dur2.put("id", ids[1]);
                        dur2.put("duration", Integer.valueOf(dur_2.replace(" min", "")));
                        json_array_cares.put(dur2);
                    }
                    if (!dur_3.equals("0 min")) {
                        JSONObject dur3 = new JSONObject();
                        dur3.put("id", ids[2]);
                        dur3.put("duration", Integer.valueOf(dur_3.replace(" min", "")));
                        json_array_cares.put(dur3);
                    }
                    if (!dur_4.equals("0 min")) {
                        JSONObject dur4 = new JSONObject();
                        dur4.put("id", ids[3]);
                        dur4.put("duration", Integer.valueOf(dur_4.replace(" min", "")));
                        json_array_cares.put(dur4);
                    }

                    break;
                case "Pédiatre":
                    TextView duration_bilandesante = (TextView) view.findViewById(R.id.duration_bilandesante);
                    dur_1 = duration_bilandesante.getText().toString();
                    TextView duration_desencombrement = (TextView) view.findViewById(R.id.duration_desencombrement);
                    dur_2 = duration_desencombrement.getText().toString();
                    TextView duration_medecinegenerale = (TextView) view.findViewById(R.id.duration_medecinegenerale);
                    dur_3 = duration_medecinegenerale.getText().toString();

                    ids = getResources().getStringArray(R.array.care_id_pediatre);
                    if (!dur_1.equals("0 min")){
                        JSONObject dur1 = new JSONObject();
                        dur1.put("id", ids[0]);
                        dur1.put("duration", Integer.valueOf(dur_1.replace(" min", "")));
                        json_array_cares.put(dur1);
                    }
                    if (!dur_2.equals("0 min")) {
                        JSONObject dur2 = new JSONObject();
                        dur2.put("id", ids[1]);
                        dur2.put("duration", Integer.valueOf(dur_2.replace(" min", "")));
                        json_array_cares.put(dur2);
                    }
                    if (!dur_3.equals("0 min")) {
                        JSONObject dur3 = new JSONObject();
                        dur3.put("id", ids[2]);
                        dur3.put("duration", Integer.valueOf(dur_3.replace(" min", "")));
                        json_array_cares.put(dur3);
                    }

                    break;
                case "Kinésithérapeute":
                    TextView duration_reeducation = (TextView) view.findViewById(R.id.duration_reeducation);
                    dur_1 = duration_reeducation.getText().toString();

                    TextView duration_medecinedusport = (TextView) view.findViewById(R.id.duration_medecinedusport);
                    dur_2 = duration_medecinedusport.getText().toString();

                    TextView duration_mobilisation = (TextView) view.findViewById(R.id.duration_mobilisation);
                    dur_3 = duration_mobilisation.getText().toString();
                    ids = getResources().getStringArray(R.array.care_id_kinésithérapeuthe);
                    if (!dur_1.equals("0 min")){
                        JSONObject dur1 = new JSONObject();
                        dur1.put("id", ids[0]);
                        dur1.put("duration", Integer.valueOf(dur_1.replace(" min", "")));
                        json_array_cares.put(dur1);
                    }
                    if (!dur_2.equals("0 min")) {
                        JSONObject dur2 = new JSONObject();
                        dur2.put("id", ids[1]);
                        dur2.put("duration", Integer.valueOf(dur_2.replace(" min", "")));
                        json_array_cares.put(dur2);
                    }
                    if (!dur_3.equals("0 min")) {
                        JSONObject dur3 = new JSONObject();
                        dur3.put("id", ids[2]);
                        dur3.put("duration", Integer.valueOf(dur_3.replace(" min", "")));
                        json_array_cares.put(dur3);
                    }
                    break;
            }
            res.put("cares", json_array_cares);
            Log.d("JSON_RESUME", res.toString());
        }
        /*for(int i = 0; i < datas.size(); ++i) {
            Log.d("ParentFragm ALL DATA", datas.keySet().toArray()[i] + " " + datas.values().toArray()[i]);
        }*/

        HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url), res, "");
        task.execute();
        progress = ProgressDialog.show(getActivity(), "Création de l'utilisateur", "Création en cours, merci de patienter...", true);
    }

    private Boolean editTextVerification(int position) {
        Boolean versul = false;
        Log.d("Position", String.valueOf(position));

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        EditText cpassword = (EditText) view.findViewById(R.id.cpassword);

        switch (position) {
            case 0:
                if (ParentFragmentSignIn.pro_patient) {
                    first_name = (EditText) view.findViewById(R.id.firstname);
                    last_name = (EditText) view.findViewById(R.id.lastname);
                    job = (EditText) view.findViewById(R.id.job);
                    if (last_name.getText().toString().length() == 0)
                        last_name.setError("Un Nom de famille valide est requis");
                    else if (first_name.getText().toString().length() == 0)
                        first_name.setError("Un Prénom valide est requis");
                    else if (job.getText().toString().length() == 0)
                        job.setError("Une Profession valide est requise");
                    else
                        versul = true;
                } else {
                    first_name = (EditText) view.findViewById(R.id.firstname_pro);
                    last_name = (EditText) view.findViewById(R.id.lastname_pro);
                    job_pro = (Spinner) view.findViewById(R.id.job_pro);
                    if (last_name.getText().toString().length() == 0)
                        last_name.setError("Un Nom de famille valide est requis");
                    else if (first_name.getText().toString().length() == 0)
                        first_name.setError("Un Prénom valide est requis");
                    else
                        versul = true;
                }
                break;
            case 1:
                address = (EditText) view.findViewById(R.id.address);
                phone = (EditText) view.findViewById(R.id.phone);
                if( address.getText().toString().length() == 0 || !address.getText().toString().contains(","))
                    address.setError("Une Adresse valide est requise");
                else if( phone.getText().toString().length() != 10 )
                    phone.setError("Un Numéro valide est requis");
                else
                    versul = true;
                break;
            case 2:

                if (TextUtils.isEmpty(email.getText()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Une adresse email valide est requise");
                } else if (password.getText().length() < 4) {
                    password.setError("Un mot de passe de plus de 4 caractères est requis");
                }
                else if (TextUtils.isEmpty(password.getText()) || !password.getText().toString().equals(cpassword.getText().toString())) {
                    password.setError("Un mot de passe valide est requis. Le mot de passe de confirmation doit être identique au mot de passe renseigné");
                }
                else
                    versul = true;
                break;

            case 3:
                versul = true;
                //ADELI
                adeli = (EditText)view.findViewById(R.id.adeli);
                if (adeli.getText().length() < 6) {
                    adeli.setError("Mauvais numéro ADELI");
                    versul = false;
                }
                String pro_job = job_pro.getSelectedItem().toString();
                EditText care_title = (EditText) view.findViewById(R.id.cares_title_signin);
                switch (pro_job) {
                    case "Infirmier":
                        TextView duration_prisedesang = (TextView)view.findViewById(R.id.duration_prisedesang);
                        String dur_1 = duration_prisedesang.getText().toString();
                        TextView duration_toilettes = (TextView)view.findViewById(R.id.duration_toilettes);
                        String dur_2 = duration_toilettes.getText().toString();
                        TextView duration_pansements = (TextView)view.findViewById(R.id.duration_pansement);
                        String dur_3 = duration_pansements.getText().toString();
                        TextView duration_injection = (TextView)view.findViewById(R.id.duration_injection);
                        String dur_4 = duration_injection.getText().toString();
                        int res = 0;
                        if (dur_1.equals("0 min")){
                            ++res;
                        }
                        if (dur_2.equals("0 min")) {
                            ++res;
                        }
                        if (dur_3.equals("0 min")) {
                            ++res;
                        }
                        if (dur_4.equals("0 min")) {
                            ++res;
                        }
                        if (res == 4) {
                            versul = false;
                            care_title.setError("Veuillez choisir un type de soin au minimum.");
                        }
                        break;
                    case "Pédiatre":
                        TextView duration_bilandesante = (TextView)view.findViewById(R.id.duration_bilandesante);
                        dur_1 = duration_bilandesante.getText().toString();
                        TextView duration_desencombrement = (TextView)view.findViewById(R.id.duration_desencombrement);
                        dur_2 = duration_desencombrement.getText().toString();
                        TextView duration_medecinegenerale = (TextView)view.findViewById(R.id.duration_medecinegenerale);
                        dur_3 = duration_medecinegenerale.getText().toString();

                        res = 0;
                        if (dur_1.equals("0 min")){
                            ++res;
                        }
                        if (dur_2.equals("0 min")) {
                            ++res;
                        }
                        if (dur_3.equals("0 min")) {
                            ++res;
                        }
                        if (res == 3) {
                            versul = false;
                            care_title.setError("Veuillez choisir un type de soin au minimum.");
                        }
                        break;
                    case "Kinésithérapeute":
                        TextView duration_reeducation = (TextView)view.findViewById(R.id.duration_reeducation);
                        dur_1 = duration_reeducation.getText().toString();

                        TextView duration_medecinedusport = (TextView)view.findViewById(R.id.duration_medecinedusport);
                        dur_2 = duration_medecinedusport.getText().toString();

                        TextView duration_mobilisation = (TextView)view.findViewById(R.id.duration_mobilisation);
                        dur_3 = duration_mobilisation.getText().toString();
                        res = 0;
                        if (dur_1.equals("0 min")){
                            ++res;
                        }
                        if (dur_2.equals("0 min")) {
                            ++res;
                        }
                        if (dur_3.equals("0 min")) {
                            ++res;
                        }
                        if (res == 3) {
                            versul = false;
                            care_title.setError("Veuillez choisir un type de soin au minimum.");
                        }
                        break;
                }

                Log.d(TAG, adeli.getText().toString());

            default:
                break;
        }

        return versul;
    }

    public void setPro_Patient(Boolean q) {
        pro_patient = q;
        Log.d("In Parent SignIn", String.valueOf(pro_patient));
    }

    @Override
    public void sendData(String message) {
        SigninProFragment f = (SigninProFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pro");
        f = (SigninProFragment) vAdapter.mFragmentList.get(3);
        f.displayReceivedData(message);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            }
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
                    Toast toast = Toast.makeText(getContext(), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    Toast toast = Toast.makeText(getContext(), "Une erreur s'est produite, veuillez essayer de nouveau. (" + ManageErrorText.manage_my_error(response) + ")", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    //go to login
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new LoginFragment(), "LoginFragment()");
                    ft.addToBackStack("login");
                    ft.commit();
                }
            }
        }
    };

}
