package com.timer.hyunjae.starttimer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timer.hyunjae.starttimer.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    public static ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_time_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView count = (TextView) convertView.findViewById(R.id.lv_count) ;
        TextView timeList = (TextView) convertView.findViewById(R.id.lv_timelist) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        count.setText(listViewItem.getCountStr());
        timeList.setText(listViewItem.getTimeListStr());

        return convertView;
    }

    public void addItem(String count, String list){
        ListViewItem item = new ListViewItem();

        item.setcount(count+".");
        item.setTimeList(list);

        listViewItemList.add(item);
    }

    public void clearListview(){
        listViewItemList.clear();
    }
}
