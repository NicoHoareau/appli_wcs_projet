package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.RequestModel;

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

        //Génération d'un id unique
        String key1 = getString(R.string.num);
        String key2 = getString(R.string.letters);
        final String codeRequest = String.format("%s%s%s", getString(R.string.req), generateString(3, key1), generateString(3, key2));

        TextView tvDate = findViewById(R.id.tv_date_request);
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = formatter.format(currentTime);
        tvDate.setText(date);

        final EditText etDescription = findViewById(R.id.et_description);

        Button validateRequest = findViewById(R.id.btn_validate_request);
        validateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = etDescription.getText().toString();
                if (description == null || description.isEmpty()) {
                    Toast.makeText(CreateRequestActivity.this, R.string.add_request, Toast.LENGTH_SHORT).show();
                } else {
                    mCreateRequestRef = mDatabase.getReference("Request").child(codeRequest);
                    // Read from the database
                    mCreateRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            RequestModel requestModel = new RequestModel(description, codeRequest, currentTime.getTime(), false, mUid);
                            mCreateRequestRef.setValue(requestModel);

                            Intent intent = new Intent(CreateRequestActivity.this, MenuActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
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
