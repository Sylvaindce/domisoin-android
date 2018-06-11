package com.sylvain.domisoin.Fragments.Customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sylvain.domisoin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreProDetailsCares extends Fragment {

    private ListView cares = null;


    public MoreProDetailsCares() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_pro_details_cares, container, false);

        cares = (ListView) view.findViewById(R.id.pro_more_cares_list);
        String[] cares_list = getResources().getStringArray(R.array.care_name);
        String[] cares_duration = getResources().getStringArray(R.array.care_duration);
        cares.setAdapter(createListAdapter(cares_list, cares_duration));

        return view;
    }

    private List<Map<String, String>> convertToListItems(String[] list1, String[] list2) {
        List<Map<String, String>> listItem = new ArrayList<Map<String, String>>(list1.length);

        for (int i = 0; i < list1.length; ++i) {
            Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put("text1", list1[i]);
            listItemMap.put("text2", list2[i]);
            listItem.add(listItemMap);
        }
        return listItem;
    }

    private ListAdapter createListAdapter(String[] list1, String[] list2) {
        final String[] fromMapKey = new String[] {"text1", "text2"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems(list1, list2);

        return new SimpleAdapter(getContext(), list, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
    }

}
