package com.example.migue.chessgame.Logic;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class GlobalProfile extends Application implements Serializable {

    private static final String PREFS_PROFILE_NAME = "PREFS_PROFILE_NAME";
    private static final String PREFS_PROFILE_PHOTO = "PREFS_PROFILE_PHOTO";

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.profile = readProfileFromSharedPreferences();
    }

    public Profile readProfileFromSharedPreferences() {
        Profile p = new Profile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        p.setName(prefs.getString(PREFS_PROFILE_NAME, null));

        String photo=(prefs.getString(PREFS_PROFILE_PHOTO, null));

         if(photo!=null) {
             byte[] decodedByte = Base64.decode(photo, 0);
             p.setPhoto(decodedByte);
         }

        return p;
    }

    public void writeProfileInSharedPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString(PREFS_PROFILE_NAME, profile.getName());


        if (profile.getPhoto() != null) {
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(profile.getPhoto(), 0, profile.getPhoto().length, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            editor.putString(PREFS_PROFILE_PHOTO, imageEncoded);
        }

        editor.commit();
    }

    public void savePhoto(byte[]photo){
        profile.setPhoto(photo);
        writeProfileInSharedPreferences();
    }

    public void saveProfile(Profile newProfile) {
        profile.setName(newProfile.getName());
        writeProfileInSharedPreferences();
    }
}
