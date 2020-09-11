package com.larryngo.shinyhunter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Game_Pokemon;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class PokemonViewAdapter extends RecyclerView.Adapter<PokemonViewAdapter.ViewHolder> {
    private View view;
    private Context context;
    private ArrayList<Game_Pokemon> data;
    private PokemonViewListener listener;

    public PokemonViewAdapter(Context context, ArrayList<Game_Pokemon> data, PokemonViewListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_view_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game_Pokemon game = data.get(position);
        String name = game.getName();
        holder.name.setText(name);

        Glide.with(context)
                .load(data.get(position).getImage_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(holder.image);
    }

    public void clearDataList()
    {
        data.clear();
    }

    public void addDataList(ArrayList<Game_Pokemon> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Game_Pokemon> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<Game_Pokemon> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private GifImageView image;
        private TextView name;

        public ViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.pokemon_list_entry_image);
            name = view.findViewById(R.id.pokemon_list_entry_name);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface PokemonViewListener {
        void onClick(View v, int position);
    }
}
