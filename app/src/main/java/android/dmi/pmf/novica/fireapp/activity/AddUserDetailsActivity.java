package android.dmi.pmf.novica.fireapp.activity;

import android.content.Intent;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.model.User;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.dmi.pmf.novica.fireapp.R;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_add_user_details)
public class AddUserDetailsActivity extends AppCompatActivity {
    static final int PHOTO_PICKER = 11;

    @ViewById
    TextView username;

    @ViewById
    TextView email;

//    @ViewById
//    EditText description;

    @ViewById
    SimpleDraweeView userImage;

    @Bean
    UserDao userDao;

    @AfterViews
    public void init(){
        username.setText(userDao.getCurrentUser().getUsername());
        email.setText(userDao.getCurrentUser().getEmail());
        if(userDao.getCurrentUser().getPhotoUrl() == null){
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                        .withBorder(3)
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .toUpperCase()
                    .endConfig()
                    .buildRect(usernameFirstLetter(userDao.getCurrentUser()), Color.parseColor("#2E7D32"));
            userImage.setImageDrawable(drawable);
        }else{
            userImage.setImageURI(userDao.getCurrentUser().getPhotoUrl());
        }
    }

    @Click
    public void selectPhotoButton(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER);
    }

    @OnActivityResult(value = PHOTO_PICKER)
    public void pictureIsTaken(int resultCode, Intent data){
        if (resultCode != RESULT_OK) {
            return;
        }
        userDao.getCurrentUser().setPhotoUrl(data.getDataString());
        userDao.updateUser(userDao.getCurrentUser());
        init();
//        Toast.makeText(this, " -data uri" + data.getData().getLastPathSegment().toString(), Toast.LENGTH_SHORT).show();
    }

    @Click
    void doneUpdate(){
        finish();
    }

    @NonNull
    private String usernameFirstLetter(User user) {
        return user.getUsername().substring(0,1);
    }
}
