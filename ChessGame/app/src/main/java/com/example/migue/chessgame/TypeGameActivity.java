package com.example.migue.chessgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypeGameActivity extends Activity {

    Button bMultiLocal;
    Button bSingle;
    Button bMultiNet;


    Intent intent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_game);

        bSingle = (Button) findViewById(R.id.bPlaySolo);
        bMultiLocal = (Button) findViewById(R.id.bLocalMult);
        bMultiNet = (Button) findViewById(R.id.bMultiNet);


        bSingle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(context, GameActivity.class);
                intent.putExtra("mode",GameActivity.TYPEGAMES);
                startActivity(intent);
                finish();
                return;
            }
        });

        bMultiLocal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(context, GameActivity.class);
                intent.putExtra("mode",GameActivity.TYPEGAMEML);
                startActivity(intent);
                finish();
                return;
            }
        });

        bMultiNet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog selection = new AlertDialog.Builder(context).setTitle(R.string.AlertDialogTitleS)
                        .setMessage(R.string.ChoseDialogSelMes)
                        .setPositiveButton(getString(R.string.PosButDialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent = new Intent(context, GameActivity.class);
                                intent.putExtra("mode",GameActivity.TYPEGAMEMS);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        })
                        .setNegativeButton(R.string.NegButDiaLog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent = new Intent(context, GameActivity.class);
                                intent.putExtra("mode",GameActivity.TYPEGAMEMC);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }).create();
                selection.show();
                /*
                intent = new Intent(context, GameActivity.class);
                intent.putExtra("mode",GameActivity.TYPEGAMEML);
                startActivity(intent);
*/
            }
        });


    }


}
