package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CreateRequestActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mCreateRequestRef;
    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextView tvCodeRequest = findViewById(R.id.tv_code_request);
        //Génération d'un id unique
        String key1 = "123456789";
        String key2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String codeRequest = String.format("#%s%s%s", "REQ", generateString(3, key1), generateString(3, key2));
        tvCodeRequest.setText(codeRequest);

        TextView tvDate  = (TextView) findViewById(R.id.tv_date_request);
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = formatter.format(currentTime);
        tvDate.setText(date);

        Button validateRequest = (Button) findViewById(R.id.btn_validate_request);
        validateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etDescription = (EditText) findViewById(R.id.et_description);
                final String description = etDescription.getText().toString();

                mCreateRequestRef = mDatabase.getReference("Request").child(mUid);
                mCreateRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RequestModel requestModel = new RequestModel(description, codeRequest, currentTime.getTime());
                        mCreateRequestRef.setValue(requestModel);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(CreateRequestActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    //Génération de code unique personnalisé :
    private String generateString(int length, String key) {
        char[] char1 = key.toCharArray();
        StringBuilder stringBuilder1 = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = char1[random.nextInt(char1.length)];
            stringBuilder1.append(c);
        }
        return stringBuilder1.toString();
    }

}
