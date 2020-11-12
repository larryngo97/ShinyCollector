package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import com.larryngo.shinyhunter.adapters.MethodListAdapter;
import com.larryngo.shinyhunter.models.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class MethodListFragment extends Fragment {
    private View view;
    private GridView gridView;
    private SearchView searchView;
    private MethodListAdapter adapter;

    private ArrayList<Method> list_methods = new ArrayList<>();
    private List<String> list_methods_names;

    private FragmentMethodListListener listener;
    private LoadingDialog loadingDialog;

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
        if(view == null) {
            view = inflater.inflate(R.layout.method_list_layout, container, false);
            list_methods_names = Arrays.asList(getResources().getStringArray(R.array.list_methods_names));
            gridView = view.findViewById(R.id.method_list_grid);
            searchView = view.findViewById(R.id.method_list_search);

            init();
            setupGrid();
            adapter = new MethodListAdapter(this.getContext(), list_methods);
            gridView.setAdapter(adapter);

        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    public void init() {
        //gridview clicks
        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            try {
                listener.onInputMethodSent(adapter.getList().get(position)); //sends that method to the main start hunt menu.
            } catch (IOException e) {
                e.printStackTrace();
            }
            fm.popBackStack(); //go back
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        loadingDialog.setMessage("Loading methods...");

        for(int i = 0; i < list_methods_names.size(); i++) {
            Method method = new Method(i, list_methods_names.get(i)); //creates the method based on the names and assigns them an id
            list_methods.add(method);
        }

        loadingDialog.dismissDialog();
    }

    @Override
    public void onAttach(Context context) {
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
}
