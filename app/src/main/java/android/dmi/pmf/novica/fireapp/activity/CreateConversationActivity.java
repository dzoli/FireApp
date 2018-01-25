package android.dmi.pmf.novica.fireapp.activity;

import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.adapter.UserAdapter;
import android.dmi.pmf.novica.fireapp.dao.ConversationDao;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.model.User;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_create_conversation)
public class CreateConversationActivity extends AppCompatActivity {

    @ViewById
    ListView listViewUsers;

    @ViewById
    Button createConversation;

    @ViewById
    EditText conversationName;

    @Bean
    UserAdapter userAdapter;

    @Bean
    UserDao userDao;

    @Bean
    ConversationDao conversationDao;

    @AfterViews
    public void init(){
        enableButton();
        List<User> userData = userDao.getUserList();
        userData.remove(userDao.getCurrentUser()); // current user is not visible

        userAdapter.setUserData(userData); //solution 2. is to use otto bus
        listViewUsers.setTextFilterEnabled(true);
        listViewUsers.setAdapter(userAdapter);
    }

    @Click
    void createConversation(){
        final String name = conversationName.getText().toString();
        final Conversation newConversation = new Conversation();

        newConversation.setId(null);
        newConversation.setTitle(name);
        newConversation.setUsers(generateUsersMap());
        conversationDao.write(newConversation);
        UserAdapter.clearSelectedUsers();
        finish();
    }

    private Map<String,User> generateUsersMap(){
        Map<String,User> retVal = new HashMap<>();

        retVal.put(userDao.getCurrentUser().getId(), userDao.getCurrentUser()); //default user
        for(User u : UserAdapter.getSelectedUsers()){ //add checked users
            retVal.put(u.getId(),u);
        }
        return retVal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void enableButton(){
        conversationName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0){
                    createConversation.setEnabled(true);
                }else{
                    createConversation.setEnabled(false);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

//    @ItemClick
//    void listViewUsers(User user){
////        Toast.makeText(this, "user" + position, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "user" + user.getUsername(), Toast.LENGTH_SHORT).show();
//
//    }
}