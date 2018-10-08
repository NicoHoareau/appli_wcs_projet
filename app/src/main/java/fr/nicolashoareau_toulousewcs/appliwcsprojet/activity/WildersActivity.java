package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter.WildersAdapter;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.UserModel;

public class WildersActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mWilderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilders);

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WildersActivity.this, MenuActivity.class));
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        final ListView listWilders = findViewById(R.id.list_wilders);
        final ArrayList<UserModel> userModels = new ArrayList<>();
        final WildersAdapter adapter = new WildersAdapter(WildersActivity.this, userModels);


        mWilderRef = mDatabase.getReference("User");
        mWilderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModels.clear();
                for (DataSnapshot wildersSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = wildersSnapshot.child("Profil").getValue(UserModel.class);
                    userModels.add(userModel);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listWilders.setAdapter(adapter);

        final Spinner spinnerLanguage = findViewById(R.id.spinner);
        //Utiliser un Adapter pour rentrer les données du spinner_array
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.spinnerLanguage, android.R.layout.simple_spinner_item);
        //Spécifier le layout à utiliser pour afficher les données
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Appliquer l'adapter au spinner
        spinnerLanguage.setAdapter(adapter2);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = spinnerLanguage.getItemAtPosition(position).toString();
                if (selection.matches("Java")) {
                    mWilderRef = mDatabase.getReference("User");
                    mWilderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModels.clear();
                            for (DataSnapshot wildersSnapshot : dataSnapshot.getChildren()) {
                                if (wildersSnapshot.child("Profil").child("language").getValue(String.class).equals("Java")) {
                                    UserModel userModel = wildersSnapshot.child("Profil").getValue(UserModel.class);
                                    userModels.add(userModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listWilders.setAdapter(adapter);
                }
                if (selection.matches("Javascript")) {
                    mWilderRef = mDatabase.getReference("User");
                    mWilderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModels.clear();
                            for (DataSnapshot wildersSnapshot : dataSnapshot.getChildren()) {
                                if (wildersSnapshot.child("Profil").child("language").getValue(String.class).equals("Javascript")) {
                                    UserModel userModel = wildersSnapshot.child("Profil").getValue(UserModel.class);
                                    userModels.add(userModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listWilders.setAdapter(adapter);
                }
                if (selection.matches("Sélectionner un language :")) {
                    mWilderRef = mDatabase.getReference("User");
                    mWilderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModels.clear();
                            for (DataSnapshot wildersSnapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = wildersSnapshot.child("Profil").getValue(UserModel.class);
                                userModels.add(userModel);

                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listWilders.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView ivStore = findViewById(R.id.iv_store);
        ivStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(WildersActivity.this);
                LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mView = li.inflate(R.layout.store_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                //Interactions avec les boutons :
                Button btnClose = mView.findViewById(R.id.btn_cancel);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                ImageView googlePlay = mView.findViewById(R.id.iv_play_store);
                googlePlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getString(R.string.url_playstore);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                dialog.show();
            }
        });
    }
}
