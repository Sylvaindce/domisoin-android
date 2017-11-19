package com.sylvain.domisoin.Fragments.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sylvain.domisoin.Dialogs.ProMore;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.Models.TitleCalendarValueModel;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//implements CalendarView.OnDateChangeListener
//teest github 19.11.2017 MALIK SKANDER BENDIF 

public class MoreProDetailsFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private static final String TAG = MoreProDetailsFragment.class.getName();
    private CalendarView calendarView = null;
    private CustomExpandableListAdapter listAdapterExp;
    private UserModel user = null;
    private ExpandableListView expListView;
    private Context context;


    private View dialogFragment123 = null;


    private List<TitleCalendarValueModel> listDataHeader;

    private List<String> listDataHeader1;
    private HashMap<String, List<String>> listDataChild;
    private Calendar currentCalendarView = null;
    private SimpleDateFormat sdf_disp = null;
    private SimpleDateFormat sdf_api = null;
    private ButtonInterface buttonInterface = null;
    private Button btnmore;
    public  TextView txtmore;

    public MoreProDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // dialogFragment123 = inflater.inflate(R.layout.presentatio_more, container, false);
       // getDialog().setTitle("Plus d'informations");


        View view = inflater.inflate(R.layout.fragment_pro_details, container, false);
        //txtmore = (TextView)MoreProDetailsFragment.getRoot().findViewById(R.id.presentation);
        user = ((ProMore)getParentFragment()).get_user();
        currentCalendarView = Calendar.getInstance();

        sdf_disp = new SimpleDateFormat("EEE dd MMM yyyy");
        sdf_api = new SimpleDateFormat("yyyy-MM-dd");

        TextView address = (TextView)view.findViewById(R.id.pro_more_address);
        Button More = (Button)view.findViewById(R.id.Dialogplus);
         Button Langues = (Button)view.findViewById(R.id.buttonlangues);

        More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MoreProDetailsFragment.this.getActivity());
                builder.setTitle("Presentation du practicien ");
                builder.setMessage("Le docteur blablabla");
                builder.setCancelable(false);

                builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        //code here

                    }
                });

                /*langues

                Langues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MoreProDetailsFragment.this.getActivity());
                        builder.setTitle("Langues parlées par le practicien ");
                        builder.setMessage("Français,Arabe,Angaais");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                                //code here

                            }
                        }); */
                //langues


                AlertDialog alert=builder.create();
                alert.show();



                //startActivity(new Intent(AccountFragment.this.getActivity(), pop.class));
            }
        });





        address.setText(user.getAddress());

        Log.d("TEST", user.getEvents());

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.expList2);
      //  expListView = (ExpandableListView) view.findViewById(R.id.listpres);



        // preparing list data
        prepareListData();

        listAdapterExp = new CustomExpandableListAdapter(view.getContext(), listDataHeader, listDataChild);


        // setting list adapter
        expListView.setAdapter(listAdapterExp);

        expListView.setOnChildClickListener(this);

        return view;
    }

    public void openDialog3(View view) {



    }
    private void prepareListData() {
        listDataHeader = new LinkedList<TitleCalendarValueModel>();
        listDataHeader1 = new ArrayList<String>();

        listDataChild = new HashMap<String, List<String>>();
        LinkedList<String> listpro_events = new LinkedList<String>();

        //init header semaine
        Calendar tmp = currentCalendarView;
        for (int i = 0; i < 7; ++i) {
            tmp.add(Calendar.DAY_OF_MONTH, 1);
            TitleCalendarValueModel titleCalendarValue = new TitleCalendarValueModel(sdf_disp.format(tmp.getTime()), tmp.getTime());
            listDataHeader.add(titleCalendarValue);
        }

        // Adding child data
        List<String> horaires = new ArrayList<String>();
        horaires.add("08h00 - 09h00");
        horaires.add("09h00 - 10h00");
        horaires.add("10h00 - 11h00");
        horaires.add("11h00 - 12h00");
        horaires.add("13h00 - 14h00");
        horaires.add("14h00 - 15h00");
        horaires.add("15h00 - 16h00");
        horaires.add("16h00 - 17h00");
        horaires.add("17h00 - 18h00");
        horaires.add("18h00 - 19h00");

        //Check pro events
        try {
            JSONArray pro_events = new JSONArray(user.getEvents());

            for (int i = 0; i < pro_events.length(); ++i) {
                JSONObject one_event = pro_events.getJSONObject(i);
                listpro_events.add(one_event.getString("start_date"));
                Log.d("Test Smart Calendar", one_event.getString("start_date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int flag = 0;
        String day_api = "";
        String hour_api = "";
        String dayhour_api = "";

        for (int i = 0; i < listDataHeader.size(); ++i) {
            day_api = sdf_api.format(listDataHeader.get(i).get_time())+"T";
            List<String> tmp_horaires = new ArrayList<String>();
            for (int j = 0; j < horaires.size(); ++j) {
                hour_api = horaires.get(j).substring(0, 5);
                hour_api = hour_api.replace('h', ':');
                hour_api = hour_api+":00Z";
                dayhour_api = day_api+hour_api;
                flag = 0;
                for (int k = 0; k < listpro_events.size(); ++k) {
                    //Log.d("Test Smart Cal today", dayhour_api);
                    //Log.d("Test Smart Cal pro", listpro_events.get(k));
                    if (dayhour_api.equals(String.valueOf(listpro_events.get(k)))) {
                        Log.d("Smart Cal Equal", dayhour_api);
                        flag = -1;
                    }
                }
                if(flag == 0) {
                    tmp_horaires.add(horaires.get(j));
                }
            }
            listDataChild.put(listDataHeader.get(i).get_title(), tmp_horaires);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        Toast.makeText(getContext(), listDataHeader.get(i).get_title() + listDataChild.get(listDataHeader.get(i).get_title()).get(i1), Toast.LENGTH_LONG).show();

        String Bhour = listDataChild.get(listDataHeader.get(i).get_title()).get(i1).split("-")[0].replaceAll(" ", "");
        Bhour = Bhour.replaceAll("h", ":");
        Bhour = Bhour + ":00Z";

        String Ehour = listDataChild.get(listDataHeader.get(i).get_title()).get(i1).split("-")[1].replaceAll(" ", "");
        Ehour = Ehour.replaceAll("h", ":");
        Ehour = Ehour + ":00Z";



        String Bdate = sdf_api.format(listDataHeader.get(i).get_time()) + "T" + Bhour;
        String Edate = sdf_api.format(listDataHeader.get(i).get_time()) + "T" + Ehour;
        buttonInterface.onBookClick(Bdate, Edate);

        return false;
    }

    public void setInterface(ButtonInterface ourInterface) {
        this.buttonInterface = ourInterface;
    }

}
