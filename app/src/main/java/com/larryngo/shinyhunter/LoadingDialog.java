package com.larryngo.shinyhunter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    private View view;
    private TextView tv_text;

    LoadingDialog(Activity mActivity) {
        activity = mActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.loading_dialog, null);
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
