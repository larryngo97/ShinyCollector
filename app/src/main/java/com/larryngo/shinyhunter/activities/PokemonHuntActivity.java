package com.larryngo.shinyhunter.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.models.Counter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import pl.droidsonroids.gif.GifImageView;

public class PokemonHuntActivity extends AppCompatActivity {
    private ConstraintLayout screen;
    private TextView pokemon_name;
    private TextView game_name;
    private GifImageView pokemon_image;
    private ImageView platform_image;
    private TextView counter_count;

    private ImageButton button_undo;
    private Button button_increment;
    private ImageButton button_edit;
    private ImageButton button_options;

    private Counter counter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_hunting_layout);
        screen = findViewById(R.id.counter_screen);
        pokemon_name = findViewById(R.id.counter_pokemon_name);
        game_name = findViewById(R.id.counter_game_name);
        pokemon_image = findViewById(R.id.counter_image_pokemon);
        platform_image = findViewById(R.id.counter_image_platform);
        counter_count = findViewById(R.id.pokemon_count);
        button_undo = findViewById(R.id.button_undo);
        button_increment = findViewById(R.id.button_increment);
        button_edit = findViewById(R.id.button_edit);
        button_options = findViewById(R.id.button_options);

        if(getIntent().hasExtra("counter")){
            counter = getIntent().getParcelableExtra("counter"); //receives object
            if(counter != null) {
                updateView();
            }
        } else {
            Toast.makeText(this, "Did not receive data...", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupButtons();
    }

    public void updateView() {
        pokemon_name.setText(counter.getPokemon().getName());

        String gameTitle = "GAME: " + counter.getGame().getName();
        game_name.setText(gameTitle);

        runOnUiThread(() -> {
            Glide.with(getApplicationContext())
                    .load(counter.getPokemon().getImage())
                    .placeholder(R.drawable.missingno)
                    .into(pokemon_image);

            Glide.with(getApplicationContext())
                    .load(counter.getPlatform().getImage())
                    .placeholder(R.drawable.missingno)
                    .into(platform_image);
        });

        counter_count.setText(String.valueOf(counter.getCount()));

    }

    public void setupButtons() {
        screen.setOnClickListener(view -> {
            counter.add(counter.getStep());
            counter_count.setText(String.valueOf(counter.getCount()));
        });

        button_undo.setOnClickListener(view -> {
            counter.add(-counter.getStep());
            counter_count.setText(String.valueOf(counter.getCount()));
        });

        String incrementText = "+" + counter.getStep();
        button_increment.setText(incrementText);
        button_increment.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(PokemonHuntActivity.this);
            View dialogView = inflater.inflate(R.layout.edit_count_dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PokemonHuntActivity.this);
            dialog.setView(dialogView);
            EditText et_input = dialogView.findViewById(R.id.edit_count);
            et_input.setText(String.valueOf(counter.getStep()));

            //new dialog sequence
            dialog.setTitle("Set Incremental Value")
                    .setMessage("Enter a number 1-99. Changes the value added/subtracted.")
                    .setPositiveButton("SET", (dialog13, which) -> {
                        if(et_input.getText().toString().isEmpty()) {
                            dialog13.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 1 && newCount <= 99) {
                            counter.setStep(newCount);
                            String incrementText1 = "+" + counter.getStep();
                            button_increment.setText(incrementText1);
                        } else {
                            Toast.makeText(PokemonHuntActivity.this, "Number needs to be 1-99!", Toast.LENGTH_SHORT).show();
                            dialog13.cancel();
                        }
                    })
                    .setNegativeButton("CANCEL", (dialog14, which) -> {
                        dialog14.cancel(); //goes back
                    });
            dialog.show();
        });

        button_edit.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(PokemonHuntActivity.this);
            View dialogView = inflater.inflate(R.layout.edit_count_dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PokemonHuntActivity.this);
            dialog.setView(dialogView);
            EditText et_input = dialogView.findViewById(R.id.edit_count);
            et_input.setText(String.valueOf(counter.getCount()));

            //new dialog sequence
            dialog.setTitle("Set Counter")
                    .setMessage("Enter a number 0-99999. Changes the counter.")
                    .setPositiveButton("SET", (dialog1, which) -> {
                        if(et_input.getText().toString().isEmpty())
                        {
                            dialog1.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 0 && newCount <= 99999) {
                            counter.setCount(newCount);
                            counter_count.setText(String.valueOf(counter.getCount()));
                        } else
                        {
                            Toast.makeText(PokemonHuntActivity.this, "Number needs to be 0-99999!", Toast.LENGTH_SHORT).show();
                            dialog1.cancel();
                        }
                    })
                    .setNegativeButton("CANCEL", (dialog12, which) -> {
                        dialog12.cancel(); //goes back
                    });
            dialog.show();
        });

    }
}
