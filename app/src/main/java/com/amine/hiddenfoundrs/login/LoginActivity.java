package com.amine.hiddenfoundrs.login;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.amine.hiddenfoundrs.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private com.facebook.login.widget.LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);




            if (fragment == null) {

//test

//Generation KEY HASH
                try {
                    PackageInfo info = getPackageManager().getPackageInfo("com.amine.hiddenfoundrs",         PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                        Log.e("MY KEY HASH:", sign);
                  //      Toast.makeText(getApplicationContext(),sign,         Toast.LENGTH_LONG).show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                } catch (NoSuchAlgorithmException e) {
                }

                fragment = new FacebookFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }

    }
}
