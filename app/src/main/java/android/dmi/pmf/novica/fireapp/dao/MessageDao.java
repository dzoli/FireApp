package android.dmi.pmf.novica.fireapp.dao;

import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.MessagesUpdatedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.model.Message;
import android.net.Uri;
import android.support.annotation.NonNull;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Novica on 6/11/2017.
 */

@EBean
public class MessageDao {

    private static final String MESSAGE_DB_TAG = "messages";
    private static final String CHAT_STORAGE_DB_TAG = "chat_photos";

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Conversation conversation;
    private Map<String, Message> messageMap = new HashMap<>();
    private List<Message> messagesList = new ArrayList<>();
    private final FirebaseStorage storageDb = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storageDb.getReference().child(CHAT_STORAGE_DB_TAG);

    @Bean
    OttoBus bus;

    @Bean
    UserDao userDao;

    public void initFor(Conversation conversation){
        this.conversation = conversation;
        db.getReference(ConversationDao.CONVERSATION_DB_TAG + "/"
                + conversation.getId() + "/" + MESSAGE_DB_TAG).addValueEventListener(new ValueEventListener() {
            //listen for conversations/id/messages
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Message>>() {});
                publish();
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void publish() {
        if (messageMap == null) {
            return;
        }
        messagesList = new ArrayList<>(messageMap.values());
        Collections.sort(messagesList);
        bus.post(new MessagesUpdatedEvent());
    }

    // write message to database in conversations/id/messages/pushID
    public void write(Message message) {
        final DatabaseReference msgRef = db.getReference(ConversationDao.CONVERSATION_DB_TAG + "/"
                + conversation.getId() + "/" + MESSAGE_DB_TAG)
                                           .push();
        message.setId(msgRef.getKey());
        msgRef.setValue(message);
    }

    public void writePhotoMessage(Uri selectedPhotoUri) {
        StorageReference photoRef = storageReference.child(selectedPhotoUri.getLastPathSegment());
        photoRef.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUri = taskSnapshot.getDownloadUrl();

                Message photoMessage = new Message();
                photoMessage.setUser(userDao.getCurrentUser());
                photoMessage.setPhotoUrl(downloadUri.toString());
                final DatabaseReference msgRef = db.getReference(ConversationDao.CONVERSATION_DB_TAG + "/"
                        + conversation.getId() + "/" + MESSAGE_DB_TAG).push();
                photoMessage.setId(msgRef.getKey());
                msgRef.setValue(photoMessage);
            }
        });
    }

    @NonNull
    private String gerReferenceFor(Conversation conversation) {
        return ConversationDao.CONVERSATION_DB_TAG
                + "/" + conversation.getId()
                + "/" + MESSAGE_DB_TAG;
    }

    public List<Message> getMessagesList(){
        return messagesList;
    }
}



