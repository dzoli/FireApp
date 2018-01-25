package android.dmi.pmf.novica.fireapp.view;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.adapter.UserAdapter;
import android.dmi.pmf.novica.fireapp.model.User;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Novica on 6/8/2017.
 */

@EViewGroup(R.layout.item_view_user)
public class UserItemView extends LinearLayout {

    @ViewById
    SimpleDraweeView userPhoto;

    @ViewById
    TextView username;

    @ViewById
    TextView emailAddress;

    @ViewById
    CheckBox checkBoxSelected;


    public UserItemView(Context context) {
        super(context);
    }

    public void bind(final User user){
        username.setText(user.getUsername());
        emailAddress.setText(user.getEmail());

        if(user.getPhotoUrl() == null){
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(3)
                    .bold()
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(usernameFirstLetter(user), Color.parseColor("#2E7D32"), 60);
            userPhoto.setImageDrawable(drawable);
        }else{
            userPhoto.setImageURI(user.getPhotoUrl());
        }

        checkBoxSelected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxSelected.isChecked()){
                    UserAdapter.addSelectedUser(user);
                }else{
                    UserAdapter.removeSelectedUser(user);
                }
            }
        });
    }

    public void setCheckBoxChekStatus(boolean status){
        checkBoxSelected.setChecked(status);
    }

    private String usernameFirstLetter(User user) {
        if(user != null)
            return user.getUsername().substring(0,1);
        else
            return "n";
    }
}
