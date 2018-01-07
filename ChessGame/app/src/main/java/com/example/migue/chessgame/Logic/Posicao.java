package com.example.migue.chessgame.Logic;

import java.io.Serializable;

/**
 * Created by migue on 29/12/2017.
 */

public class Posicao implements Serializable {
    int x;
    int y;

    public Posicao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
