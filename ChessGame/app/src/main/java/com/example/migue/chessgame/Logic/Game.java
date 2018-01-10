package com.example.migue.chessgame.Logic;

import android.util.Log;
import android.widget.EditText;

import com.example.migue.chessgame.Peaces.*;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by migue on 26/11/2017.
 */

public class Game implements Serializable {
    boolean singleplayer;
    boolean isWhiteTurn;
    int nJogada;
    Table table;
    boolean gameover;
    int time, bestMove;
    private ArrayList<JogadaIA> jogadasIA;
    private ArrayList<Peace> blackP;
    private ArrayList<Peace> whiteP;
    int levelIA = 3;
    boolean iaTurn;

    private ArrayList<Jogada> historico;

    public ArrayList<Jogada> getHistorico() {
        return historico;
    }
// Istate state;


    public Game(boolean singleplayer, int time) {
        this.time = time;
        isWhiteTurn = true;
        this.singleplayer = singleplayer;
        gameover=false;
        table = new Table();
        nJogada=0;
        historico = new ArrayList<>();
        //state = new IBeginning(table);
    }

    public int getTime() {
        return time;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }



    public boolean doIt(int sl, int sn, int l, int n) {


        if (getPeace(sl, sn).action(table, l, n)) { //se a accão devolver true

            if(table.getPeace(sl,sn) instanceof King && isKingCheck(l, n)){
                Peace p2 = new Empty(true);
                boolean isRooqD=false;
                boolean isRooqE=false;
                if(sl-2==l){
                    p2=    table.getPeace(sl-1,sn);
                    isRooqE = true;
                }
                else if(sl+2==l){
                    p2=    table.getPeace(sl+1,sn);
                    isRooqD = true;
                }
                if(isRooqD){
                    table.setPeace(sl+3, sn, sl+1, sn);       //Meter a peça no local Inicial
                    table.setThisPeace(p2, sl+3, sn);//Voltar a meter a peça removida no destino
                }
                if(isRooqE){
                    table.setPeace(sl-4, sn, sl-1, sn);       //Meter a peça no local Inicial
                    table.setThisPeace(p2, sl-4, sn);//Voltar a meter a peça removida no destino
                }
                return false;
            }
            else if(changePeace(sl,sn,l,n)) {
                if(GameOver()&& singleplayer){
                    Log.i("GAME","GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER");
                    gameover=true;
                }
                else {
                    Log.i("GAME","Continuar");
                }
                isWhiteTurn = !isWhiteTurn;
                table.setJogada(++nJogada);
                historico.add(new Jogada(getPeace(l,n),new Posicao(sl,sn),new Posicao(l,n)));
                verPawnToQueen();
                if(singleplayer)
                   startIA();
                if(GameOver()){
                    Log.i("GAME","GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER -GAME OVER");
                    gameover=true;
                }
                else {
                    Log.i("GAME","Continuar");
                }
                return true;
            }
            /*
            table.setPeace(sl, sn, l, n);

            */

            return true;
        }

        return false;
    }

    public boolean getGameOver(){return gameover;};
    private void verPawnToQueen() {
        for(int i = 0; i <8;i++){
            if(table.getPeace(i,0) instanceof Pawn){
                changeToQueen(i,0);
            }
            if(table.getPeace(i,7) instanceof Pawn){
                changeToQueen(i,7);
            }
        }
    }

    private void changeToQueen(int l, int n) {
        Peace p = new Queen(table.getPeace(l,n).isWhite(),l,n);
        table.setThisPeace(p,l,n);
        table.rmv(table.getPeace(l,n));
        table.addList(p);
    }

    public Peace getPeace(int i, int j) {
        return table.getPeace(i, j);
    }

    public boolean IsKCheck() {

        return isKingCheck((table.getlista().getKing(isWhiteTurn).getL()), (table.getlista().getKing(isWhiteTurn).getN()));
    }

    public boolean changePeace(int sl, int sn, int l, int n){
        boolean isF=false;
        Peace p = table.getPeace(l,n);      //Guarda a posição destino
        Peace p2 = table.getPeace(sl,sn);
        if(p.isFirstPlay())isF = true;
        table.setPeace(sl, sn, l, n);       //Move a peça selecionada para o destino
        table.setPeace(-1, -1, sl, sn);     //Mete a posição actual vazia

        if(IsKCheck()){                     //Caso o rei esteja em check:
            table.setPeace(l, n, sl, sn);       //Meter a peça no local Inicial
            table.setThisPeace(p, l, n);//Voltar a meter a peça removida no destino
            if(isF){
                p.setFirstMove();
            }
            return false;
        }
        else{
            table.rmv(p);
            if(p2.isFirstPlay())
                p2.setJogadaFirst(nJogada);
        }
        return true;
    }

    public boolean GameOver(){

        if (isWhiteTurn){

            for (int i = 0; i < table.getlista().white.size(); i++)
            {

                Log.i("Var",table.getlista().white.get(i).getType() + table.getlista().white.size() + " --> L " + table.getlista().white.get(i).getL()+ " - N " + table.getlista().white.get(i).getN());
                if(SaveMyKing(table.getlista().white.get(i).getL(),table.getlista().white.get(i).getN()))return false;
            }

        }else{
            for (int i = 0; i < table.getlista().black.size(); i++)
            {
                Log.i("Var",table.getlista().black.get(i).getType() + table.getlista().black.size() + " --> L " + table.getlista().black.get(i).getL()+ " - N " + table.getlista().black.get(i).getN());
                if(SaveMyKing(table.getlista().black.get(i).getL(),table.getlista().black.get(i).getN()))return false;
            }
        }
        return true;
    }

    public boolean trychangePeace(int sl, int sn, int l, int n){
        if(l<0||l>7||n<0||n>7)return false;
        Peace p = table.getPeace(l,n);
        if(p.isWhite()==isWhiteTurn && !(p instanceof Empty))return false;
        table.setPeace(sl, sn, l, n);
        table.setPeace(-1, -1, sl, sn);
        if(IsKCheck()){
            table.setPeace(l, n, sl, sn);
            table.setThisPeace(p, l, n);
            return false;
        }
        table.setPeace(l, n, sl, sn);
        table.setThisPeace(p, l, n);
        return true;
    }

    public void startIA(){
        iaTurn = isWhiteTurn;
        ArrayList<JogadaIA> jIA = firstIA(levelIA,null);
        if(jIA.size()==0) {
            gameover = true;
            return;
        }
        changePeace(jIA.get(0).getJogada().inicial.getX(),jIA.get(0).getJogada().inicial.getY(),jIA.get(0).getJogada().Final.getX(),jIA.get(0).getJogada().Final.getY());
        isWhiteTurn = true;
    }

    int maxPoints;

    ArrayList<JogadaIA> firstIA(int lvl, ArrayList<JogadaIA> jogadaIA){
        ArrayList<JogadaIA> jogadasIAtemp = jogadaIA;
        Jogada jIA = null;
        maxPoints = -9999;

        if(jogadaIA==null){
            jogadaIA = new ArrayList<>();
            jogadasIAtemp = new ArrayList<>();
            for (int i = 0; i < table.getlista().black.size(); i++) {
                jogadasIAtemp = iaMinMax(jogadasIAtemp, table, table.getlista().black.get(i),lvl,true);
            }
        }
        else{
            for(int j = 0; j < jogadaIA.size() ; j++) {
                Jogada jogada = jogadasIAtemp.get(j).getJogada();
                changePeace(jogada.inicial.getX(),jogada.inicial.getY(),jogada.Final.getX(),jogada.Final.getY());
                if (isWhiteTurn) {
                    for (int i = 0; i < table.getlista().white.size(); i++) {
                        jogadasIAtemp = iaMinMax(jogadasIAtemp, table, table.getlista().black.get(i),lvl,false);
                    }
                } else {
                    for (int i = 0; i < table.getlista().black.size(); i++) {
                        jogadasIAtemp = iaMinMax(jogadasIAtemp, table, table.getlista().black.get(i),lvl,true);
                    }
                }
                for(int i = 0; i<jogadasIAtemp.size();i++){
                    if(jogadasIAtemp.get(i).getPoints()<maxPoints){
                        jogadasIAtemp.remove(i--);
                    }
                }
                jogadaIA.get(j).setPoints(maxPoints);
            }
        }

        jogadaIA=jogadasIAtemp;

        for(int i = 0; i<jogadaIA.size();i++){
            if(jogadaIA.get(i).getPoints()<maxPoints){
                jogadaIA.remove(i--);
            }
        }
/*
        while(lvl>=0){
            isWhiteTurn=!isWhiteTurn;
            jogadaIA = firstIA(--lvl, jogadasIAtemp);

        }*/
        return jogadaIA;
    }

    private ArrayList<JogadaIA> iaMinMax(ArrayList<JogadaIA> jogadasIAtemp, Table tempTable, Peace peace, int lvl, boolean b) {
        int tentativa = 0;
        while(true){

            JogadaIA jAux = tryIAJogada(peace.getL(), peace.getN(), lvl, iaTurn, tentativa);

            if(jAux==null) {
                break;
            }
            else {
                tentativa++;
            }
            int points = getPoints(table, b);
            if(points >= maxPoints){
                jogadasIAtemp.add(jAux);
                maxPoints = points;
            }
        }
        return jogadasIAtemp;
    }

    private JogadaIA tryIAJogada(int l, int n, int level, boolean IAturn, int tentativa) {
        Jogada jFinal = null;
        Table tempTable = table;
        int points = bestMove;

        if(table.getPeace(l,n) instanceof King){
            int sl=l+1;
            int sn = n;
            if(trychangePeace(l,n,l+1,n)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));

                    return new JogadaIA(jFinal,PointsChangeIA(jFinal));
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l+1,n+1)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n+1));
                    return new JogadaIA(jFinal,PointsChangeIA(jFinal));
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l+1,n-1)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n-1));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l,n-1)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-1));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l,n+1)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+1));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;

            }
            if(trychangePeace(l,n,l-1,n)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l-1,n+1)){
                if(tentativa==0) {
                  //changePeace(l,n,l-1,n+1);
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n+1));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            if(trychangePeace(l,n,l-1,n-1)){
                if(tentativa==0) {
                 // changePeace(l,n,l-1,n-1);
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n-1));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            if(table.getPeace(l,n).isFirstPlay()){
                if(table.getPeace(l+1,n) instanceof Empty && table.getPeace(l+2,n) instanceof Empty && table.getPeace(l+3,n).isFirstPlay()){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+2,n));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-2,n) instanceof Empty && table.getPeace(l-3,n) instanceof Empty && table.getPeace(l-4,n).isFirstPlay()){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-2,n));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
            }
        } else if(table.getPeace(l,n) instanceof Pawn){
            if(table.getPeace(l,n).isWhite()){
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n+1) instanceof  Empty && table.getPeace(l,n+2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n+2)){
                        if(tentativa==0) {
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+2));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                if(!(table.getPeace(l+1,n+1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n+1)){
                        if(tentativa==0) {
                          //changePeace(l,n,l+1,n+1);
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n+1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
                if(!(table.getPeace(l-1,n+1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n + 1)) {
                        if(tentativa==0) {
                          //changePeace(l,n,l-1,n+1);
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n+1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
                if(table.getPeace(l,n+1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n+1)){
                        if(tentativa==0) {
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
            }
            else{
                if(!(table.getPeace(l+1,n-1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n-1)){
                        if(tentativa==0) {
                          //changePeace(l,n,l+1,n-1);
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n-1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
                if(!(table.getPeace(l-1,n-1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n - 1)) {
                        if(tentativa==0) {
                         // changePeace(l,n,l-1,n-1);
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n-1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n-1) instanceof  Empty && table.getPeace(l,n-2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n-2)){
                        if(tentativa==0) {
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-2));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                if(table.getPeace(l,n-1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n-1)){
                        if(tentativa==0) {
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-1));
                            int temp = PointsChangeIA(jFinal);
                            return new JogadaIA(jFinal,temp);
                        }
                        else tentativa--;
                    }
                }
            }
        }else if (table.getPeace(l,n) instanceof Bishop){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }
        }else if (table.getPeace(l,n) instanceof Horse){
            int sl = l - 1;
            int sn = n + 2;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l - 1;
            sn = n - 2;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l + 1;
            sn = n + 2;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l + 1;
            sn = n - 2;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l - 2;
            sn = n + 1;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l - 2;
            sn = n - 1;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l + 2;
            sn = n + 1;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
            sl = l + 2;
            sn = n - 1;
            if(trychangePeace(l,n,sl,sn)){
                if(tentativa==0) {
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    int temp = PointsChangeIA(jFinal);
                    return new JogadaIA(jFinal,temp);
                }
                else tentativa--;
            }
        } else if (table.getPeace(l,n) instanceof Queen){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }


            sl = l;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        } else if (table.getPeace(l,n) instanceof Tower){
            int sl = l;
            int sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    if(tentativa==0) {
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                        int temp = PointsChangeIA(jFinal);
                        return new JogadaIA(jFinal,temp);
                    }
                    else tentativa--;
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        }

        return null;
    }

    private int PointsChangeIA(Jogada jFinal) {
        int points;
        if(jFinal.Final.getX()> 7|| jFinal.Final.getX() <0 || jFinal.Final.getY()> 7 || jFinal.Final.getY() <0)
            return -9999;
        Peace p1 = table.getPeace(jFinal.inicial.getX(),jFinal.inicial.getY());      //Guarda a posição destino
        Peace p2 = table.getPeace(jFinal.Final.getX(),jFinal.Final.getY());      //Guarda a posição destino

        p1.setCoord(jFinal.Final.getX(),jFinal.Final.getY());
        p2.setCoord(-10,-10);

        points = getPoints(table,true);

        p1.setCoord(jFinal.inicial.getX(),jFinal.inicial.getY());
        p2.setCoord(jFinal.Final.getX(),jFinal.Final.getY());
        return points;
    }


    int getPoints(Table t, boolean IAturn){
        int wp = 0;
        int bp =0;
    //    char [][] array = new char [8][8];

        /*
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                array[i][k] = ' ';
            }
        }*/
        for (int i = 0; i < t.getlista().white.size(); i++) {
            wp+=t.getlista().white.get(i).getValue();
//            array[t.getlista().white.get(i).getL()][t.getlista().white.get(i).getN()]='W';
        }
        for (int i = 0; i < t.getlista().black.size(); i++) {
            bp+=t.getlista().black.get(i).getValue();
  //          array[t.getlista().black.get(i).getL()][t.getlista().black.get(i).getN()]='B';
        }

        /*
        for (int i = 0; i < 8; i++) {
            System.out.print(i+1 +" :  ");
            for (int k = 0; k < 8; k++) {
                System.out.print(array[i][k]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();*/
        if(!isWhiteTurn)
            return bp-wp;
        return wp-bp;
    }

    public boolean SaveMyKing(int l, int n){

        if(table.getPeace(l,n) instanceof King){
            if(trychangePeace(l,n,l+1,n))return true;
            if(trychangePeace(l,n,l+1,n+1))return true;
            if(trychangePeace(l,n,l+1,n-1))return true;
            if(trychangePeace(l,n,l,n-1))return true;
            if(trychangePeace(l,n,l,n+1))return true;
            if(trychangePeace(l,n,l-1,n))return true;
            if(trychangePeace(l,n,l-1,n+1))return true;
            if(trychangePeace(l,n,l-1,n-1))return true;
            if(table.getPeace(l,n).isFirstPlay()){
                if(table.getPeace(l+1,n) instanceof Empty && table.getPeace(l+2,n) instanceof Empty && table.getPeace(l+3,n).isFirstPlay())return true;
                if(table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-2,n) instanceof Empty && table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-4,n).isFirstPlay())return true;
            }
        } else if(table.getPeace(l,n) instanceof Pawn){
            if(table.getPeace(l,n).isWhite()){
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n+1) instanceof  Empty && table.getPeace(l,n+2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n+2))return true;
                if(!(table.getPeace(l+1,n+1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n+1))return true;
                }
                if(!(table.getPeace(l-1,n+1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n + 1)) return true;
                }
                if(table.getPeace(l,n+1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n+1))return true;
                }
            }
            else{
                if(!(table.getPeace(l+1,n-1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n-1))return true;
                }
                if(!(table.getPeace(l-1,n-1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n - 1)) return true;
                }
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n-1) instanceof  Empty && table.getPeace(l,n-2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n-2))return true;
                if(table.getPeace(l,n-1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n-1))return true;
                }
            }
        }else if (table.getPeace(l,n) instanceof Bishop){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                   if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                   if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }
        }else if (table.getPeace(l,n) instanceof Horse){
            int sl = l - 1;
            int sn = n + 2;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l - 1;
            sn = n - 2;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l + 1;
            sn = n + 2;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l + 1;
            sn = n - 2;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l - 2;
            sn = n + 1;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l - 2;
            sn = n - 1;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l + 2;
            sn = n + 1;
            if(trychangePeace(l,n,sl,sn))return true;
            sl = l + 2;
            sn = n - 1;
            if(trychangePeace(l,n,sl,sn))return true;
        } else if (table.getPeace(l,n) instanceof Queen){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }


            sl = l;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        } else if (table.getPeace(l,n) instanceof Tower){
            int sl = l;
            int sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))return true;
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        }
        return false;
    }

    public boolean isKingCheck(int l, int n) {
        int sl, sn, p;

        sl = l - 1;
        sn = n + 1;

        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião sup esq");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","Dama ou bispo ataque sup esq");
                return true;
            }

            p = 0;
            sl = sl - 1;
            sn = sn + 1;
        }


        sl = l;
        sn = n;
        sl = sl + 1;
        sn = sn + 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião sup dir");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","D ou B sup dir");
                return true;
            }

            p=0;
            sl = sl + 1;
            sn = sn + 1;
        }

        sl = l + 1;
        sn = n - 1;
        p = 1;
        while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {

                    Log.i("Ataque","Pião inf dir");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","D ou B inf dir");
                return true;
            }

            p = 0;
            sl = sl + 1;
            sn = sn - 1;

        }

        sl = l -1;
        sn = n -1;
        p=1;
        while((sl<=7 && sl>=0) && (sn<=7 && sn>=0)) {//inf esq

            if (!(table.getPeace(sl, sn) instanceof Empty) && ((table.getPeace(sl, sn).isWhite() == isWhiteTurn) || !(table.getPeace(sl, sn) instanceof Queen) && !(table.getPeace(sl, sn) instanceof Bishop))) {
                if (table.getPeace(sl, sn) instanceof Pawn && p != 1)
                    break;

                else if (table.getPeace(sl, sn) instanceof Pawn && p == 1 && table.getPeace(sl, sn).isWhite()!=isWhiteTurn) {
                    Log.i("Ataque","Pião inf esq");
                    return true;
                } else
                    break;
            } else if (table.getPeace(sl, sn) instanceof Queen || table.getPeace(sl, sn) instanceof Bishop) {
                Log.i("Ataque","B ou D inf esq");
                return true;
            }

            p=0;
            sl = sl -1;
            sn = sn -1;
        }

            //se nao for diagonal
        sn=n;
        sl = l -1;
        while(sl<=7 && sl>=0) {//esq
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D esq");
                return true;
            }
            sl = sl -1;
        }

        sn=n;
        sl = l +1;

        while(sl<=7 && sl>=0) {//dir
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D dir");
                return true;
            }
            sl = sl +1;
        }


        sl=l;
        sn = n -1;

        while(sn<=7 && sn>=0) {//inf
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D baixo");
                return true;
            }
            sn = sn -1;
        }

        sl=l;
        sn = n +1;
        while(sn<=7 && sn>=0) {//sup
            if(!(table.getPeace(sl,sn) instanceof Empty) && (table.getPeace(sl,sn).isWhite()==isWhiteTurn || (!(table.getPeace(sl,sn) instanceof Queen) && !(table.getPeace(sl,sn) instanceof Tower)))){
                break;
            }
            else if (table.getPeace(sl,sn) instanceof Queen || table.getPeace(sl,sn) instanceof Tower) {
                Log.i("Ataque","T ou D top");
                return true;
            }
            sn = sn +1;
        }

        //cavalo
        sl=l;
        sn=n;
        if(sl+1<=7 && sn+2<=7)            //sup sup dir
            if( table.getPeace(sl+1,sn+2) instanceof Horse && table.getPeace(sl+1,sn+2).isWhite()!=isWhiteTurn) {
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+2<=7 && sn+1<=7)            //sup dir dir
            if( table.getPeace(sl+2,sn+1) instanceof Horse && table.getPeace(sl+2,sn+1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+2<=7 && sn-1>=0)            //inf dir dir
            if( table.getPeace(sl+2,sn-1) instanceof Horse && table.getPeace(sl+2,sn-1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl+1<=7 && sn-2>=0)            //inf inf dir
            if( table.getPeace(sl+1,sn-2) instanceof Horse && table.getPeace(sl+1,sn-2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-1>=0 && sn-2>=0)            //inf inf esq
            if( table.getPeace(sl-1,sn-2) instanceof Horse && table.getPeace(sl-1,sn-2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-2>=0 && sn-1>=0)            //inf esq esq
            if( table.getPeace(sl-2,sn-1) instanceof Horse && table.getPeace(sl-2,sn-1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-2>=0 && sn+1<=7)            //sup esq esq
            if( table.getPeace(sl-2,sn+1) instanceof Horse && table.getPeace(sl-2,sn+1).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }
        if(sl-1>=0 && sn+2<=7)            //sup sup esq
            if( table.getPeace(sl-1,sn+2) instanceof Horse && table.getPeace(sl-1,sn+2).isWhite()!=isWhiteTurn){
                Log.i("Ataque","Cavalo");
                return true;
            }

        return false;
    }

    public boolean isMyTurn(int mode) {
        if(mode < 2) return true;
        if(isWhiteTurn && mode==2)return true;
        if(!isWhiteTurn && mode==3)return true;
        return false;
    }
}
