package com.iamageo.conexaopororocav1.ui.home;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.iamageo.conexaopororocav1.R;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private Button btn_play;

    String stream = "https://s1.guaracast.com:8427/stream";

    boolean prepared = false;
    boolean started = false;

    MediaPlayer mediaPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btn_play = root.findViewById(R.id.btn_play);

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