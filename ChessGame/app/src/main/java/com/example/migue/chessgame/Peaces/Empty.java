package com.example.migue.chessgame.Peaces;

import android.content.Context;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Empty extends Peace implements Serializable {
    static final long serialVersionUID = 42L;

    public Empty(boolean isWhite) {
        super(isWhite, 0, 0);
    }

    @Override
    public boolean action(Table table ,int l, int n) {
        return false;
    }
    public String getType(){return "Empty";}

    public int getValue() {
        return 0;
    }

    @Override
    public String toStringA(Context context) {
        return null;
    }
}
