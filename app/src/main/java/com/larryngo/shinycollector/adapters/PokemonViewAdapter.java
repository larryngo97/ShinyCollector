package com.larryngo.shinycollector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinycollector.R;
import com.larryngo.shinycollector.models.PokemonGameIcon;
import com.larryngo.shinycollector.util.Settings;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PokemonViewAdapter extends RecyclerView.Adapter<PokemonViewAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<PokemonGameIcon> data;
    private final PokemonViewListener listener;

    public PokemonViewAdapter(Context context, ArrayList<PokemonGameIcon> data, PokemonViewListener listener) {
        this.mContext = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_view_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PokemonGameIcon game = data.get(position);

        if(Settings.isAnimModeOn()) {
            holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.list_anim_grow_right));
        }

        String name = game.getName();
        holder.name.setText(name);

        Glide.with(mContext)
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
        private final LinearLayout container;
        private final ImageView image;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.pokemon_view_entry_card);
            image = view.findViewById(R.id.pokemon_view_entry_image);
            name = view.findViewById(R.id.pokemon_view_entry_name);

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
