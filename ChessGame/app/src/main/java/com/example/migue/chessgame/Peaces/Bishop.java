package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class Bishop extends Peace {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean action(Table table, int sl, int sn, int l, int n) {
        int auxl, auxn;
        if(table.getPeace(sl,sn) instanceof Empty || table.getPeace(sl,sn).isWhite()!=isWhite)
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
                    if(!(table.getPeace(sl,sn) instanceof Empty)){
                        return false;
                    }
                }
                return true;

            }

        return false;
    }


}
