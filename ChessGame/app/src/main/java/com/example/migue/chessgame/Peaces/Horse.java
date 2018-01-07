package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Horse extends Peace implements Serializable {
    public Horse(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
    }
    int value = 3;
    @Override
    public boolean action(Table table ,int l, int n) {
        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite){
            if( ((l-sl==1 || l-sl==-1) && n-sn==2) || ((l-sl==1 || l-sl==-1) && n-sn==-2) || ((n-sn==1 || n-sn==-1) && l-sl==2) || ((n-sn==1 || n-sn==-1) && l-sl==-2) ){
                return true;
            }
        }

        return false;
    }

    public int getValue() {
        return value;
    }

    public String getType(){return "Horse";}
}
