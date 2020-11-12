package com.larryngo.shinyhunter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.PokemonGameIcon;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

public class PokemonViewAdapter extends RecyclerView.Adapter<PokemonViewAdapter.ViewHolder> {
    private View view;
    private Context context;
    private ArrayList<PokemonGameIcon> data;
    private PokemonViewListener listener;

    public PokemonViewAdapter(Context context, ArrayList<PokemonGameIcon> data, PokemonViewListener listener) {
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
        PokemonGameIcon game = data.get(position);
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

    public void addData(ArrayList<PokemonGameIcon> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<PokemonGameIcon> getData() {
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
            try {
                listener.onClick(view, getAdapterPosition());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface PokemonViewListener {
        void onClick(View v, int position) throws ExecutionException, InterruptedException;
    }
}
