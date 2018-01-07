package com.example.migue.chessgame.Logic;

/**
 * Created by migue on 06/01/2018.
 */

public class JogadaIA {
    Jogada jogada;
    int points;

    public JogadaIA(Jogada jogada, int points) {
        this.jogada = jogada;
        this.points = points;
    }

    public Jogada getJogada() {
        return jogada;
    }

    public void setJogada(Jogada jogada) {
        this.jogada = jogada;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
