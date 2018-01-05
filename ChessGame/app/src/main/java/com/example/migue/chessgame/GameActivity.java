package com.example.migue.chessgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.migue.chessgame.Logic.Game;
import com.example.migue.chessgame.Peaces.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import static android.R.attr.id;


public class GameActivity extends Activity {

    //Modos de Jogo
    int mode = 0;   //iniciação do modo de jogo
    public static final int TYPEGAMES = 0;  //SinglePlayer
    public static final int TYPEGAMEML = 1; //MultiPlayer One Phone
    public static final int TYPEGAMEMS = 2; //MultiPlayer Rede Server
    public static final int TYPEGAMEMC = 3; //MultiPlayer Rede Cliente

    public static final int RECIVE_GAME = 0;
    public static final int RECIVE_PROFILE = 1;
    public static final int RECIVE_DIALOG_CLIENT =2;
    public static final int RECIVE_DIALOG_SERVER =3;

    ProgressDialog pd = null;

    boolean mBound = false;
    MyReciver myReceiver;
    public Game game;
    int sl;
    int sn;
    int time,sec1,min1,sec2,min2;

    TextView edtTimeP1;
    TextView edtTimeP2;
    ImageButton homes[][] = new ImageButton[8][8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        edtTimeP1 = (TextView) findViewById(R.id.timep1);
        edtTimeP2 = (TextView) findViewById(R.id.timep2);

        if(savedInstanceState == null) {//se não tem nada guardado!
            //Verifica o modo de jogo enviado

            Intent intent = getIntent();

            if (intent != null)
                mode = intent.getIntExtra("mode", 0); //Get Game Mode
            if (mode == TYPEGAMES) {
                game = new Game(true, -1);
                time = -1;
            }
            else{
                time =intent.getIntExtra("Time", -1);
                min1=min2=time;
                sec1=sec2=0;
                if(time <= 0)
                    time = -1;
                game = new Game(false,time);
                if(time>0){
                    showTimeP1(min1,sec1);
                    showTimeP2(min2,sec2);
                    if(mode==TYPEGAMEML){
                        tTime.start();
                    }
                }
            }


            // TODO: 02/01/2018 Ver Bind Service e chamar por funções -.-' bindService(MyService, 0);
        }
        else{
            game = (Game) savedInstanceState.getSerializable("SavedGame");
            mode = (int) savedInstanceState.getInt("Mode");
            time = (int) savedInstanceState.getInt("Time");
            if(time>0){
                min1 = (int) savedInstanceState.getInt("M1");
                sec1 = (int) savedInstanceState.getInt("S1");
                min2 = (int) savedInstanceState.getInt("M2");
                sec2 = (int) savedInstanceState.getInt("S2");
                showTimeP1(min1,sec1);
                showTimeP2(min2,sec2);
                tTime.start();
            }
        }
        tabSize();
        buttons();

        refreshTable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mode == TYPEGAMEMS || mode == TYPEGAMEMC) {
            send(0, "");

            myReceiver = new MyReciver();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("SENDG");
            intentFilter.addAction("ServConnection");
            registerReceiver(myReceiver, intentFilter);
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        tabSize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (mBound) {
            unbindService(sc);
            mBound = false;
        }*/
    }

    void tabSize(){
        LinearLayout l = findViewById(R.id.tab);
        int widthDisp=this.getResources().getDisplayMetrics().widthPixels;
        int heightDisp =this.getResources().getDisplayMetrics().heightPixels;
        if(widthDisp<heightDisp)
            l.setLayoutParams(new LinearLayout.LayoutParams(widthDisp,widthDisp));
        else
            l.setLayoutParams(new LinearLayout.LayoutParams(heightDisp,heightDisp));
    }

    private void buttons() {


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

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                homes[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                homes[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                String str = getResources().getResourceName(view.getId());
                int l = str.charAt(str.length() - 2) - 'a';
                int n = Character.getNumericValue(str.charAt(str.length() - 1)) - 1;

                if (game.isMyTurn(mode)) {
                    if (sl < 0 || sn < 0) {//Se a peça não estiver selecionada
                        if ((!(game.getPeace(l, n) instanceof Empty)) && game.getPeace(l, n).isWhite() == game.isWhiteTurn()) {
                            view.setBackgroundColor(Color.BLUE);
                            sl = l;
                            sn = n;
                        } else {
                            sl = -1;
                            sn = -1;
                        }
                    } else {
                        if (game.getPeace(l, n) instanceof Empty || game.getPeace(l, n).isWhite() != game.isWhiteTurn()) {
                            view.setBackgroundColor(Color.RED);
                            if (game.doIt(sl, sn, l, n)) {
                                refreshTable();
                                //ENVIA O JOGO DEPOIS DE SER JOGADO!
                                if(mode >1)
                                    sendGame();
                            } else {
                                sl = -1;
                                sn = -1;
                                refreshTable();
                            }
                        } else {
                            refreshTable();
                            view.setBackgroundColor(Color.GREEN);
                            sl = l;
                            sn = n;
                        }
                    }
                }
                //Toast.makeText(GameActivity.this, Integer.toString(sl) + " + " + Integer.toString(sn) , Toast.LENGTH_SHORT).show();
                if (game.IsKCheck())
                    Toast.makeText(GameActivity.this, "CAUTION: King is under attack!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }


    }

    Thread tTime = new Thread(new Runnable() {
        @Override
        public void run() {

            while (time>0) {
                try {
                    Thread.sleep(1000);
                    if(game.isWhiteTurn()) {
                        sec1--;
                        if (sec1 < 0) {
                            sec1 = 59;
                            min1 -= 1;
                        }
                        if(min1<0){
                            // TODO: 04/01/2018 Black Win
                            time=-1;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTimeP1(min1,sec1);
                            }
                        });

                    }
                    else{
                        sec2--;
                        if (sec2 < 0) {
                            sec2 = 59;
                            min2 -= 1;
                        }
                        if(min1<0){
                            // TODO: 04/01/2018 White Win
                            time=-1;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTimeP2(min2,sec2);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    void showTimeP1(int min1, int sec1){
        if(sec1>=10)
            edtTimeP1.setText(min1+":"+sec1);
        else
            edtTimeP1.setText(min1+":0"+sec1);
    }

    void showTimeP2(int min1, int sec1){
        if(sec1>=10)
            edtTimeP2.setText(min1+":"+sec1);
        else
            edtTimeP2.setText(min1+":0"+sec1);
    }

    private ServiceConnection sc = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    public void server() {

        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.servDlgWindow) + "\n(IP: " + ip
                + ")");
        pd.setTitle(getString(R.string.servDlgWindowTit));
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                unbindService(sc);
            }
        });
        pd.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SavedGame", game);
        outState.putInt("Mode", mode);
        outState.putInt("M1", min1);
        outState.putInt("M2", min2);
        outState.putInt("S1", sec1);
        outState.putInt("S2", sec2);
        outState.putInt("Time", time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mode==TYPEGAMEMS){
            server();
        }
        else if(mode==TYPEGAMEMC)
            clientDlg();
    }

    void sendGame(){

        send(2, "");
    }


    private void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("192.168.1.3");
        AlertDialog selection = new AlertDialog.Builder(this).setTitle(R.string.AlertDialogTitleS)
                .setMessage("Server IP")
                .setView(edtIP)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        send(1, edtIP.getText().toString());
                        tTime.start();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //finish();
                    }
                }).create();
        selection.show();
    }


    void send (int state, String data){
        Intent intentServ = new Intent(this,MyService.class);
        intentServ.putExtra("state",state);

        if (state == 0)
            intentServ.putExtra("mode",mode);
        if (state ==1)
            intentServ.putExtra("ip", data);
        if (state ==2)
            intentServ.putExtra("game", game);

        startService(intentServ);
    }

    private class MyReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("SENDG")){
                game = (Game) intent.getSerializableExtra("Game");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        refreshTable();
                    }
                });
            }
            if(intent.getAction().equals("ServConnection")) {
                int i = (int) intent.getSerializableExtra("flag");
                pd.dismiss();
                tTime.start();
            }

        }
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
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time=-1;
        if (tTime.isAlive())
            try {
                tTime.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}