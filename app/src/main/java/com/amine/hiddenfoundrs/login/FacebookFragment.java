package com.amine.hiddenfoundrs.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amine.hiddenfoundrs.idAlbums.AlbumIdActivity;
import com.amine.hiddenfoundrs.adapters.JSONParser;
import com.amine.hiddenfoundrs.adapters.ImageHelper;

import com.amine.hiddenfoundrs.R;
import com.amine.hiddenfoundrs.model.IdAlbum;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FacebookFragment extends Fragment{

    private LoginButton loginButton;
    private Button getUserInterests;
    private boolean postingEnabled = false;

    private static final String PERMISSION = "publish_actions";
    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.example.hellofacebook:PendingAction";

    private ImageView profilePicImageView;
    private TextView greeting;
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;

    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    public static ArrayList<IdAlbum> albumsId = new ArrayList<IdAlbum>();

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("FacebookFragment", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FacebookFragment", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
    String idUser="";

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        // Other app specific specialization
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_facebook, parent, false);
        loginButton = (LoginButton) v.findViewById(R.id.loginButton);
        // If using in a fragment
        loginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast toast = Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT);
                postingEnabled = true;
                getUserInterests.setVisibility(View.VISIBLE);

                toast.show();
                handlePendingAction();
                updateUI();
            }


            @Override
            public void onCancel() {
                // App code
                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE
                        && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();

            }

            private void showAlert() {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.cancelled)
                        .setMessage(R.string.permission_not_granted)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

        });
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                handlePendingAction();
            }
        };


        profilePicImageView = (ImageView) v.findViewById(R.id.profilePicture);
        greeting = (TextView) v.findViewById(R.id.greeting);




        getUserInterests = (Button) v.findViewById(R.id.getInterestsButton);

        getUserInterests.setOnClickListener(new View.OnClickListener() {



















            @Override
            public void onClick(View v) {











                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),


                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                if (object != null) {
                                    Log.d("Flogin", object.toString());
                                    String name = JSONParser.getName(object);
                                    String id = JSONParser.getId(object);
                                    String album=JSONParser.getAlbums(object);




                                     albumsId = JSONParser.getAlbumsId(album);


                                    String s ="Name : "+name+"\n";
                                    s +="Id : "+id+"\n";
                                    s +="albumsId : "+"\n";
                                    s+="albums :"+album+"\n";
                                    idUser=id+"";

                                    for(int i = 0; i < albumsId.size(); i++) {
                                        String p="";

                                        p += ((i + 1) + ". " + albumsId.get(i).getDateCreation()).toString() + "\n";
                                            //GetFacebookImages(albumsId.get(i));
                                         /*  Toast t = Toast.makeText(getActivity(), p, Toast.LENGTH_LONG);
                                            t.show();*/

                                    }


                                    Intent mIntent=new Intent(getActivity(),AlbumIdActivity.class);
                                    Bundle arg=new Bundle();
                                    arg.putSerializable("albumsId",(Serializable)albumsId );
                                    mIntent.putExtra("BUNDLE",arg);



                                    startActivity(mIntent);




/*
                                    Toast t = Toast.makeText(getActivity(),s, Toast.LENGTH_LONG);
                                    t.show();
                                    */
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                // parameters.putString("fields", "id,name,picture.type(album),count,link,favorite_athletes,favorite_teams");

                parameters.putString("fields", "id,name,albums,link");
                request.setParameters(parameters);
                request.executeAsync();





/*
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                        "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d("TAG", "Facebook Albums: " + response.toString());

                                    if (response.getError() == null) {
                                        JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                        if (joMain.has("data")) {
                                            JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                            Toast p = Toast.makeText(getActivity(), jaData.toString(), Toast.LENGTH_LONG);
                                            p.show();
                                        }
                                    } else
                                        Log.d("Test", response.getError().toString());


                            }
                        }
                ).executeAsync();


                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/albums",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                Toast p = Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG);
                                p.show();

                            Log.d("token is","Token is"+   AccessToken.getCurrentAccessToken());

/*
                                String albumID = null;
                                try{
                                    JSONObject json = response.getJSONObject();
                                    JSONArray jarray = json.getJSONArray("data");

                                    Toast p = Toast.makeText(getActivity(),jarray.toString(), Toast.LENGTH_LONG);
                                    p.show();

                                    for(int i = 0; i < jarray.length(); i++) {
                                        JSONObject oneAlbum = jarray.getJSONObject(i);
                                        //get albums id
                                        if (oneAlbum.getString("name").equals("Profile Pictures")) {
                                            albumID = oneAlbum.getString("id");
                                        }

                                    }
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();



*/



            }
        });



        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(
                ShareLinkContent.class);

        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(
                SharePhotoContent.class);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile","user_photos"));
                if(!postingEnabled) {

                    postingEnabled = true;

                    getUserInterests.setVisibility(View.VISIBLE);
                }else{

                    postingEnabled = false;

                    getUserInterests.setVisibility(View.GONE);

                }

//                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject object,
//                                    GraphResponse response) {
//                                if (object != null) {
//                                    Log.d("Me Request",object.toString());
//                                    Toast t = Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_SHORT);
//                                    t.show();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,link,email");
//                request.setParameters(parameters);
//                request.executeAsync();
            }
        });


//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
//
//
//
//            }
//        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


        AppEventsLogger.activateApp(getActivity());

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();


        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }



    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;



        Profile profile = Profile.getCurrentProfile();


        if (enableButtons && profile != null) {
            new LoadProfileImage(profilePicImageView).execute(profile.getProfilePictureUri(200, 200).toString());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
            postingEnabled = true;

            getUserInterests.setVisibility(View.VISIBLE);

        } else {
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_default);
            profilePicImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
            greeting.setText(null);
            postingEnabled = false;

            getUserInterests.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;

        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {


                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),resized,250,200,200, false, false, false, false));

            }
        }
    }


}





