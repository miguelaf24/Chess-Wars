package com.example.migue.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.migue.chessgame.Logic.GlobalProfile;
import com.example.migue.chessgame.Logic.Profile;

import java.io.ByteArrayInputStream;

public class ProfileActivity extends Activity {

    private TextView tv;
    private ImageView ivPhoto;
    private Profile profile;
    GlobalProfile globalProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv= findViewById(R.id.pname);
        ivPhoto= findViewById(R.id.image);
        globalProfile = (GlobalProfile) getApplicationContext();
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
