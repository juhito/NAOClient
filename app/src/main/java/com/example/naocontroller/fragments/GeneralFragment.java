package com.example.naocontroller.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;
import com.example.naocontroller.activities.ConnectNAOActivity;
import com.example.naocontroller.network.NAOClient;

import java.util.Arrays;

public class GeneralFragment extends Fragment {

    public static NAOClient client;

    private Spinner commandList;
    private TextView messageField;
    private Button sendMessage;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.general_fragment, container, false);
        if(client == null)
            client = new NAOClient(ConnectNAOActivity.getIP(), 8888);
        else
            System.out.println("Client already connected!");

        commandList = view.findViewById(R.id.commandList);
        messageField = view.findViewById(R.id.messageField);
        sendMessage = view.findViewById(R.id.sendMessageButton);

        // Let's populate spinner first
        Object[] response = client.sendMessage("getCommands");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,
                Arrays.stream(response).toArray(String[]::new));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commandList.setAdapter(adapter);

        sendMessage.setOnClickListener(i -> {
            if(commandList.getSelectedItem().equals("naoSpeak")) {
                // TODO: Checks for if the textfield is empty
                client.sendMessage(commandList.getSelectedItem().toString(),
                        messageField.getText().toString());
            }
            else {
                System.out.println(Arrays.toString(client.sendMessage(commandList.getSelectedItem().toString())));
            }
        });
        return(view);
    }
}
