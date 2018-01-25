package android.dmi.pmf.novica.fireapp.adapter;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.dao.MessageDao;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.MessagesUpdatedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.model.Message;
import android.dmi.pmf.novica.fireapp.view.MessageItemView;
import android.dmi.pmf.novica.fireapp.view.MessageItemView_;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novica on 6/11/2017.
 */

@EBean
public class MessageAdapter  extends BaseAdapter{
    private List<Message> messages = new ArrayList<>();

        @Bean
        UserDao userDao;

        @Bean
        MessageDao messageDao;

        @RootContext
        Context context;

        @Bean
        OttoBus bus;

        public void initFor(Conversation conversation){
            bus.register(this);
            messageDao.initFor(conversation);
        }

        @Subscribe
        public void updateMessages(MessagesUpdatedEvent event) {
            messages = messageDao.getMessagesList();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MessageItemView messageItemView;

            if(convertView == null){
                messageItemView = MessageItemView_.build(context);
            }else{
                messageItemView = (MessageItemView) convertView;
            }

            // bind data to the view
            messageItemView.bind((Message) getItem(position));
            return messageItemView;
        }
}
