package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter.ChatRecyclerAdapter;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ChatModel;


public class ChatActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private EditText message;
    private TextView tvDate, tvMessage, tvName;
    private DatabaseReference root;
    private String temp_key;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        pseudo = getIntent().getExtras().get("pseudo").toString();
        message = findViewById(R.id.et_msg_person);


        final Date date = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        final ArrayList<ChatModel> chatModels = new ArrayList<>();
        final RecyclerView listChat = findViewById(R.id.recycler_chat);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listChat.setLayoutManager(layoutManager);
        final ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(this, chatModels, pseudo);
        listChat.setAdapter(adapter);

        final DatabaseReference chat = FirebaseDatabase.getInstance().getReference("Chat");
        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModels.clear();
                for (DataSnapshot message: dataSnapshot.getChildren()) {
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


        fab = (FloatingActionButton) findViewById(R.id.fab_chat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(temp_key);

                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", pseudo);
                map2.put("msg", message.getText().toString());
                map2.put("date", sdf.format(date));

                message_root.updateChildren(map2);

                message.setText("");
            }
        });
    }
}
