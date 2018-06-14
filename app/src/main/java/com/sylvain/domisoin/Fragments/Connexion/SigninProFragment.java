package com.sylvain.domisoin.Fragments.Connexion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.sylvain.domisoin.R;


public class SigninProFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View view = null;

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
        view = inflater.inflate(R.layout.fragment_signinpro, container, false);

        //Infirmier
        SeekBar seekBar_prisedesang = (SeekBar) view.findViewById(R.id.seekbar_prisedesang);
        seekBar_prisedesang.setOnSeekBarChangeListener(this);
        SeekBar seekBar_toilettes = (SeekBar) view.findViewById(R.id.seekbar_toilettes);
        seekBar_toilettes.setOnSeekBarChangeListener(this);
        SeekBar seekbar_pansement = (SeekBar) view.findViewById(R.id.seekbar_pansement);
        seekbar_pansement.setOnSeekBarChangeListener(this);
        SeekBar seekbar_injection = (SeekBar) view.findViewById(R.id.seekbar_injection);
        seekbar_injection.setOnSeekBarChangeListener(this);

        //Pediatre
        SeekBar seekbar_bilandesante = (SeekBar) view.findViewById(R.id.seekbar_bilandesante);
        seekbar_bilandesante.setOnSeekBarChangeListener(this);
        SeekBar seekbar_desencombrement = (SeekBar) view.findViewById(R.id.seekbar_desencombrement);
        seekbar_desencombrement.setOnSeekBarChangeListener(this);
        SeekBar seekbar_medecinegenerale = (SeekBar) view.findViewById(R.id.seekbar_medecinegenerale);
        seekbar_medecinegenerale.setOnSeekBarChangeListener(this);

        //Kine
        SeekBar seekbar_reeducation = (SeekBar) view.findViewById(R.id.seekbar_reeducation);
        seekbar_reeducation.setOnSeekBarChangeListener(this);
        SeekBar seekbar_medecinedusport = (SeekBar) view.findViewById(R.id.seekbar_medecinedusport);
        seekbar_medecinedusport.setOnSeekBarChangeListener(this);
        SeekBar seekbar_mobilisation = (SeekBar) view.findViewById(R.id.seekbar_mobilisation);
        seekbar_mobilisation.setOnSeekBarChangeListener(this);

        return view;
    }

    private void manageView(String message) {
        LinearLayout infirmier_container = (LinearLayout)view.findViewById(R.id.infirmier_care_container);
        LinearLayout pediatre_container = (LinearLayout) view.findViewById(R.id.pediatre_care_container);
        LinearLayout kine_container = (LinearLayout) view.findViewById(R.id.kine_care_container);

        switch (message) {
            case "Infirmier":
                infirmier_container.setVisibility(View.VISIBLE);
                pediatre_container.setVisibility(View.GONE);
                kine_container.setVisibility(View.GONE);
                break;
            case "Pédiatre":
                infirmier_container.setVisibility(View.GONE);
                pediatre_container.setVisibility(View.VISIBLE);
                kine_container.setVisibility(View.GONE);
                break;
            case "Kinésithérapeute":
                infirmier_container.setVisibility(View.GONE);
                pediatre_container.setVisibility(View.GONE);
                kine_container.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void displayReceivedData(String message)
    {
        Log.d("SignInProFrag_Receive", message);
        manageView(message);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
        int min = 15;
        int ourv = value % 15;
        /*if (value < 15)
            value = 15;*/
        if (ourv != 0) {
            int res = min - ourv;
            value = value+res;
        }
        Log.d("SeekBar", String.valueOf(value));

        switch (seekBar.getId()) {
            case R.id.seekbar_prisedesang:
                TextView duration_prisedesang = (TextView)view.findViewById(R.id.duration_prisedesang);
                duration_prisedesang.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_toilettes:
                TextView duration_toilettes = (TextView)view.findViewById(R.id.duration_toilettes);
                duration_toilettes.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_pansement:
                TextView duration_pansements = (TextView)view.findViewById(R.id.duration_pansement);
                duration_pansements.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_injection:
                TextView duration_injection = (TextView)view.findViewById(R.id.duration_injection);
                duration_injection.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_bilandesante:
                TextView duration_bilandesante = (TextView)view.findViewById(R.id.duration_bilandesante);
                duration_bilandesante.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_desencombrement:
                TextView duration_desencombrement = (TextView)view.findViewById(R.id.duration_desencombrement);
                duration_desencombrement.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_medecinegenerale:
                TextView duration_medecinegenerale = (TextView)view.findViewById(R.id.duration_medecinegenerale);
                duration_medecinegenerale.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_reeducation:
                TextView duration_reeducation = (TextView)view.findViewById(R.id.duration_reeducation);
                duration_reeducation.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_medecinedusport:
                TextView duration_medecinedusport = (TextView)view.findViewById(R.id.duration_medecinedusport);
                duration_medecinedusport.setText(String.valueOf(value) + " min");
                break;
            case R.id.seekbar_mobilisation:
                TextView duration_mobilisation = (TextView)view.findViewById(R.id.duration_mobilisation);
                duration_mobilisation.setText(String.valueOf(value) + " min");
                break;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
