package android.dmi.pmf.novica.fireapp.activity;

import android.content.Intent;
import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.adapter.ConversationAdapter;
import android.dmi.pmf.novica.fireapp.adapter.UserAdapter;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.UsersLoadedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.model.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;

@OptionsMenu({R.menu.menu_signout,
              R.menu.menu_profile})
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 1;

    @ViewById
    ListView listViewConversations;

    @Bean
    UserDao userDao;

    @Bean
    OttoBus bus;

    @Bean
    UserAdapter userAdapter; //just instantiate for Bus.register()

    @Bean
    ConversationAdapter conversationAdapter;

    @AfterViews
    public void init(){
        auth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currUser = firebaseAuth.getCurrentUser();
                if (currUser != null) {
                    userDao.init(); //user is signed-in = read from database
                }else{
                    //user is signed out = force login
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        listViewConversations.setAdapter(conversationAdapter);
    }

    @Subscribe
    public void usersLoaded(UsersLoadedEvent event) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (userDao.userExists(firebaseUser.getUid())) {
            userDao.setCurrentUser(userDao.getUserById(firebaseUser.getUid())); //if exists set current user
        } else {
            //write current logged user (get from fire-base Auth) to fire-base db
            final User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail().toString());
            userDao.write(user);
            userDao.setCurrentUser(user); //set current user to singleton
        }
        this.setTitle("Welcome " + firebaseUser.getDisplayName() + "!");
        conversationAdapter.resetConversationFlow();
    }

    @OnActivityResult(value = RC_SIGN_IN)
    public void loginSucceeded(int resultCode) {
        if (resultCode != RESULT_OK) {
            return;
        }
        userDao.init();
        conversationAdapter.resetConversationFlow();
    }

    @ItemClick
    public void listViewConversations(Conversation conversation){
        ConversationActivity_.intent(this)
                             .conversation(conversation)
                             .start();
    }

    @OptionsItem
    void signOut() {
        FirebaseAuth.getInstance().signOut();

        auth.removeAuthStateListener(authStateListener);
        AuthUI.getInstance().signOut(this);
        // restart this activity after user is logged out because if there is no user we will start
        // login activity
        final Intent intent = getIntent();
        finish();
        startActivity(intent); //restart activity
    }

    @OptionsItem
    void editProfile(){
        Toast.makeText(this, "edit user profile", Toast.LENGTH_SHORT).show();
        AddUserDetailsActivity_.intent(this)
                               .start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Click
    void fabAdd(){
        CreateConversationActivity_.intent(this).start();
    }
}
