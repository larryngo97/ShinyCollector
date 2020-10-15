package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.PokemonList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;


public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {
    private View view;
    private ArrayList<PokemonList> data;
    private PokemonListListener listener;
    private Context context;
    private int limit = 890;

    public PokemonListAdapter(Context context, ArrayList<PokemonList> data, PokemonListListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_list_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PokemonList pokemon = data.get(position);

        String name = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1); //capitalize first letter of word
        holder.name.setText(name);

        int index = pokemon.getId();
        String index_number;
        if(index < 10) {
            index_number = "#00" + index;
            holder.id.setText(index_number);
        }
        else if (index < 100) {
            index_number = "#0" + index;
            holder.id.setText(index_number);
        }
        else {
            index_number = "#" + index;
            holder.id.setText(index_number);
        }

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + pokemon.getId() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(holder.image);

    }

    public void addDataList(ArrayList<PokemonList> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void setItemCount(int count) {
        limit = count;
    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), limit);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GifImageView image;
        private TextView id;
        private TextView name;

        public ViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.pokemon_list_entry_image);
            id = view.findViewById(R.id.pokemon_list_entry_id);
            name = view.findViewById(R.id.pokemon_list_entry_name);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface PokemonListListener {
        void onClick(View v, int position);
    }
}
