package com.larryngo.shinyhunter.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Counter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.HomeHuntingFragment.huntingViewModel;

public class HuntingAdapter extends RecyclerView.Adapter<HuntingAdapter.ViewHolder>{
    private View view;
    private Context mContext;
    private List<Counter> counters;
    private HuntingListener listener;

    public HuntingAdapter(Context c, HuntingListener listener){
        this.mContext = c;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_hunting_grid_entry, parent, false);
        return new HuntingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] pokemon_image = counters.get(position).getPokemon().getImage();
        String pokemon_name = counters.get(position).getPokemon().getName();
        String game_name = counters.get(position).getGame().getName();
        int count = counters.get(position).getCount();

        Glide.with(mContext)
                .load(pokemon_image)
                .placeholder(R.drawable.missingno)
                .into(holder.iconView);

        holder.nameView.setText(pokemon_name);
        holder.gameView.setText(game_name);
        holder.countView.setText(String.valueOf(count));
        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(mContext, v);
                menu.getMenuInflater().inflate(R.menu.hunting_entry, menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.hunting_menu_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage(mContext.getResources().getString(R.string.dialog_deleteconfirmation))
                                    .setPositiveButton(mContext.getResources().getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            huntingViewModel.deleteCounter(counters.get(position));
                                        }
                                    })
                                    .setNegativeButton(mContext.getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().show();
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return counters.size();
    }

    public void setCountersList(final List<Counter> update) {
        if(counters == null) {
            counters = update;
            notifyItemRangeInserted(0, update.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return counters.size();
                }

                @Override
                public int getNewListSize() {
                    return update.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return counters.get(oldItemPosition).getId() == update.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Counter oldCounter = counters.get(oldItemPosition);
                    Counter newCounter = counters.get(newItemPosition);
                    return newCounter.getId() == oldCounter.getId()
                            && Objects.equals(newCounter.getGame(), oldCounter.getGame())
                            && Objects.equals(newCounter.getPokemon(), oldCounter.getPokemon())
                            && Objects.equals(newCounter.getPlatform(), oldCounter.getPlatform())
                            && Objects.equals(newCounter.getMethod(), oldCounter.getMethod())
                            && newCounter.getCount() == oldCounter.getCount()
                            && newCounter.getStep() == oldCounter.getStep();
                }
            });
            counters = update;
            result.dispatchUpdatesTo(this);
        }
    }

    public Counter getCounterObject(int position) {
        return counters.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        GifImageView iconView;
        TextView nameView;
        TextView gameView;
        TextView countView;
        ImageView optionsMenu;

        public ViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.hunting_list_entry_image);
            nameView = view.findViewById(R.id.hunting_list_entry_name);
            gameView = view.findViewById(R.id.hunting_list_entry_game);
            countView = view.findViewById(R.id.hunting_list_entry_count);
            optionsMenu = view.findViewById(R.id.hunting_list_options);

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

    public interface HuntingListener {
        void onClick(View v, int position) throws ExecutionException, InterruptedException;
    }
}
