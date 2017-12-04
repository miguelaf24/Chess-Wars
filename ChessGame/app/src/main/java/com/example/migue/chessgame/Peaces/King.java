package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class King extends Peace {
    boolean firstPlay;
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
                    table.setPeace(-1, -1, sl-4, sn);;
                    return true;
                }

            }
        }
        return false;
    }
    public boolean isFirstPlay(){return firstPlay;}
    public void setFirstMove(){firstPlay=true;}
    public String getType(){return "King";}
}
