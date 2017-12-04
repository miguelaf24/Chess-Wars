package com.example.migue.chessgame.Peaces;

import android.util.Log;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class Pawn extends Peace {
    boolean firstPlay;
    public Pawn(boolean isWhite, int l, int n) {
        super(isWhite,l,n); firstPlay=true;}


    @Override
    public boolean action(Table table ,int l, int n) {

        if(table.getPeace(l,n) instanceof Empty) {
            if (isWhite && ( n- sn == 1  ||(firstPlay && n - sn == 2)) && l==sl ) {
                if (firstPlay) firstPlay = false;
                return true;
            } else if (!isWhite && (n - sn == -1 || (firstPlay && n - sn == -2)) && l==sl) {
                if (firstPlay) firstPlay = false;
                return true;
            }
        } else{
            if(isWhite && ( ((l==sl+1) || l==sl-1) && (n==sn+1)) && table.getPeace(l,n).isWhite()!=isWhite){
                return true;
            }
            if(!isWhite && ( ((l==sl+1) || l==sl-1) && (n==sn-1)) && table.getPeace(l,n).isWhite()!=isWhite){
                return true;
            }
        }


        return false;
    }
    public String getType(){return "Pawn";}
}
