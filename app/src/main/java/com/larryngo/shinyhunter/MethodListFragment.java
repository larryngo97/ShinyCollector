package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
    private GridView gv;
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
            gv = view.findViewById(R.id.method_list_grid);

            //gridview clicks
            gv.setOnItemClickListener((adapterView, view, position, id) -> {
                Method entry = list_methods.get(position); //gets the current method clicked on
                try {
                    listener.onInputMethodSent(entry); //sends that method to the main start hunt menu.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fm.popBackStack(); //go back
            });

            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            loadingDialog.setMessage("Loading methods...");

            adapter = new MethodListAdapter(this.getContext(), list_methods);
            gv.setAdapter(adapter);
            setupGrid();

            loadingDialog.dismissDialog();

        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    public void setupGrid() {
        for(int i = 0; i < list_methods_names.size(); i++) {
            Method method = new Method(i, list_methods_names.get(i)); //creates the method based on the names and assigns them an id
            list_methods.add(method);
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
