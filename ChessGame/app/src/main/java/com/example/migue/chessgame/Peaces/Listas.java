package com.example.migue.chessgame.Peaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno Santos on 11/29/2017.
 */

public class Listas {

    public ArrayList<Peace> white;
    public ArrayList<Peace> black;

    public Listas(){
        white = new ArrayList<>();
        black = new ArrayList<>();
    }
    public void addWhite(Peace c){
        white.add(c);
    }
    public void addBlack(Peace c){
        black.add(c);
    }
    public void rmvWhite(Peace c){
        white.remove(c);
    }
    public void rmvBlack(Peace c){
        black.remove(c);
    }

    public Peace getWhite(int l, int n){
        for(int i = 0 ; i<white.size(); i++){
            if(white.get(i).getL()==l && white.get(i).getN()==n){
                return white.get(i);
            }
        }
        return new Empty(false);
    }

    public Peace getKing(boolean isWhite){
     if (isWhite) {
         for (int i = 0; i < white.size(); i++) {
             if (white.get(i) instanceof King) {
                 return white.get(i);
             }
         }
     }else
         for(int i = 0 ; i<black.size(); i++){
             if(black.get(i) instanceof King){
                 return black.get(i);
             }
         }
         return null;
    }
    public Peace getBlack(int l, int n){
        for(int i = 0 ; i<black.size(); i++){
            if(black.get(i).getL()==l && black.get(i).getN()==n){
                return black.get(i);
            }
        }
        return new Empty(false);
    }
}
