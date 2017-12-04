package com.example.migue.chessgame.Logic;

import com.example.migue.chessgame.Peaces.Bishop;
import com.example.migue.chessgame.Peaces.Empty;
import com.example.migue.chessgame.Peaces.Horse;
import com.example.migue.chessgame.Peaces.King;
import com.example.migue.chessgame.Peaces.Listas;
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
    private Listas lista;
    //private char[][] peaces;

    public Table() {
        //peaces = new char[8][8];
        coord = new Peace[8][8];
        lista= new Listas();
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
        for(int j = 0; j < 8 ; j++){
            lista.addWhite(coord[j][1]);
            lista.addBlack(coord[j][6]);
        }
        lista.addWhite(coord[3][0]);
        lista.addWhite(coord[4][0]);
        lista.addBlack(coord[3][7]);
        lista.addBlack(coord[4][7]);
        lista.addBlack(coord[1][7]);
        lista.addBlack(coord[6][7]);
        lista.addWhite(coord[0][0]);
        lista.addWhite(coord[7][0]);
        lista.addBlack(coord[0][7]);
        lista.addBlack(coord[7][7]);
        lista.addWhite(coord[2][0]);
        lista.addWhite(coord[5][0]);
        lista.addBlack(coord[2][7]);
        lista.addBlack(coord[5][7]);
        lista.addWhite(coord[1][0]);
        lista.addWhite(coord[6][0]);
    }
    public Listas getlista(){return lista;}

    public Peace getPeace(int i, int j){
        if((i>=0&&i<=7) && (j>=0&&j<=7))
            return coord[i][j];
        return null;
    }


    public void setPeace(int l, int n, int i, int j){

        //TO DO -> Caso coma uma peça

        if (l==-1){ //se o metodo for chamado para preencher uma casa com vazia, recebe -1 -1 e casa a preenche
            coord[i][j]=new Empty(false);
            return;
        }
        Peace c=getPeace(l,n);
        coord[i][j] =c; //actualiza as coordenadas da tabela
        c.setCoord(i,j); //actualiza info da peça

        //actualiza as listas de peças existentes
        if(c.isWhite())
            lista.getWhite(l,n).setCoord(i,j);
        else
            lista.getBlack(l,n).setCoord(i,j);
}

    public void setThisPeace(Peace p, int l, int n) {
        coord[l][n] = p; //actualiza as coordenadas da tabela
    }

    public void rmv(Peace p) {
        if(!(p instanceof Empty)) {
            if (p.isWhite())
                lista.rmvWhite(p);
            else
                lista.rmvBlack(p);
        }
    }
}
