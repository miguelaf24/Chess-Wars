package com.example.migue.chessgame;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.migue.chessgame.Logic.AllHistoricGames;
import com.example.migue.chessgame.Logic.GlobalProfile;
import com.example.migue.chessgame.Logic.Profile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ProfileActivity extends Activity {

    private TextView tv;
    private ImageView ivPhoto;
    private Profile profile;
    private TextView tvAllGames;
    private TextView tvMultiplayerGames;
    private TextView tvWinGames;
    private TextView tvLostGames;
    GlobalProfile globalProfile;
    AllHistoricGames allgames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    2501);
        }
        */
        File file = new File(Environment.getExternalStorageDirectory(), "history.dat");
        if(file.exists()) { //seFicheiroJaExistir
            try {
                ObjectInputStream ois = null;
                ois = new ObjectInputStream(new FileInputStream(file));
                allgames = (AllHistoricGames) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        tv= findViewById(R.id.pname);
        ivPhoto= findViewById(R.id.image);
        globalProfile = (GlobalProfile) getApplicationContext();
        tvAllGames = (TextView) findViewById(R.id.jogados);
        tvMultiplayerGames = (TextView) findViewById(R.id.multiplayer);
        tvWinGames = (TextView) findViewById(R.id.jogadosganhos);
        tvLostGames = (TextView) findViewById(R.id.jogadosperdidos);

        tvAllGames.setText(""+ allgames.getJogos().size());
        tvMultiplayerGames.setText("" + allgames.getJogosMultiPlayer());
        tvWinGames.setText("" + allgames.getJogosGanhos());
        tvLostGames.setText("" + allgames.getJogosPerdidos());
    }


    @Override
    protected void onResume() {
        super.onResume();
        profile = globalProfile.getProfile();
        tv.setText(profile.getName());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(profile.getPhoto() != null) {
            setPic(ivPhoto, profile.getPhoto());
            ivPhoto.setImageDrawable(null);
        }
    }

    private void setPic(View ivPhoto, byte[] photo) {
        Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        BitmapDrawable image = new BitmapDrawable(ivPhoto.getResources(), bmp);
            ivPhoto.setBackground(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_profile){
            startActivity(new Intent(this, CameraActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(tv.getText().length()>0)
        {
            profile.setName(tv.getText().toString());
            globalProfile.saveProfile(profile);
            finish();
        }

    }
}
