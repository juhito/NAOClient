package com.example.naocontroller.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;
import com.example.naocontroller.activities.ConnectNAOActivity;
import com.example.naocontroller.network.NAOThreadConnection;

import java.io.IOException;
import java.util.List;

public class SettingsFragment extends Fragment {
    private static NAOThreadConnection handler;
    private TextView cpuText;
    private TextView batText;
    private TextView logText;
    private Button clearLog;
    private Handler repeatHandler;
    private Runnable repeatRunnable;
    private boolean stopRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        repeatHandler = new Handler();

        cpuText = view.findViewById(R.id.cpuTextView);
        batText = view.findViewById(R.id.batTextView);
        logText = view.findViewById(R.id.logText);
        clearLog = view.findViewById(R.id.clearLog);

        if(handler == null) {
            try {
                handler = new NAOThreadConnection(ConnectNAOActivity.getIP(), 8888,
                        "getTempData");
                new Thread(handler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        clearLog.setOnClickListener(v -> {
            logText.setText("");
        });


        stopRunnable = false;
        handler.enableMessages();

        repeatRunnable = () -> {
            try {
                if(cpuText.getText().length() < 1)
                    cpuText.setText("Getting info from server, just a second...");

                List<Double> test = (List<Double>) handler.getAnswer();


                if(handler.getAnswer() != null) {
                    Double cpuTemp = test.get(0);
                    Double batTemp = test.get(1);

                    String cpuTextString = "CPU:";
                    String cpuTempString = cpuTextString + " " + String.format("%.2f", cpuTemp);

                    String batTextString = "BAT:";
                    String batTempString = batTextString + " " + String.format("%.2f", batTemp);

                    Spannable cpuSpannable = new SpannableString(cpuTempString);
                    Spannable batSpannable = new SpannableString(batTempString);

                    // Beautiful
                    if(cpuTemp > 55 && cpuTemp < 70) {
                        cpuSpannable.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                cpuTextString.length() +1 ,
                                cpuTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                    else if(cpuTemp > 70) {
                        cpuSpannable.setSpan(new ForegroundColorSpan(Color.RED),
                                cpuTextString.length() + 1,
                                cpuTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        cpuSpannable.setSpan(new ForegroundColorSpan(Color.GREEN),
                                cpuTextString.length() + 1,
                                cpuTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if(batTemp > 55 && batTemp < 70) {
                        batSpannable.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                batTextString.length() + 1,
                                batTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if(batTemp > 70) {
                        batSpannable.setSpan(new ForegroundColorSpan(Color.RED),
                                batTextString.length() + 1,
                                batTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        batSpannable.setSpan(new ForegroundColorSpan(Color.GREEN),
                                batTextString.length() + 1,
                                batTempString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    cpuText.setText(cpuSpannable, TextView.BufferType.SPANNABLE);
                    batText.setText(batSpannable, TextView.BufferType.SPANNABLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!stopRunnable)
                repeatHandler.postDelayed(repeatRunnable, 1000);
        };

        repeatHandler.post(repeatRunnable);

        return(view);
    }

    @Override
    public void onStop() {
        super.onStop();

        stopRunnable = true;
        handler.disableMessages();
    }
}
