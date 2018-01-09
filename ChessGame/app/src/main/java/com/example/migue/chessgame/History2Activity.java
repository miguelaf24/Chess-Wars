package com.example.migue.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.migue.chessgame.Logic.GameHistoric;

import java.util.ArrayList;

public class History2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);

        Intent intent = getIntent();
        GameHistoric game= (GameHistoric) intent.getSerializableExtra("gameDetails");
        TextView players = (TextView) findViewById(R.id.playerNames);
        ListView list = (ListView)findViewById(R.id.list);

        final ArrayList<String> DynamicListElements = new ArrayList<>();

        players.setText(game.toString());

        for(int i=0; i<game.getHistorico().size();i++)
            DynamicListElements.add(game.getHistorico().get(i).toString());

        ArrayAdapter<String> mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, DynamicListElements);
        list.setAdapter(mAdapter);
    }
}
