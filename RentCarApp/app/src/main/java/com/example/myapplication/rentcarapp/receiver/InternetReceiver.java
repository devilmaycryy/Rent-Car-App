package com.example.myapplication.rentcarapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.util.CheckInternetUtil;
import com.google.android.material.button.MaterialButton;

public class InternetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternetUtil.getNetworkInfo(context);
        if(status.equals("disconnected")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(layout_dialog);
            MaterialButton retryButton = layout_dialog.findViewById(R.id.retryButton);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);

            retryButton.setOnClickListener(view -> {
                alertDialog.dismiss();
                onReceive(context, intent);
            });
        }
    }
}