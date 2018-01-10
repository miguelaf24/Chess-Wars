package com.example.migue.chessgame.Peaces;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.migue.chessgame.Logic.Table;
import com.example.migue.chessgame.R;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Pawn extends Peace implements Serializable {
    static final long serialVersionUID = 42L;

    boolean firstPlay;
    int jf;
    int value = 1;
    boolean twocases;
    public Pawn(boolean isWhite, int l, int n) {
        super(isWhite,l,n); twocases=false; firstPlay=true; jf=-2;}


    @Override
    public boolean action(Table table ,int l, int n) {

        if(table.getPeace(l,n) instanceof Empty) {
            if (isWhite && ( n- sn == 1  ||(firstPlay && n - sn == 2)) && l==sl ) {
                twocases=true;
                jf=table.getJogada();
                firstPlay = false;
                return true;
            } else if (!isWhite && (n - sn == -1 || (firstPlay && n - sn == -2)) && l==sl) {
                twocases=true;
                jf=table.getJogada();
                firstPlay = false;
                return true;
            } else if (isWhite && !table.getPeace(sl+1, sn).isWhite() &&table.getPeace((sl+1), sn)instanceof Pawn && table.getPeace(sl+1,sn).getTwoCases(table) && (n-sn==1 && l-sl==1)){
                table.rmv(table.getPeace(sl+1, sn)); table.setThisPeace(new Empty(true), sl+1, sn);
                return true;
            }else if (isWhite && !table.getPeace(sl+1, sn).isWhite() && table.getPeace((sl-1), sn)instanceof Pawn && table.getPeace(sl-1,sn).getTwoCases(table) && (n-sn==1 && l-sl==-1)){
                table.rmv(table.getPeace(sl-1, sn)); table.setThisPeace(new Empty(true), sl-1, sn);
                return true;
            } else if (!isWhite && table.getPeace(sl+1, sn).isWhite() && table.getPeace((sl+1), sn)instanceof Pawn && table.getPeace(sl+1,sn).getTwoCases(table) && (n-sn==-1 && l-sl==1)){
                table.rmv(table.getPeace(sl+1, sn)); table.setThisPeace(new Empty(true), sl+1, sn);
                return true;
            }else if (!isWhite && table.getPeace(sl+1, sn).isWhite() && table.getPeace((sl-1), sn)instanceof Pawn && table.getPeace(sl-1,sn).getTwoCases(table) && (n-sn==-1 && l-sl==-1)){
                table.rmv(table.getPeace(sl-1, sn)); table.setThisPeace(new Empty(true), sl-1, sn);
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

    public boolean getTwoCases(Table table){
        if(table.getJogada() == jf + 1 && twocases)
            return true;
        return false;
    };



    public String toStringA(Context cont) {
        Resources res = cont.getResources();

        return res.getString(R.string.pawn);
    }
    public int getValue() {
        return value;
    }
    public boolean isFirstPlay(){return firstPlay;}
    public void setFirstMove(){firstPlay=true;}
    public String getType(){return "Pawn";}
    public void setJogadaFirst(int a){
        firstPlay=false;
        jf = a;}
    public int getJogadaFirst(){return jf;}
}
