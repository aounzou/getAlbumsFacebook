package com.amine.hiddenfoundrs.adapters;

/**
 * Created by AMINE on 26/11/2017.
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amine.hiddenfoundrs.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
   public static List<String> idAlbums= Collections.emptyList();;


    public ImageAdapter(Context c, List<String> idAlbums) {
        mContext = c;
        this.idAlbums=idAlbums;
    }
    public int getCount() {
        System.out.println("LHSSAb2"+idAlbums.size());

        return idAlbums.size();

    }

    public Object getItem(int position) {
        return idAlbums.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // If it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
System.out.println("SS"+idAlbums.get(position));
        Picasso.with(mContext)
                .load(idAlbums.get(position))
                .resize(300,300)
                .centerInside().into(imageView);
       // imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // References to our images in res > drawable

}