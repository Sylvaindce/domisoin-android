package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Fragments.Customer.MoreProDetails2Fragment;
import com.sylvain.domisoin.Fragments.Customer.MoreProDetailsFragment;
import com.sylvain.domisoin.Fragments.Customer.SearchFragment;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sylvain on 05/07/2017.
 */

public class ProMore extends DialogFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, ButtonInterface {
    private static final String TAG = ProMore.class.getName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PROMORE";
    private View dialogFragment = null;
    private UserModel _user = null;

    public ViewPager viewpager = null;
    private ViewPagerAdapter vAdapter = null;

    private Button nextPage = null;
    private Button previousPage = null;

    //private String ourDate = "";
    public String ourBeginDate = "";
    public String ourEndDate = "";

    public String userid_1 = "";

    private Map<String, String> datas = null;

    private ProgressDialog progress = null;

    private SearchFragment mainFrag = null;

    private ButtonInterface buttonInterface = null;
    private String mToken = null;

    public ProMore(){}

    @SuppressLint("ValidFragment")
    public ProMore(String token){
        mToken = token;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogFragment = inflater.inflate(R.layout.professional_form, container, false);
        getDialog().setTitle("Plus d'informations");
        getDialog().setCancelable(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView name = (TextView) dialogFragment.findViewById(R.id.pro_more_name);
        name.setText(_user.getFirst_name() + " " + _user.getLast_name());
        TextView profession = (TextView) dialogFragment.findViewById(R.id.pro_more_job);
        profession.setText(_user.getJob_title());



        /*TextView address = (TextView) dialogFragment.findViewById(R.id.pro_more_address);
        address.setText(_user.getAddress());*/


        /*Button delete_button = (Button) dialogFragment.findViewById(R.id.delete_appointment_button);
        delete_button.setOnClickListener(this);

        Button close_button = (Button) dialogFragment.findViewById(R.id.close_appointment_more_button);
        close_button.setOnClickListener(this);*/

        nextPage = (Button)dialogFragment.findViewById(R.id.next_pro_button);
        previousPage = (Button)dialogFragment.findViewById(R.id.previous_pro_button);
        nextPage.setOnClickListener(this);
        previousPage.setOnClickListener(this);

        datas = new LinkedHashMap<String, String>();

        return dialogFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.50f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
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
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        int screenHeight = (int) (metrics.heightPixels * 0.95);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);

        viewpager = (ViewPager) dialogFragment.findViewById(R.id.pro_more_viewpager);
        setupViewPager(viewpager);
    }

    public void setOurDate(String _ourBeginDate, String _ourEndDate) {
        Log.d(TAG, _ourBeginDate + " " + _ourEndDate);
        ourBeginDate = _ourBeginDate;
        ourEndDate = _ourEndDate;
    }

    public String getOurBeginDate() {
        return ourBeginDate;
    }

    public String getOurEndDate() {
        return ourEndDate;
    }


    @Override
    public void onClick(View v) {
        int pos = viewpager.getCurrentItem();

        switch (v.getId()) {
            case R.id.next_pro_button:
                if (pos < vAdapter.getCount()-1) {
                    //add verif
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
                    //pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }else if (pos == vAdapter.getCount()-1) {
                    //add verif
                    //if (editTextVerification(pager.getCurrentItem())) {
                    //getAlldata();

                    EditText desc = (EditText)dialogFragment.findViewById(R.id.pro_more_desc);
                    Log.d("RESUME", desc.getText().toString());
                    //Log.d("RESUME", ourDate + " " + ourBeginHours + " " + ourEndHours);
                    Log.d("RESUME", ourBeginDate + " " + ourEndDate);
                    Log.d("RESUME", _user.getId() + " " + userid_1);

                    datas.put("description", desc.getText().toString());
                    datas.put("client", _user.getId());
                    datas.put("author", userid_1);
                    datas.put("type", "RDV");
                    datas.put("start_date", ourBeginDate); //modify
                    datas.put("end_date", ourEndDate); //modify
                    datas.put("location", "Paris, France"); //modify

                    HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_events_url), datas, mToken);
                    task.execute();
                    progress = ProgressDialog.show(getActivity(), "Reservation", "Reservation en cours, merci de patienter...", true);
                    //}
                }
                break;
            case R.id.previous_pro_button:
                if (pos > 0) {
                    viewpager.setCurrentItem(viewpager.getCurrentItem()-1, true);
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
                nextPage.setVisibility(View.INVISIBLE);
                break;
            default:
                previousPage.setVisibility(View.VISIBLE);
                nextPage.setVisibility(View.VISIBLE);
                break;
        }
        /*if (position == max) {
            //nextPage.setVisibility(View.INVISIBLE);
            nextPage.setText("Inscription");
            nextPage.setTextSize(16);
        } else {
            if (nextPage.getText() != "Suivant") {
                nextPage.setText("Suivant");
                nextPage.setTextSize(18);
            }
        }*/
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setUserid_1(String id) {
        userid_1 = id;
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

        viewPager.setOffscreenPageLimit(1);

        MoreProDetailsFragment details1 = new MoreProDetailsFragment();
        details1.setInterface(this);
        MoreProDetails2Fragment details2 = new MoreProDetails2Fragment();

        vAdapter.addFrag(details1, "details1");
        vAdapter.addFrag(details2, "details2");
        viewPager.setAdapter(vAdapter);
        viewPager.setOffscreenPageLimit(vAdapter.getCount());
        viewPager.addOnPageChangeListener(this);
    }

    public void set_user(UserModel apt) {
        this._user = apt;
    }

    public UserModel get_user() {
        return _user;
    }

    public void setButtonInterface(ButtonInterface ourInterface) {
        this.buttonInterface = ourInterface;
    }

    @Override
    public void buttonClicked(View v) {}

    @Override
    public void onBookClick(String _ourBeginDate, String _ourEndDate) {
        setOurDate(_ourBeginDate, _ourEndDate);
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
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
                if (Integer.decode(response_code) > 226 ) {
                    Snackbar.make(getActivity().findViewById(R.id.viewpager_signin), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. ("+response+")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    dismiss();
                    Log.d("Our Response", response);
                    try {
                        JSONArray tmp = new JSONArray(((HomeCustomerActivity)getActivity()).getUserInfo().events.get());
                        JSONObject tmp2 = new JSONObject(response);
                        tmp.put(tmp2);
                        ((HomeCustomerActivity)getActivity()).getUserInfo().events.set(tmp.toString());
                        //to update pro json
                        buttonInterface.buttonClicked(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

}
