package com.example.migue.chessgame.Logic;

import com.example.migue.chessgame.Peaces.*;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public class Game implements Serializable {
    boolean multiplayer;
    boolean isWhiteTurn;
    Table table;
    // Istate state;

    public Game(boolean multiplayer) {
        isWhiteTurn=true;
        this.multiplayer = multiplayer;
        table = new Table();
        //state = new IBeginning(table);
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean doIt(int sl, int sn, int l, int n) {

            if(getPeace(sl, sn).action(table, sl,sn,l,n)){ //se a accão devolver true

                return true;
            }

        return false;
    }
    public Peace getPeace(int i, int j){return table.getPeace(i, j);}
}
