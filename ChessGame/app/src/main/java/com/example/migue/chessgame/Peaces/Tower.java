package com.example.migue.chessgame.Peaces;

import android.util.Log;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class Tower extends Peace {
    public Tower(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
    }

    @Override
    public boolean action(Table table ,int l, int n)
    {
    int auxl=0, auxn=0, auxfor=0;
        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite){
            if( (n!=sn && l==sl) || (l!=sl && n==sn)){
                if(sl>l){//se for para a esq.
                    auxl = -1;
                    auxfor = sl-l;
                }
                else if(sl<l){//dir
                    auxl = 1;
                    auxfor = sl-l;
                }
                if(sn>n){//baixo
                    auxn=-1;
                    auxfor = sn-n;
                }
                else if(sn<n){//cima
                    auxn=1;
                    auxfor = sn-n;
                }


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

        }
        return false;
    }
    public String getType(){return "Tower";}
}
