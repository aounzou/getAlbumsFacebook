package com.amine.hiddenfoundrs.idAlbums;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amine.hiddenfoundrs.albums.AlbumActivity;
import com.amine.hiddenfoundrs.R;
import com.amine.hiddenfoundrs.model.IdAlbum;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
 import java.util.List;

public class AlbumIdActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    public static ArrayList<String> idb=new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumid_lyout);

        setTitle("Albums Name");


        Intent intent=getIntent();
        Bundle args=intent.getBundleExtra("BUNDLE");
        List<IdAlbum> albums =(List<IdAlbum>) args.getSerializable("albumsId");




        // set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, albums);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
      // Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();


        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + adapter.getItem(position) + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);

                        if (response.getError() == null) {


                            JSONObject joMain = response.getJSONObject();
                            if (joMain.has("data")) {
                                JSONArray jaData = joMain.optJSONArray("data");
                                idb.clear();

                                for (int i = 0; i < jaData.length(); i++) {
                                    try {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");

                                        if(jaImages.length()>0)
                                        {
                                            System.out.println("wa SOURCE1 "+jaImages.getJSONObject(0).getString("source"));

                                            idb.add(jaImages.getJSONObject(0).getString("source"));

                                        }



                                    }catch (Exception e){

                                    }




                                }

                                Intent mIntent=new Intent(getApplication(),AlbumActivity.class);
                                Bundle arg=new Bundle();
                                arg.putSerializable("albums",(Serializable)idb );
                                mIntent.putExtra("BUNDLE",arg);



                                startActivity(mIntent);



                                System.out.println("LH"+idb.size());
                            }

                        }
                    }}
        ).executeAsync();







    }







}