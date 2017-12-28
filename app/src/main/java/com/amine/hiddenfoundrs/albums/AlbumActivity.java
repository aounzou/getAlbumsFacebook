package com.amine.hiddenfoundrs.albums;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amine.hiddenfoundrs.R;
import com.amine.hiddenfoundrs.adapters.ImageAdapter;

import com.amine.hiddenfoundrs.idAlbums.MyRecyclerViewAdapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class AlbumActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    static String  val;
    GridView gridview;
    public static  List<String> idAlbums= Collections.emptyList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_album_row);


        setTitle("Albums Pictures");
        // Get the view from grid_view.xml

        // Set the images from ImageAdapter.java to GridView
         gridview = (GridView) findViewById(R.id.gridview);

        Intent intent=getIntent();
        Bundle args=intent.getBundleExtra("BUNDLE");
        idAlbums =(List<String>) args.getSerializable("albums");

if(idAlbums.size()==0){
    Toast.makeText(this, "This Album is Empty" + idAlbums.size() , Toast.LENGTH_SHORT).show();

}else{
    Toast.makeText(this, "This Album Containe : " + idAlbums.size()+" Itms" , Toast.LENGTH_SHORT).show();

}

        gridview.setAdapter(new ImageAdapter(this,idAlbums));

        // Listening to GridView item click
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


            }
        });


/*
        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, idAlbum);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public void onItemClick(View view, int position) {
       // Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}