package com.example.migue.chessgame.Peaces;

import android.content.Context;
import android.content.res.Resources;

import com.example.migue.chessgame.Logic.Table;
import com.example.migue.chessgame.R;

import java.io.Serializable;

/**
 * Created by miguel on 27/11/2017.
 */

public class King extends Peace implements Serializable {
    static final long serialVersionUID = 42L;

    boolean firstPlay;
    int value =1000;
    public King(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
        firstPlay=true;
    }

    @Override
    public boolean action(Table table ,int l, int n)
    {

        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite){
            if( (n-sn==1 || n-sn==-1 || n==sn) && (l-sl==1 || l-sl==-1 || l==sl)){
                if (firstPlay) firstPlay = false;
                return true;
            }
            if(firstPlay){
                if(table.getPeace(sl+1,sn) instanceof Empty && table.getPeace(sl+2,sn) instanceof Empty && table.getPeace(sl+3,sn).isFirstPlay()){
                    table.setPeace(sl+3,sn,sl+1,sn);
                    table.setPeace(-1, -1, sl+3, sn);
                    return true;
                }
                if(table.getPeace(sl-1,sn) instanceof Empty && table.getPeace(sl-2,sn) instanceof Empty && table.getPeace(sl-1,sn) instanceof Empty && table.getPeace(sl-4,sn).isFirstPlay()){
                    table.setPeace(sl-4,sn,sl-1,sn);
                    table.setPeace(-1, -1, sl-4, sn);
                    return true;
                }

            }
        }
        return false;
    }


    public String toStringA(Context cont) {
        Resources res = cont.getResources();

        return res.getString(R.string.king);
    }
    public int getValue() {
        return value;
    }
    public boolean isFirstPlay(){return firstPlay;}
    public void setFirstMove(){firstPlay=true;}
    public String getType(){return "King";}
}
