package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.larryngo.shinyhunter.adapters.HomeHuntingAdapter;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class HomeHuntingFragment extends Fragment {
    protected static ArrayList<Counter> list = new ArrayList<>();
    protected HomeHuntingAdapter adapter;
    protected static HuntingViewModel huntingViewModel;

    private View view;
    private GridView gv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
            gv = view.findViewById(R.id.home_hunting_grid);

            huntingViewModel = new ViewModelProvider(this).get(HuntingViewModel.class);
            huntingViewModel.init();
            huntingViewModel.getHuntingList().observe(getViewLifecycleOwner(), new Observer<List<Counter>>() {
                @Override
                public void onChanged(List<Counter> counters) {
                    adapter.notifyDataSetChanged();
                }
            });
            huntingViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean) {
                        System.out.println("Adding entry...");
                    }
                    else {
                        System.out.println("Finished adding entry!");
                    }
                }
            });

            initList();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    public void initList() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PokemonHuntActivity.class);
                Counter counter = adapter.getCounterObject(i);
                intent.putExtra("counter", counter);

                startActivity(intent);
            }
        });

        adapter = new HomeHuntingAdapter(this.getContext(), huntingViewModel.getHuntingList().getValue());
        gv.setAdapter(adapter);
    }

    public void addToGrid(Counter counter) {
        if(adapter != null) {
            adapter.addData(counter);
        }
    }
}
