package com.example.migue.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class win_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_);
        boolean mode = true;
        boolean winner = true;

        Intent intent = getIntent();

        if (intent != null) {
            mode = intent.getBooleanExtra("mode", true); //Get who wins
            winner = intent.getBooleanExtra("ImWinner", true); //get whose player is
        }
        TextView t = (TextView) findViewById(R.id.winMsg);
        if (mode) {//whiteWins
            if(winner)
                t.setText("white wins and you are white");
            else
                t.setText("white wins and you are a fkng looser");

        } else{//blackWins
            if(winner)
                t.setText("black wins and you are black");
            else
                t.setText("bloack wins and you are a fkng looser");

        }

    }

    @Override
    public void onBackPressed() {
        finish();
     }
}
