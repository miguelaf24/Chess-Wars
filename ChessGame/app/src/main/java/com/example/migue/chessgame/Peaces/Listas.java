package com.example.migue.chessgame.Peaces;

import java.util.List;

/**
 * Created by Bruno Santos on 11/29/2017.
 */

public class Listas {

    List<Peace> white;
    List<Peace> black;

    void addWhite(Peace c){
        white.add(c);
    }
    void addBlack(Peace c){
        black.add(c);
    }
    void rmvWhite(Peace c){
        white.remove(c);
    }
    void rmvBlack(Peace c){
        black.remove(c);
    }
}
