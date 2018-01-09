package com.example.migue.chessgame.Logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by migue on 03/01/2018.
 */

public class AllHistoricGames implements Serializable {
    static final long serialVersionUID = 42L;
    private ArrayList<GameHistoric> Jogos;

    public ArrayList<GameHistoric> getJogos() {
        return Jogos;
    }

    public void setJogos(ArrayList<GameHistoric> jogos) {
        Jogos = jogos;
    }

    public void AddJogo(GameHistoric e){
        getJogos().add(e);
    }

    public AllHistoricGames(ArrayList<GameHistoric> jogos) {
        Jogos = jogos;
    }

    public AllHistoricGames(){Jogos= new ArrayList<GameHistoric>();}
}
