package com.example.migue.chessgame;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.migue.chessgame.Logic.GlobalProfile;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    Button bPlay;
    Button bProfile;
    Button bHistory;
    Button bAbout;
    Button bExit;
    Intent intent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        GlobalProfile globalProfile= (GlobalProfile) getApplicationContext();
        if (globalProfile.readProfileFromSharedPreferences().getName() == null) {
            Log.e(">>>>>>>>>>>", "NO profile name dude");
            Toast.makeText(this,"Please create a profile.", Toast.LENGTH_SHORT).show();

            int timeout = 1000; // make the activity visible for 4 seconds

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    //finish();
                    Intent homepage = new Intent(MainActivity.this, ProfileActivity.class);

                    startActivity(homepage);
                }
            }, timeout);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    2501);
        }

        bPlay = (Button) findViewById(R.id.bPlay);

        bPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(context, TypeGameActivity.class);
                startActivity(intent);
            }
        });

        bProfile = (Button) findViewById(R.id.btnCapture);

        bProfile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });


        bHistory = (Button) findViewById(R.id.bHistorico);

        bHistory.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                intent = new Intent(context, HistoryActivity.class);
                startActivity(intent);
            }
        });

        bAbout = (Button) findViewById(R.id.bAbout);

        bAbout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                intent = new Intent(context, AboutActivity.class);
                startActivity(intent);
            }
        });
        bExit = (Button) findViewById(R.id.bExit);

        bExit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

    }


}
