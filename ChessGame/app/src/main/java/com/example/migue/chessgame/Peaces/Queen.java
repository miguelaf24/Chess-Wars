package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

import java.io.Serializable;

/**
 * Created by migue on 27/11/2017.
 */

public class Queen extends Peace implements Serializable {
    public Queen(boolean isWhite, int l, int n) {
        super(isWhite,l,n);
    }

    @Override
    public boolean action(Table table ,int l, int n) {
        int auxl=0, auxn=0;
        if(table.getPeace(l,n) instanceof Empty || table.getPeace(l,n).isWhite()!=isWhite) {
            if (l - sl == n - sn || l - sl == sn - n) {//verifica isDiagonal
                if (sl > l) {//se for para a esq.
                    auxl = -1;
                } else {//dir
                    auxl = 1;
                }
                if (sn > n) {//baixo
                    auxn = -1;
                } else {//cima
                    auxn = 1;
                }
                int auxfor = sl - l;
                if (auxfor < 0) {
                    auxfor = -auxfor;
                }

                for (int i = 0; i < auxfor; i++) {
                    sl = sl + auxl;
                    sn = sn + auxn;
                    if (sl == l && sn == n && table.getPeace(l, n).isWhite != isWhite)
                        return true;
                    if (!(table.getPeace(sl, sn) instanceof Empty)) {
                        return false;
                    }
                }
                return true;
            }
            //se nao for diagonal
            if( (n!=sn && l==sl) || (l!=sl && n==sn)){
                int auxfor=0;
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

    public String getType(){return "Queen";}
}
