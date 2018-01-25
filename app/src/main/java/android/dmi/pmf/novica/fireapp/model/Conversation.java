package android.dmi.pmf.novica.fireapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novica on 6/7/2017.
 */

public class Conversation extends BaseModel implements Serializable{

    private String title;
    private Map<String, Message> messages = new HashMap<>();
    private Map<String, User> users = new HashMap<>(); //user who creates conversation is default first user of new Conversation!


    public Conversation() {
    }
    public Conversation(String id, String title, Map<String, Message> messages, User currUser) {
        super(id);
        this.title = title;
        this.messages = messages;
        users = new HashMap<>();
        users.put(currUser.getId().toString(), currUser);
    }
    public Conversation(String id, String title, Map<String, Message> messages, Map<String, User> users) {
        super(id);
        this.title = title;
        this.messages = messages;
        this.users = users;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Conversation{");
        sb.append("title='").append(title).append('\'');
        sb.append(", messages=").append(messages);
        sb.append(", users=").append(users);
        sb.append('}');
        return sb.toString();
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}