package com.example.migue.chessgame.Logic;

import com.example.migue.chessgame.Peaces.Peace;

import java.io.Serializable;

/**
 * Created by migue on 29/12/2017.
 */

public class Jogada implements Serializable {
    Peace p;
    Posicao inicial, Final;

    @Override
    public String toString() {
        return "Jogada{" +
                "p=" + p.toString() +
                ", inicial=" + Character.toString((char) ('A'+inicial.getX())) + (inicial.getY()+1) +
                ", Final=" + Character.toString((char) ('A'+Final.getX())) + (Final.getY()+1) +
                '}';
    }

    public Jogada(Peace p, Posicao inicial, Posicao aFinal) {
        this.p = p;
        this.inicial = inicial;
        Final = aFinal;
    }


}
