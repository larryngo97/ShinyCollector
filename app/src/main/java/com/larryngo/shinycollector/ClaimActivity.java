package com.larryngo.shinycollector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.larryngo.shinycollector.app.HomeActivity;
import com.larryngo.shinycollector.databinding.ActivityClaimBinding;
import com.larryngo.shinycollector.models.Counter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.larryngo.shinycollector.HomeCompletedFragment.completedViewModel;
import static com.larryngo.shinycollector.HomeHuntingFragment.huntingViewModel;

public class ClaimActivity extends AppCompatActivity {
    private final static String ARGUMENT_CLAIM_COUNTER = "ARGUMENT_CLAIM_COUNTER";
    private final static String ARGUMENT_CLAIM_COUNTER_ID = "ARGUMENT_CLAIM_COUNTER_ID";
    private Counter counter;
    private InterstitialAd ad;
    private ActivityClaimBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClaimBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getWindow().setEnterTransition(null);

        ad = new InterstitialAd(this);
        ad.setAdUnitId(getString(R.string.admob_interstitial_claim_id));
        ad.loadAd(new AdRequest.Builder().build());

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
        binding.pokemonNameText.setText(counter.getPokemon().getName());
        binding.numberEncountersText.setText(String.valueOf(counter.getCount()));
        binding.gameText.setText(counter.getGame().getName());
        binding.methodText.setText(counter.getMethod().getName());
        binding.dateStartText.setText(counter.getDateCreated());
        binding.dateEndText.setText(counter.getDateFinished());
        binding.timeElapsedText.setText(counter.timeElapsed());

        Glide.with(getApplicationContext())
                .load(counter.getPlatform().getImage())
                .placeholder(R.drawable.missingno)
                .into(binding.imagePlatform);

        Glide.with(getApplicationContext())
                .load(counter.getPokemon().getImage())
                .placeholder(R.drawable.missingno)
                .into(binding.imagePokemon);

        binding.buttonBack.setOnClickListener(v -> onBackPressed());

        binding.buttonAddcollection.setOnClickListener(v -> {
            huntingViewModel.deleteCounter(counter); //remove from current hunting list
            completedViewModel.addCounter(counter); //add to completed list

            Intent intent = new Intent(ClaimActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if(ad.isLoaded() || ad.isLoading()) {
                ad.show();
            }

            finish();
        });
    }
}
