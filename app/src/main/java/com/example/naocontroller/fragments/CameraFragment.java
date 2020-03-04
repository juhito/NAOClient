package com.example.naocontroller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class CameraFragment extends Fragment {

    private Button takePicture;
    private ImageView imageView;
    private static Drawable finalImage;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);
        takePicture = view.findViewById(R.id.takePicture);

        if(finalImage != null)
            imageView.setImageDrawable(finalImage);

        takePicture.setOnClickListener(i -> {
            AsyncTask.execute(() -> {
                byte[] byteData = (byte[]) GeneralFragment.client.sendMessage("takeImage");

                finalImage = Drawable.createFromStream(new ByteArrayInputStream(byteData), null);

                getActivity().runOnUiThread(() -> {
                    imageView.setImageDrawable(finalImage);
                });
            });
        });

        return(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
