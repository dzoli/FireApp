package android.dmi.pmf.novica.fireapp.adapter;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.UsersLoadedEvent;
import android.dmi.pmf.novica.fireapp.model.User;
import android.dmi.pmf.novica.fireapp.view.UserItemView;
import android.dmi.pmf.novica.fireapp.view.UserItemView_;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Novica on 6/8/2017.
 */
@EBean
public class UserAdapter extends BaseAdapter implements Filterable {

    private List<User> userData = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();
    private static List<User> selectedUsers = new ArrayList<>();

    @RootContext
    Context context;

    @Bean
    UserDao userDao;

    @Bean
    OttoBus bus;


    @AfterInject
    public void init(){
        bus.register(this);

    }

    public void resetConversationFlow(){
        userDao.init();
    }

    @Override
    public int getCount() {
        return filteredUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserItemView userItemView;
        if(convertView == null){
            userItemView = UserItemView_.build(context);
        }else{
            userItemView = (UserItemView) convertView;
        }

        if(selectedUsers.contains(getItem(position))){
            userItemView.setCheckBoxChekStatus(true);
        }else {
            userItemView.setCheckBoxChekStatus(false);
        }

        // bind data to view
        userItemView.bind((User) getItem(position));
        return userItemView;
    }

    public void setUserData(List<User> userData){
        this.filteredUsers.clear();
        if(filteredUsers != null) {
            Collections.sort(this.filteredUsers);
            Collections.sort(this.userData);
            this.filteredUsers = userData;
            this.userData = userData;
        }
        notifyDataSetChanged();
    }

    //TODO: refresh adapter when user change profile pic
    @Subscribe
    public void usersLoadedEvent(UsersLoadedEvent event){
        setUserData(userDao.getUserList());
    }

    public static List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public static void addSelectedUser(User u){
        selectedUsers.add(u);
    }

    public static void removeSelectedUser(User u){
        selectedUsers.remove(u);
    }

    public static void clearSelectedUsers(){
        selectedUsers.clear();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final List<User> listForSearch = userData;

                int count = listForSearch.size();
                final ArrayList<User> newList = new ArrayList<>(count);

                for(User u : listForSearch){
                    if (u.getUsername().toLowerCase().contains(filterString)) {
                        newList.add(u);
                    }
                }
                results.values = newList;
                results.count = newList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredUsers = (ArrayList<User>) results.values;
                Collections.sort(filteredUsers);
                notifyDataSetChanged();
            }
        };
    }
}
