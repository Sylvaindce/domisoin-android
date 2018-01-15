package com.sylvain.domisoin.Fragments.Pro;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Customer.AccountFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.GooglePlaceArrayAdapter;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.databinding.FragmentProAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountProFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_HOME_PRO_ACTIVITY";

    private FragmentProAccountBinding fragmentProAccountBinding = null;
    private HomeProActivity homeActivity = null;
    private userInfo UserInfo = null;
    private ImageButton editInfo = null;

    private EditText account_jobtitle = null;
    private EditText account_workphone = null;
    private EditText account_email = null;
    private EditText account_address = null;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private GooglePlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    public AccountProFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro_account, container, false);
        homeActivity = (HomeProActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentProAccountBinding.setUser(UserInfo);

        editInfo = (ImageButton) fragmentProAccountBinding.getRoot().findViewById(R.id.account_modify_button_pro);
        editInfo.setOnClickListener(this);

        account_jobtitle = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_jobtitle_pro);
        account_workphone = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_workphone_pro);
        account_email = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_email_pro);
        //account_address = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_address_pro);

        //UserInfo.jsonAnswer.set(getArguments().getString("infofrag"));

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) fragmentProAccountBinding.getRoot().findViewById((R.id.account_address_pro));
        mAutocompleteTextView.setThreshold(3);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new GooglePlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        return fragmentProAccountBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.account_modify_button_pro:
                Log.d(TAG, "account modify info");
                if (account_jobtitle.isEnabled()) {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_create_black_24dp));
                    account_jobtitle.setEnabled(false);
                    account_workphone.setEnabled(false);
                    account_email.setEnabled(false);
                    mAutocompleteTextView.setEnabled(false);
                    doUpdate();
                } else {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
                    account_jobtitle.setEnabled(true);
                    account_workphone.setEnabled(true);
                    account_email.setEnabled(true);
                    mAutocompleteTextView.setEnabled(true);
                }
                break;
        }

    }

    private void doUpdate() {
        userInfo userinfo = ((HomeProActivity)getActivity()).getUserInfo();

        if (!verif_fields()) {
            try {
                JSONObject newjson = new JSONObject(userinfo.json.get());
                newjson.put("job_title", account_jobtitle.getText());
                newjson.put("workphone", account_workphone.getText());
                newjson.put("email", account_email.getText());
                newjson.put("adresse", mAutocompleteTextView.getText());

                HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url) + newjson.get("id") + "/", newjson, UserInfo.token.get());
                task.execute();
                ((HomeProActivity) getActivity()).progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean verif_fields() {
        Boolean result = false;
        if ( mAutocompleteTextView.getText().toString().length() == 0 || !mAutocompleteTextView.getText().toString().contains(",")) {
            mAutocompleteTextView.setError("Une Adresse valide est requise");
            result = true;
        }
        if ( account_workphone.getText().toString().length() != 10 ) {
            account_workphone.setError("Un Numéro valide est requis");
            result = true;
        }
        if( account_jobtitle.getText().toString().length() == 0 ) {
            account_jobtitle.setError("Une Profession valide est requise");
            result = true;
        }
        if (TextUtils.isEmpty(account_email.getText()) || !Patterns.EMAIL_ADDRESS.matcher(account_email.getText()).matches()) {
            account_email.setError("Une adresse email valide est requise");
            result = true;
        }
        return result;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final GooglePlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mAutocompleteTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mAutocompleteTextView.clearFocus();

            /*mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*/
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getContext(), "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }


    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
