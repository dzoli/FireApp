package android.dmi.pmf.novica.fireapp.activity;

import android.dmi.pmf.novica.fireapp.adapter.MessageAdapter;
import android.dmi.pmf.novica.fireapp.fragment.CreateMessageFragment;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.support.v7.app.AppCompatActivity;
import android.dmi.pmf.novica.fireapp.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_conversation)
public class ConversationActivity extends AppCompatActivity {

    @ViewById
    ListView listViewMessages;

    //createMessageFragment is ID of fragment
    @FragmentByTag
    CreateMessageFragment createMessageFragment;

    @Extra
    Conversation conversation;

    @Bean
    MessageAdapter messageAdapter;

    @AfterViews
    void init(){
        createMessageFragment.initFor(conversation);

        initListView();
    }

    private void initListView(){
        messageAdapter.initFor(conversation);
        listViewMessages.setAdapter(messageAdapter);
    }
}