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
                "p=" + p +
                ", inicial=" + inicial +
                ", Final=" + Final +
                '}';
    }

    public Jogada(Peace p, Posicao inicial, Posicao aFinal) {
        this.p = p;
        this.inicial = inicial;
        Final = aFinal;
    }


}
