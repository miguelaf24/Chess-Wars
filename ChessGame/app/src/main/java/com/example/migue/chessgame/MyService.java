package com.example.migue.chessgame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.EditText;

import com.example.migue.chessgame.Logic.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.migue.chessgame.GameActivity.getLocalIpAddress;
import static java.lang.Integer.parseInt;

public class MyService extends Service {
    private final IBinder mBinder = new LocalBinder();
    int mode;
    //Service
    boolean started = false;
    boolean run = true;
    int temp=-1;
    int state = -1;
    Handler procMsg = null;
    //Rede
    private static final int PORT = 8899;   //Port para rede
    private static final int PORTaux = 9988; // to test with emulators

    ServerSocket serverSocket=null;
    Socket socketGame = null;
    ObjectInputStream input;
    ObjectOutputStream output;

    class MyBinder extends Binder {
        void setInc(int i) {

        }
        int getValor() {
            return 1;
        }

        // TODO: 03/01/2018 Ver Para Apagar ou não 
        MyService get() {
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        test("onCreate");
        state = -1;
    }


    void setIP(String ip)
    {
        client(ip, PORT);

    }
    public void start(int mode){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !(networkInfo.isConnected())) {
            //TODO --> Mandar terminar GameActivity
        }
        test("mode = " + mode);
        started=true;

        if(mode==2) {
            server();
        }
        else if(mode==3) {

        }
    }

    public void sendGameAct(Object var){

        Intent intent = new Intent();
        intent.setAction("SENDG");
        intent.putExtra("Game", (Serializable) var);
        sendBroadcast(intent);

    }

    public void flag(int var){

        Intent intent = new Intent();
        intent.setAction("ServConnection");

        intent.putExtra("flag", var);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public MyService getServ(){
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        test("onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        test("onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        test("onUnbind");
        if (serverSocket!=null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
            serverSocket=null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        run=false;
        if (commThread.isAlive())
            try {
                commThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        test("onDestroy");
    }

    public void test(String str){
        Log.d(">>>>>>>>>", "Service: " + str);
    }


    public void sendGame(final Game gameS){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Coms", "Sending game "+gameS.GameOver());
                    output.writeObject(gameS);
                    output.flush();
                } catch (Exception e) {
                    Log.d("Coms", "Error sending a move" + e);
                }
            }
        });
        t.start();
    }


    public void server() {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    test("inicio Socket");
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket=null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                test("Send flag???");
                flag(1);
            }
        });
        t.start();
    }



    private void client(final String strIP,final int port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("RPS", "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, port);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                   /* procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            //finish();
                        }
                    });*/
                    return;
                }
                commThread.start();
            }
        });
        t.start();
    }

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(socketGame.getOutputStream());
                input = new ObjectInputStream(socketGame.getInputStream());

                while (!Thread.currentThread().isInterrupted()) {
                    Game temp = (Game) input.readObject();
                    Log.e("COMS","teste ao game over "+ temp.GameOver());
                    sendGameAct(temp);
                    Log.d("Coms", "Received: game");
                }
            } catch (final Exception e) {
                /*
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                       // if(game.GameOver()||Thread.currentThread().isInterrupted())
                    //        finish();
                        //TODO -> VERIFICAR TOAST
                        Log.d("Coms", "Jogo terminou?" + e.toString());
                      //  finish();
                    }
                });
                */
            }
        }
    });


}