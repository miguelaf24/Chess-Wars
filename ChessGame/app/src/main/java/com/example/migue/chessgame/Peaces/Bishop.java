package com.example.migue.chessgame.Peaces;

import android.content.Context;
import android.content.res.Resources;

import com.example.migue.chessgame.Logic.Table;
import com.example.migue.chessgame.R;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Bishop extends Peace  implements Serializable {
    static final long serialVersionUID = 42L;

    public Bishop(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
    }

    int value=3;


    public String toStringA(Context cont) {
        Resources res = cont.getResources();

        return res.getString(R.string.bishop);
    }
    @Override
    public boolean action(Table table ,int l, int n) {
        int auxl, auxn;
        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite)
            if(l-sl == n-sn || l-sl == sn-n){//verifica isDiagonal
                if(sl>l){//se for para a esq.
                    auxl = -1;
                }
                else{//dir
                    auxl = 1;
                }
                if(sn>n){//baixo
                    auxn=-1;
                }
                else{//cima
                    auxn=1;
                }
                int auxfor = sl-l;
                if(auxfor<0){
                    auxfor=-auxfor;
                }

                for(int i = 0; i <auxfor; i++){
                    sl=sl+auxl;
                    sn=sn+auxn;
                    if(sl==l && sn ==n && table.getPeace(l,n).isWhite!=isWhite)
                        return true;
                    if(!(table.getPeace(sl,sn) instanceof Empty)){
                        return false;
                    }
                }
                return true;

            }

        return false;
    }

    public int getValue() {
        return value;
    }

    public String getType(){return "Bishop";}
}
