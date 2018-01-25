package android.dmi.pmf.novica.fireapp.activity;

import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Novica on 6/6/2017.
 */

@EActivity
public class BaseActivity extends AppCompatActivity {

    //auth
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        FireAuth.getInstance().signOut();
        auth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FireAuth.getInstance().signOut();
        auth.removeAuthStateListener(authStateListener); //fireAuth have logic if(l!=null) a.remove
    }
}

