package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.larryngo.shinyhunter.app.HomeActivity;
import com.larryngo.shinyhunter.models.Counter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.HomeCompletedFragment.completedViewModel;
import static com.larryngo.shinyhunter.HomeHuntingFragment.huntingViewModel;

public class ClaimActivity extends AppCompatActivity {
    private final static String ARGUMENT_CLAIM_COUNTER = "ARGUMENT_CLAIM_COUNTER";
    private final static String ARGUMENT_CLAIM_COUNTER_ID = "ARGUMENT_CLAIM_COUNTER_ID";
    private Counter counter;

    private AppBarLayout appBarLayout;
    private ImageButton button_back;
    private TextView tv_pokemon_name;
    private TextView tv_encounters;
    private TextView tv_game_name;
    private TextView tv_method_name;
    private TextView tv_date_start;
    private TextView tv_date_end;
    private TextView tv_time_elapsed;
    private Button button_addToCollection;
    private ImageView image_platform;
    private GifImageView image_pokemon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        getWindow().setEnterTransition(null);

        appBarLayout = findViewById(R.id.appbar);
        button_back = findViewById(R.id.button_back);
        tv_pokemon_name = findViewById(R.id.pokemon_name_text);
        tv_encounters = findViewById(R.id.number_encounters_text);
        tv_game_name = findViewById(R.id.game_text);
        tv_method_name = findViewById(R.id.method_text);
        tv_date_start = findViewById(R.id.date_start_text);
        tv_date_end = findViewById(R.id.date_end_text);
        tv_time_elapsed = findViewById(R.id.time_elapsed_text);
        image_platform = findViewById(R.id.image_platform);
        image_pokemon = findViewById(R.id.image_pokemon);
        button_addToCollection = findViewById(R.id.button_addcollection);

        Bundle extras = getIntent().getExtras();

        if(extras.containsKey(ARGUMENT_CLAIM_COUNTER)){
            counter = getIntent().getParcelableExtra(ARGUMENT_CLAIM_COUNTER); //receives object
            if(counter != null) {
                updateView();

                int counter_id = getIntent().getIntExtra(ARGUMENT_CLAIM_COUNTER_ID, 0);
                counter.setId(counter_id);
            } else {
                Toast.makeText(this, R.string.error_counter_not_found, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.error_data_not_found, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void updateView() {
        tv_pokemon_name.setText(counter.getPokemon().getName());
        tv_encounters.setText(String.valueOf(counter.getCount()));
        tv_game_name.setText(counter.getGame().getName());
        tv_method_name.setText(counter.getMethod().getName());
        tv_date_start.setText(counter.getDateCreated());
        tv_date_end.setText(counter.getDateFinished());
        tv_time_elapsed.setText(counter.timeElapsed());

        Glide.with(getApplicationContext())
                .load(counter.getPlatform().getImage())
                .placeholder(R.drawable.missingno)
                .into(image_platform);

        Glide.with(getApplicationContext())
                .load(counter.getPokemon().getImage())
                .placeholder(R.drawable.missingno)
                .into(image_pokemon);

        button_back.setOnClickListener(v -> onBackPressed());

        button_addToCollection.setOnClickListener(v -> {
            huntingViewModel.deleteCounter(counter); //remove from current hunting list
            completedViewModel.addCounter(counter); //add to completed list

            Intent intent = new Intent(ClaimActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        });
    }
}
