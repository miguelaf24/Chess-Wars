package com.example.migue.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class win_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_);
        boolean mode = true;
        boolean winner = true;

        ImageView img= (ImageView)this.findViewById(R.id.winimg);

        Intent intent = getIntent();

        if (intent != null) {
            mode = intent.getBooleanExtra("mode", true); //Get who wins
            winner = intent.getBooleanExtra("ImWinner", true); //get whose player is
        }

        if (mode) {//whiteWins
            if(winner)
                img.setImageResource(R.drawable.whitewinswhite);
            else
                img.setImageResource(R.drawable.whitewinsblack);

        } else{//blackWins
            if(winner)
                img.setImageResource(R.drawable.blackwinsblack);
            else
                img.setImageResource(R.drawable.blackwinswhite);

        }

    }

    @Override
    public void onBackPressed() {
        finish();
     }
}
