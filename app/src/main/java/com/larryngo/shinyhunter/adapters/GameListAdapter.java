package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.util.Settings;

public class GameListAdapter extends BaseAdapter implements Filterable {
    private final Context mContext;
    private final List<Game> list_games;
    private final List<Game> list_games_all;

    public GameListAdapter(Context c, List<Game> list_games){
        this.mContext = c;
        this.list_games = list_games;
        this.list_games_all = new ArrayList<>(list_games);
    }

    public List<Game> getList() {
        return list_games;
    }

    public int getCount() {
        return list_games.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int id)
    {
        return null;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //grabs inflate info
            gridView = inflater.inflate(R.layout.game_list_entry, parent, false); //inflates the grid
        }

        LinearLayout layoutView = gridView.findViewById(R.id.game_list_entry_card);
        if(Settings.isAnimModeOn()) {
            layoutView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.list_anim_grow_right));
        }

        ImageView iconView = gridView.findViewById(R.id.game_list_entry_image);
        TextView titleView = gridView.findViewById(R.id.game_list_entry_title);

        byte[] image = list_games.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        iconView.setImageBitmap(bitmap); //sets the picture
        titleView.setText(list_games.get(position).getName());

        return gridView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Game> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()) {
                filteredList.addAll(list_games_all);
            } else {
                for (Game game : list_games_all) {
                    //System.out.println(game.getName() + constraint.toString().toLowerCase());
                    if (game.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(game);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list_games.clear();
            list_games.addAll((Collection<? extends Game>) results.values);
            notifyDataSetChanged();
        }
    };
}