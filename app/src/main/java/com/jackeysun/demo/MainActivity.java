package com.jackeysun.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.swipe_target);

        List<String> datas = new ArrayList<>();

        for (int i = 0; i<10;i++){
            datas.add(new String("第"+i+"项"));
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas);
        listview.setAdapter(adapter);

    }

    public void click(View view){
        startActivity(new Intent(MainActivity.this,Main3Activity.class));
    }
}
