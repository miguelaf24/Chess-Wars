package com.example.migue.chessgame.Logic;

/**
 * Created by migue on 03/01/2018.
 */

public class Profile {
    String name;

    byte[] Photo;

    public Profile(byte[] photo, String name) {
        Photo = photo;
        this.name = name;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
