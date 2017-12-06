package com.example.migue.chessgame.Logic;

import android.util.Log;

import com.example.migue.chessgame.Peaces.*;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public class Game implements Serializable {
    boolean singleplayer;
    boolean isWhiteTurn;
    int nJogada;
    Table table;
    // Istate state;


    public Game(boolean singleplayer) {
        isWhiteTurn = true;
        this.singleplayer = singleplayer;
        table = new Table();
        nJogada=0;
        //state = new IBeginning(table);
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean doIt(int sl, int sn, int l, int n) {


        if (getPeace(sl, sn).action(table, l, n)) { //se a accão devolver true

            if(table.getPeace(sl,sn) instanceof King && isKingCheck(l, n)){
                return false;
            }
            else if(changePeace(sl,sn,l,n)) {
                isWhiteTurn = !isWhiteTurn;
                nJogada++;
                verPawnToQueen();
                if(GameOver()){
                    Log.i("GAME","GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER");
                }
                else {
                    Log.i("GAME","Continuar");
                }
                return true;
            }
            /*
            table.setPeace(sl, sn, l, n);

            */

            return true;
        }

        return false;
    }

    private void verPawnToQueen() {
        for(int i = 0; i <8;i++){
            if(table.getPeace(i,0) instanceof Pawn){
                changeToQueen(i,0);
            }
            if(table.getPeace(i,7) instanceof Pawn){
                changeToQueen(i,7);
            }
        }
    }

    private void changeToQueen(int l, int n) {
        Peace p = new Queen(table.getPeace(l,n).isWhite(),l,n);
        table.setThisPeace(p,l,n);
        table.rmv(table.getPeace(l,n));
        table.addList(p);
    }

    public Peace getPeace(int i, int j) {
        return table.getPeace(i, j);
    }

    public boolean IsKCheck() {

        return isKingCheck((table.getlista().getKing(isWhiteTurn).getL()), (table.getlista().getKing(isWhiteTurn).getN()));
    }

    public boolean changePeace(int sl, int sn, int l, int n){
        boolean isF=false;
        Peace p = table.getPeace(l,n);      //Guarda a posição destino
        if(p.isFirstPlay())isF = true;
        table.setPeace(sl, sn, l, n);       //Move a peça selecionada para o destino
        table.setPeace(-1, -1, sl, sn);     //Mete a posição actual vazia

        if(IsKCheck()){                     //Caso o rei esteja em check:
            table.setPeace(l, n, sl, sn);       //Meter a peça no local Inicial
            table.setThisPeace(p, l, n);//Voltar a meter a peça removida no destino
            if(isF){
                p.setFirstMove();
            }
            return false;
        }
        else{
            table.rmv(p);
            if(isF && table.getPeace(l,n) instanceof Pawn)table.getPeace(l,n).setJogadaFirst(nJogada);
        }
        return true;
    }

    public boolean GameOver(){

        if (isWhiteTurn){

            for (int i = 0; i < table.getlista().white.size(); i++)
            {

                Log.i("Var",table.getlista().white.get(i).getType() + table.getlista().white.size() + " --> L " + table.getlista().white.get(i).getL()+ " - N " + table.getlista().white.get(i).getN());
                if(SaveMyKing(table.getlista().white.get(i).getL(),table.getlista().white.get(i).getN()))return false;
            }

        }else{
            for (int i = 0; i < table.getlista().black.size(); i++)
            {
                Log.i("Var",table.getlista().black.get(i).getType() + table.getlista().black.size() + " --> L " + table.getlista().black.get(i).getL()+ " - N " + table.getlista().black.get(i).getN());
                if(SaveMyKing(table.getlista().black.get(i).getL(),table.getlista().black.get(i).getN()))return false;
            }
        }
        return true;
    }

    public boolean trychangePeace(int sl, int sn, int l, int n){
        if(l<0||l>7||n<0||n>7)return false;
        Peace p = table.getPeace(l,n);
        if(p.isWhite()==isWhiteTurn && !(p instanceof Empty))return false;
        table.setPeace(sl, sn, l, n);
        table.setPeace(-1, -1, sl, sn);
        if(IsKCheck()){
            table.setPeace(l, n, sl, sn);
            table.setThisPeace(p, l, n);
            return false;
        }
        table.setPeace(l, n, sl, sn);
        table.setThisPeace(p, l, n);
        return true;
    }

    public boolean SaveMyKing(int l, int n){

        if(table.getPeace(l,n) instanceof King){
            if(trychangePeace(l,n,l+1,n))return true;
            if(trychangePeace(l,n,l+1,n+1))return true;
            if(trychangePeace(l,n,l+1,n-1))return true;
            if(trychangePeace(l,n,l,n-1))return true;
            if(trychangePeace(l,n,l,n+1))return true;
            if(trychangePeace(l,n,l-1,n))return true;
            if(trychangePeace(l,n,l-1,n+1))return true;
            if(trychangePeace(l,n,l-1,n-1))return true;
            if(table.getPeace(l,n).isFirstPlay()){
                if(table.getPeace(l+1,n) instanceof Empty && table.getPeace(l+2,n) instanceof Empty && table.getPeace(l+3,n).isFirstPlay())return true;
                if(table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-2,n) instanceof Empty && table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-4,n).isFirstPlay())return true;
            }
        } else if(table.getPeace(l,n) instanceof Pawn){
            if(table.getPeace(l,n).isWhite()){
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n+1) instanceof  Empty && table.getPeace(l,n+2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n+2))return true;
                if(!(table.getPeace(l+1,n+1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n+1))return true;
                }
                if(!(table.getPeace(l-1,n+1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n + 1)) return true;
                }
                if(table.getPeace(l,n+1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n+1))return true;
                }
            }
            else{
                if(!(table.getPeace(l+1,n-1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n-1))return true;
                }
                if(!(table.getPeace(l-1,n-1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n - 1)) return true;
                }
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n-1) instanceof  Empty && table.getPeace(l,n-2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n-2))return true;
                if(table.getPeace(l,n-1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n-1))return true;
                }
            }
        }else if (table.getPeace(l,n) instanceof Bishop){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                   if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                   if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }
        }else if (table.getPeace(l,n) instanceof Horse){
        } else if (table.getPeace(l,n) instanceof Queen){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }


            sl = l;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        } else if (table.getPeace(l,n) instanceof Tower){
            int sl = l;
            int sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        }
        return false;
    }

    public boolean isKingCheck(int l, int n) {
        int sl, sn, p;

        sl = l - 1;
        sn = n + 1;

        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião sup esq");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","Dama ou bispo ataque sup esq");
                return true;
            }

            p = 0;
            sl = sl - 1;
            sn = sn + 1;
        }


        sl = l;
        sn = n;
        sl = sl + 1;
        sn = sn + 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião sup dir");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","D ou B sup dir");
                return true;
            }

            p=0;
            sl = sl + 1;
            sn = sn + 1;
        }

        sl = l + 1;
        sn = n - 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {

                    Log.i("Ataque","Pião inf dir");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","D ou B inf dir");
                return true;
            }

            p = 0;
            sl = sl + 1;
            sn = sn - 1;

        }

        sl = l -1;
        sn = n -1;
        p=1;
        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//inf esq

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião inf esq");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","B ou D inf esq");
                return true;
            }

            p=0;
            sl = sl -1;
            sn = sn -1;
        }

            //se nao for diagonal
        sn=n;
        sl = l -1;
        while(sl<=7 && sl>=0) {//esq
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D esq");
                return true;
            }
            sl = sl -1;
        }

        sn=n;
        sl = l +1;

        while(sl<=7 && sl>=0) {//dir
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D dir");
                return true;
            }
            sl = sl +1;
        }


        sl=l;
        sn = n -1;

        while(sn<=7 && sn>=0) {//inf
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D baixo");
                return true;
            }
            sn = sn -1;
        }

        sl=l;
        sn = n +1;
        while(sn<=7 && sn>=0) {//sup
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D top");
                return true;
            }
            sn = sn +1;
        }

        //cavalo
        sl=l;
        sn=n;
        if(sl+1<=7 && sn+2<=7)            //sup sup dir
            if( table.getPeace(sl+1,sn+2) instanceof Horse && table.getPeace(sl+1,sn+2).isWhite()!=isWhiteTurn) {
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+2<=7 && sn+1<=7)            //sup dir dir
            if( table.getPeace(sl+2,sn+1) instanceof Horse && table.getPeace(sl+2,sn+1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+2<=7 && sn-1>=0)            //inf dir dir
            if( table.getPeace(sl+2,sn-1) instanceof Horse && table.getPeace(sl+2,sn-1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+1<=7 && sn-2>=0)            //inf inf dir
            if( table.getPeace(sl+1,sn-2) instanceof Horse && table.getPeace(sl+1,sn-2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-1>=0 && sn-2>=0)            //inf inf esq
            if( table.getPeace(sl-1,sn-2) instanceof Horse && table.getPeace(sl-1,sn-2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-2>=0 && sn-1>=0)            //inf esq esq
            if( table.getPeace(sl-2,sn-1) instanceof Horse && table.getPeace(sl-2,sn-1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-2>=0 && sn+1<=7)            //sup esq esq
            if( table.getPeace(sl-2,sn+1) instanceof Horse && table.getPeace(sl-2,sn+1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-1>=0 && sn+2<=7)            //sup sup esq
            if( table.getPeace(sl-1,sn+2) instanceof Horse && table.getPeace(sl-1,sn+2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }

        return false;
    }


}
