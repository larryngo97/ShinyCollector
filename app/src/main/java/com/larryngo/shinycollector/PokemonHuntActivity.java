package com.larryngo.shinycollector;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.larryngo.shinycollector.databinding.ActivityPokemonhuntBinding;
import com.larryngo.shinycollector.models.Counter;
import com.larryngo.shinycollector.util.Settings;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.transition.Fade;


import static com.larryngo.shinycollector.HomeHuntingFragment.huntingViewModel;
import static java.lang.Math.abs;

public class PokemonHuntActivity extends AppCompatActivity {
    ActivityPokemonhuntBinding binding;
    private static final String ARGUMENT_COUNTER_ID = "ARGUMENT_COUNTER_ID";
    private static final String ARGUMENT_COUNTER = "ARGUMENT_COUNTER";

    private Vibrator vibrator;
    public static int MAX_COUNT_VALUE = 999999;
    private static final int MAX_STEP_VALUE = 99;
    private static final int VIBRATION_TIME = 50;
    private static final long COUNTER_ANIMATION_DURATION = 1000;

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
        binding = ActivityPokemonhuntBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setExitTransition(null);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle extras = getIntent().getExtras();

        if(extras.containsKey("ARGUMENT_COUNTER")){
            counter = getIntent().getParcelableExtra("ARGUMENT_COUNTER"); //receives object
            if(counter != null) {
                updateView();
                setupButtons();

                int counter_id = getIntent().getIntExtra("ARGUMENT_COUNTER_ID", 0);
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
        binding.namePokemon.setText(counter.getPokemon().getNickname());

        Glide.with(getApplicationContext())
                .load(counter.getPokemon().getImage())
                .placeholder(R.drawable.missingno)
                .into(binding.counterImagePokemon);

        Glide.with(getApplicationContext())
                .load(counter.getPlatform().getImage())
                .placeholder(R.drawable.missingno)
                .into(binding.counterImagePlatform);

        binding.countPokemon.setText(String.valueOf(counter.getCount()));

    }

    public void setupButtons() {
        binding.huntAppbar.setOnClickListener(v -> {
            //Clicking does nothing
        });

        binding.counterScreen.setOnClickListener(view -> {
            vibrate();
            editCounter(counter.getStep());
        });

        binding.buttonBack.setOnClickListener(v -> onBackPressed());

        binding.buttonUndo.setOnClickListener(view -> {
            vibrate();
            editCounter(-counter.getStep());
        });

        String incrementText = "+" + counter.getStep();
        binding.buttonIcrementText.setText(incrementText);
        binding.buttonIncrement.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(PokemonHuntActivity.this);
            View dialogView = inflater.inflate(R.layout.edit_count_dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PokemonHuntActivity.this);
            dialog.setView(dialogView);
            EditText et_input = dialogView.findViewById(R.id.edit_count);
            et_input.setText(String.valueOf(counter.getStep()));

            //new dialog sequence
            dialog.setTitle(R.string.dialog_increment_title)
                    .setMessage(R.string.dialog_increment_desc)
                    .setPositiveButton(R.string.dialog_set, (dialog13, which) -> {
                        if(et_input.getText().toString().isEmpty()) {
                            dialog13.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 1 && newCount <= MAX_STEP_VALUE) {
                            counter.setStep(newCount);
                            String incrementText1 = "+" + counter.getStep();
                            binding.buttonIcrementText.setText(incrementText1);

                            huntingViewModel.modifyStep(counter, newCount);
                        } else {
                            Toast.makeText(PokemonHuntActivity.this, R.string.dialog_increment_invalid, Toast.LENGTH_SHORT).show();
                            dialog13.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, (dialog14, which) -> {
                        dialog14.cancel(); //goes back
                    });
            dialog.show();
        });

        binding.buttonEditcount.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(PokemonHuntActivity.this);
            View dialogView = inflater.inflate(R.layout.edit_count_dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PokemonHuntActivity.this);
            dialog.setView(dialogView);
            EditText et_input = dialogView.findViewById(R.id.edit_count);
            et_input.setText(String.valueOf(counter.getCount()));

            //new dialog sequence
            dialog.setTitle(R.string.dialog_count_title)
                    .setMessage(R.string.dialog_count_desc)
                    .setPositiveButton(R.string.dialog_set, (dialog1, which) -> {
                        if(et_input.getText().toString().isEmpty())
                        {
                            dialog1.cancel();
                        }

                        int newCount = Integer.parseInt(et_input.getText().toString());

                        if(newCount >= 0 && newCount <= MAX_COUNT_VALUE) {
                            counter.setCount(newCount);
                            binding.countPokemon.setText(String.valueOf(counter.getCount()));

                            huntingViewModel.modifyCounter(counter, counter.getCount());
                        } else
                        {
                            Toast.makeText(PokemonHuntActivity.this, R.string.dialog_count_invalid, Toast.LENGTH_SHORT).show();
                            dialog1.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, (dialog12, which) -> {
                        dialog12.cancel(); //goes back
                    });
            dialog.show();
        });

        binding.buttonEdithunt.setOnClickListener(v -> {
            Intent intent = new Intent(PokemonHuntActivity.this, StartHuntActivity.class);
            intent.putExtra("ARGUMENT_ACTIVE_HUNT", counter.getId());
            startActivity(intent);
        });

        binding.buttonClaim.setOnClickListener(v -> {
            if(counter.getCount() <= 0) {
                Toast.makeText(PokemonHuntActivity.this, R.string.error_claim_zero_encounters, Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PokemonHuntActivity.this);
                builder.setMessage(R.string.dialog_claim_desc)
                        .setTitle(R.string.dialog_claim_title)
                        .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                            Date date = new Date();
                            String stringDate = DateFormat.getDateTimeInstance().format(date);
                            counter.setDateFinished(stringDate);

                            Intent intent = new Intent(PokemonHuntActivity.this, ClaimActivity.class);
                            intent.putExtra("ARGUMENT_CLAIM_COUNTER_ID", counter.getId());
                            intent.putExtra("ARGUMENT_CLAIM_COUNTER", counter);

                            if(Settings.isAnimModeOn()) {
                                Pair<View, String>[] pairs = new Pair[2];
                                pairs[0] = new Pair<>(binding.counterImagePokemon, ViewCompat.getTransitionName(binding.counterImagePokemon));
                                pairs[1] = new Pair<>(binding.counterImagePlatform, ViewCompat.getTransitionName(binding.counterImagePlatform));
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        PokemonHuntActivity.this, pairs);

                                startActivity(intent, options.toBundle());
                            } else {
                                startActivity(intent);
                            }

                            /*
                            huntingViewModel.deleteCounter(counter); //remove from current hunting list
                            completedViewModel.addCounter(counter); //add to completed list
                            finish();

                             */
                        })
                        .setNegativeButton(R.string.dialog_no, (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });

    }

    public void editCounter(int step) {
        int value = counter.getCount() + step; //value when clicked
        if(value < 0) { //cannot go below 0
            value = 0;
        }

        if(Settings.isAnimModeOn()) {
            long duration = abs(step * 100); // 0.1 second per step
            if (duration > COUNTER_ANIMATION_DURATION) {
                duration = COUNTER_ANIMATION_DURATION; //1 second max (10 is the threshhold)
            }

            ObjectAnimator numberSizeAnimation =
                    ObjectAnimator.ofPropertyValuesHolder(binding.countPokemon,
                            PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f, 1.0f),
                            PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.5f, 1.0f));
            numberSizeAnimation.setDuration(duration);
            numberSizeAnimation.start();

            ValueAnimator numberChangeAnimation = ValueAnimator.ofInt(counter.getCount(), value);
            numberChangeAnimation.setDuration(duration);
            numberChangeAnimation.addUpdateListener(animation -> binding.countPokemon.setText(numberChangeAnimation.getAnimatedValue().toString()));
            numberChangeAnimation.start();
        } else {

            binding.countPokemon.setText(String.valueOf(value));
        }

        counter.add(step);
        huntingViewModel.modifyCounter(counter, counter.getCount());
    }

    public void vibrate() {
        if(Settings.isVibrateModeOn()) {
            vibrator.vibrate(VIBRATION_TIME);
        }
    }
}
