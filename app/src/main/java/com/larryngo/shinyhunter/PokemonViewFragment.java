package com.larryngo.shinyhunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.larryngo.shinyhunter.PokeAPI.PokeAPIService;
import com.larryngo.shinyhunter.models.Game_Pokemon;
import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.models.PokemonList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class PokemonViewFragment extends Fragment {
    private View view;
    private RecyclerView rv;
    private PokemonViewAdapter adapter;
    private GifImageView image_pokemon;
    private TextView tv_dex;
    private TextView tv_name;
    private TextView tv_type1;
    private TextView tv_type2;
    private Button button_confirm;

    private ArrayList<Game_Pokemon> game_list = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();
    private Pokemon pokemon = new Pokemon();

    private FragmentPokemonViewListener fragment_listener;
    private PokemonViewAdapter.PokemonViewListener rv_listener;


    public interface FragmentPokemonViewListener {
        void onInputPokemonSent(Pokemon pokemon) throws IOException;
    }

    public void receiveIndex(PokemonList pokemonList) {
        String name = pokemonList.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        int id = pokemonList.getId();

        String image_url = PokeAPIService.baseURL_shiny + id + ".png";

        pokemon.setName(name);
        pokemon.setId(pokemonList.getId());
        pokemon.setImage_url(image_url);

    }

    public void setTypesHelper(TextView tv_type, String token) {
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
        Glide.with(view.getContext())
                .load(pokemon.getImage_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(image_pokemon);

        String index_number;
        if(pokemon.getId()< 10) {
            index_number = "#00" + pokemon.getId();
            tv_dex.setText(index_number);
        }
        else if (pokemon.getId() < 100) {
            index_number = "#0" + pokemon.getId();
            tv_dex.setText(index_number);
        }
        else {
            index_number = "#" + pokemon.getId();
            tv_dex.setText(index_number);
        }

        tv_name.setText(pokemon.getName());

        tv_type1.setVisibility(View.INVISIBLE);
        tv_type2.setVisibility(View.INVISIBLE);
        if(!pokemon.getTypes().isEmpty()) {
            tv_type1.setVisibility(View.VISIBLE);
            String type = pokemon.getTypes().get(0);
            setTypesHelper(tv_type1, type);
        }
        if(pokemon.getTypes().size() == 2) {
            tv_type2.setVisibility(View.VISIBLE);
            String type = pokemon.getTypes().get(1);
            setTypesHelper(tv_type2, type);
        }
    }

    //https://stackoverflow.com/questions/27753634/android-bitmap-save-without-transparent-area
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

    public void setOnClickListener() {
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
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if(getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    Glide.with(view.getContext())
                            .load(adapter.getData().get(position).getImage_url())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.missingno)
                            .into(image_pokemon);

                    pokemon.setImage_url(adapter.getData().get(position).getImage_url());
                });
            }
        }).start();
    }

    void collectData() {
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
                Game_Pokemon game;

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
                    game = new Game_Pokemon("Default", iconUrl);
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
                    game = new Game_Pokemon("Red/Blue", iconUrl);
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
                    game = new Game_Pokemon("Yellow", iconUrl);
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
                    game = new Game_Pokemon("Gold", iconUrl);
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
                    game = new Game_Pokemon("Silver", iconUrl);
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
                    game = new Game_Pokemon("Crystal", iconUrl);
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
                    game = new Game_Pokemon("Ruby/Sapphire", iconUrl);
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
                    game = new Game_Pokemon("Emerald", iconUrl);
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
                    game = new Game_Pokemon("FR/LG", iconUrl);
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
                    game = new Game_Pokemon("D/P", iconUrl);
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
                    game = new Game_Pokemon("D/P Female", iconUrl);
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
                    game = new Game_Pokemon("Platinum", iconUrl);
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
                    game = new Game_Pokemon("Platinum Female", iconUrl);
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
                    game = new Game_Pokemon("HG/SS", iconUrl);
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
                    game = new Game_Pokemon("HG/SS Female", iconUrl);
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
                    game = new Game_Pokemon("B/W", iconUrl);
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
                    game = new Game_Pokemon("B/W Female", iconUrl);
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
                    game = new Game_Pokemon("X/Y", iconUrl);
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
                    game = new Game_Pokemon("X/Y Female", iconUrl);
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
                    game = new Game_Pokemon("OR/AS", iconUrl);
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
                    game = new Game_Pokemon("OR/AS Female", iconUrl);
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
                    game = new Game_Pokemon("Sun/Moon", iconUrl);
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
                    game = new Game_Pokemon("Sun/Moon Female", iconUrl);
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
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.pokemon_view_layout, container, false);
            image_pokemon = view.findViewById(R.id.pokemon_view_image);
            tv_dex = view.findViewById(R.id.pokemon_view_id);
            tv_name = view.findViewById(R.id.pokemon_view_name);
            tv_type1 = view.findViewById(R.id.pokemon_view_type1);
            tv_type2 = view.findViewById(R.id.pokemon_view_type2);
            button_confirm = view.findViewById(R.id.pokemon_view_button_confirm);
            rv = view.findViewById(R.id.pokemon_view_recycler);

            button_confirm.setOnClickListener(view -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable)image_pokemon.getDrawable()).getBitmap();
                    bitmap = cropBitmapTransparency(bitmap); //crop the bitmap
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    pokemon.setImage(bytes);

                    fragment_listener.onInputPokemonSent(pokemon);
                    fm.popBackStack();
                    fm.popBackStack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            setOnClickListener();
            adapter = new PokemonViewAdapter(this.getContext(), new ArrayList<>(), rv_listener);
            rv.setHasFixedSize(true);
            rv.setAdapter(adapter);

            final GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
            rv.setLayoutManager(layoutManager);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        typeList.clear();
        game_list.clear();

        collectData();
    }

    @Override
    public void onAttach(Context context) {
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
}
