package com.example.migue.chessgame.Logic;

import com.example.migue.chessgame.Peaces.Bishop;
import com.example.migue.chessgame.Peaces.Empty;
import com.example.migue.chessgame.Peaces.Horse;
import com.example.migue.chessgame.Peaces.King;
import com.example.migue.chessgame.Peaces.Pawn;
import com.example.migue.chessgame.Peaces.Peace;
import com.example.migue.chessgame.Peaces.Queen;
import com.example.migue.chessgame.Peaces.Tower;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public class Table implements Serializable{

    private Peace[][] coord;
    //private char[][] peaces;

    public Table() {
        //peaces = new char[8][8];
        coord = new Peace[8][8];
        startTable();
    }

    public void startTable(){

        //Tower
        coord[0][0] = new Tower(true);
        coord[7][0] = new Tower(true);
        coord[0][7] = new Tower(false);
        coord[7][7] = new Tower(false);

        //Bishop
        coord[2][0] = new Bishop(true);
        coord[5][0] = new Bishop(true);
        coord[2][7] = new Bishop(false);
        coord[5][7] = new Bishop(false);

        //Horses
        coord[1][0] = new Horse(true);
        coord[6][0] = new Horse(true);
        coord[1][7] = new Horse(false);
        coord[6][7] = new Horse(false);

        //King and Queen
        coord[3][0] = new Queen(true);
        coord[4][0] = new King(true);
        coord[3][7] = new Queen(false);
        coord[4][7] = new King(false);

        //pawn
        for(int j = 0; j < 8 ; j++){
            coord[j][1] = new Pawn(true);
            coord[j][6] = new Pawn(false);
        }

        //Null
        for(int i = 2; i < 6 ; i++){
            for(int j = 0; j < 8 ; j++){
                coord[j][i] = new Empty(false);
            }
        }
    }

    public Peace getPeace(int i, int j){return coord[i][j];}

    public void setPeace(Peace c, int i, int j){coord[i][j] = c;}
}
