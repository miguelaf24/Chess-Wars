package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public abstract class Peace implements Serializable {
    boolean isWhite;
    int sl, sn;

    public boolean isWhite() {
        return isWhite;
    }

    public Peace(boolean isWhite, int l, int n) {
        this.isWhite = isWhite; sl=l; sn=n;
    }

    public abstract boolean action(Table table, int l, int n);

    public void setCoord(int i, int j){sl=i;sn=j;}

    public int getL(){
        return sl;
    }
    public int getN(){
        return sn;
    }

    public abstract String getType();
}
