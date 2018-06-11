package com.sylvain.domisoin.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sylvain.domisoin.Fragments.Connexion.ConnexionFragment;
import com.sylvain.domisoin.Fragments.Connexion.LoginFragment;
import com.sylvain.domisoin.Fragments.Connexion.SigninProFragment;
import com.sylvain.domisoin.Fragments.Customer.MoreProDetailsCares;
import com.sylvain.domisoin.Fragments.Customer.MoreProDetailsFragment;
import com.sylvain.domisoin.Fragments.Pro.AccountProFragment;
import com.sylvain.domisoin.Fragments.Pro.CustomerListFragment;
import com.sylvain.domisoin.Fragments.Pro.PlanningProFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.Login;

import java.util.ArrayList;
import java.util.List;

public class SetupProActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    public ViewPager viewPager;
    public SetupProActivity.ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pro);

        viewPager = (ViewPager) findViewById(R.id.vp_setup_pro);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SetupProActivity.ViewPagerAdapter(getSupportFragmentManager());

        SigninProFragment to = new SigninProFragment();
        MoreProDetailsCares toto = new MoreProDetailsCares();

        adapter.addFrag(to, "test1");
        adapter.addFrag(toto, "Test");
        //adapter.addFrag(planningFragment, "Agenda");
        //adapter.addFrag(searchFragment, "Rechercher");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
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
