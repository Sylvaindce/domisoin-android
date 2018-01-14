package com.sylvain.domisoin.DataBind;

import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class userInfo {
    private static final String TAG = userInfo.class.getSimpleName();

    public ObservableField<String> account = new ObservableField<String>();
    public ObservableField<String> planning = new ObservableField<String>();
    public ObservableField<String> search = new ObservableField<String>();
    public ObservableField<String> json = new ObservableField<String>();

    public ObservableField<String> job_title = new ObservableField<String>();
    public ObservableField<String> workphone = new ObservableField<String>();
    public ObservableField<String> events = new ObservableField<String>();
    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> first_name = new ObservableField<String>();
    public ObservableField<String> last_name = new ObservableField<String>();
    public ObservableField<Boolean> is_pro = new ObservableField<Boolean>();
    public ObservableField<String> address = new ObservableField<String>();
    public ObservableField<String> profile_img = new ObservableField<String>();
    public ObservableField<String> id = new ObservableField<String>();
    public ObservableField<String> token = new ObservableField<String>();
    public ObservableField<String> mdp = new ObservableField<String>();


    public userInfo() {
        account.set("You are in account section.");
        planning.set("Pas de rendez-vous prévu.");
        search.set("You are in search section.");

        json.set("empty");

        job_title.set("empty");
        workphone.set("empty");
        events.set("empty");
        email.set("empty");
        first_name.set("empty");
        last_name.set("empty");
        is_pro.set(false);
        address.set("empty");
        profile_img.set("empty");
        id.set("empty");
        token.set("");
        mdp.set("");
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG+" Before: ", s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG+" OnTextChanged: ", s.toString());
        }

        @Override public void afterTextChanged(Editable s) {
            Log.d(TAG+" After: ", s.toString());
        }
    };

}
