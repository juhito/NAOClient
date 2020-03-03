package com.example.naocontroller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

import com.example.naocontroller.R;
import com.example.naocontroller.activities.ConnectNAOActivity;
import com.example.naocontroller.network.NAOClient;

import java.util.List;
import java.nio.ByteBuffer;

public class GeneralFragment extends Fragment {

    public static NAOClient client;

    private Spinner commandList;
    private TextView messageField;
    private Button sendMessage;
    private Button moveUp;
    private Button moveBack;
    private Button moveLeft;
    private Button moveRight;
    private static ArrayAdapter<String> commandAdapter;
    private static float z_axis;


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
        moveUp = view.findViewById(R.id.moveUp);
        moveBack = view.findViewById(R.id.moveBack);
        moveLeft = view.findViewById(R.id.moveLeft);
        moveRight = view.findViewById(R.id.moveRight);

        List<String> response = (List<String>) client.sendMessage("getCommands");
        commandAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,
                response);

        commandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commandList.setAdapter(commandAdapter);

        moveUp.setOnClickListener(i -> {
            List<Float> position = (List<Float>) client.sendMessage("getPosition");
            client.sendMessage("naoMove", 0.1f, 0, 0);

        });

        moveBack.setOnClickListener(i -> {
            List<Float> position = (List<Float>) client.sendMessage("getPosition");
            client.sendMessage("naoMove", -0.1f, 0, 0);
        });

        moveLeft.setOnClickListener(i -> {
            List<Float> position = (List<Float>) client.sendMessage("getPosition");
            client.sendMessage("naoMove", 0, 0, position.get(2) + 0.001f);
        });

        moveRight.setOnClickListener(i -> {
            List<Float> position = (List<Float>) client.sendMessage("getPosition");
            client.sendMessage("naoMove", 0, 0, -position.get(2) - 0.001f);
        });

        sendMessage.setOnClickListener(i -> {

            String command = commandList.getSelectedItem().toString();
            String message = messageField.getText().toString();

            if(command.equals("naoSpeak")) {
                // TODO: Checks for if the textfield is empty
                if(message.length() < 1) {
                    Toast t = Toast.makeText(view.getContext(),"Message can't be empty!",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                else
                    client.sendMessage(command, message);
            }

            else if(command.equals("takeImage")) {


            }

            else {
                System.out.println(client.sendMessage(command));
            }
        });
        return(view);
    }
}
