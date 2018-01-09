package com.example.migue.chessgame.Logic;

import android.content.Context;

import com.example.migue.chessgame.Peaces.Peace;
import com.example.migue.chessgame.R;

import java.io.Serializable;

/**
 * Created by migue on 29/12/2017.
 */

public class Jogada implements Serializable {
    Peace p;
    Posicao inicial, Final;

    public String toStringA(Context context) {
        return context.getString(R.string.piecetext) + p.toStringA(context) +
                ", Inicial=" + Character.toString((char) ('A'+inicial.getX())) + (inicial.getY()+1) +
                ", Final=" + Character.toString((char) ('A'+Final.getX())) + (Final.getY()+1)
        ;
    }

    public Jogada(Peace p, Posicao inicial, Posicao aFinal) {
        this.p = p;
        this.inicial = inicial;
        Final = aFinal;
    }


}
