package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.models.Counter;

import pl.droidsonroids.gif.GifImageView;

public class PokemonHuntActivity extends AppCompatActivity {
    private ConstraintLayout counter_screen;
    private TextView counter_name;
    private GifImageView counter_pokemon_image;
    private ImageView counter_platform_image;
    private TextView counter_count;


    private Counter counter;
    /*

    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;

     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_hunting_layout);
        counter_screen = findViewById(R.id.counter_screen);
        counter_name = findViewById(R.id.counter_pokemon_name);
        counter_pokemon_image = findViewById(R.id.counter_image_pokemon);
        counter_platform_image = findViewById(R.id.counter_image_platform);
        counter_count = findViewById(R.id.pokemon_count);

        if(getIntent().hasExtra("counter")){
            counter = getIntent().getParcelableExtra("counter"); //receives object
            if(counter != null) {
                counter_name.setText(counter.getPokemon().getName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getApplicationContext())
                                .load(counter.getPokemon().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.missingno)
                                .into(counter_pokemon_image);

                        Glide.with(getApplicationContext())
                                .load(counter.getPlatform().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.missingno)
                                .into(counter_platform_image);
                    }
                });
                counter_count.setText(String.valueOf(counter.getCount()));

                setupButtons();
            }
        } else {
            Toast.makeText(this, "Did not receive data...", Toast.LENGTH_SHORT).show();
        }
    }

    public void setupButtons() {
        counter_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter.add(counter.getIncrement_count());
                counter_count.setText(String.valueOf(counter.getCount()));
            }
        });

        counter_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Count is currently: " + counter.getCount());
            }
        });
    }
}
