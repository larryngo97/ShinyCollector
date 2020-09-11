package com.larryngo.shinyhunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class StartHuntFragment extends Fragment {
    protected Button button_game;
    private Button button_pokemon;
    private Button button_platform;
    private Button button_method;
    private GifImageView image_pokemon;
    private ImageView image_platform;
    private Button button_start;

    private View view;
    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;


    public void updateGame(Game input){
        game = input;
        button_game.setText(game.getName());

        button_pokemon.setEnabled(true);
    }

    public void updatePokemon(Pokemon input) throws IOException {
        pokemon = input;
        button_pokemon.setText(pokemon.getName());

        if(pokemon.getIconType() == 1) {
            Glide.with(view.getContext())
                    .load(pokemon.getImage_url())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.missingno)
                    .into(image_pokemon);
        } else {
            AsyncTaskCrop task = new AsyncTaskCrop();
            task.execute();
        }

        button_platform.setEnabled(true);
    }

    public void updatePlatform(Platform input) {
        platform = input;
        button_platform.setText(platform.getName());

        Glide.with(view.getContext())
                .load(platform.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(image_platform);

        button_method.setEnabled(true);
    }

    public void updateMethod(Method input) {
        method = input;
        button_method.setText(method.getName());

        button_start.setEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_starthunt, container, false);

            button_game = view.findViewById(R.id.starthunt_button_selectgame);
            button_pokemon = view.findViewById(R.id.starthunt_button_selectpokemon);
            button_platform = view.findViewById(R.id.starthunt_button_selectplatform);
            button_method = view.findViewById(R.id.starthunt_button_selectmethod);
            image_pokemon = view.findViewById(R.id.starthunt_image_pokemon);
            image_platform = view.findViewById(R.id.starthunt_image_platform);
            button_start = view.findViewById(R.id.starthunt_button_start);

            init();
        }
        return view;
    }

    Bitmap cropBitmapTransparency(Bitmap sourceBitmap)
    {
        if(sourceBitmap == null) { return null; }
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++)
        {
            for(int x = 0; x < sourceBitmap.getWidth(); x++)
            {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if(alpha > 0)   // pixel is not 100% transparent
                {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }
        if((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public void init() {
        button_game.setEnabled(true);
        button_pokemon.setEnabled(false);
        button_platform.setEnabled(false);
        button_method.setEnabled(false);
        button_start.setEnabled(false);

        button_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                        .replace(R.id.starthunt_fragment_container, new GameListFragment()).addToBackStack(null).commit();
            }
        });

        button_pokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                        .replace(R.id.starthunt_fragment_container, new PokemonListFragment()).addToBackStack(null).commit();
            }
        });

        button_platform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                        .replace(R.id.starthunt_fragment_container, new PlatformListFragment()).addToBackStack(null).commit();
            }
        });

        button_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                        .replace(R.id.starthunt_fragment_container, new MethodListFragment()).addToBackStack(null).commit();
            }
        });
    }

    private class AsyncTaskCrop extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL url = new URL(pokemon.getImage_url());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream input = null;
                input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Bitmap croppedBitmap = cropBitmapTransparency(bitmap);
            pokemon.setBitmap(croppedBitmap);

            Glide.with(view.getContext())
                    .load(pokemon.getBitmap())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.missingno)
                    .into(image_pokemon);

        }
    }

}
