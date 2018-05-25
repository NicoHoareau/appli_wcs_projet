package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements RequestFragment.OnFragmentInteractionListener, ValidationRequestFragment.OnFragmentInteractionListener, ActualityFragment.OnFragmentInteractionListener{

    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView deco = findViewById(R.id.btn_deco);
        deco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ConnexionActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });



        final ImageView profile = findViewById(R.id.iv_profile);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference pathID = mDatabase.getReference("User").child(uid);

        pathID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Profil").child("profilPic").getValue() != null)) {
                    String mUrlSave = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                    Glide.with(getApplicationContext()).load(mUrlSave)
                            .apply(RequestOptions.circleCropTransform()).into(profile);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToProfile = new Intent(MenuActivity.this, ProfilActivity.class);
                MenuActivity.this.startActivity(goToProfile);
            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab3));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new fr.nicolashoareau_toulousewcs.appliwcsprojet.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userID = mDatabase.getReference("User").child(uid);

        userID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Profil").child("pseudo").getValue() != null)) {
                    final String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);

                    ImageView imageIconWCS = findViewById(R.id.img_icon);
                    imageIconWCS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
                            intent.putExtra("pseudo", pseudo);
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }


}
