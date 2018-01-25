package android.dmi.pmf.novica.fireapp.view;

import android.content.Context;
import android.dmi.pmf.novica.fireapp.R;
import android.dmi.pmf.novica.fireapp.model.Conversation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Novica on 6/7/2017.
 */

@EViewGroup(R.layout.item_view_conversation)
public class ConversationItemView extends LinearLayout{

    @ViewById
    TextView title;

    @ViewById
    TextView messagesNumber;

    @ViewById
    TextView usersNumber;

    public ConversationItemView(Context context) {
        super(context);
    }

    public void bind(Conversation conversation) {
        title.setText(conversation.getTitle());
        messagesNumber.setText(conversation.getMessages().size() + " Messages");
        usersNumber.setText(conversation.getUsers().size() + " Users");
    }

}
