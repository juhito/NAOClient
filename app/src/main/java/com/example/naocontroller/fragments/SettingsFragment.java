package com.example.naocontroller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;
import com.example.naocontroller.activities.ConnectNAOActivity;
import com.example.naocontroller.activities.MainActivity;
import com.example.naocontroller.network.NAOClient;
import com.example.naocontroller.network.NaoThreadHandler;

import java.io.IOException;
import java.util.Arrays;

public class SettingsFragment extends Fragment {
    private TextView textView;
    private static NaoThreadHandler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);


        textView = view.findViewById(R.id.tempText);


        if(handler == null) {
            try {
                handler = new NaoThreadHandler(ConnectNAOActivity.getIP(), 8888, "getTempData", "");
                new Thread(handler).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        System.out.println("inside thread");
                        // TODO: Find a way to update this when not in this fragment!
                        getActivity().runOnUiThread(() ->  {
                            if(handler.getAnswer() == null && textView.getText().length() < 1)
                                textView.setText("Getting information from server, just a second...");

                            if(handler.getAnswer() != null) textView.setText(String.format("%s %s", handler.getAnswer()[0], handler.getAnswer()[1]));
                        });
                        sleep(5000);
                    }
                } catch (Exception ignored) {

                }
            }
        }.start();

        return(view);
    }
}
