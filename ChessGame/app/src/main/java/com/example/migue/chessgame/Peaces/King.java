package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class King extends Peace {
    public King(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
    }

    @Override
    public boolean action(Table table ,int l, int n)
    {

        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite){
            if( (n-sn==1 || n-sn==-1 || n==sn) && (l-sl==1 || l-sl==-1 || l==sl)){
                return true;
            }

        }
        return false;
    }
    public String getType(){return "King";}
}
