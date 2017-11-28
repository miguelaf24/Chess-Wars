package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public abstract class Peace implements Serializable {
    boolean isWhite;

    public boolean isWhite() {
        return isWhite;
    }

    public Peace(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public abstract boolean action(Table table, int sl, int ln, int s, int l);
}
