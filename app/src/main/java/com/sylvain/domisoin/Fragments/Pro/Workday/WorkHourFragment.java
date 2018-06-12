package com.sylvain.domisoin.Fragments.Pro.Workday;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sylvain.domisoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkHourFragment extends Fragment {

    private String day_name = "";

    public WorkHourFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public WorkHourFragment(String day) {
        this.day_name = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_hour, container, false);

        TextView text = (TextView) view.findViewById(R.id.workhour_day);
        switch (this.day_name) {
            case "Lundi":
                Log.d("Toto", "changeID");
                text.setId(R.id.workhour_title_lundi);
                break;
            case "Mardi":
                text.setId(R.id.workhour_title_mardi);
                break;
            case "Mercredi":
                text.setId(R.id.workhour_title_mercredi);
                break;
            case "Jeudi":
                text.setId(R.id.workhour_title_jeudi);
                break;
            case "Vendredi":
                text.setId(R.id.workhour_title_vendredi);
                break;
            case "Samedi":
                text.setId(R.id.workhour_title_samedi);
                break;
            case "Dimanche":
                text.setId(R.id.workhour_title_dimanche);
                break;


        }
        text.setText(this.day_name);

        return view;
    }

}
