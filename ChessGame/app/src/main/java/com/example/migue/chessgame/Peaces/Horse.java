package com.example.migue.chessgame.Peaces;

import com.example.migue.chessgame.Logic.Table;

/**
 * Created by migue on 27/11/2017.
 */

public class Horse extends Peace {
    public Horse(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean action(Table table, int sl, int ln, int s, int l) {
        return false;
    }
}
