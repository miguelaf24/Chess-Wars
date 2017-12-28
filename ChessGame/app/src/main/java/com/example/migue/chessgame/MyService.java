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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import com.example.migue.chessgame.Logic.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.migue.chessgame.GameActivity.getLocalIpAddress;

public class MyService extends Service {

    int mode;
    //Service
    boolean started = false;
    boolean run = true;
    int temp=-1;
    int state = -1;

    //Rede
    private static final int PORT = 8899;   //Port para rede
    private static final int PORTaux = 9988; // to test with emulators
    ProgressDialog pd = null;
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
            mode = intent.getIntExtra("state", 0);
        }

        if(state ==0){
            if(!started) {
                start(intent);
            }
        }
        else if(state == 1){

        }

        temp++;
        if(!task.isAlive()) {
            task.start();
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
            clientDlg();
        }
    }

    public void send(int var){
        if(var == 0){
            
        }
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
        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.servDlgWindow) + "\n(IP: " + ip
                + ")");
        pd.setTitle(getString(R.string.servDlgWindowTit));
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //finish();
                if (serverSocket!=null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket=null;
                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket=null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();
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
                client(edtIP.getText().toString(), PORT);
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
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
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
                    game = (Game) input.readObject();
                    Log.d("Coms", "Received: game");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            refreshTable();
                        }
                    });
                }
            } catch (final Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        if(game.GameOver()||Thread.currentThread().isInterrupted())
                    //        finish();
                        //TODO -> VERIFICAR TOAST
                        Log.d("Coms", "Jogo terminou?" + e.toString());
                      //  finish();
                    }
                });
            }
        }
    });


}