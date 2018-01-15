package com.sylvain.domisoin.Fragments.Customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Dialogs.ProMore;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomProListAdapter;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;
import com.sylvain.domisoin.databinding.FragmentSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemClickListener, ButtonInterface {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_SEARCH_FRAG";

    public ProgressDialog progress = null;
    private FragmentSearchBinding fragmentSearchBinding = null;
    private HomeCustomerActivity homeActivity = null;
    private userInfo UserInfo = null;
    private GoogleMap mMap = null;
    private List<UserModel> list_pro = null;
    private ListView listView_pro = null;
    private CustomProListAdapter mAdapter = null;
    private ImageButton search_button = null;
    private EditText search_edittext = null;
    private TextView search_pro_range = null;

    public String userid = "";
    private  LocationListener locationListener = null;
    private LocationManager locationManager = null;

    public SearchFragment() {
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

        fragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        homeActivity = (HomeCustomerActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentSearchBinding.setUser(UserInfo);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment)).getMapAsync(this);
        getCurrentLocation();

        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        //mapFragment.getMapAsync(this);

        list_pro = new LinkedList<UserModel>();

        listView_pro = (ListView) fragmentSearchBinding.getRoot().findViewById(R.id.listView_pro);

        userid = UserInfo.id.get();

        search_button = (ImageButton)fragmentSearchBinding.getRoot().findViewById(R.id.validate_search_button);
        search_button.setOnClickListener(this);
        search_edittext = (EditText)fragmentSearchBinding.getRoot().findViewById(R.id.search_edittext);
        search_pro_range = (TextView)fragmentSearchBinding.getRoot().findViewById(R.id.search_pro_range);
        search_pro_range.setOnClickListener(this);

        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
        getPro();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validate_search_button:
                getSearch();
                break;
            case R.id.search_pro_range:
                //draw circle on map + change range on api
                Log.d(TAG, "Click on pro_range");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProMore dialog = new ProMore(UserInfo.token.get());
        dialog.setUserid_1(UserInfo.id.get());
        dialog.set_user(mAdapter.getItem(position));
        dialog.setButtonInterface(this);
        dialog.show(getFragmentManager(), "more");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Paris and move the camera
        LatLng paris = new LatLng(48.866667, 2.333333);
        mMap.addMarker(new MarkerOptions().position(paris).title("Marker in Paris"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));

    }

    public void setLocationOnMap(Location location) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> address;
        String yourAddress = "";
        String yourCity = "";
        //String yourCountry;
        try {
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (address.size() > 0)
            {
                yourAddress = address.get(0).getAddressLine(0);
                yourCity = address.get(0).getAddressLine(1);
                UserInfo.actualloc.set((yourAddress + ", " + yourCity).replace(", null", ""));
                //yourCountry = address.get(0).getAddressLine(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(cur).title("Position Actuelle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
        mMap.moveCamera((CameraUpdateFactory.zoomTo(15)));
        //getPro();
    }

    public void getCurrentLocation() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                setLocationOnMap(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "No Permission for gps");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void getPro() {
        HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url) + "?is_pro=true", UserInfo.token.get());
        task.execute();
        progress = ProgressDialog.show(getActivity(), "Recherche", "Mise à jour de la liste des professionnels locaux en cours, merci de patienter...", true);
    }

    public void getSearch() {
        String search_text = String.valueOf(search_edittext.getText());
        Log.d("TEST", search_text);
        getProFromJobTitle(search_text);
    }

    public void getProFromJobTitle(String job_title) {
        HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url) + "?is_pro=true&job_title="+job_title, UserInfo.token.get());
        task.execute();
        progress = ProgressDialog.show
                (getActivity(), "Recherche", "Mise à jour de la liste des professionnels locaux en cours, merci de patienter...", true);
    }


    public void setItemListView(String response) {
        mAdapter = new CustomProListAdapter(getContext());

        JSONArray resp = null;
        try {
            resp = new JSONArray(response);
            for (int i = 0; i < resp.length(); ++i) {
                JSONObject tmp = resp.getJSONObject(i);
                UserModel pro = new UserModel();
                pro.setId(tmp.getString("id"));
                pro.setEmail(tmp.getString("email"));
                pro.setFirst_name(tmp.getString("first_name"));
                pro.setLast_name(tmp.getString("last_name"));
                pro.setJob_title(tmp.getString("job_title"));
                pro.setAddress(tmp.getString("adresse"));
                pro.setWorkphone(tmp.getString("workphone"));
                pro.setProfile_img(tmp.getString("profile_img"));
                pro.setIs_pro(tmp.getBoolean("is_pro"));
                pro.setEvents(tmp.getString("events"));
                list_pro.add(pro);
                mAdapter.addItem(pro);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView_pro.setAdapter(mAdapter);
        listView_pro.setOnItemClickListener(this);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);
            if (response != null) {
                String response_code = "400";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }
                if (response.equals("0")) {
                    Toast toast = Toast.makeText(getContext(), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    Toast toast = Toast.makeText(getContext(), "Une erreur s'est produite, veuillez essayer de nouveau. (" + ManageErrorText.manage_my_error(response) + ")", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    setItemListView(response);
                    Log.d("test", response);
                }
            }
        }
    };

    @Override
    public void buttonClicked(View v) {
        getSearch();
    }

    @Override
    public void onBookClick(String _ourBeginDate, String _ourEndDate) {

    }
}
