package com.example.thong.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class OptionActivity extends AppCompatActivity {

    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Intent intent = getIntent();
        if (intent.getStringExtra(PlayActivity.EXTRA_MESSAGE).length() != 0)
            message = intent.getStringExtra(PlayActivity.EXTRA_MESSAGE);
        SharedPreferences settings = getSharedPreferences("Sound&Music", Context.MODE_PRIVATE);
        String userName = settings.getString("username","USERNAME");
        String soundValue = settings.getString("sound_value","0");
        String musicValue = settings.getString("music_value","0");
        final SeekBar seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        final SeekBar seekBarSound = (SeekBar) findViewById(R.id.seekBarSound);
        final TextView MusicValue = (TextView) findViewById(R.id.textViewMusicValue);
        final TextView SoundValue = (TextView) findViewById(R.id.textViewSoundValue);
        final EditText Username = (EditText) findViewById(R.id.editTextName);
        Username.setText(userName);
        MusicValue.setText(musicValue);
        SoundValue.setText(soundValue);
        seekBarMusic.setProgress(Integer.valueOf(musicValue));
        seekBarSound.setProgress(Integer.valueOf(soundValue));
        seekBarMusic.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    int progressValue;
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        MusicValue.setText(String.valueOf(progressValue));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                        progressValue = progress;
                        MusicValue.setText(String.valueOf(progress));
                    }
                }
        );
        seekBarSound.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    int progressValue;
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SoundValue.setText(String.valueOf(progressValue));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                        progressValue = progress;
                        SoundValue.setText(String.valueOf(progress));
                    }
                }
        );
        final Button ok = (Button) findViewById(R.id.buttonOK);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final TextView MusicValue = (TextView) findViewById(R.id.textViewMusicValue);
                final TextView SoundValue = (TextView) findViewById(R.id.textViewSoundValue);
                final EditText Username = (EditText) findViewById(R.id.editTextName);
                SharedPreferences settings = getSharedPreferences("Sound&Music", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sound_value", (String) SoundValue.getText());
                editor.putString("music_value", (String) MusicValue.getText());
                editor.putString("username", String.valueOf(Username.getText()));
                editor.apply();
                if (message == "ingame") {
                    Intent intent= new Intent(OptionActivity.this, PlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(intent, 0);
                } else {
                    finish();
                }

            }
        });
        final Button cancel = (Button) findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (message == "ingame") {
                    Intent intent= new Intent(OptionActivity.this, PlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(intent, 0);
                } else {
                    finish();
                }
            }
        });
    }

}
