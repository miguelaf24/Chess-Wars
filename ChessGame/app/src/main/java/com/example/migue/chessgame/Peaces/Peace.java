package com.example.migue.chessgame.Peaces;

import java.io.Serializable;

/**
 * Created by migue on 26/11/2017.
 */

public class Peace implements Serializable {
    boolean isWhite;

    public boolean isWhite() {
        return isWhite;
    }

    public Peace(boolean isWhite) {
        this.isWhite = isWhite;

    }
}
