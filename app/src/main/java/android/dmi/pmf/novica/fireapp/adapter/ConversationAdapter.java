package android.dmi.pmf.novica.fireapp.adapter;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.dao.ConversationDao;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.ConversationUpdatedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.view.ConversationItemView;
import android.dmi.pmf.novica.fireapp.view.ConversationItemView_;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novica on 6/7/2017.
 */

@EBean
public class ConversationAdapter extends BaseAdapter {

    private List<Conversation> conversations = new ArrayList<>();

    @RootContext
    Context context;

    @Bean
    ConversationDao conversationDao;

    @Bean
    OttoBus bus;

    @AfterInject
    void init(){
        bus.register(this);
    }

    public void resetConversationFlow(){
        conversationDao.init();
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ConversationItemView conversationItemView;

        if(convertView == null){
            conversationItemView = ConversationItemView_.build(context);
        }else{
            conversationItemView = (ConversationItemView) convertView;
        }

        //bind data to view
        conversationItemView.bind((Conversation) getItem(position));

        return conversationItemView;
    }

    private void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;

        // notify that data set changed so that the list is refreshed
        notifyDataSetChanged();
    }

    @Subscribe
    public void conversationsUpdated(ConversationUpdatedEvent conversationsUpdatedEvent){
        setConversations(conversationDao.getConversationList());
    }
}
