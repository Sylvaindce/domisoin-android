package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Sylvain on 29/06/2017.
 */

public class CustomPlanningListView extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private ArrayList<Boolean> statList = new ArrayList<Boolean>();

    private LayoutInflater mInflater;

    public CustomPlanningListView(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item, String strid) {
        mData.add(item);
        id.add(strid);
        notifyDataSetChanged();
    }

    public void addItemStat(final String item, String strid, Boolean valid) {
        mData.add(item);
        id.add(strid);
        statList.add(valid);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        id.add("null");
        statList.add(false);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    public void addIs_Validate(Boolean stat) {
        statList.add(stat);
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemIdStr(int position) {
        return id.get(position);
    }

    public void remove(int position) {
        mData.remove(position);
        id.remove(position);
        statList.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        id.clear();
        statList.clear();
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.itemlist_planning_description, null);
                    convertView.setClickable(false);
                    holder.textView = (TextView) convertView.findViewById(R.id.item_planning_description);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.itemlist_planning_date, null);
                    convertView.setClickable(true);
                    holder.textView = (TextView) convertView.findViewById(R.id.item_planning_date);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*if (statList.get(position) == false) {
            holder.textView.setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimaryLight));
        }*/
        holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

}