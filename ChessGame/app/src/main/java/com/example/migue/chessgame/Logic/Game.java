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
        isWhiteTurn = true;
        this.multiplayer = multiplayer;
        table = new Table();
        //state = new IBeginning(table);
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean doIt(int sl, int sn, int l, int n) {


        if (getPeace(sl, sn).action(table, l, n)) { //se a accão devolver true

            if(table.getPeace(sl,sn) instanceof King && isKingCheck(l, n)){
                return false;
            }
            else if(changePeace(sl,sn,l,n)) {
                isWhiteTurn = !isWhiteTurn;
                return true;
            }
            /*
            table.setPeace(sl, sn, l, n);

            */

            return true;
        }

        return false;
    }

    public Peace getPeace(int i, int j) {
        return table.getPeace(i, j);
    }

    public boolean IsKCheck() {

        return isKingCheck((table.getlista().getKing(isWhiteTurn).getL()), (table.getlista().getKing(isWhiteTurn).getN()));
    }

    public boolean changePeace(int sl, int sn, int l, int n){

        Peace p = table.getPeace(l,n);
        table.setPeace(sl, sn, l, n);
        table.setPeace(-1, -1, sl, sn);
        if(IsKCheck()){
            table.setPeace(l, n, sl, sn);
            table.setThisPeace(p, l, n);
            return false;
        }
        return true;
    }

    public boolean isKingCheck(int l, int n) {
        int sl, sn, p;
        sl = l;
        sn = n;


        sl = sl - 1;
        sn = sn + 1;

        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                return true;
            }

            p = 0;
            sl = sl - 1;
            sn = sn + 1;
        }


        sl = l;
        sn = n;
        sl = sl + 1;
        sn = sn + 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                return true;
            }

            p=0;
            sl = sl + 1;
            sn = sn + 1;
        }

        sl = l;
        sn = n;

        sl = sl + 1;
        sn = sn - 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                return true;
            }

            p = 0;
            sl = sl + 1;
            sn = sn - 1;

        }

        sl=l;
        sn=n;

        sl = sl -1;
        sn = sn -1;
        p=1;
        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//inf esq

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                return true;
            }

            p=0;
            sl = sl -1;
            sn = sn -1;
        }

        sl=l;
        sn=n;
            //se nao for diagonal
        sl = sl -1;
        while(sl<=7 && sl>=0) {//esq
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                return true;
            }
            sl = sl -1;
        }

        sl=l;
        sn=n;

        sl = sl +1;

        while(sl<=7 && sl>=0) {//dir
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                return true;
            }
            sl = sl +1;
        }


        sl=l;
        sn=n;

        sn = sn -1;

        while(sn<=7 && sn>=0) {//inf
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                return true;
            }
            sn = sn -1;
        }

        sl=l;
        sn=n;
        sn = sn +1;
        while(sn<=7 && sn>=0) {//sup
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                return true;
            }
            sn = sn +1;
        }

        //cavalo
        sl=l;
        sn=n;
        if(sl+1<=7 && sn+2<=7)            //sup sup dir
            if( table.getPeace(sl+1,sn+2) instanceof Horse && table.getPeace(sl+1,sn+2).isWhite()!=isWhiteTurn)
                return true;
        if(sl+2<=7 && sn+1<=7)            //sup dir dir
            if( table.getPeace(sl+2,sn+1) instanceof Horse && table.getPeace(sl+2,sn+1).isWhite()!=isWhiteTurn)
                return true;
        if(sl+2<=7 && sn-1>=0)            //inf dir dir
            if( table.getPeace(sl+2,sn-1) instanceof Horse && table.getPeace(sl+2,sn-1).isWhite()!=isWhiteTurn)
                return true;
        if(sl+1<=7 && sn-2>=0)            //inf inf dir
            if( table.getPeace(sl+1,sn-2) instanceof Horse && table.getPeace(sl+1,sn-2).isWhite()!=isWhiteTurn)
                return true;
        if(sl-1>=0 && sn-2>=0)            //inf inf esq
            if( table.getPeace(sl-1,sn-2) instanceof Horse && table.getPeace(sl-1,sn-2).isWhite()!=isWhiteTurn)
                return true;
        if(sl-2>=0 && sn-1>=0)            //inf esq esq
            if( table.getPeace(sl-2,sn-1) instanceof Horse && table.getPeace(sl-2,sn-1).isWhite()!=isWhiteTurn)
                return true;
        if(sl-2>=0 && sn+1<=7)            //sup esq esq
            if( table.getPeace(sl-2,sn+1) instanceof Horse && table.getPeace(sl-2,sn+1).isWhite()!=isWhiteTurn)
                return true;
        if(sl-1>=0 && sn+2<=7)            //sup sup esq
            if( table.getPeace(sl-1,sn+2) instanceof Horse && table.getPeace(sl-1,sn+2).isWhite()!=isWhiteTurn)
                return true;

        return false;
    }


}
