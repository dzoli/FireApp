package android.dmi.pmf.novica.fireapp.fragment;

import android.content.Intent;
import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.activity.MainActivity;
import android.dmi.pmf.novica.fireapp.dao.MessageDao;
import android.dmi.pmf.novica.fireapp.dao.UserDao;
import android.dmi.pmf.novica.fireapp.eventbus.OttoBus;
import android.dmi.pmf.novica.fireapp.eventbus.events.MessagesUpdatedEvent;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.dmi.pmf.novica.fireapp.model.Message;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Novica on 6/11/2017.
 */

@EFragment(R.layout.fragment_create_message)
public class CreateMessageFragment extends android.support.v4.app.Fragment {

    private static final int PHOTO_PICKER =  2;

    @ViewById
    EditText messageText;

    @ViewById
    Button sendMessage;

    @ViewById
    ImageButton photoPickerButton;

    @Bean
    MessageDao messageDao;

    @Bean
    UserDao userDao;

    @Bean
    OttoBus bus;

    public CreateMessageFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init(){
        enableButton();
    }

    public void initFor(Conversation conversation) {
        messageDao.initFor(conversation);
    }

    @Click
    void sendMessage(){
        final String text = messageText.getText().toString();


        final Message message = new Message(userDao.getCurrentUser(), text);

        messageDao.write(message);
        messageText.setText("");
    }

    @Click
    void photoPickerButton(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER);
    }

    @OnActivityResult(value = PHOTO_PICKER)
    public void imageSelected(int resultCode, Intent data){
        if(resultCode != RESULT_OK){
            return;
        }
        messageDao.writePhotoMessage(data.getData());
    }

    private void enableButton() {
        messageText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0){
                    sendMessage.setEnabled(true);
                }else{
                    sendMessage.setEnabled(false);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
