package com.example.naocontroller.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;

import java.util.Arrays;

public class BehaviorFragment extends Fragment {
    private Spinner behaviorList;
    private Button sendMessage;
    private static ArrayAdapter<String> behaviorAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.behavior_fragment, container, false);


        behaviorList = view.findViewById(R.id.behaviorList);
        sendMessage = view.findViewById(R.id.runBehaviorButton);


        if(behaviorAdapter == null) {
            Object[] response = GeneralFragment.client.sendMessage("getBehaviors");
            behaviorAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
                    Arrays.stream(response).toArray(String[]::new));

            behaviorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            behaviorList.setAdapter(behaviorAdapter);
        }

        sendMessage.setOnClickListener(i -> {
            System.out.println(Arrays.toString(
                    GeneralFragment.client.sendMessage("runBehavior",
                    behaviorList.getSelectedItem().toString())
            ));
        });

        return(view);
    }
}
