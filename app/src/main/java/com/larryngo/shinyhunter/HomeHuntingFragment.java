package com.larryngo.shinyhunter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.larryngo.shinyhunter.models.Counter;

import java.util.ArrayList;

public class HomeHuntingFragment extends Fragment {
    protected static ArrayList<Counter> list = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    protected static HomeHuntingAdapter adapter;

    private View view;
    private GridView gv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
            gv = view.findViewById(R.id.home_hunting_grid);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), PokemonHuntActivity.class);
                    Counter counter = adapter.getCounterObject(i);
                    intent.putExtra("counter", counter);

                    startActivity(intent);
                }
            });

            adapter = new HomeHuntingAdapter(this.getContext(), list);
            gv.setAdapter(adapter);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    public static void addToGrid(Counter counter) {
        if(adapter != null) {
            adapter.addData(counter);
        }
    }
}
