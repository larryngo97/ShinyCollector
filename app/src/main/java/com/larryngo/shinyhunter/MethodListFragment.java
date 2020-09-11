package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.larryngo.shinyhunter.models.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class MethodListFragment extends Fragment {
    private View view;
    private GridView gv;
    private MethodListAdapter adapter;

    private ArrayList<Method> list_methods = new ArrayList<>();
    private List<String> list_methods_names;

    private FragmentMethodListListener listener;

    public interface FragmentMethodListListener {
        void onInputMethodSent(Method entry) throws IOException;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.method_list_layout, container, false);
            list_methods_names = Arrays.asList(getResources().getStringArray(R.array.list_methods_names));
            gv = view.findViewById(R.id.method_list_grid);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Method entry = list_methods.get(position);
                    try {
                        listener.onInputMethodSent(entry);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fm.popBackStack();
                }
            });
        }

        adapter = new MethodListAdapter(this.getContext(), list_methods);
        gv.setAdapter(adapter);
        setupGrid();
        return view;
    }

    public void setupGrid() {
        for(int i = 0; i < list_methods_names.size(); i++) {
            Method method = new Method(i, list_methods_names.get(i));
            list_methods.add(method);
            System.out.println(list_methods.size());
        }
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
