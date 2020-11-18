package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larryngo.shinyhunter.adapters.HuntingAdapter;
import com.larryngo.shinyhunter.viewmodels.CompletedViewModel;
import com.larryngo.shinyhunter.viewmodels.CompletedViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCompletedFragment extends Fragment {
    public static CompletedViewModel completedViewModel;
    private HuntingAdapter adapter;
    private HuntingAdapter.RecyclerViewListener listener;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
        recyclerView = view.findViewById(R.id.home_hunting_rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CompletedViewModelFactory factory = new CompletedViewModelFactory(requireActivity().getApplication());
        completedViewModel = new ViewModelProvider(this, factory).get(CompletedViewModel.class);
        adapter = new HuntingAdapter(this.getContext(), listener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        completedViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                adapter.setCountersList(counters);
                recyclerView.setAdapter(adapter);
            }
        });

    }
}
