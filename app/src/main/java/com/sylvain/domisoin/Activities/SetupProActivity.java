package com.sylvain.domisoin.Activities;

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

import java.util.ArrayList;
import java.util.List;

public class SetupProActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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
        viewPager.addOnPageChangeListener(this);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                TextView text = (TextView) findViewById(R.id.workhour_title_mardi);
                Log.d("test", text.getText().toString());
                finish();
                break;
        }
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
