package com.larryngo.shinycollector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.larryngo.shinycollector.adapters.MethodListAdapter;
import com.larryngo.shinycollector.databinding.MethodListLayoutBinding;
import com.larryngo.shinycollector.models.Method;
import com.larryngo.shinycollector.util.LoadingDialog;
import com.larryngo.shinycollector.util.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.larryngo.shinycollector.StartHuntActivity.fm;

public class MethodListFragment extends Fragment {
    private MethodListLayoutBinding binding;
    private MethodListAdapter adapter;

    private final ArrayList<Method> list_methods = new ArrayList<>();
    private List<String> list_methods_names;

    private FragmentMethodListListener listener;

    public interface FragmentMethodListListener {
        void onInputMethodSent(Method entry) throws IOException;
    }

    /*
        OVERVIEW

        A simple list of all the methods that are currently available. This is just to remind the user
        what method they are using and doesn't provide much value.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MethodListLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        list_methods_names = Arrays.asList(getResources().getStringArray(R.array.list_methods_names));

        init();
        setupGrid();
        adapter = new MethodListAdapter(this.getContext(), list_methods);
        binding.methodListGrid.setAdapter(adapter);

        return view;
    }

    public void init() {
        //gridview clicks
        binding.methodListGrid.setOnItemClickListener((adapterView, view, position, id) -> {
            try {
                listener.onInputMethodSent(adapter.getList().get(position)); //sends that method to the main start hunt menu.
                Utilities.closeKeyboard(getActivity());
                fm.popBackStack(); //go back
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.methodListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    public void setupGrid() {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        loadingDialog.setMessage(R.string.dialog_loadingdata);

        for(int i = 0; i < list_methods_names.size(); i++) {
            Method method = new Method(i, list_methods_names.get(i)); //creates the method based on the names and assigns them an id
            list_methods.add(method);
        }
        Method otherMethod = new Method (100, "Other");
        list_methods.add(otherMethod);

        loadingDialog.dismissDialog();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentMethodListListener) {
            listener = (FragmentMethodListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentMethodListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
