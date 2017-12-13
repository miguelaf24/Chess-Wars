package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Empty extends Peace implements Serializable {
    public Empty(boolean isWhite) {
        super(isWhite, 0, 0);
    }

    @Override
    public boolean action(Table table ,int l, int n) {
        return false;
    }
    public String getType(){return "Empty";}

}
