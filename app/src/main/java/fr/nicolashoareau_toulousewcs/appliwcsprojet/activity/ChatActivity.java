package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter.ChatRecyclerAdapter;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ChatModel;


public class ChatActivity extends AppCompatActivity {

    FloatingActionButton mFabAddMessage;
    private EditText mMessage;
    private String mTempKey;
    private String mPseudo;

    public final static String PSEUDO = "pseudo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mPseudo = getIntent().getExtras().get(PSEUDO).toString();
        mMessage = findViewById(R.id.et_msg);

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MenuActivity.class));
            }
        });

        final ArrayList<ChatModel> chatModels = new ArrayList<>();
        final RecyclerView listChat = findViewById(R.id.recycler_chat);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listChat.setLayoutManager(layoutManager);
        final ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(this, chatModels, mPseudo);
        listChat.setAdapter(adapter);

        final DatabaseReference chat = FirebaseDatabase.getInstance().getReference("Chat");
        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModels.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()) {
                    ChatModel chatModel = message.getValue(ChatModel.class);
                    chatModels.add(new ChatModel(chatModel.getName(), chatModel.getMsg()));
                }
                listChat.smoothScrollToPosition(chatModels.size() - 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Chat");

        mFabAddMessage = findViewById(R.id.fab_chat);
        mFabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                mTempKey = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(mTempKey);

                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", mPseudo);
                map2.put("msg", mMessage.getText().toString());

                message_root.updateChildren(map2);

                mMessage.setText("");
            }
        });
    }
}
