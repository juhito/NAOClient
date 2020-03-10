package com.example.naocontroller.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CameraFragment extends Fragment {

    private Button takePicture;
    private Button recordAudio;
    private ImageView imageView;
    private AudioRecord recorder;
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

        if(finalImage != null)
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

            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                try {
                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            44100,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            AudioRecord.getMinBufferSize(44100,
                                    AudioFormat.CHANNEL_IN_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT));
                    soundData = new byte[AudioRecord.getMinBufferSize(44100,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT)];


                    recorder.startRecording();

                    while(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                        recorder.read(soundData, 0, soundData.length);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP) {
                if(recorder != null) {
                    recorder.stop();
                    recorder.release();

                    GeneralFragment.client.sendMessage("playRecording", (Object) soundData);
                }
            }
            /*
            AsyncTask.execute(() -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        int permission = ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.RECORD_AUDIO);

                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            String[] PERMISSIONS_STORAGE = {
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO
                            };
                            int REQUEST_EXTERNAL_STORAGE = 1;

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    PERMISSIONS_STORAGE,
                                    REQUEST_EXTERNAL_STORAGE
                            );
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
            });

             */

            return(false);
        });

        return(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
