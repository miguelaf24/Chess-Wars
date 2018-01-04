package com.example.migue.chessgame.Logic;

import java.util.ArrayList;

/**
 * Created by migue on 03/01/2018.
 */

public class GameHistoric {
    private ArrayList<Jogada> historico;
    private boolean win;

    public GameHistoric(ArrayList<Jogada> historico, boolean win) {
        this.historico = historico;
        this.win = win;
    }

    public boolean isWin() {
        return win;
    }

    public ArrayList<Jogada> getHistorico() {
        return historico;
    }

    public void setHistorico(ArrayList<Jogada> historico) {
        this.historico = historico;
    }
}
