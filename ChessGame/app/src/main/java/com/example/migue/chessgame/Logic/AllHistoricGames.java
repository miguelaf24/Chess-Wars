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

    public int getJogosGanhos(){
        int aux = 0;
        for(int i = 0 ; i < Jogos.size() ; i++){
            if(Jogos.get(i).isWin())aux++;
        }
        return  aux;
    }
    public int getJogosPerdidos(){
        int aux = 0;
        for(int i = 0 ; i < Jogos.size() ; i++){
            if(!Jogos.get(i).isWin())aux++;
        }
        return  aux;
    }
    public int getJogosMultiPlayer(){
        int aux = 0;
        for(int i = 0 ; i < Jogos.size() ; i++){
            if(Jogos.get(i).getPlayer2().compareTo("CPU")!=0)aux++;
        }
        return  aux;
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
