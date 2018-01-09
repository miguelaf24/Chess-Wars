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
                isWhiteTurn = !isWhiteTurn;
                table.setJogada(++nJogada);
                historico.add(new Jogada(getPeace(l,n),new Posicao(sl,sn),new Posicao(l,n)));
                verPawnToQueen();
                if(!isWhiteTurn && singleplayer)
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
            if(isF && table.getPeace(l,n) instanceof Pawn)table.getPeace(l,n).setJogadaFirst(nJogada);
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
        jogadasIA = new ArrayList<>();
        bestMove = -2000;
        //Table tableIA = table;
        isWhiteTurn = true;
        ArrayList<JogadaIA> jIA = firstIA(levelIA,null);
        changePeace(jIA.get(jIA.size()-1).getJogada().inicial.getX(),jIA.get(jIA.size()-1).getJogada().inicial.getY(),jIA.get(jIA.size()-1).getJogada().Final.getX(),jIA.get(jIA.size()-1).getJogada().Final.getY());
        isWhiteTurn = true;
    }

    ArrayList<JogadaIA> firstIA(int lvl, ArrayList<JogadaIA> jogadaIA){
        ArrayList<JogadaIA> jogadasIAtemp = jogadaIA;
        ArrayList<Peace> blackPeaces = table.getlista().black;
        ArrayList<Peace> whitePeaces = table.getlista().white;
        Table tempTable = table;

        Jogada jIA = null;
        int maxPoints = -9999;

        if(jogadaIA==null){
            for (int i = 0; i < table.getlista().black.size(); i++) {
                int tentativa = 0;
                while(true){
                    Jogada jAux = tryIAJogada(table.getlista().black.get(i).getL(), table.getlista().black.get(i).getN(), lvl, true);
                    // TODO: 06/01/2018 change tryIAJogada para retornar todas as Jogadas da peça
                    if(jAux==null)
                        break;
                    else
                        tentativa++;
                    int points = getPoints(table, true);
                    if(points >= maxPoints){
                        jogadasIAtemp.add(new JogadaIA(jAux, getPoints(table, true)));
                        maxPoints = points;
                    }
                    reporIA(tempTable,whitePeaces,blackPeaces);
                }

            }
        }
        else{
            for(int j = 0; j < jogadasIAtemp.size() ; j++) {
                Jogada jogada = jogadasIAtemp.get(j).getJogada();
                changePeace(jogada.inicial.getX(),jogada.inicial.getY(),jogada.Final.getX(),jogada.Final.getY());

                if (isWhiteTurn) {
                    for (int i = 0; i < table.getlista().white.size(); i++) {
                        Jogada jAux = tryIAJogada(table.getlista().white.get(i).getL(), table.getlista().white.get(i).getN(), lvl, true);
                        int points = getPoints(table, true) + jogadasIAtemp.get(j).getPoints();
                        if (points > maxPoints) {
                            maxPoints = points;
                            jogadasIAtemp.get(j).addPoints(points);
                        }
                        reporIA(tempTable, whitePeaces, blackPeaces);
                    }
                } else {
                    for (int i = 0; i < table.getlista().black.size(); i++) {
                        Jogada jAux = tryIAJogada(table.getlista().black.get(i).getL(), table.getlista().black.get(i).getN(), lvl, true);
                        int points = getPoints(table, true) + jogadasIAtemp.get(j).getPoints();
                        if (points > maxPoints) {
                            maxPoints = points;
                            jogadasIAtemp.get(j).addPoints(points);
                        }
                        reporIA(tempTable, whitePeaces, blackPeaces);
                    }
                }
            }
        }
        for(int i = 0; i<jogadasIAtemp.size();i++){
            if(jogadaIA.get(i).getPoints()<maxPoints){
                jogadasIAtemp.remove(i--);
            }
        }

        if(lvl>0)
            return firstIA(lvl, jogadasIAtemp);
        return jogadasIAtemp;
    }

    private void reporIA(Table tempTable, ArrayList<Peace> whitePeaces, ArrayList<Peace> blackPeaces) {
        table=tempTable;
        table.getlista().white.clear();
        table.getlista().white = whitePeaces;
        table.getlista().black.clear();
        table.getlista().black = blackPeaces;
    }

    private Jogada tryIAJogada(int l, int n, int level, boolean IAturn) {
        Jogada jFinal = null;
        Table tempTable = table;
        int points = bestMove;
        if(table.getPeace(l,n) instanceof King){
            if(trychangePeace(l,n,l+1,n)){
                tempTable=startIA(l,n,l+1,n, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n));
                }
            }
            if(trychangePeace(l,n,l+1,n+1)){
                tempTable=startIA(l,n,l+1,n+1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n+1));
                }
            }
            if(trychangePeace(l,n,l+1,n-1)){
                tempTable=startIA(l,n,l+1,n-1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n-1));
                }
            }
            if(trychangePeace(l,n,l,n-1)){
                tempTable=startIA(l,n,l,n-1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-1));
                }
            }
            if(trychangePeace(l,n,l,n+1)){
                tempTable=startIA(l,n,l,n+1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+1));
                }
            }
            if(trychangePeace(l,n,l-1,n)){
                tempTable=startIA(l,n,l-1,n, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n));
                }
            }
            if(trychangePeace(l,n,l-1,n+1)){
                tempTable=startIA(l,n,l-1,n+1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n+1));
                }
            }
            if(trychangePeace(l,n,l-1,n-1)){
                tempTable=startIA(l,n,l-1,n-1, level, IAturn);
                int temp = getPoints(tempTable, IAturn);
                if(temp>=points){
                    points = temp;
                    jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n-1));
                }
            }
            if(table.getPeace(l,n).isFirstPlay()){
                if(table.getPeace(l+1,n) instanceof Empty && table.getPeace(l+2,n) instanceof Empty && table.getPeace(l+3,n).isFirstPlay()){
                    tempTable=startIA(l,n,l+2,n, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+2,n));
                    }
                }
                if(table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-2,n) instanceof Empty && table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-4,n).isFirstPlay()){
                    tempTable=startIA(l,n,l-2,n, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-2,n));
                    }
                }
            }
        } else if(table.getPeace(l,n) instanceof Pawn){
            if(table.getPeace(l,n).isWhite()){
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n+1) instanceof  Empty && table.getPeace(l,n+2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n+2)){
                        tempTable=startIA(l,n,l,n+2, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+2));
                        }
                    }
                if(!(table.getPeace(l+1,n+1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n+1)){
                        tempTable=startIA(l,n,l+1,n+1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n+1));
                        }
                    }
                }
                if(!(table.getPeace(l-1,n+1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n + 1)) {
                        tempTable=startIA(l,n,l-1,n+1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n+1));
                        }
                    }
                }
                if(table.getPeace(l,n+1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n+1)){
                        tempTable=startIA(l,n,l,n+1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n+1));
                        }
                    }
                }
            }
            else{
                if(!(table.getPeace(l+1,n-1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n-1)){
                        tempTable=startIA(l,n,l+1,n-1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l+1,n-1));
                        }
                    }
                }
                if(!(table.getPeace(l-1,n-1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n - 1)) {
                        tempTable=startIA(l,n,l-1,n-1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l-1,n-1));
                        }
                    }
                }
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n-1) instanceof  Empty && table.getPeace(l,n-2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n-2)){
                        tempTable=startIA(l,n,l,n-2, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-2));
                        }
                    }
                if(table.getPeace(l,n-1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n-1)){
                        tempTable=startIA(l,n,l,n-1, level, IAturn);
                        int temp = getPoints(tempTable, IAturn);
                        if(temp>=points){
                            points = temp;
                            jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(l,n-1));
                        }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }
        }else if (table.getPeace(l,n) instanceof Horse){
        } else if (table.getPeace(l,n) instanceof Queen){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn)){
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
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
                    tempTable=startIA(l,n,sl,sn, level, IAturn);
                    int temp = getPoints(tempTable, IAturn);
                    if(temp>=points){
                        points = temp;
                        jFinal = new  Jogada(getPeace(l,n),new Posicao(l,n),new Posicao(sl,sn));
                    }
                }
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        }

        return jFinal;
    }


    public Table ia(int level, Table tempTable, boolean IAturn){
        if(level<=0)return tempTable;
        isWhiteTurn = !isWhiteTurn;
        Table tableIA = tempTable;
        Table tableAux;
        if(!IAturn)
            for (int i = 0; i < tempTable.getlista().white.size(); i++)
            {
                table=tempTable;
                tableAux=tryIAPiece(tempTable.getlista().white.get(i).getL(),tempTable.getlista().white.get(i).getN(),tempTable,level, IAturn);
                if(getPoints(tableAux,IAturn)>= getPoints(tableIA,IAturn)){
                    tableIA=tableAux;
                }
            }
        else {
            for (int i = 0; i < tempTable.getlista().black.size(); i++) {
                table = tempTable;
                tableAux = tryIAPiece(tempTable.getlista().black.get(i).getL(), tempTable.getlista().black.get(i).getN(), tempTable, level, IAturn);
                if (getPoints(tableAux, IAturn) >= getPoints(tableIA, IAturn)) {
                    tableIA = tableAux;
                }
            }
        }

        table = tableIA;
        isWhiteTurn = !isWhiteTurn;
        return table;
    }

    Table startIA(int l, int n,int sl, int sn, int level, boolean IAturn){
        Peace p = table.getPeace(l,n);
        Peace p2 =table.getPeace(sl,sn);

        //if(p.isWhite()==isWhiteTurn && !(p instanceof Empty))return table;
        table.setPeace(sl, sn, l, n);
        table.setPeace(-1, -1, sl, sn);
        if(IsKCheck()){
            table.setPeace(l, n, sl, sn);
            table.setThisPeace(p, l, n);
            table.setThisPeace(p2, sl, sn);
            return table;
        }


        Table aux = table;
        Table tempTable=ia(--level, table, !IAturn);

        int points =getPoints(tempTable, IAturn);
        if(points>=bestMove){
            bestMove=points;

            table.setPeace(l, n, sl, sn);
            table.setThisPeace(p, l, n);
            table.setThisPeace(p2, sl, sn);

            table = aux;
            return tempTable;
        }

        table.setPeace(l, n, sl, sn);
        table.setThisPeace(p, l, n);
        table.setThisPeace(p2, sl, sn);

        table = aux;
        return table;
    }

    int getPoints(Table t, boolean IAturn){
        int wp = 0;
        int bp =0;
        char [][] array = new char [8][8];

        /*
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                array[i][k] = ' ';
            }
        }*/
        for (int i = 0; i < t.getlista().white.size(); i++) {
            wp+=t.getlista().white.get(i).getValue();
            array[t.getlista().white.get(i).getL()][t.getlista().white.get(i).getN()]='W';
        }
        for (int i = 0; i < t.getlista().black.size(); i++) {
            bp+=t.getlista().black.get(i).getValue();
            array[t.getlista().black.get(i).getL()][t.getlista().black.get(i).getN()]='B';
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
        if(IAturn)
            return bp-wp;
        return wp-bp;
    }

    Table tryIAPiece(int l, int n, Table tempTable, int level, boolean IAturn){
        table = tempTable;
        if(table.getPeace(l,n) instanceof King){
            if(trychangePeace(l,n,l+1,n))tempTable=startIA(l,n,l+1,n, level, IAturn);
            if(trychangePeace(l,n,l+1,n+1))tempTable=startIA(l,n,l+1,n+1, level, IAturn);
            if(trychangePeace(l,n,l+1,n-1))tempTable=startIA(l,n,l+1,n-1, level, IAturn);
            if(trychangePeace(l,n,l,n-1))tempTable=startIA(l,n,l,n-1, level, IAturn);
            if(trychangePeace(l,n,l,n+1))tempTable=startIA(l,n,l,n+1, level, IAturn);
            if(trychangePeace(l,n,l-1,n))tempTable=startIA(l,n,l-1,n, level, IAturn);
            if(trychangePeace(l,n,l-1,n+1))tempTable=startIA(l,n,l-1,n+1, level, IAturn);
            if(trychangePeace(l,n,l-1,n-1))tempTable=startIA(l,n,l-1,n-1, level, IAturn);
            if(table.getPeace(l,n).isFirstPlay()){
                //if(table.getPeace(l+1,n) instanceof Empty && table.getPeace(l+2,n) instanceof Empty && table.getPeace(l+3,n).isFirstPlay())tempTable=startIA(l,n,l+2,n, level, IAturn);
                //if(table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-2,n) instanceof Empty && table.getPeace(l-1,n) instanceof Empty && table.getPeace(l-4,n).isFirstPlay())tempTable=startIA(l,n,l-2,n, level, IAturn);
            }
        } else if(table.getPeace(l,n) instanceof Pawn){
            if(table.getPeace(l,n).isWhite()){
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n+1) instanceof  Empty && table.getPeace(l,n+2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n+2))tempTable=startIA(l,n,l,n+2, level, IAturn);
                if(!(table.getPeace(l+1,n+1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n+1))tempTable=startIA(l,n,l+1,n+1, level, IAturn);
                }
                if(!(table.getPeace(l-1,n+1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n + 1)) tempTable=startIA(l,n,l-1,n+1, level, IAturn);
                }
                if(table.getPeace(l,n+1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n+1))tempTable=startIA(l,n,l,n+1, level, IAturn);
                }
            }
            else{
                if(!(table.getPeace(l+1,n-1) instanceof  Empty)){
                    if(trychangePeace(l,n,l+1,n-1))tempTable=startIA(l,n,l+1,n-1, level, IAturn);
                }
                if(!(table.getPeace(l-1,n-1) instanceof  Empty)) {
                    if (trychangePeace(l, n, l - 1, n - 1)) tempTable=startIA(l,n,l-1,n-1, level, IAturn);
                }
                if(((Pawn) table.getPeace(l,n)).isFirstPlay() && table.getPeace(l,n-1) instanceof  Empty && table.getPeace(l,n-2) instanceof  Empty)
                    if(trychangePeace(l,n,l,n-2))tempTable=startIA(l,n,l,n-2, level, IAturn);
                if(table.getPeace(l,n-1) instanceof  Empty){
                    if(trychangePeace(l,n,l,n-1))tempTable=startIA(l,n,l,n-1, level, IAturn);
                }
            }
        }else if (table.getPeace(l,n) instanceof Bishop){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }
        }else if (table.getPeace(l,n) instanceof Horse){
        } else if (table.getPeace(l,n) instanceof Queen){
            int sl = l - 1;
            int sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn + 1;
            }
            sl = l + 1;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
                sn = sn - 1;
            }
            sl = l + 1;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
                sn = sn - 1;
            }


            sl = l;
            sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        } else if (table.getPeace(l,n) instanceof Tower){
            int sl = l;
            int sn = n - 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//inf
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn - 1;
            }
            sl = l;
            sn = n + 1;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//sup
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sn = sn + 1;
            }
            sl = l - 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//esq
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl - 1;
            }
            sl = l + 1;
            sn = n;
            while ((sl <= 7 && sl >= 0) && (sn <= 7 && sn >= 0)) {//dir
                if(!(table.getPeace(l,n) instanceof Empty) && table.getPeace(l,n).isWhite()==isWhiteTurn()) break;
                else
                if(trychangePeace(l,n,sl,sn))tempTable=startIA(l,n,sl,sn, level, IAturn);
                if(!(table.getPeace(l,n) instanceof Empty)) break;
                sl = sl + 1;
            }
        }

        return tempTable;
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
