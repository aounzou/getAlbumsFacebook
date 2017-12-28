package com.amine.hiddenfoundrs.adapters;
import com.amine.hiddenfoundrs.model.IdAlbum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
public class JSONParser {
    public static ArrayList<IdAlbum> albumId = new ArrayList<IdAlbum>();
    public static ArrayList<String> favTeams = new ArrayList<>();


    public static ArrayList<IdAlbum> getAlbumsId(String obj) {
        albumId.clear();

        System.out.println("obj"+obj+"");
        try {
            JSONObject object1 = (JSONObject) new JSONTokener(obj).nextValue();
            JSONArray arr = object1.getJSONArray("data");

             String s;
            for (int i = 0; i < arr.length(); i++) {
                object1 = arr.getJSONObject(i);
                s = object1.getString("id");
                System.out.println("IDS"+s+"");
                IdAlbum idAlbum=new IdAlbum(object1.getString("id"),object1.getString("name"),object1.getString("created_time"));


                albumId.add(idAlbum);
            }

        }
        catch (Exception c){

        }

        return albumId;
    }

    public static ArrayList<String> getFavTeams(JSONObject obj) {
        favTeams.clear();
        try {

            JSONArray arr = obj.getJSONArray("favorite_teams");
            String s;
            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);
                s = obj.getString("name");
                favTeams.add(s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return favTeams;
    }

    public static String getName(JSONObject obj) {
        String s1 = "";
        try {

             s1 = obj.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }


    public static String getId(JSONObject obj) {
        String s1 = "";
        try {

            s1 = obj.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }
    public static String getAlbums(JSONObject obj) {
        String s1 = "";
        try {

            s1 = obj.getString("albums");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }

}