package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Game;

public class GameListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Game> list_games;

    public GameListAdapter(Context c, List<Game> list_games){
        this.mContext = c;
        this.list_games = list_games;
    }

    public void addDataList(ArrayList<Game> entry) {
        list_games = entry;
        notifyDataSetChanged();
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
            gridView = inflater.inflate(R.layout.game_list_entry, null); //inflates the grid
        }

        ImageView iconView = gridView.findViewById(R.id.game_list_entry_image);
        TextView titleView = gridView.findViewById(R.id.game_list_entry_title);

        byte[] image = list_games.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        iconView.setImageBitmap(bitmap); //sets the picture
        titleView.setText(list_games.get(position).getName());

        return gridView;
    }

}