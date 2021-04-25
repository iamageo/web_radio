package com.iamageo.conexaopororocav1.ui.home;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.iamageo.conexaopororocav1.R;

import java.io.IOException;

public class HomeFragment extends Fragment {

    //botão pay
    private Button btn_play;

    //linear layout play e progress bar
    private ProgressBar progressBarLoading;
    private LinearLayout linearLayout;

    //volume
    private AudioManager audioManager;

    //stream link
    String stream = "https://s1.guaracast.com:8427/stream";

    //variaveis do carregamento da stream
    boolean prepared = false;
    boolean started = false;

    MediaPlayer mediaPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btn_play = root.findViewById(R.id.btn_play);
        progressBarLoading = root.findViewById(R.id.pb_loading);
        linearLayout = root.findViewById(R.id.linearLayout);

        linearLayout.setVisibility(View.INVISIBLE);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);

        btn_play.setOnClickListener(v -> {
            if (started) {
                started = false;
                mediaPlayer.pause();
                btn_play.setText("PLAY");


            } else {
                started = true;
                mediaPlayer.start();
                btn_play.setText("PAUSE");

            }
        });

        return root;
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBarLoading.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            btn_play.setEnabled(true);
            btn_play.setText("PLAY");
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        if(started) {
            mediaPlayer.pause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(started) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(prepared) {
            mediaPlayer.release();
        }

    }
}