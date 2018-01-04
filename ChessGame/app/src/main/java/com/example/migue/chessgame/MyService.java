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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        test("onStartCommands");

        if (intent != null) {
            state = intent.getIntExtra("state", 0);
        }

        test("" + state);

        if(state ==0){
            test("State 0");
            if(!started) {
                start(intent);
            }
        }
        else if(state == 1){
            test("State 1");
            String ip= intent.getStringExtra("ip");
            client(ip, PORT);
        }
        else if (state==2){
            test("State 2");
            sendGame(intent.getStringExtra("game"));

        }
        return START_STICKY; //START_NOT_STICKY;
    }

    public void start(Intent intent){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !(networkInfo.isConnected())) {
            //TODO --> Mandar terminar GameActivity
        }
        if (intent != null) {
            mode = intent.getIntExtra("mode", 2);
        }
        test("mode = " + mode);
        started=true;

        if(mode==2) {
            server();
        }
        else if(mode==3) {

        }
    }

    public void sendGameAct(Game var){

        Intent intent = new Intent();
        intent.setAction("SENDG");

        intent.putExtra("game", var);
        sendBroadcast(intent);

    }

    public void flag(int var){

        Intent intent = new Intent();
        intent.setAction("ServConnection");

        intent.putExtra("flag", var);
        sendBroadcast(intent);

    }

    public void sendProf(Object var){
 //TODO -> enviar profile e definir o tipo de objecto de var
        Intent intent = new Intent();
        intent.setAction("SENDG");

       // intent.putExtra("prof", var);
        sendBroadcast(intent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        test("onBind");
        return new MyBinder();
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
        if (task.isAlive())
            try {
                task.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        test("onDestroy");
    }

    public void test(String str){
        Log.d(">>>>>>>>>", "Service: " + str);
    }


    Thread task = new Thread(new Runnable() {
        @Override
        public void run() {

            while (run) {
                try {
                    Thread.sleep(500);
                    test(temp+"n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf();
        }
    });

    public void sendGame(final Object gameS){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Coms", "Sending game");
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
                    sendGameAct((Game) input.readObject());
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