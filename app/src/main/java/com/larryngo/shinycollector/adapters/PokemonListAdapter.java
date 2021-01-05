package com.larryngo.shinycollector.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.larryngo.shinycollector.R;
import com.larryngo.shinycollector.models.Pokemon;
import com.larryngo.shinycollector.models.PokemonGameIcon;
import com.larryngo.shinycollector.models.PokemonList;
import com.larryngo.shinycollector.util.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;


public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> implements Filterable {
    private final List<PokemonList> data;
    private List<PokemonList> data_all;
    private final PokemonListListener listener;
    private final Context mContext;
    private int limit = 890;

    public PokemonListAdapter(Context context, ArrayList<PokemonList> data, PokemonListListener listener) {
        this.mContext = context;
        this.data = data;
        this.data_all = new ArrayList<>(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_list_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PokemonList pokemon = data.get(position);

        if(Settings.isAnimModeOn()) {
            holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.list_anim_grow_right));
        }
        holder.name.setText(Pokemon.getDisplayName(pokemon.getId(), pokemon.getName()));
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

        Glide.with(mContext)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + pokemon.getId() + ".png")
                .centerCrop()
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
                    if (Pokemon.getDisplayName(entry.getId(), entry.getName()).toLowerCase().contains(constraint.toString().toLowerCase()) || //search by name
                            String.valueOf(entry.getId()).toLowerCase().startsWith(constraint.toString().toLowerCase())) { //search by ID
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
        private final LinearLayout container;
        private final ImageView image;
        private final TextView id;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.pokemon_list_entry_card);
            image = view.findViewById(R.id.pokemon_view_entry_image);
            id = view.findViewById(R.id.pokemon_list_entry_id);
            name = view.findViewById(R.id.pokemon_view_entry_name);

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
