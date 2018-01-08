package com.example.migue.chessgame.Peaces;

import android.util.Log;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Pawn extends Peace implements Serializable {
    boolean firstPlay;
    int jf;
    int value = 1;
    public Pawn(boolean isWhite, int l, int n) {
        super(isWhite,l,n); firstPlay=true; jf=-2;}


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
    public int getValue() {
        return value;
    }
    public boolean isFirstPlay(){return firstPlay;}
    public void setFirstMove(){firstPlay=true;}
    public String getType(){return "Pawn";}
    public void setJogadaFirst(int a){jf = a;}
    public int getJogadaFirst(){return jf;}
}
