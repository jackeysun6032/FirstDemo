package com.jackeysun.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.jackeysun.demo.R.id.recycler_view;

public class Main2Activity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRecyclerView = (RecyclerView) findViewById(recycler_view);
        mLayoutManager = new LinearLayoutManager(Main2Activity.this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new LinearLayoutAdapter(this);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LinearLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Main2Activity.this,"第" + position + "项",Toast.LENGTH_LONG).show();
            }
        });

        adapter.setOnLongClickListener(new LinearLayoutAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                Toast.makeText(Main2Activity.this,"第" + position + "项",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(new String("第" + i + "项"));
        }
        adapter.setItems(datas);
    }
}
