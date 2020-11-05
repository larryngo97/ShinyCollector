package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Counter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.GifImageView;

public class HomeHuntingAdapter extends BaseAdapter {
    private Context mContext;
    private List<Counter> list_counters;

    public HomeHuntingAdapter(Context c){
        this.mContext = c;
    }

    public HomeHuntingAdapter(Context c, List<Counter> list_counters){
        this.mContext = c;
        this.list_counters = list_counters;
    }

    public void addData(Counter entry) {
        list_counters.add(entry);
        notifyDataSetChanged();
    }

    public int getCount() {
        return list_counters.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int id)
    {
        return null;
    }

    public Counter getCounterObject(int position) {
        return list_counters.get(position);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //grabs inflate info
            gridView = inflater.inflate(R.layout.fragment_home_hunting_grid_entry, null); //inflates the grid
        }

        GifImageView iconView = gridView.findViewById(R.id.hunting_list_entry_image);
        TextView nameView = gridView.findViewById(R.id.hunting_list_entry_name);
        TextView gameView = gridView.findViewById(R.id.hunting_list_entry_game);
        TextView countView = gridView.findViewById(R.id.hunting_list_entry_count);

        byte[] pokemon_image = list_counters.get(position).getPokemon().getImage();
        String pokemon_name = list_counters.get(position).getPokemon().getName();
        String game_name = list_counters.get(position).getGame().getName();
        int count = list_counters.get(position).getCount();

        Glide.with(mContext)
                .load(pokemon_image)
                .placeholder(R.drawable.missingno)
                .into(iconView);

        nameView.setText(pokemon_name);
        gameView.setText(game_name);
        countView.setText(String.valueOf(count));

        return gridView;
    }

}