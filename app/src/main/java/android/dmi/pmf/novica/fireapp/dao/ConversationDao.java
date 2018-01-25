package android.dmi.pmf.novica.fireapp.dao;

import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.ConversationUpdatedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Novica on 6/7/2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class ConversationDao {

    public static final String CONVERSATION_DB_TAG = "Conversations";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private List<Conversation> conversationList = new ArrayList<>();

    private Map<String, Conversation> conversationMap = new HashMap<>();

    @Bean
    OttoBus bus;

    @AfterInject
    public void init(){
        db.getReference(CONVERSATION_DB_TAG).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversationMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Conversation>>() {});
                publish();
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void write(Conversation conversation){
        final DatabaseReference conversationsRef = db.getReference(CONVERSATION_DB_TAG).push();
        conversation.setId(conversationsRef.getKey());
        conversationsRef.setValue(conversation);
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    private void publish() {
        conversationList.clear();
        if(conversationMap != null ){
//            conversationList.addAll(conversationMap.values());

            for(Conversation c: conversationMap.values()){
                if(c.getUsers().values().contains(UserDao.currentUser)){
                    conversationList.add(c);
                }
            }
        }
        bus.post(new ConversationUpdatedEvent());
    }

}
