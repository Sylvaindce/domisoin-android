package com.sylvain.domisoin.Fragments.Connexion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.sylvain.domisoin.R;


public class SigninProFragment extends Fragment {

    public SigninProFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signinpro, container, false);


        com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar rangeSeekbar = (com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar);
        rangeSeekbar.setCornerRadius(10);

        // get min and max text view
        final TextView begin_hour = (TextView) view.findViewById(R.id.begin_hour_pro);
        final TextView end_hour = (TextView) view.findViewById(R.id.end_hour_pro);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                begin_hour.setText(String.valueOf(minValue));
                end_hour.setText(String.valueOf(maxValue));
            }
        });


        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });



        return view;
    }
}
