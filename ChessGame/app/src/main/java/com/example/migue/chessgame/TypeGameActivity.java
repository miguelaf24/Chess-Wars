package com.example.migue.chessgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

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
                time(GameActivity.TYPEGAMEML);
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
                            time(GameActivity.TYPEGAMEMS);
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

    void time(final int type){
        final EditText edt = new EditText(this);
        edt.setText("0");
        edt.setSelectAllOnFocus(true);

        edt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // TODO: 04/01/2018 Meter para apenas receber numeros
        AlertDialog selection = new AlertDialog.Builder(this).setTitle("asd")
                .setMessage("Insira o tempo do relógio:(Valores > 0)")
                .setView(edt)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(context, GameActivity.class);
                        intent.putExtra("mode",type);
                        intent.putExtra("Time",Integer.parseInt(edt.getText().toString()));
                        startActivity(intent);
                        finish();
                        return;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                    }
                }).create();
        selection.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        selection.show();
    }
}
