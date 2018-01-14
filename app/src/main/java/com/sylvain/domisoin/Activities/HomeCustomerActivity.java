package com.sylvain.domisoin.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Customer.AccountFragment;
import com.sylvain.domisoin.Fragments.Customer.MainChatFragment;
import com.sylvain.domisoin.Fragments.Customer.PlanningFragment;
import com.sylvain.domisoin.Fragments.Customer.SearchFragment;
import com.sylvain.domisoin.Fragments.Customer.SettingsChatMenuFragment;
import com.sylvain.domisoin.Fragments.Misc.OptionsFragment;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HomeCustomerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener { //ViewPager.OnPageChangeListener,  ButtonInterface
    private static final String TAG = HomeCustomerActivity.class.getSimpleName();

    private TabLayout tabLayout;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_HOME_CUSTOMER_ACTIVITY";

    private Intent homeIntent = null;

    public userInfo UserInfo = null;

    //public String name = "lol";

    public FloatingActionButton fab = null;

    public ProgressDialog progress = null;

    private BottomSheetBehavior mBottomSheetBehavior = null;
    private View bottomSheet = null;
    private int height = 0;
    private int width = 0;
    private Double mheight = 0.0;
    private Drawable oldDrawable = null;
    private AppCompatImageButton deconnexionButton = null;
    private OptionsFragment frag1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home_customer);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().show();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUserData();

        fab = (FloatingActionButton) findViewById(R.id.fab_cust);
        fab.setOnClickListener(this);

        deconnexionButton = (AppCompatImageButton) findViewById(R.id.deconnexion_button_customer);
        deconnexionButton.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager_home_customer);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_home_customer);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        //HTTPGetRequest task = new HTTPGetRequest(this, ACTION_FOR_INTENT_CALLBACK, ACCOUNT_URL + UserInfo.id.get());
        //task.execute();
        //progress = ProgressDialog.show(this, "Information", "Téléchargement en cours, merci de patienter...", true);

        /*DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bottomSheet = findViewById(R.id.bottom_sheet_customer);
        mheight = Double.valueOf(height - getSupportActionBar().getHeight() - (height * 0.2));
        ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
        params.height = mheight.intValue();
        params.width = width;
        bottomSheet.setLayoutParams(params);*/
        bottomSheet = findViewById(R.id.bottom_sheet_customer);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        frag1 = new OptionsFragment();
        ft.replace(R.id.bottom_sheet_customer, frag1, "Options");
        ft.commit();
    }

    private void setupTabIcons() {
        //ImageView tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon, null);
        //tabOne.setImageResource(R.drawable.ic_event_black_24dp);
        //tabLayout.getTabAt(0).setCustomView(tabOne);

        //ImageView tabTwo = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon, null);
        //tabTwo.setImageResource(R.drawable.ic_search_blue_24dp);
        //tabLayout.getTabAt(1).setCustomView(tabTwo);

        //ImageView tabThree = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon, null);
        //tabThree.setImageResource(R.drawable.ic_account_circle_black_24dp);
        //tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabOne.setText("Agenda");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_event_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabTwo.setText("Rechercher");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search_blue_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabThree.setText("Compte");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_account_circle_black_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        AccountFragment accountFragment = new AccountFragment();
        Bundle info = new Bundle();
        info.putString("infofrag", homeIntent.getExtras().getString("info"));
        accountFragment.setArguments(info);

        SearchFragment searchFragment = new SearchFragment();

        PlanningFragment planningFragment = new PlanningFragment();

        adapter.addFrag(planningFragment, "Agenda");
        adapter.addFrag(searchFragment, "Rechercher");
        adapter.addFrag(accountFragment, "Compte");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_cust:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    oldDrawable = fab.getDrawable();
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_clear_white_24dp));
                    frag1.setUserInfo(UserInfo);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab.setImageDrawable(oldDrawable);
                }
                break;
            case R.id.deconnexion_button_customer:
                SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.save_account));
                editor.remove(getString(R.string.save_password));
                editor.apply();
                Intent connIntent = new Intent(this, ConnexionActivity.class);
                connIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(connIntent);
                break;
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //final ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();

        if (position == 2) {

            fab.setVisibility(View.VISIBLE);
            /*final SettingsChatMenuFragment frag1 = new SettingsChatMenuFragment();
            frag1.setInterface(this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    params.height = 350;
                    params.width = width;
                    bottomSheet.setLayoutParams(params);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //SettingsChatMenuFragment frag1 = new SettingsChatMenuFragment();
                    ft.replace(R.id.bottom_sheet_chat_settings, frag1, "SettingChatMenuFragment");
                    ft.commit();
                    //frag1.setInterface(this);
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_more_white_24dp));
                    oldDrawable = fab.getDrawable();
                }
            });*/
        } else {
            fab.setVisibility(View.INVISIBLE);
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab.setImageDrawable(oldDrawable);
            }
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainChatFragment myFragment = (MainChatFragment) getSupportFragmentManager().findFragmentByTag("MainChatFragment");
                    if (myFragment != null && myFragment.isVisible()) {
                        //Log.d("HomeActivity", "MainChatFragment already load");
                    } else {
                        params.height = mheight.intValue();
                        params.width = width;
                        bottomSheet.setLayoutParams(params);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        MainChatFragment frag1 = new MainChatFragment();
                        ft.replace(R.id.bottom_sheet_chat_settings, frag1, "MainChatFragment");
                        ft.commit();
                        fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_chat_white_24dp));
                        oldDrawable = fab.getDrawable();
                    }
                }
            });*/
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public userInfo getUserInfo() {
        return UserInfo;
    }

    private void setUserData() {
        homeIntent = getIntent();
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String value = homeIntent.getExtras().getString("json");
        Log.d("HOME", value);

        String mdp = homeIntent.getExtras().getString("mdp");

        UserInfo = new userInfo();
        UserInfo.mdp.set(mdp);
        UserInfo.json.set(homeIntent.getExtras().getString("json"));

        JSONObject myjson = null;
        try {
            myjson = new JSONObject(UserInfo.json.get());
            updateUserInfo(myjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUserInfo(JSONObject myjson) {
        try {
            if (!myjson.has("token")) {
                myjson.put("token", UserInfo.token.get());
            }
            UserInfo.json.set(myjson.toString());
            UserInfo.job_title.set(myjson.getString("job_title"));
            UserInfo.workphone.set(myjson.getString("workphone"));
            UserInfo.events.set(myjson.getString("events"));
            UserInfo.email.set(myjson.getString("email"));
            UserInfo.first_name.set(myjson.getString("first_name"));
            UserInfo.last_name.set(myjson.getString("last_name"));
            UserInfo.is_pro.set(myjson.getBoolean("is_pro"));
            UserInfo.address.set(myjson.getString("adresse"));
            UserInfo.profile_img.set(myjson.getString("profile_img"));
            UserInfo.id.set(myjson.getString("id"));
            UserInfo.token.set(myjson.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*@Override
    public void buttonClicked(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
        params.height = mheight.intValue();
        params.width = width;
        switch (v.getId()) {
            case R.id.chat_button:
                Log.v("OnClick", "Chat Button");
                bottomSheet.setLayoutParams(params);
                ft.replace(R.id.bottom_sheet_chat_settings, new MainChatFragment(), "MainChatFragment2");
                ft.addToBackStack("MainChatFragment");
                ft.commit();
                break;
            case R.id.settings_button:
                Log.v("OnClick", "Settings Button");
                break;
        }

    }*/

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            if (response == null){
                response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            }
            Log.i(TAG, "RESPONSE = " + response);
            if (response != null) {
                String response_code = "";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }
                if (Integer.decode(response_code) > 226 ) {
                    try {
                        JSONObject jsonObject = new JSONObject(UserInfo.json.get());
                        updateUserInfo(jsonObject);
                        Snackbar.make(findViewById(android.R.id.content), "Une erreur s'est produite, veuillez essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        updateUserInfo(jsonObj);
                        Snackbar.make(findViewById(android.R.id.content), "Mise à jour effectuée avec succès.", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.GREEN)
                                .show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("GetItem ViewPager", String.valueOf(position));
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
}
