package com.sylvain.domisoin.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sylvain.domisoin.Fragments.Customer.MoreProDetailsCares;
import com.sylvain.domisoin.Fragments.Pro.Workday.WorkHourFragment;
import com.sylvain.domisoin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SetupProActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TabLayout tabLayout;
    public ViewPager viewPager;
    public SetupProActivity.ViewPagerAdapter adapter;

    private CheckBox lundi = null;
    private CheckBox mardi = null;
    private CheckBox mercredi = null;
    private CheckBox jeudi = null;
    private CheckBox vendredi = null;
    private CheckBox samedi = null;
    private CheckBox dimanche = null;
    private JSONObject hours_json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pro);
        viewPager = (ViewPager) findViewById(R.id.vp_setup_pro);
        viewPager.setOffscreenPageLimit(7);
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs_pro_hours);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        lundi = (CheckBox)findViewById(R.id.wd0);
        lundi.setOnCheckedChangeListener(this);
        mardi = (CheckBox)findViewById(R.id.wd1);
        mardi.setOnCheckedChangeListener(this);
        mercredi = (CheckBox)findViewById(R.id.wd2);
        mercredi.setOnCheckedChangeListener(this);
        jeudi = (CheckBox)findViewById(R.id.wd3);
        jeudi.setOnCheckedChangeListener(this);
        vendredi = (CheckBox)findViewById(R.id.wd4);
        vendredi.setOnCheckedChangeListener(this);
        samedi = (CheckBox)findViewById(R.id.wd5);
        samedi.setOnCheckedChangeListener(this);
        dimanche = (CheckBox)findViewById(R.id.wd6);
        dimanche.setOnCheckedChangeListener(this);
        LinearLayout tabBar = ((LinearLayout)tabLayout.getChildAt(0));
        for (int i = 1; i < tabBar.getChildCount(); ++i) {
            tabBar.getChildAt(i).setClickable(false);
            tabBar.getChildAt(i).setEnabled(false);
        }
        Button suivant = (Button) findViewById(R.id.next_pro_hour_button);
        suivant.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SetupProActivity.ViewPagerAdapter(getSupportFragmentManager());

        WorkHourFragment t1 = new WorkHourFragment("Lundi");
        WorkHourFragment t2 = new WorkHourFragment("Mardi");
        WorkHourFragment t3 = new WorkHourFragment("Mercredi");
        WorkHourFragment t4 = new WorkHourFragment("Jeudi");
        WorkHourFragment t5 = new WorkHourFragment("Vendredi");
        WorkHourFragment t6 = new WorkHourFragment("Samedi");
        WorkHourFragment t7 = new WorkHourFragment("Dimanche");

        adapter.addFrag(t1, "WKD0");
        adapter.addFrag(t2, "WKD1");
        adapter.addFrag(t3, "WKD2");
        adapter.addFrag(t4, "WKD3");
        adapter.addFrag(t5, "WKD4");
        adapter.addFrag(t6, "WKD5");
        adapter.addFrag(t7, "WKD6");

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabOne.setText("L");
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabTwo.setText("M");
        tabLayout.getTabAt(1).setCustomView(tabTwo);


        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabThree.setText("M");
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabFour.setText("J");
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabFive.setText("V");
        tabLayout.getTabAt(4).setCustomView(tabFive);

        TextView tabSix = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabSix.setText("S");
        tabLayout.getTabAt(5).setCustomView(tabSix);

        TextView tabSeven = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_text, null);
        tabSeven.setText("D");
        tabLayout.getTabAt(6).setCustomView(tabSeven);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        LinearLayout tabBar = ((LinearLayout)tabLayout.getChildAt(0));
        switch (compoundButton.getId()) {
            case R.id.wd0:
                if (b) {
                    tabBar.getChildAt(0).setClickable(true);
                    tabBar.getChildAt(0).setEnabled(true);
                    viewPager.setCurrentItem(0);
                } else {
                    tabBar.getChildAt(0).setClickable(false);
                    tabBar.getChildAt(0).setEnabled(false);
                }
                break;
            case R.id.wd1:
                if (b) {
                    tabBar.getChildAt(1).setClickable(true);
                    tabBar.getChildAt(1).setEnabled(true);
                    viewPager.setCurrentItem(1);
                } else {
                    tabBar.getChildAt(1).setClickable(false);
                    tabBar.getChildAt(1).setEnabled(false);
                }
                break;
            case R.id.wd2:
                if (b) {
                    tabBar.getChildAt(2).setClickable(true);
                    tabBar.getChildAt(2).setEnabled(true);
                    viewPager.setCurrentItem(2);
                } else {
                    tabBar.getChildAt(2).setClickable(false);
                    tabBar.getChildAt(2).setEnabled(false);
                }
                break;
            case R.id.wd3:
                if (b) {
                    tabBar.getChildAt(3).setClickable(true);
                    tabBar.getChildAt(3).setEnabled(true);
                    viewPager.setCurrentItem(3);
                } else {
                    tabBar.getChildAt(3).setClickable(false);
                    tabBar.getChildAt(3).setEnabled(false);
                }
                break;
            case R.id.wd4:
                if (b) {
                    tabBar.getChildAt(4).setClickable(true);
                    tabBar.getChildAt(4).setEnabled(true);
                    viewPager.setCurrentItem(4);
                } else {
                    tabBar.getChildAt(4).setClickable(false);
                    tabBar.getChildAt(4).setEnabled(false);
                }
                break;
            case R.id.wd5:
                if (b) {
                    tabBar.getChildAt(5).setClickable(true);
                    tabBar.getChildAt(5).setEnabled(true);
                    viewPager.setCurrentItem(5);
                } else {
                    tabBar.getChildAt(5).setClickable(false);
                    tabBar.getChildAt(5).setEnabled(false);
                }
                break;
            case R.id.wd6:
                if (b) {
                    tabBar.getChildAt(6).setClickable(true);
                    tabBar.getChildAt(6).setEnabled(true);
                    viewPager.setCurrentItem(6);
                } else {
                    tabBar.getChildAt(6).setClickable(false);
                    tabBar.getChildAt(6).setEnabled(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_pro_hour_button:
                if (doValidation()) {
                    Log.d("ProActivity + WD", hours_json.toString());
                }

                //finish();
                break;
        }
    }

    private Boolean doValidation() {
        int errors = 0;
        try {
            hours_json = new JSONObject();

            // Lundi
            CheckBox wd = (CheckBox) findViewById(R.id.wd0);
            TextView morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_lundi);
            TextView morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_lundi);
            TextView aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_lundi);
            TextView aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_lundi);

            JSONObject lundi = new JSONObject();
            JSONObject lundi_morning = new JSONObject();
            int start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            int end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            lundi_morning.put("start", start_morn);
            lundi_morning.put("end", end_morn);
            lundi.put("morning", lundi_morning);
            JSONObject lundi_afternoon = new JSONObject();
            int start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            int end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            lundi_afternoon.put("start", start_aft);
            lundi_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                lundi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                lundi.put("is_work", 0);
            lundi.put("afternoon", lundi_afternoon);
            hours_json.put("monday", lundi);

            // Mardi
            wd = (CheckBox) findViewById(R.id.wd1);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_mardi);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_mardi);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_mardi);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_mardi);

            JSONObject mardi = new JSONObject();
            JSONObject mardi_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            mardi_morning.put("start", start_morn);
            mardi_morning.put("end", end_morn);
            mardi.put("morning", mardi_morning);
            JSONObject mardi_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            mardi_afternoon.put("start",start_aft);
            mardi_afternoon.put("end", end_aft);
            mardi.put("afternoon", mardi_afternoon);
            if (wd.isChecked()) {
                mardi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                mardi.put("is_work", 0);
            hours_json.put("tuesday", mardi);

            // Mercredi
            wd = (CheckBox) findViewById(R.id.wd2);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_mercredi);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_mercredi);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_mercredi);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_mercredi);

            JSONObject mercredi = new JSONObject();
            JSONObject mercredi_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            mercredi_morning.put("start", start_morn);
            mercredi_morning.put("end", end_morn);
            mercredi.put("morning", mercredi_morning);
            JSONObject mercredi_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            mercredi_afternoon.put("start", start_aft);
            mercredi_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                mercredi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                mercredi.put("is_work", 0);
            mercredi.put("afternoon", mercredi_afternoon);
            hours_json.put("wenesday", mercredi);

            // Jeudi
            wd = (CheckBox) findViewById(R.id.wd3);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_jeudi);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_jeudi);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_jeudi);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_jeudi);

            JSONObject jeudi = new JSONObject();
            JSONObject jeudi_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            jeudi_morning.put("start", start_morn);
            jeudi_morning.put("end", end_morn);
            jeudi.put("morning", jeudi_morning);
            JSONObject jeudi_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            jeudi_afternoon.put("start", start_aft);
            jeudi_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                jeudi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                jeudi.put("is_work", 0);
            jeudi.put("afternoon", jeudi_afternoon);
            hours_json.put("thursay", jeudi);

            // Vendredi
            wd = (CheckBox) findViewById(R.id.wd4);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_vendredi);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_vendredi);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_vendredi);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_vendredi);

            JSONObject vendredi = new JSONObject();
            JSONObject vendredi_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            vendredi_morning.put("start",start_morn);
            vendredi_morning.put("end", end_morn);
            vendredi.put("morning", vendredi_morning);
            JSONObject vendredi_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            vendredi_afternoon.put("start", start_aft);
            vendredi_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                vendredi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                vendredi.put("is_work", 0);
            vendredi.put("afternoon", vendredi_afternoon);
            hours_json.put("friday", vendredi);

            // Samedi
            wd = (CheckBox) findViewById(R.id.wd5);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_samedi);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_samedi);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_samedi);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_samedi);

            JSONObject samedi = new JSONObject();

            JSONObject samedi_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            samedi_morning.put("start", start_morn);
            samedi_morning.put("end", end_morn);
            samedi.put("morning", samedi_morning);
            JSONObject samedi_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            samedi_afternoon.put("start", start_aft);
            samedi_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                samedi.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                samedi.put("is_work", 0);
            samedi.put("afternoon", samedi_afternoon);
            hours_json.put("saturday", samedi);

            // Dimanche
            wd = (CheckBox) findViewById(R.id.wd6);
            morning_st = (TextView) findViewById(R.id.workhour_hour_matin_begin_dimanche);
            morning_end = (TextView) findViewById(R.id.workhour_hour_matin_end_dimanche);
            aft_st = (TextView) findViewById(R.id.workhour_hour_midi_begin_dimanche);
            aft_end = (TextView) findViewById(R.id.workhour_hour_midi_end_dimanche);

            JSONObject dimanche = new JSONObject();
            JSONObject dimanche_morning = new JSONObject();
            start_morn = hourToMinute(morning_st.getText().toString().split(":")[0], morning_st.getText().toString().split(":")[1]);
            end_morn = hourToMinute(morning_end.getText().toString().split(":")[0], morning_end.getText().toString().split(":")[1]);
            dimanche_morning.put("start", start_morn);
            dimanche_morning.put("end", end_morn);
            dimanche.put("morning", dimanche_morning);
            JSONObject dimanche_afternoon = new JSONObject();
            start_aft = hourToMinute(aft_st.getText().toString().split(":")[0], aft_st.getText().toString().split(":")[1]);
            end_aft = hourToMinute(aft_end.getText().toString().split(":")[0], aft_end.getText().toString().split(":")[1]);
            dimanche_afternoon.put("start", start_aft);
            dimanche_afternoon.put("end", end_aft);
            if (wd.isChecked()) {
                dimanche.put("is_work", 1);
                if (start_aft < end_morn)
                    ++errors;
            }
            else
                dimanche.put("is_work", 0);
            dimanche.put("afternoon", dimanche_afternoon);
            hours_json.put("sunday", dimanche);

            if (errors > 0) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Oups")
                        .setMessage("Il semblerait que vos horaires ne soient pas correctement renseignées. Veuillez-effectuer les modifications nécessaires.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int hourToMinute(String hour, String minute) {
        int houri = Integer.decode(hour);
        int mini = Integer.decode(minute);

        //int result = (int)Math.floor(houri * 60);
        int result = houri * 60;
        result = result + mini;
        return result;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            //Log.e("GetItem ViewPager", String.valueOf(position));
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
