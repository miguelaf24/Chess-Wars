package com.example.migue.chessgame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.migue.chessgame.Logic.Game;
import com.example.migue.chessgame.Peaces.*;
import com.example.migue.chessgame.R;

public class GameActivity extends Activity {

    Game game;
    int sl;
    int sn;

    ImageButton homes[][] = new ImageButton[8][8];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = new Game(false);

        sn = -1;
        sl = -1;

        homes[0][0] = (ImageButton) findViewById(R.id.a1);
        homes[0][1] = (ImageButton) findViewById(R.id.a2);
        homes[0][2] = (ImageButton) findViewById(R.id.a3);
        homes[0][3] = (ImageButton) findViewById(R.id.a4);
        homes[0][4] = (ImageButton) findViewById(R.id.a5);
        homes[0][5] = (ImageButton) findViewById(R.id.a6);
        homes[0][6] = (ImageButton) findViewById(R.id.a7);
        homes[0][7] = (ImageButton) findViewById(R.id.a8);

        homes[1][0] = (ImageButton) findViewById(R.id.b1);
        homes[1][1] = (ImageButton) findViewById(R.id.b2);
        homes[1][2] = (ImageButton) findViewById(R.id.b3);
        homes[1][3] = (ImageButton) findViewById(R.id.b4);
        homes[1][4] = (ImageButton) findViewById(R.id.b5);
        homes[1][5] = (ImageButton) findViewById(R.id.b6);
        homes[1][6] = (ImageButton) findViewById(R.id.b7);
        homes[1][7] = (ImageButton) findViewById(R.id.b8);


        homes[2][0] = (ImageButton) findViewById(R.id.c1);
        homes[2][1] = (ImageButton) findViewById(R.id.c2);
        homes[2][2] = (ImageButton) findViewById(R.id.c3);
        homes[2][3] = (ImageButton) findViewById(R.id.c4);
        homes[2][4] = (ImageButton) findViewById(R.id.c5);
        homes[2][5] = (ImageButton) findViewById(R.id.c6);
        homes[2][6] = (ImageButton) findViewById(R.id.c7);
        homes[2][7] = (ImageButton) findViewById(R.id.c8);

        homes[3][0] = (ImageButton) findViewById(R.id.d1);
        homes[3][1] = (ImageButton) findViewById(R.id.d2);
        homes[3][2] = (ImageButton) findViewById(R.id.d3);
        homes[3][3] = (ImageButton) findViewById(R.id.d4);
        homes[3][4] = (ImageButton) findViewById(R.id.d5);
        homes[3][5] = (ImageButton) findViewById(R.id.d6);
        homes[3][6] = (ImageButton) findViewById(R.id.d7);
        homes[3][7] = (ImageButton) findViewById(R.id.d8);

        homes[4][0] = (ImageButton) findViewById(R.id.e1);
        homes[4][1] = (ImageButton) findViewById(R.id.e2);
        homes[4][2] = (ImageButton) findViewById(R.id.e3);
        homes[4][3] = (ImageButton) findViewById(R.id.e4);
        homes[4][4] = (ImageButton) findViewById(R.id.e5);
        homes[4][5] = (ImageButton) findViewById(R.id.e6);
        homes[4][6] = (ImageButton) findViewById(R.id.e7);
        homes[4][7] = (ImageButton) findViewById(R.id.e8);

        homes[5][0] = (ImageButton) findViewById(R.id.f1);
        homes[5][1] = (ImageButton) findViewById(R.id.f2);
        homes[5][2] = (ImageButton) findViewById(R.id.f3);
        homes[5][3] = (ImageButton) findViewById(R.id.f4);
        homes[5][4] = (ImageButton) findViewById(R.id.f5);
        homes[5][5] = (ImageButton) findViewById(R.id.f6);
        homes[5][6] = (ImageButton) findViewById(R.id.f7);
        homes[5][7] = (ImageButton) findViewById(R.id.f8);

        homes[6][0] = (ImageButton) findViewById(R.id.g1);
        homes[6][1] = (ImageButton) findViewById(R.id.g2);
        homes[6][2] = (ImageButton) findViewById(R.id.g3);
        homes[6][3] = (ImageButton) findViewById(R.id.g4);
        homes[6][4] = (ImageButton) findViewById(R.id.g5);
        homes[6][5] = (ImageButton) findViewById(R.id.g6);
        homes[6][6] = (ImageButton) findViewById(R.id.g7);
        homes[6][7] = (ImageButton) findViewById(R.id.g8);

        homes[7][0] = (ImageButton) findViewById(R.id.h1);
        homes[7][1] = (ImageButton) findViewById(R.id.h2);
        homes[7][2] = (ImageButton) findViewById(R.id.h3);
        homes[7][3] = (ImageButton) findViewById(R.id.h4);
        homes[7][4] = (ImageButton) findViewById(R.id.h5);
        homes[7][5] = (ImageButton) findViewById(R.id.h6);
        homes[7][6] = (ImageButton) findViewById(R.id.h7);
        homes[7][7] = (ImageButton) findViewById(R.id.h8);

        for(int i = 0; i < 8 ; i++){
            for(int j = 0; j < 8 ; j++){
                homes[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                homes[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str = getResources().getResourceName(view.getId());
                        int l = str.charAt(str.length()-2) - 'a';
                        int n = Character.getNumericValue(str.charAt(str.length()-1))-1;


                        if(sl < 0 || sn < 0){//Se a peça não estiver selecionada


                            if((!(game.getPeace(l,n) instanceof Empty)) && game.getPeace(l,n).isWhite()==game.isWhiteTurn()){
                                view.setBackgroundColor(Color.BLUE);
                                sl = l;
                                sn = n;
                            }
                            else{
                                sl = -1;
                                sn = -1;
                            }
                        }
                        else{
                            if(game.getPeace(l,n) instanceof Empty || game.getPeace(l,n).isWhite()!=game.isWhiteTurn()){
                                view.setBackgroundColor(Color.RED);
                                if(game.doIt(sl,sn,l,n)){
                                    refreshTable();
                                }
                                else{
                                    sl = -1;
                                    sn = -1;
                                    refreshTable();
                                }
                            }
                            else{
                                refreshTable();
                                view.setBackgroundColor(Color.GREEN);
                                sl = l;
                                sn = n;
                            }
                        }
                        //Toast.makeText(GameActivity.this, Integer.toString(sl) + " + " + Integer.toString(sn) , Toast.LENGTH_SHORT).show();
                        if(game.IsKCheck())
                            Toast.makeText(GameActivity.this, "CAUTION: King is under attack!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        refreshTable();
    }

    void refreshTable(){
        for(int i = 0; i < 8 ; i++){
            for(int j = 0; j < 8 ; j++){
                if(i%2==0 && j%2!=0)homes[i][j].setBackgroundColor(Color.parseColor("#2E2EFE"));
                else if((i%2!=0 && j%2==0))homes[i][j].setBackgroundColor(Color.parseColor("#2E2EFE"));
                else homes[i][j].setBackgroundColor(Color.parseColor("#DAA520"));


                if(game.getPeace(i,j) instanceof Tower){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.tower_a);
                    else
                        homes[i][j].setImageResource(R.drawable.tower_b);
                }
                if(game.getPeace(i,j) instanceof Bishop){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.bishop_a);
                    else
                        homes[i][j].setImageResource(R.drawable.bishop_b);
                }
                if(game.getPeace(i,j) instanceof Horse){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.horse_a);
                    else
                        homes[i][j].setImageResource(R.drawable.horse_b);
                }
                if(game.getPeace(i,j) instanceof King){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.king_a);
                    else
                        homes[i][j].setImageResource(R.drawable.king_b);
                }
                if(game.getPeace(i,j) instanceof Pawn){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.peer_a);
                    else
                        homes[i][j].setImageResource(R.drawable.peer_b);
                }
                if(game.getPeace(i,j) instanceof Queen){
                    if((game.getPeace(i,j)).isWhite())
                        homes[i][j].setImageResource(R.drawable.queen_a);
                    else
                        homes[i][j].setImageResource(R.drawable.queen_b);
                }
                if(game.getPeace(i,j) instanceof Empty){
                    homes[i][j].setImageResource(android.R.color.transparent);
                }

            }
        }
    }
}