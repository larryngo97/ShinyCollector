package com.larryngo.shinyhunter.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Counter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.HomeHuntingFragment.huntingViewModel;
import static com.larryngo.shinyhunter.HomeCompletedFragment.completedViewModel;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements Filterable {
    private final Context mContext;
    private List<Counter> counters;
    private List<Counter> counters_all;
    private final RecyclerViewListener listener;

    public HomeListAdapter(Context c, RecyclerViewListener listener){
        this.mContext = c;
        this.listener = listener;
        this.counters = new ArrayList<>();
        this.counters_all = new ArrayList<>(counters);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_grid_entry, parent, false);
        return new HomeListAdapter.ViewHolder(view);
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
        holder.optionsMenu.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(mContext, v);
            menu.getMenuInflater().inflate(R.menu.hunting_entry, menu.getMenu());
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_details) {
                    popupDetails(position);
                }
                else if (item.getItemId() == R.id.menu_delete) {
                    popupDelete(position);
                }
                return true;
            });
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
                    Counter newCounter = update.get(newItemPosition);
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
            counters_all = new ArrayList<>(counters);
            result.dispatchUpdatesTo(this);
        }
    }

    public void popupDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.dialog_deleteconfirmation))
                .setPositiveButton(mContext.getResources().getString(R.string.dialog_yes), (dialog, which) -> {
                    if(counters.get(position).getDateFinished() == null) { //hasn't been finished and therefore is in the hunting list
                        huntingViewModel.deleteCounter(counters.get(position));
                    } else {
                        completedViewModel.deleteCounter(counters.get(position)); //there is a finished date and therefore is completed
                    }
                })
                .setNegativeButton(mContext.getResources().getString(R.string.dialog_no), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public void popupDetails(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popupDialog = inflater.inflate(R.layout.popup_details, null);
        ImageView iv_image = popupDialog.findViewById(R.id.details_image_pokemon);
        TextView tv_nickname = popupDialog.findViewById(R.id.details_name_pokemon);
        TextView tv_id = popupDialog.findViewById(R.id.details_id);
        TextView tv_encounters = popupDialog.findViewById(R.id.details_encounters);
        TextView tv_timeElapsed = popupDialog.findViewById(R.id.details_timeelapsed);
        TextView tv_startDate = popupDialog.findViewById(R.id.details_startdate);
        TextView tv_captureDate = popupDialog.findViewById(R.id.details_capturedate);
        TextView tv_game = popupDialog.findViewById(R.id.details_game);
        TextView tv_generation = popupDialog.findViewById(R.id.details_generation);
        TextView tv_pokemon = popupDialog.findViewById(R.id.details_pokemon);
        TextView tv_method = popupDialog.findViewById(R.id.details_method);

        //center image
        Glide.with(mContext)
                .load(counters.get(position).getPokemon().getImage())
                .placeholder(R.drawable.missingno)
                .into(iv_image);
        //nickname
        if(counters.get(position).getPokemon().getNickname() != null)
            tv_nickname.setText(counters.get(position).getPokemon().getNickname());
        //encounters
        tv_encounters.setText(String.valueOf(counters.get(position).getCount()));
        //start date
        if(counters.get(position).getDateCreated() != null)
            tv_startDate.setText(counters.get(position).getDateCreated());
        //capture date
        if(counters.get(position).getDateFinished() != null)
            tv_captureDate.setText(counters.get(position).getDateFinished());
        //time elapsed
        tv_timeElapsed.setText(counters.get(position).timeElapsed());
        //game
        if(counters.get(position).getGame().getName() != null)
            tv_game.setText(counters.get(position).getGame().getName());
        //generation
        tv_generation.setText(String.valueOf(counters.get(position).getGame().getGeneration()));
        //id
        tv_id.setText(String.valueOf(counters.get(position).getPokemon().getId()));
        //pokemon
        if(counters.get(position).getPokemon().getName() != null)
            tv_pokemon.setText(counters.get(position).getPokemon().getName());
        //method
        if(counters.get(position).getMethod().getName() != null)
            tv_method.setText(counters.get(position).getMethod().getName());


        builder.setView(popupDialog);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Counter> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()) {
                filteredList.addAll(counters_all);
            } else {
                for (Counter counter : counters_all) {
                    if (counter.getPokemon().getName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                    counter.getPokemon().getNickname().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                    String.valueOf(counter.getPokemon().getId()).toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(counter);
                    }
                }
            }
            filteredList.sort(Counter.COMPARE_BY_LISTID_DESC);
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            counters.clear();
            counters.addAll((Collection<? extends Counter>) results.values);
            notifyDataSetChanged();
        }
    };

    public void refreshList() {
        counters.clear();
        counters.addAll(counters_all);
        counters.sort(Counter.COMPARE_BY_LISTID_DESC);
        notifyDataSetChanged();
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

    public interface RecyclerViewListener {
        void onClick(View v, int position) throws ExecutionException, InterruptedException;
    }
}
