package com.example.migue.chessgame.Logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by migue on 03/01/2018.
 */

public class AllHistoricGames implements Serializable {
    private ArrayList<GameHistoric> Jogos;

    public ArrayList<GameHistoric> getJogos() {
        return Jogos;
    }

    public void setJogos(ArrayList<GameHistoric> jogos) {
        Jogos = jogos;
    }

    public AllHistoricGames(ArrayList<GameHistoric> jogos) {
        Jogos = jogos;
    }
}
