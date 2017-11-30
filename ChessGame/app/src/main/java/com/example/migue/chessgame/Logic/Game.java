package com.example.migue.chessgame.Logic;

import android.util.Log;

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


        if(getPeace(sl, sn).action(table,l,n)){ //se a accão devolver true
            if(table.getlista().getKing(isWhiteTurn).getL()==l && table.getlista().getKing(isWhiteTurn).getN()==n && isKingCheck(l,n)){
                return false;
            }
            table.setPeace(getPeace(sl, sn), l, n);
            table.setPeace(new Empty(false), sl, sn);
            isWhiteTurn=!isWhiteTurn;
            return true;
        }

        return false;
    }
    public Peace getPeace(int i, int j){return table.getPeace(i, j);}

    public boolean IsKCheck(){

        return isKingCheck((table.getlista().getKing(isWhiteTurn).getL()), (table.getlista().getKing(isWhiteTurn).getN()));
    }

    public boolean isKingCheck(int l, int n){
        int sl, sn;
        sl=l;
        sn=n;

        if(!isWhiteTurn && ( ((l==sl+1) || l==sl-1) && (n==sn+1)) && table.getPeace(l,n).isWhite()!=isWhiteTurn && table.getPeace(l,n) instanceof Pawn){
            return true;
        }
        if(isWhiteTurn && ( ((l==sl+1) || l==sl-1) && (n==sn-1)) && table.getPeace(l,n).isWhite()!=isWhiteTurn && table.getPeace(l,n) instanceof Pawn){
            return true;
        }

        sl = sl -1;
        sn = sn +1;
        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//sup esq


            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Bishop))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sl = sl -1;
            sn = sn +1;
        }


        sl=l;
        sn=n;
        sl = sl +1;
        sn = sn +1;
        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//sup dir


            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Bishop))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sl = sl +1;
            sn = sn +1;
        }

        sl=l;
        sn=n;

        sl = sl +1;
        sn = sn -1;

        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//inf dir

            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Bishop))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;

            sl = sl +1;
            sn = sn -1;
        }

        sl=l;
        sn=n;

        sl = sl -1;
        sn = sn -1;

        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//inf esq

            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Bishop))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sl = sl -1;
            sn = sn -1;
        }

        sl=l;
        sn=n;
            //se nao for diagonal
        sl = sl -1;
        while(sl<=7 && sl>=0) {//esq

            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sl = sl -1;
        }

        sl=l;
        sn=n;

        sl = sl +1;

        while(sl<=7 && sl>=0) {//dir
            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sl = sl +1;
        }


        sl=l;
        sn=n;

        sn = sn -1;

        while(sn<=7 && sn>=0) {//inf
            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sn = sn -1;
        }

        sl=l;
        sn=n;
        sn = sn +1;
        while(sn<=7 && sn>=0) {//sup
            if (!(table.getPeace(sl,sn) instanceof Empty) && !(table.getPeace(sl,sn).isWhite()!=isWhiteTurn && (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower))) {
                return false;
            }
            else if(!(table.getPeace(sl,sn) instanceof Empty))return true;
            sn = sn +1;
        }



        return false;
    }


}
