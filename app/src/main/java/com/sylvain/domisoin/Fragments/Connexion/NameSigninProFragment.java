package com.sylvain.domisoin.Fragments.Connexion;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.sylvain.domisoin.R;

public class NameSigninProFragment extends Fragment {

    private SendMessage SM;
    private View view = null;
    private Spinner spinner= null;

    public NameSigninProFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_name_signin_pro, container, false);

        EditText lastname = (EditText)view.findViewById(R.id.lastname_pro);
        lastname.requestFocus();
        spinner = (Spinner) view.findViewById(R.id.job_pro);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.jobs_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Log.d("NameSIgnInProFrag", "isVisible()");
        }
        else {
            Log.d("NameSIgnInProFrag", "isNotVisible()");
            if (SM != null) {
                SM.sendData(spinner.getSelectedItem().toString());
            }
        }
    }

    interface SendMessage {
        void sendData(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

}
