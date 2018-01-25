package android.dmi.pmf.novica.fireapp.view;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.model.Message;
import android.dmi.pmf.novica.fireapp.model.User;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Novica on 6/11/2017.
 */

@EViewGroup(R.layout.item_view_message)
public class MessageItemView extends LinearLayout{

    @ViewById
    TextView messageText;

    @ViewById
    SimpleDraweeView photoMessage;

    @ViewById
    TextView messageTime;

    @ViewById
    TextView messageUser;

    @ViewById
    SimpleDraweeView userPhoto;


    public MessageItemView(Context context) {
        super(context);
    }

    public void bind(Message message){
        messageTime.setText(DateFormat.format("HH:mm dd MMM", message.getTimestamp()));
        messageUser.setText(message.getUser().getUsername());
//        userPhoto.setImageURI(Uri.parse("https://safe-in-cloud.com/images/user_male_2.png"));
        if(message.getUser().getPhotoUrl() == null){
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(3)
                    .bold()
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(usernameFirstLetter(message.getUser()), Color.parseColor("#2E7D32"), 60);
            userPhoto.setImageDrawable(drawable);
        }else{
            userPhoto.setImageURI(message.getUser().getPhotoUrl());
        }
        boolean isPhoto = message.getPhotoUrl() != null;

        if (isPhoto) {
            messageText.setVisibility(View.GONE);
            photoMessage.setVisibility(View.VISIBLE);
            photoMessage.setImageURI(message.getPhotoUrl());
        }else{
            messageText.setVisibility(View.VISIBLE);
            photoMessage.setVisibility(View.GONE);
            messageText.setText(message.getText());
        }
    }

    @NonNull
    private String usernameFirstLetter(User user) {
        return user.getUsername().substring(0,1);
    }

}
