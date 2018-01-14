package com.sylvain.domisoin.Fragments.Connexion;

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
import android.widget.EditText;

import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sylvain on 23/02/2017.
 */

public class ParentFragmentSignIn extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
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
    private EditText address = null;
    private EditText phone = null;
    private EditText email = null;
    private EditText password = null;

    private Map<String, String> datas = null;

    private Boolean pro_patient = false;

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

        NameSigninFragment name = new NameSigninFragment();
        Info1SigninFragment info1 = new Info1SigninFragment();
        Info2SigninFragment info2 = new Info2SigninFragment();


        vAdapter.addFrag(name, "Name");
        vAdapter.addFrag(info2, "info2");
        vAdapter.addFrag(info1, "info1");

        if (!pro_patient) {
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
                    //if (editTextVerification(pager.getCurrentItem())) {
                        getAlldata();
                    //}
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

    private void getAlldata() {
        datas.put("email", String.valueOf(email.getText()));
        datas.put("first_name", String.valueOf(first_name.getText()));
        datas.put("last_name", String.valueOf(last_name.getText()));
        datas.put("job_title", String.valueOf(job.getText()));
        datas.put("adresse", String.valueOf(address.getText())); //modify
        datas.put("workphone", String.valueOf(phone.getText())); //modify
        datas.put("is_pro", "0"); //modify
        datas.put("password", String.valueOf(password.getText()));

        /*for(int i = 0; i < datas.size(); ++i) {
            Log.d("ParentFragm ALL DATA", datas.keySet().toArray()[i] + " " + datas.values().toArray()[i]);
        }*/

        HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url), datas, "");
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
                first_name = (EditText) view.findViewById(R.id.firstname);
                last_name = (EditText) view.findViewById(R.id.lastname);
                job = (EditText) view.findViewById(R.id.job);
                if( last_name.getText().toString().length() == 0 )
                    last_name.setError("Un Nom de famille valide est requis");
                else if( first_name.getText().toString().length() == 0 )
                    first_name.setError("Un Prénom valide est requis");
                else if( job.getText().toString().length() == 0 )
                    job.setError("Une Profession valide est requise");
                else
                    versul = true;
                break;
            case 1:
                address = (EditText) view.findViewById(R.id.address);
                phone = (EditText) view.findViewById(R.id.phone);
                if( address.getText().toString().length() == 0 )
                    address.setError("Une Adresse valide est requise");
                else if( phone.getText().toString().length() != 10 )
                    phone.setError("Un Numéro valide est requis");
                else
                    versul = true;
                break;
            case 2:

                if (TextUtils.isEmpty(email.getText()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Une adresse email valide est requise");
                }
                else if (TextUtils.isEmpty(password.getText()) || !password.getText().toString().equals(cpassword.getText().toString())) {
                    password.setError("Un mot de passe valide est requis. Le mot de passe de confirmation doit être identique au mot de passe renseigné");
                }
                else
                    versul = true;
                break;
            default:
                break;
        }

        return versul;
    }

    public void setPro_Patient(Boolean q) {
        pro_patient = q;
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
            Log.i(TAG, "RESPONSE = " + response);
            if (response != null) {
                String response_code = "-1";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }
                if (Integer.decode(response) == 0) {
                    Snackbar.make(view.findViewById(R.id.viewpager_signin), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    Snackbar.make(view.findViewById(R.id.viewpager_signin), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. ("+response+")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
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
