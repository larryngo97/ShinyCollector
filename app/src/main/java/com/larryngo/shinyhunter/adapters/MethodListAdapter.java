package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Method;

import java.util.ArrayList;

public class MethodListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Method> list_methods;

    public MethodListAdapter(Context context, ArrayList<Method> list_methods) {
        this.context = context;
        this.list_methods = list_methods;
    }

    @Override
    public int getCount() {
        return list_methods.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //grabs inflate info
            gridView = inflater.inflate(R.layout.method_list_entry, null); //inflates the grid
        }

        TextView titleView = gridView.findViewById(R.id.method_list_entry_title);
        titleView.setText(list_methods.get(position).getName());

        return gridView;
    }
}
