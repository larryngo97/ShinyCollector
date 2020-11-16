package com.larryngo.shinyhunter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import com.larryngo.shinyhunter.models.Counter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.HomeHuntingFragment.huntingViewModel;

public class PokemonHuntActivity extends AppCompatActivity {
    private static String ARGUMENT_COUNTER_ID = "ARGUMENT_COUNTER_ID";
    private static String ARGUMENT_COUNTER = "ARGUMENT_COUNTER";

    public static int MAX_COUNT_VALUE = 999999;
    private final int MAX_STEP_VALUE = 99;

    private ImageButton button_back;
    private ConstraintLayout screen;
    private TextView pokemon_name;
    private TextView game_name;
    private TextView method_name;
    private GifImageView pokemon_image;
    private ImageView platform_image;
    private TextView counter_count;

    private ImageButton button_undo;
    private Button button_increment;
    private ImageButton button_editCount;
    private ImageButton button_editHunt;

    private Counter counter;

    public static void start(Activity activity, Counter counter) {
        Intent intent = new Intent(activity, PokemonHuntActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counter.getId());
        intent.putExtra(ARGUMENT_COUNTER, counter);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_hunting_layout);
        screen = findViewById(R.id.counter_screen);
        pokemon_name = findViewById(R.id.counter_pokemon_name);
        game_name = findViewById(R.id.counter_game_name);
        method_name = findViewById(R.id.counter_method_name);
        pokemon_image = findViewById(R.id.counter_image_pokemon);
        platform_image = findViewById(R.id.counter_image_platform);
        counter_count = findViewById(R.id.pokemon_count);
        button_back = findViewById(R.id.button_back);
        button_undo = findViewById(R.id.button_undo);
        button_increment = findViewById(R.id.button_increment);
        button_editCount = findViewById(R.id.button_editcount);
        button_editHunt = findViewById(R.id.button_edithunt);

        Bundle extras = getIntent().getExtras();

        if(extras.containsKey("ARGUMENT_COUNTER")){
            counter = getIntent().getParcelableExtra("ARGUMENT_COUNTER"); //receives object
            if(counter != null) {
                updateView();
                setupButtons();

                int counter_id = getIntent().getIntExtra("ARGUMENT_COUNTER_ID", 0);
                counter.setId(counter_id);
            } else {
                Toast.makeText(this, R.string.pokemonhuntactivity_error_counternotfound, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.pokemonhuntactivity_error_datanotfound, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void updateView() {
        pokemon_name.setText(counter.getPokemon().getNickname());
        game_name.setText(counter.getGame().getName().toUpperCase());
        method_name.setText(counter.getMethod().getName().toUpperCase());

        Glide.with(getApplicationContext())
                .load(counter.getPokemon().getImage())
                .placeholder(R.drawable.missingno)
                .into(pokemon_image);

        Glide.with(getApplicationContext())
                .load(counter.getPlatform().getImage())
                .placeholder(R.drawable.missingno)
                .into(platform_image);

        counter_count.setText(String.valueOf(counter.getCount()));

    }

    public void setupButtons() {
        screen.setOnClickListener(view -> {
            counter.add(counter.getStep());
            counter_count.setText(String.valueOf(counter.getCount()));

            huntingViewModel.modifyCounter(counter, counter.getCount());
        });

        button_back.setOnClickListener(v -> onBackPressed());

        button_undo.setOnClickListener(view -> {
            counter.add(-counter.getStep());
            counter_count.setText(String.valueOf(counter.getCount()));

            huntingViewModel.modifyCounter(counter, counter.getCount());
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
            dialog.setTitle(R.string.pokemonhuntactivity_dialog_increment_title)
                    .setMessage(R.string.pokemonhuntactivity_dialog_increment_desc)
                    .setPositiveButton(R.string.dialog_set, (dialog13, which) -> {
                        if(et_input.getText().toString().isEmpty()) {
                            dialog13.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 1 && newCount <= MAX_STEP_VALUE) {
                            counter.setStep(newCount);
                            String incrementText1 = "+" + counter.getStep();
                            button_increment.setText(incrementText1);

                            huntingViewModel.modifyStep(counter, newCount);
                        } else {
                            Toast.makeText(PokemonHuntActivity.this, R.string.pokemonhuntactivity_dialog_increment_invalid, Toast.LENGTH_SHORT).show();
                            dialog13.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, (dialog14, which) -> {
                        dialog14.cancel(); //goes back
                    });
            dialog.show();
        });

        button_editCount.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(PokemonHuntActivity.this);
            View dialogView = inflater.inflate(R.layout.edit_count_dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PokemonHuntActivity.this);
            dialog.setView(dialogView);
            EditText et_input = dialogView.findViewById(R.id.edit_count);
            et_input.setText(String.valueOf(counter.getCount()));

            //new dialog sequence
            dialog.setTitle(R.string.pokemonhuntactivity_dialog_count_title)
                    .setMessage(R.string.pokemonhuntactivity_dialog_count_desc)
                    .setPositiveButton(R.string.dialog_set, (dialog1, which) -> {
                        if(et_input.getText().toString().isEmpty())
                        {
                            dialog1.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 0 && newCount <= MAX_COUNT_VALUE) {
                            counter.setCount(newCount);
                            counter_count.setText(String.valueOf(counter.getCount()));

                            huntingViewModel.modifyCounter(counter, counter.getCount());
                        } else
                        {
                            Toast.makeText(PokemonHuntActivity.this, R.string.pokemonhuntactivity_dialog_count_invalid, Toast.LENGTH_SHORT).show();
                            dialog1.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, (dialog12, which) -> {
                        dialog12.cancel(); //goes back
                    });
            dialog.show();
        });

        button_editHunt.setOnClickListener(v -> {
            Intent intent = new Intent(PokemonHuntActivity.this, StartHuntActivity.class);
            intent.putExtra("ARGUMENT_ACTIVE_HUNT", counter.getId());
            startActivity(intent);
        });

    }
}
