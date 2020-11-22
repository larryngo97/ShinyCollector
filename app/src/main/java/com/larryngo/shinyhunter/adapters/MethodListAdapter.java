package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodListAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private final List<Method> list_methods;
    private final List<Method> list_methods_all;

    public MethodListAdapter(Context context, ArrayList<Method> list_methods) {
        this.context = context;
        this.list_methods = list_methods;
        this.list_methods_all = new ArrayList<>(list_methods);
    }

    public List<Method> getList() {
        return list_methods;
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
            gridView = inflater.inflate(R.layout.method_list_entry, parent, false); //inflates the grid
        }

        TextView titleView = gridView.findViewById(R.id.method_list_entry_title);
        titleView.setText(list_methods.get(position).getName());

        return gridView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Method> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()) {
                filteredList.addAll(list_methods_all);
            } else {
                for (Method method : list_methods_all) {
                    //System.out.println(game.getName() + constraint.toString().toLowerCase());
                    if (method.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(method);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list_methods.clear();
            list_methods.addAll((Collection<? extends Method>) results.values);
            notifyDataSetChanged();
        }
    };
}
