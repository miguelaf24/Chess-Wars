package com.example.migue.chessgame.Logic;

import java.io.Serializable;

/**
 * Created by migue on 03/01/2018.
 */

public class Profile implements Serializable{
    String name;

    byte[] Photo;

    public Profile( String name) {

        this.name = name;
    }
    public Profile(){
        name=null;
        Photo=null;
    };
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
