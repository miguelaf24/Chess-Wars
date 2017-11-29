package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class King extends Peace {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean action(Table table, int sl, int sn, int l, int n)
    {

        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite){
            if( (n-sn==1 || n-sn==-1 || n==sn) && (l-sl==1 || l-sl==-1 || l==sl)){
                return true;
            }

        }
        return false;
    }
}
