package com.example.migue.chessgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.migue.chessgame.Logic.AllHistoricGames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HistoryActivity extends Activity {

    AllHistoricGames allgames;



    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        allgames= new AllHistoricGames();



        LinearLayout linearLayout = new LinearLayout(this);

        ListView DynamicListView = new ListView(this);
        try {
            openFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final ArrayList<String> DynamicListElements = new ArrayList<>();

        for(int i=0; i<allgames.getJogos().size();i++)
            DynamicListElements.add(allgames.getJogos().get(i).toStringA(context));


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (HistoryActivity.this, android.R.layout.simple_list_item_1, DynamicListElements);

        DynamicListView.setAdapter(adapter);

        linearLayout.addView(DynamicListView);

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        DynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, History2Activity.class);
                intent.putExtra("gameDetails",allgames.getJogos().get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cleanH:
                File file = new File(Environment.getExternalStorageDirectory(), "history.dat");
                file.delete();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openFile() throws IOException, ClassNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory(), "history.dat");
        if(file.exists()) { //seFicheiroJaExistir
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            allgames = (AllHistoricGames) ois.readObject();
            buildList();
        }
        else{
            Toast.makeText(this, R.string.NoGames,Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private ArrayList<String>  buildList(){
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i< allgames.getJogos().size();i++){
            temp.add(allgames.getJogos().get(i).getPlayer1()+ " vs " +allgames.getJogos().get(i).getPlayer2());
        }

        return temp;
    }


}
