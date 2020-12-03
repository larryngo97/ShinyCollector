package com.larryngo.shinycollector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.larryngo.shinycollector.adapters.PlatformListAdapter;
import com.larryngo.shinycollector.databinding.PlatformListLayoutBinding;
import com.larryngo.shinycollector.models.Platform;
import com.larryngo.shinycollector.util.LoadingDialog;
import com.larryngo.shinycollector.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import static com.larryngo.shinycollector.StartHuntActivity.fm;

public class PlatformListFragment extends Fragment {
    private PlatformListLayoutBinding binding;

    private PlatformListAdapter adapter;
    private final ArrayList<Platform> list_platforms = new ArrayList<>();
    private List<String> list_platforms_names;
    private List<String> list_platforms_tokens;

    private FragmentPlatformListListener fragment_listener;
    private PlatformListAdapter.PlatformListListener rv_listener;

    public interface FragmentPlatformListListener {
        void onInputPlatformSent(Platform platform) throws IOException;
    }

    /*
        OVERVIEW

        Shows up a list of platforms that can be selected and be sent to the main hunt menu. This is only
        a graphical addition.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlatformListLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        list_platforms_names = Arrays.asList(getResources().getStringArray(R.array.list_platforms_names));
        list_platforms_tokens = Arrays.asList(getResources().getStringArray(R.array.list_platforms_tokens));

        init();

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.platformListRecycler.setLayoutManager(layoutManager);

        adapter = new PlatformListAdapter(getContext(), new ArrayList<>(), rv_listener);
        binding.platformListRecycler.setAdapter(adapter);

        collectData();

        return view;
    }

    public void init() {
        rv_listener = (v, position) -> {
            try {
                fragment_listener.onInputPlatformSent(adapter.getList().get(position)); //sends the platform to the main hunt menu
                Utilities.closeKeyboard(getActivity());
                fm.popBackStack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        binding.platformListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    void collectData() {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        loadingDialog.setMessage(R.string.dialog_loadingdata);

        for (int i = 0; i < list_platforms_tokens.size(); i++)
        {
            try{
                if(getContext() != null)
                {
                    String token = list_platforms_tokens.get(i);

                    InputStream is = getContext().getAssets().open("icons/platforms/new/" + token + ".png"); // uses token (file name) to get the image from assets
                    byte[] image = new byte[is.available()]; //creates image
                    is.read(image); //necessary for image to show up
                    is.close();

                    Platform platform = new Platform(list_platforms_names.get(i), image); //creates the platform
                    list_platforms.add(platform); //adds platform to the list
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        adapter.addDataList(list_platforms); //tells adapter to update the list based on the current list
        loadingDialog.dismissDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentPlatformListListener) {
            fragment_listener = (FragmentPlatformListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentPlatformListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragment_listener = null;
        rv_listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
