package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.models.PokemonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;


public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> implements Filterable {
    private View view;
    private List<PokemonList> data;
    private List<PokemonList> data_all;
    private PokemonListListener listener;
    private Context context;
    private int limit = 890;

    public PokemonListAdapter(Context context, ArrayList<PokemonList> data, PokemonListListener listener) {
        this.context = context;
        this.data = data;
        this.data_all = new ArrayList<>(data);
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

    public List<PokemonList> getList() {
        return data;
    }

    public void addDataList(ArrayList<PokemonList> list) {
        data.clear();
        data.addAll(list);
        data_all = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setItemCount(int count) {
        limit = count;
    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), limit);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PokemonList> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()) {
                filteredList.addAll(data_all);
            } else {
                for (PokemonList entry : data_all) {
                    //System.out.println(game.getName() + constraint.toString().toLowerCase());
                    if (entry.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || //search by name
                    String.valueOf(entry.getId()).contains(constraint)) { //search by ID
                        filteredList.add(entry);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends PokemonList>) results.values);
            notifyDataSetChanged();
        }
    };

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
