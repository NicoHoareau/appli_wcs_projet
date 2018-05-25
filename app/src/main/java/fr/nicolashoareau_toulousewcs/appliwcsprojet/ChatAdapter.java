package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ChatModel> {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatModels) {
        super(context, 0, chatModels);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ChatModel chatModel = (ChatModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);
        }




        return convertView;
    }
}
