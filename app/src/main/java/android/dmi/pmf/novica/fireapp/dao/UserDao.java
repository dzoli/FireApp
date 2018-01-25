package android.dmi.pmf.novica.fireapp.dao;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.UsersLoadedEvent;
import android.dmi.pmf.novica.fireapp.model.User;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Novica on 6/6/2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class UserDao {

    private static final String USERS_DB_TAG = "Users";
    private static final String USERS_STORAGE_DB_TAG = "user_photos";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private Map<String, User> users = new HashMap<>();
    private List<User> userList = new ArrayList<>();

    public static User currentUser;

    private final FirebaseStorage storageDb = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storageDb.getReference().child(USERS_STORAGE_DB_TAG);

    @Bean
    public OttoBus bus;

    @RootContext
    Context ctx;

    //every time when init() is invoked we attach listener to "Users" reference in db
    // and every time when data is changed onDataChange() is invoked!!
    public void init() {
        final DatabaseReference usersRef = db.getReference(USERS_DB_TAG);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, User>>() {});
//                bus.post(new UsersLoadedEvent());
                publish();
                Log.d("users loaded", "users loaded");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("users cancelled", "users cancelled");
            }
        });
    }

    private void publish() {
        userList.clear();
        if(users != null){
            userList.addAll(users.values());
        }
        bus.post(new UsersLoadedEvent());
    }

    public void write(User user) {
        db.getReference(USERS_DB_TAG + "/" + user.getId()).setValue(user); //write to db
    }

    public void updateUser(final User user){
        Uri selectedPhotoUri = Uri.parse(user.getPhotoUrl());
        StorageReference photoRef = storageReference.child(selectedPhotoUri.getLastPathSegment());
        photoRef.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUri = taskSnapshot.getDownloadUrl();

                currentUser.setPhotoUrl(downloadUri.toString());
                final DatabaseReference userRef = db.getReference(USERS_DB_TAG + "/" + user.getId());
                userRef.setValue(user);
            }
        });

        //TODO: publish changes with UsersLoadedEvent
        // Reset flow in user adapter
        publish();

    }

    public boolean userExists(String userId) {
        return users != null && users.containsKey(userId);
    }

    public User getUserById(String userId) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.get(userId);
    }

    public User getUserByUsername(String username) {
        if (users == null || users.isEmpty())
            return null;
        for (User user : users.values())
            if (user.getUsername().equals(username)) {
                return user;
            }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<User> getUserList() {
        userList.clear();
        if(users != null){
            userList.addAll(users.values());
        }
        return userList;
    }
}
