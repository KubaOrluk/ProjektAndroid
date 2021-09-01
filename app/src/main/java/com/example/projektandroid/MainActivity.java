package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

        private boolean isMute;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            findViewById(R.id.Start).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GameActivity.class)));

            TextView highScoreTxt = findViewById(R.id.highScoreTxt);
            TextView distanceTxt = findViewById(R.id.distanceTxt);

            SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
            highScoreTxt.setText(getString(R.string.highscore) + " " + prefs.getInt("highscore", 0));
            distanceTxt.setText(getString(R.string.distanceTxt) + " " + prefs.getInt("distance", 0) + "m");

            isMute = prefs.getBoolean("isMute", false);

            ImageView volumeCtrl = findViewById(R.id.volumeControl);

            if(isMute){
                volumeCtrl.setImageResource(R.drawable.sound_off);
            }
            else{
                volumeCtrl.setImageResource(R.drawable.sound_on);
            }


            volumeCtrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isMute = !isMute;
                    if(isMute) {
                        volumeCtrl.setImageResource(R.drawable.sound_off);
                    } else {
                        volumeCtrl.setImageResource(R.drawable.sound_on);
                    }

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isMute", isMute);
                    editor.apply();
                }
            });


            }
        }