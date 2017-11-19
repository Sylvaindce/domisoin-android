package com.sylvain.domisoin.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sylvain.domisoin.R;

/**
 * Created by Bms on 04/11/2017.
 */

public class PresentationDialog extends DialogFragment implements View.OnClickListener {
    Button yes,no;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.presentation_dialog, null);

        yes = (Button) view.findViewById(R.id.Yes);
        no = (Button) view.findViewById(R.id.No);

        setCancelable(false);
        return view;

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Yes)
        {
            Toast.makeText(getActivity(),"Yes button was clicked",Toast.LENGTH_LONG).show();
        }
        else
            {
                Toast.makeText(getActivity(),"No button was clicked",Toast.LENGTH_LONG).show();

            }
    }
}
