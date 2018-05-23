package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);



        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference pathID = mDatabase.getReference("User").child(uid);

        pathID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String urlSave = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                ImageView profilPic = findViewById(R.id.iv_profilPic);
                Glide.with(getApplicationContext()).load(urlSave)
                        .apply(RequestOptions.circleCropTransform()).into(profilPic);

                String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);
                TextView tvPseudo = findViewById(R.id.tv_pseudo);
                tvPseudo.setText(pseudo);

                String language = dataSnapshot.child("Profil").child("language").getValue(String.class);
                TextView tvLanguage = findViewById(R.id.tv_language);
                tvLanguage.setText(language);

                String promo = dataSnapshot.child("Profil").child("promo").getValue(String.class);
                TextView tvPromo = findViewById(R.id.tv_promo);
                tvPromo.setText(promo);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
