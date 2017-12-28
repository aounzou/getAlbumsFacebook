package com.amine.hiddenfoundrs.pager;

/**
 * Created by AMINE on 26/11/2017.
 */


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amine.hiddenfoundrs.adapters.ImageAdapter;
import com.amine.hiddenfoundrs.adapters.ImagePagerAdapter;

import com.amine.hiddenfoundrs.R;
import com.squareup.picasso.Picasso;


public class ImageViewPager extends Activity {
    // Declare Variable
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set title for the ViewPager
        setTitle("Picture Details");
        // Get the view from view_pager.xml
        setContentView(R.layout.view_pager);

        // Retrieve data from MainActivity on item click event
        Intent p = getIntent();
        position = p.getExtras().getInt("id");


        Bundle args=p.getBundleExtra("BUNDLE");
        ArrayList<String> albums =(ArrayList<String>) args.getSerializable("albumsID");

      //  ImageAdapter imageAdapter = new ImageAdapter(this);
        List<ImageView> images = new ArrayList<ImageView>();

        // Retrieve all the images
        for (int i = 0; i < albums.size(); i++) {
            ImageView imageView = new ImageView(this);
            System.out.println("SED "+albums.get(i));


            Picasso.with(this).load(albums.get(i)).into(imageView);




/*
            final Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);
            imageView.startAnimation(zoomAnimation);
*/
           // imageView.setScaleType(ImageView.ScaleType.CENTER);
            images.add(imageView);
        }

        // Set the images into ViewPager
        ImagePagerAdapter pageradapter = new ImagePagerAdapter(images);
        ViewPager viewpager = (ViewPager) findViewById(R.id.pager);
        viewpager.setAdapter(pageradapter);
        // Show images following the position
        viewpager.setCurrentItem(position);
    }
}