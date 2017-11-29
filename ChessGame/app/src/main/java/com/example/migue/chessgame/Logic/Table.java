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
        coord[0][0] = new Tower(true, 0,0);
        coord[7][0] = new Tower(true,7,0);
        coord[0][7] = new Tower(false,0,7);
        coord[7][7] = new Tower(false,7,7);

        //Bishop
        coord[2][0] = new Bishop(true,2,0);
        coord[5][0] = new Bishop(true,5,0);
        coord[2][7] = new Bishop(false,2,7);
        coord[5][7] = new Bishop(false,5,7);

        //Horses
        coord[1][0] = new Horse(true,1,0);
        coord[6][0] = new Horse(true,6,0);
        coord[1][7] = new Horse(false,1,7);
        coord[6][7] = new Horse(false,6,7);

        //King and Queen
        coord[3][0] = new Queen(true,3,0);
        coord[4][0] = new King(true,4,0);
        coord[3][7] = new Queen(false,3,7);
        coord[4][7] = new King(false,4,7);

        //pawn
        for(int j = 0; j < 8 ; j++){
            coord[j][1] = new Pawn(true,j,1);
            coord[j][6] = new Pawn(false,j,6);
        }

        //Null
        for(int i = 2; i < 6 ; i++){
            for(int j = 0; j < 8 ; j++){
                coord[j][i] = new Empty(false);
            }
        }
    }

    public Peace getPeace(int i, int j){return coord[i][j];}

    public void setPeace(Peace c, int i, int j){coord[i][j] = c; c.setCoord(i,j);}
}
