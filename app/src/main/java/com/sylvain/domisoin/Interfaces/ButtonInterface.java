package com.sylvain.domisoin.Interfaces;

import android.view.View;

/**
 * Created by Sylvain on 27/03/2017.
 */

public interface ButtonInterface {
    public void buttonClicked(View v);
    public void onBookClick(String _ourBeginDate, String _ourEndDate);
}
