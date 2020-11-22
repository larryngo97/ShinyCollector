package com.larryngo.shinyhunter.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.larryngo.shinyhunter.R;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;
    private TextView tv_text;

    public LoadingDialog(Activity mActivity) {
        activity = mActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.loading_dialog, null);
        tv_text = view.findViewById(R.id.loading_text);
        builder.setView(view);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setMessage(int message) {
        tv_text.setText(message);
    }

}
