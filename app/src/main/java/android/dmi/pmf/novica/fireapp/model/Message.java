package android.dmi.pmf.novica.fireapp.model;


import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Novica on 5/13/2017.
 */

public class Message extends BaseModel implements Serializable, Comparable<Message> {

    private String text;

    private Date timestamp;

    private User user;

    private String photoUrl;

    public Message() {
        this.timestamp = new Date();
    }

    public Message(User user, String text) {
        this.text = text;
        this.user = user;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("text='").append(text).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int compareTo(@NonNull Message o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
