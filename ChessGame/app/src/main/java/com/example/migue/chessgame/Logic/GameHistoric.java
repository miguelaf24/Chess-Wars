package com.example.migue.chessgame.Logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by migue on 03/01/2018.
 */

public class GameHistoric implements Serializable{
    static final long serialVersionUID = 42L;
    private ArrayList<Jogada> historico;
    private boolean win;
    private String Player1;
    private String Player2;
    private Date data;

    public String getPlayer1() {
        return Player1;
    }

    public void setPlayer1(String player1) {
        Player1 = player1;
    }

    public String getPlayer2() {
        return Player2;
    }

    public void setPlayer2(String player2) {
        Player2 = player2;
    }

    @Override
    public String toString() {
        String temp;
        if(win) {
            temp = "win the game";
        }
        else
           temp= "lose the game";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        String time = simpleDateFormat.format(data);
        return (Player1+" VS "+ Player2 +" and you "+temp+"\n "+time);
    }

    public GameHistoric(ArrayList<Jogada> historico, boolean win) {
        this.historico = historico;
        this.win = win;
        data= new Date();
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
