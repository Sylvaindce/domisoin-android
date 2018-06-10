package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by sylvain on 06/12/17.
 */

public class CustomPlanningExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private TreeMap<Date, LinkedList<AppointmentModel>> expandableList;
    private LayoutInflater mInflater = null;
    private SimpleDateFormat sdf_disp = new SimpleDateFormat("EEEE dd MMM");

    public CustomPlanningExpandableListAdapter(Context context, TreeMap<Date, LinkedList<AppointmentModel>> ourList) {
        this.context = context;
        this.expandableList = ourList;
        mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableList.get(this.expandableList.keySet().toArray()[listPosition]).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        AppointmentModel tmp = (AppointmentModel)getChild(listPosition, expandedListPosition);
        String hour = tmp.getStart_date_str().split("T")[1].substring(0, 5);
        final String expandedListText = hour + " - " + tmp.getType() + " " + tmp.getDescription();

        ChildViewHolder cholder = null;

        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.itemlist_planning_description, null);
            cholder.textView = (TextView) convertView.findViewById(R.id.item_planning_description);
            cholder.circle = (TextView) convertView.findViewById(R.id.is_validate_circle);
            convertView.setTag(R.string.childHolder, cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag(R.string.childHolder);
        }
        if (!tmp.getIs_validate()) {
            cholder.textView.setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimaryLight));
            cholder.circle.setVisibility(View.VISIBLE);
        }
        cholder.textView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableList.get(this.expandableList.keySet().toArray()[listPosition])
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableList.keySet().toArray()[listPosition];
    }

    @Override
    public int getGroupCount() {
        return this.expandableList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        GroupViewHolder gholder = null;

        Date dateTitle = (Date) getGroup(listPosition);
        String listTitle = sdf_disp.format(dateTitle);

        if (convertView == null) {
            gholder = new GroupViewHolder();
            convertView = this.mInflater.inflate(R.layout.itemlist_planning_date, null);
            gholder.textView = (TextView) convertView.findViewById(R.id.item_planning_date);
            convertView.setTag(R.string.groupHolder,gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag(R.string.groupHolder);
        }

        gholder.textView.setText(listTitle);

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(listPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    public void updateList(TreeMap<Date, LinkedList<AppointmentModel>> newlist) {
        this.expandableList.clear();
        this.expandableList.putAll(newlist);
    }

    public static class GroupViewHolder {
        public TextView textView;
    }

    public static class ChildViewHolder {
        public TextView circle;
        public TextView textView;
    }
}
