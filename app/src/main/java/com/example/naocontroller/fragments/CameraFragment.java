package com.example.naocontroller.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.naocontroller.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CameraFragment extends Fragment {

    private Button takePicture;
    private Button recordAudio;
    private ImageView imageView;
    private MediaRecorder recorder;
    private static Drawable finalImage;
    private byte[] soundData;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);
        takePicture = view.findViewById(R.id.takePicture);
        recordAudio = view.findViewById(R.id.recordAudio);

        soundData = new byte[16384];

        if (finalImage != null)
            imageView.setImageDrawable(finalImage);

        takePicture.setOnClickListener(i -> {
            AsyncTask.execute(() -> {
                byte[] data = (byte[]) GeneralFragment.client.sendMessage("takeImage",
                        0, 2, "jpg");


                finalImage = Drawable.createFromStream(new ByteArrayInputStream(data), null);
                getActivity().runOnUiThread(() -> {
                    imageView.setImageDrawable(finalImage);
                });
            });
        });

        recordAudio.setOnTouchListener((v, event) -> {

            Path path = Paths.get(getActivity().getExternalCacheDir().getAbsolutePath() + "/record.wav");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                try {

                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    recorder.setOutputFile(path.toAbsolutePath().toString());
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    recorder.prepare();
                    recorder.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();

                    File f = new File(path.toAbsolutePath().toString());
                    try {
                        soundData = Files.readAllBytes(path);
                        GeneralFragment.client.sendMessage("playRecording", (Object) soundData);
                        f.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return (false);
        });

        return (view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
