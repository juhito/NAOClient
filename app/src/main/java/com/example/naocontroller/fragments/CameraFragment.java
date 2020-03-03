package com.example.naocontroller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {

    private Button takePicture;
    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);
        takePicture = view.findViewById(R.id.takePicture);


        takePicture.setOnClickListener(i -> {
            List<Object> data = (ArrayList<Object>) GeneralFragment.client.sendMessage("takeImage");

            System.out.println("Received byte[]: " + data.get(6));

            byte[] byteData = (byte[]) data.get(6);
            ByteBuffer imageData = ByteBuffer.wrap(byteData);

            Drawable d = Drawable.createFromStream(new ByteArrayInputStream(byteData), null);

            imageView.setImageDrawable(d);
        });

        return(view);
    }

}
