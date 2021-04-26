package com.iamageo.conexaopororocav1.ui.home;

import android.content.Context;
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

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    //botão play, mais e menos
    private Button btn_play;
    private Button btn_mais;
    private Button btn_menos;

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
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        //inicializando components do findviewbyid
        initComponents(root);

        //escondendo o linear layout
        linearLayout.setVisibility(View.INVISIBLE);

        //variáveis do media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);

        //botão play e pause
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

        //botão volume mais
        btn_mais.setOnClickListener(v -> {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        });

        //botão volume menos
        btn_menos.setOnClickListener(v->{
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        });

        return root;
    }


    //função que iicializa os componentes da ui
    public void initComponents(View view) {
        btn_play = view.findViewById(R.id.btn_play);
        btn_mais = view.findViewById(R.id.btn_mais);
        btn_menos = view.findViewById(R.id.btn_menos);

        progressBarLoading = view.findViewById(R.id.pb_loading);
        linearLayout = view.findViewById(R.id.linearLayout);
    }

    //carregando stream
    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                System.out.println(strings[0]);
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        //apos o link ser carregado
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            //tornando linear layout visível
            progressBarLoading.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);

            //setando textos do botão
            btn_play.setEnabled(true);
            btn_play.setText("PLAY");
        }

    }

    //métodos do ciclo de vida - modificar
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