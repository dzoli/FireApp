package android.dmi.pmf.novica.fireapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Novica on 6/6/2017.
 */

public class User extends BaseModel implements Serializable, Comparable<User>{

    private String username;
    private String email;
    private String photoUrl;
    private String description;

    public User(String id, String username, String email) {
        super(id);
        this.username = username;
        this.email = email;
    }

    public User() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) { //po useru se dobija da li je odlazna ili dolazna poruka
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

//        if (username != null ? !username.equals(user.username) : user.username != null)
//            return false;

        return getId().equals(user.getId());
    }

    @Override
    public int compareTo(@NonNull User o) {
        return this.getUsername().compareTo(o.getUsername());
    }
}
