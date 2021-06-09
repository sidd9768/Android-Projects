package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioModel audioModel;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        audioModel = (AudioModel) intent.getSerializableExtra("audio");

        TextView audioName = findViewById(R.id.audioName);
        Button playBtn = findViewById(R.id.playBtn);
        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);

        if(audioModel != null){
            audioName.setText(audioModel.getaName());
        }else {
            audioName.setText("Piano.wav");
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
            }
        });

        viewAllMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }

    public void startPlaying(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        if(audioModel!=null){

            Toast.makeText(getApplicationContext(),"Playing from device; "+audioModel.getaPath(),Toast.LENGTH_SHORT).show();
            mediaPlayer = MediaPlayer.create(MusicPlayerActivity.this, Uri.parse(audioModel.getaPath()));
            mediaPlayer.start();

        }else{

            Toast.makeText(getApplicationContext(),"Playing from app",Toast.LENGTH_SHORT).show();
            try {
                AssetFileDescriptor afd = getAssets().openFd("piano.wav");
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        enableSeekBar();
    }

    private void stopPlaying() {

        if(mediaPlayer != null){
            mediaPlayer.stop();
        }

    }

    private void enableSeekBar() {

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        } , 0, 10);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        stopPlaying();
        super.onBackPressed();
    }
}
