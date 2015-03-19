package com.brianstacks.sportsupclose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brian Stacks
 * on 3/16/15
 * for FullSail.edu.
 */
public class DataAdapter extends BaseAdapter {
    private static final long ID_CONSTANT = 0x010101010L;
    private Context context;
    private ArrayList<GooglePlace> dataList;

    public DataAdapter(Context context, ArrayList<GooglePlace> objects){
        this.context = context;
        this.dataList =objects;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflator = (LayoutInflater)context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflator.inflate(R.layout.row_layout,null);
        GooglePlace enteredData = dataList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.listText);
        TextView tv2 = (TextView) view.findViewById(R.id.listText2);
        TextView tv3 = (TextView) view.findViewById(R.id.listText3);
        TextView tv4 = (TextView) view.findViewById(R.id.listText4);
        tv.setText(enteredData.getName());
        tv2.setText(enteredData.getAddress());
        tv3.setText(enteredData.getOpenNow());
        tv4.setText(enteredData.getDistance());


        return  view;
    }
}
