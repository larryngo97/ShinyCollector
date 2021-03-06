package com.larryngo.shinycollector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.larryngo.shinycollector.PokeAPI.PokeAPIService;
import com.larryngo.shinycollector.adapters.PokemonViewAdapter;
import com.larryngo.shinycollector.databinding.PokemonViewLayoutBinding;
import com.larryngo.shinycollector.models.PokemonGameIcon;
import com.larryngo.shinycollector.models.Pokemon;
import com.larryngo.shinycollector.models.PokemonList;
import com.larryngo.shinycollector.util.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import static com.larryngo.shinycollector.StartHuntActivity.fm;

/*
    OVERVIEW

    This fragment contains all the available icons that can be selected from the PokeAPI servers.
    User can see what icon they have selected and when they are satisfied with their icon, they can
    confirm it to have it be the main pokemon image when hunting.
 */
public class PokemonViewFragment extends Fragment {
    private PokemonViewLayoutBinding binding;
    private PokemonViewAdapter adapter;

    private final ArrayList<PokemonGameIcon> game_list = new ArrayList<>();
    private final ArrayList<String> typeList = new ArrayList<>();
    private Pokemon pokemon;

    private FragmentPokemonViewListener fragment_listener;
    private PokemonViewAdapter.PokemonViewListener rv_listener;

    private LoadingDialog loadingDialog;

    public interface FragmentPokemonViewListener {
        void onInputPokemonSent(Pokemon pokemon) throws IOException;
    }

    //Receives the index from the PokemonListFragment class.
    //This helps create the pokemon class and find information based on that.
    public void receiveIndex(PokemonList pokemonList) {
        int id = pokemonList.getId();
        String name = pokemonList.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        String image_url = PokeAPIService.baseURL_shiny + id + ".png"; //Image for the DEFAULT shiny.

        pokemon = new Pokemon(id, name, new ArrayList<>(), image_url); //Creating the pokemon class.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PokemonViewLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setOnClickListener(); //recycler onclick setup
        setupAdapter();

        return view;
    }

    public void setupAdapter() {
        adapter = new PokemonViewAdapter(this.getContext(), new ArrayList<>(), rv_listener);
        final GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        binding.pokemonViewRecycler.setLayoutManager(layoutManager);
        binding.pokemonViewRecycler.setAdapter(adapter);
    }

    public void setOnClickListener() {
        //Confirming the selection will crop the image and send the image to
        //StartHuntFragment to update
        binding.pokemonViewButtonConfirm.setOnClickListener(view -> new Thread(() -> {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable)binding.pokemonViewImage.getDrawable()).getBitmap(); //gets the image from the top left
            bitmap = cropBitmapTransparency(bitmap); //crop the bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compressing to PNG
            byte[] bytes = stream.toByteArray();

            if(getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                try {
                    pokemon.setImage(bytes); //sets the image

                    fragment_listener.onInputPokemonSent(pokemon); //sends StartHuntFragment the data
                    fm.popBackStack(); //go back
                    fm.popBackStack(); //go back
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start());

        //Recycler onClick. This will generate a bitmap (from the image url) of the pokemon image
        //that is selected from the recyclerview. Additionally, it will update the image on the top
        //left corner.
        rv_listener = (v, position) -> new Thread(new Runnable() {
            Bitmap bitmap;
            @Override
            public void run() {
                try {
                    if(getContext() == null) return;
                    bitmap = Glide.with(getContext())
                            .as(Bitmap.class)
                            .load(adapter.getData().get(position).getImage_url())
                            .submit()
                            .get();

                    pokemon.setImage_url(adapter.getData().get(position).getImage_url());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if(getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Glide.with(requireContext())
                                .load(adapter.getData().get(position).getImage_url())
                                .placeholder(R.drawable.missingno)
                                .into(binding.pokemonViewImage));
            }
        }).start();
    }

    //Helper class to update the view of the pokemon current types. This has no relevancy to
    //the overall goal of this application but it is a nice feature!
    public void setTypesHelper(TextView tv_type, String token) {
        //Based on the token(type), it will set the current textview to show that type.
        if(getContext() != null) {
            switch (token) {
                case "normal":
                    tv_type.setText(R.string.type_normal);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_normal));
                    break;
                case "fire":
                    tv_type.setText(R.string.type_fire);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_fire));
                    break;
                case "fighting":
                    tv_type.setText(R.string.type_fighting);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_fighting));
                    break;
                case "water":
                    tv_type.setText(R.string.type_water);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_water));
                    break;
                case "flying":
                    tv_type.setText(R.string.type_flying);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_flying));
                    break;
                case "grass":
                    tv_type.setText(R.string.type_grass);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_grass));
                    break;
                case "poison":
                    tv_type.setText(R.string.type_poison);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_poison));
                    break;
                case "electric":
                    tv_type.setText(R.string.type_electric);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_electric));
                    break;
                case "ground":
                    tv_type.setText(R.string.type_ground);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_ground));
                    break;
                case "psychic":
                    tv_type.setText(R.string.type_psychic);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_psychic));
                    break;
                case "rock":
                    tv_type.setText(R.string.type_rock);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_rock));
                    break;
                case "ice":
                    tv_type.setText(R.string.type_ice);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_ice));
                    break;
                case "bug":
                    tv_type.setText(R.string.type_bug);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_bug));
                    break;
                case "dragon":
                    tv_type.setText(R.string.type_dragon);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_dragon));
                    break;
                case "ghost":
                    tv_type.setText(R.string.type_ghost);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_ghost));
                    break;
                case "dark":
                    tv_type.setText(R.string.type_dark);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_dark));
                    break;
                case "steel":
                    tv_type.setText(R.string.type_steel);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_steel));
                    break;
                case "fairy":
                    tv_type.setText(R.string.type_fairy);
                    tv_type.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.type_fairy));
                    break;
                default:
                    tv_type.setVisibility(View.GONE);
                    tv_type.setVisibility(View.GONE);
                    break;
            }
        }
    }

    void updateView() {
        //Updates the image on the upper left, and is the pokemon image that will be sent to start the hunt.
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> Glide.with(requireContext())
                .load(pokemon.getImage_url())
                .placeholder(R.drawable.missingno)
                .into(binding.pokemonViewImage));

        //Index number that goes up to 999
        String index_number;
        if(pokemon.getId()< 10) {
            index_number = "#00" + pokemon.getId();
            binding.pokemonViewId.setText(index_number);
        }
        else if (pokemon.getId() < 100) {
            index_number = "#0" + pokemon.getId();
            binding.pokemonViewId.setText(index_number);
        }
        else {
            index_number = "#" + pokemon.getId();
            binding.pokemonViewId.setText(index_number);
        }

        binding.pokemonViewName.setText(Pokemon.getDisplayName(pokemon.getId(), pokemon.getName()));

        //Both types are invisible until their types are revealed.
        binding.pokemonViewType1.setVisibility(View.INVISIBLE);
        binding.pokemonViewType2.setVisibility(View.INVISIBLE);
        if(!pokemon.getTypes().isEmpty()) {
            binding.pokemonViewType1.setVisibility(View.VISIBLE);
            String type = pokemon.getTypes().get(0);
            setTypesHelper( binding.pokemonViewType1, type); //Sets type 1
        }
        if(pokemon.getTypes().size() == 2) {
            binding.pokemonViewType2.setVisibility(View.VISIBLE);
            String type = pokemon.getTypes().get(1);
            setTypesHelper(binding.pokemonViewType2, type); //Sets type 2
        }
    }

    //https://stackoverflow.com/questions/27753634/android-bitmap-save-without-transparent-area
    //Crops the image (as some will be shown as very small because of the image dimensions)
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


    //Main function of the class. This will connect to the PokeAPI servers on that specific pokemon
    //based on ID and generates icons (if available) for that pokemon.
    void collectData() {
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        loadingDialog.setMessage(R.string.pokeapi_loadingdata);

        new Thread(() -> {
            HttpURLConnection request;
            try {
                System.out.println("Connecting to pokemon index " + pokemon.getId() + "...");
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemon.getId());
                request = (HttpURLConnection)url.openConnection();
                request.connect();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonElement gameElement;
                PokemonGameIcon game;

                int typesSize = root.getAsJsonObject()
                        .getAsJsonArray("types")
                        .size();

                for (int i = 0; i < typesSize; i++) {
                    String type = root.getAsJsonObject()
                            .getAsJsonArray("types")
                            .get(i)
                            .getAsJsonObject()
                            .getAsJsonObject("type")
                            .get("name")
                            .getAsString();

                    typeList.add(type);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_default), iconUrl);
                    game_list.add(game);
                }


                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-i")
                        .getAsJsonObject("red-blue")
                        .get("front_default");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_redblue), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-i")
                        .getAsJsonObject("yellow")
                        .get("front_default");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_yellow), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-ii")
                        .getAsJsonObject("gold")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_gold), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-ii")
                        .getAsJsonObject("silver")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_silver), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-ii")
                        .getAsJsonObject("crystal")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_crystal), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iii")
                        .getAsJsonObject("ruby-sapphire")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_rubysapphire), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iii")
                        .getAsJsonObject("emerald")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_emerald), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iii")
                        .getAsJsonObject("firered-leafgreen")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_fireredleafgreen), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("diamond-pearl")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_diamondpearl), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("diamond-pearl")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_diamondpearl_female), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("platinum")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_platinum), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("platinum")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_platinum_female), iconUrl);
                    game_list.add(game);
                }


                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("heartgold-soulsilver")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_heartgoldsoulsilver), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-iv")
                        .getAsJsonObject("heartgold-soulsilver")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_heartgoldsoulsilver_female), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-v")
                        .getAsJsonObject("black-white")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_blackwhite), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-v")
                        .getAsJsonObject("black-white")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_blackwhite_female), iconUrl);
                    game_list.add(game);
                }

                /* Maybe will be worked on a later date
                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-v")
                        .getAsJsonObject("black-white")
                        .getAsJsonObject("animated")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new Game_Pokemon("B/W Anim.", iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-v")
                        .getAsJsonObject("black-white")
                        .getAsJsonObject("animated")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new Game_Pokemon("B/W Female Anim.", iconUrl);
                    game_list.add(game);
                }

                 */

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vi")
                        .getAsJsonObject("x-y")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_xy), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vi")
                        .getAsJsonObject("x-y")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_xy_female), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vi")
                        .getAsJsonObject("omegaruby-alphasapphire")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_omegarubyalphasapphire), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vi")
                        .getAsJsonObject("omegaruby-alphasapphire")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_omegarubyalphasapphire_female), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vii")
                        .getAsJsonObject("ultra-sun-ultra-moon")
                        .get("front_shiny");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_sunmoon), iconUrl);
                    game_list.add(game);
                }

                gameElement = root.getAsJsonObject()
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("versions")
                        .getAsJsonObject("generation-vii")
                        .getAsJsonObject("ultra-sun-ultra-moon")
                        .get("front_shiny_female");
                if(!gameElement.isJsonNull()) {
                    String iconUrl = gameElement.getAsString();
                    game = new PokemonGameIcon(getResources().getString(R.string.icon_sunmoon_female), iconUrl);
                    game_list.add(game);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //updating the graphical interfaces
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.clearDataList();
                    adapter.addData(game_list);
                    pokemon.setTypes(typeList);
                    updateView();
                });
            }

            loadingDialog.dismissDialog();
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        typeList.clear();
        game_list.clear();

        collectData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentPokemonViewListener) {
            fragment_listener = (FragmentPokemonViewListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentGameListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragment_listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
