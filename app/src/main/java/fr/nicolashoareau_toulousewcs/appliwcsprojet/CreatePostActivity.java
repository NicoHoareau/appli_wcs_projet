package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mCreateRequestRef;
    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextView dateText = findViewById(R.id.tv_date_post);
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = formatter.format(currentTime);
        dateText.setText(date);


        EditText etDescriptionPost = findViewById(R.id.et_desc_post);
    }
}
