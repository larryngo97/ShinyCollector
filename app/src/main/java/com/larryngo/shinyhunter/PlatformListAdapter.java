package com.larryngo.shinyhunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larryngo.shinyhunter.models.Platform;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlatformListAdapter extends RecyclerView.Adapter<PlatformListAdapter.ViewHolder> {
    private View view;
    private ArrayList<Platform> data;
    private Context context;
    private PlatformListListener listener;

    public PlatformListAdapter(Context c, ArrayList<Platform> data, PlatformListListener listener){
        this.context = c;
        this.data = data;
        this.listener = listener;
    }

    public void addDataList(ArrayList<Platform> entry) {
        data = entry;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlatformListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.platform_list_entry, parent, false);
        return new PlatformListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Platform platform = data.get(position);

        holder.name.setText(platform.getName());
        Bitmap bitmap = BitmapFactory.decodeByteArray(platform.getImage(), 0, platform.getImage().length);

        holder.image.setImageBitmap(bitmap); //sets the picture
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.platform_list_entry_name);
            image = view.findViewById(R.id.platform_list_entry_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface PlatformListListener {
        void onClick(View v, int position);
    }
}
