package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class PlatformListFragment extends Fragment {
    private View view;
    private SearchView sv;
    private RecyclerView rv;

    private PlatformListAdapter adapter;
    private ArrayList<Platform> list_platforms = new ArrayList<>();
    private List<String> list_platforms_names;
    private List<String> list_platforms_tokens;

    private FragmentPlatformListListener fragment_listener;
    private PlatformListAdapter.PlatformListListener rv_listener;

    private LoadingDialog loadingDialog;

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
        if(view == null) {
            view = inflater.inflate(R.layout.platform_list_layout, container, false);
            rv = view.findViewById(R.id.platform_list_recycler);

            list_platforms_names = Arrays.asList(getResources().getStringArray(R.array.list_platforms_names));
            list_platforms_tokens = Arrays.asList(getResources().getStringArray(R.array.list_platforms_tokens));

            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            loadingDialog.setMessage("Loading platforms...");

            setOnClickListener();
            adapter = new PlatformListAdapter(getActivity(), new ArrayList<>(), rv_listener);
            rv.setAdapter(adapter);

            final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
            rv.setLayoutManager(layoutManager);

            collectData();

            loadingDialog.dismissDialog();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    void collectData() {
        for (int i = 0; i < list_platforms_tokens.size(); i++)
        {
            try{
                if(getContext() != null)
                {
                    String token = list_platforms_tokens.get(i);

                    InputStream is = getContext().getAssets().open("icons/platforms/" + token + ".png"); // uses token (file name) to get the image from assets
                    byte[] image = new byte[is.available()]; //creates image
                    is.read(image); //necessary for image to show up
                    is.close();

                    Platform platform = new Platform(list_platforms_names.get(i), image); //creates the platform
                    list_platforms.add(platform); //adds platform to the list
                    adapter.addDataList(list_platforms); //tells adapter to update the list based on the current list
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //recyclerview that will update the main hunt menu when an item is clicked.
    public void setOnClickListener() {
        rv_listener = (v, position) -> {
            try {
                fragment_listener.onInputPlatformSent(list_platforms.get(position)); //sends the platform to the main hunt menu
            } catch (IOException e) {
                e.printStackTrace();
            }
            fm.popBackStack();
        };
    }

    @Override
    public void onAttach(Context context) {
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
}
