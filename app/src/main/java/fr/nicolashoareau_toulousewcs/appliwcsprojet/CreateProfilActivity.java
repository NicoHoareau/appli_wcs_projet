package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateProfilActivity extends AppCompatActivity {

    EditText mEditPseudo;
    private Uri mUri = null;
    ImageView mImgProfilPic;
    RadioButton java;
    RadioButton js;
    String language;
    RadioButton fevrier;
    RadioButton septembre;
    EditText editAnnee;
    String promo;
    String mLink;
    String mUrlSave;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profil);


        Button btnCamera;
        Button btnGallery;
        Button btnLink;
        final Button btnOK;
        final EditText edLink;

        Button btnValidModif;
        String uid;

        edLink = findViewById(R.id.et_link);
        btnCamera = findViewById(R.id.btn_camera);
        btnGallery = findViewById(R.id.btn_gallery);
        btnLink = findViewById(R.id.btn_link);
        btnOK = findViewById(R.id.btn_ok);
        mImgProfilPic = findViewById(R.id.iv_profil_pic);
        mEditPseudo = findViewById(R.id.et_enter_pseudo);
        java = findViewById(R.id.radiobutton_java);
        js = findViewById(R.id.radiobutton_js);
        fevrier = findViewById(R.id.radiobutton_fev);
        septembre = findViewById(R.id.radiobutton_sept);
        editAnnee = findViewById(R.id.et_year);
        btnValidModif = findViewById(R.id.btn_valid_modif);


        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Rappel du pseudo et de la profilPic sur la page si existant
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference pathID = mDatabase.getReference("User").child(uid);

        pathID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Profil").child("profilPic").getValue() != null)) {
                    mUrlSave = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                    Glide.with(getApplicationContext()).load(mUrlSave)
                            .apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);
                }

                if ((dataSnapshot.child("Profil").child("pseudo").getValue() != null)) {
                    String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);

                    mEditPseudo.setText(pseudo);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mUri = CameraUtils.getOutputMediaFileUri(CreateProfilActivity.this);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePicture, 0);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edLink.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.VISIBLE);

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLink = edLink.getText().toString();
                Glide.with(CreateProfilActivity.this).load(mLink).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);
                edLink.setVisibility(View.GONE);
                btnOK.setVisibility(View.GONE);
            }
        });

        btnValidModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mEditPseudo.getText().toString();
                if (firstName.isEmpty()) {
                    Toast.makeText(CreateProfilActivity.this, "Entrez un pseudo", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveUserModel();

                    Intent intentHome = new Intent(CreateProfilActivity.this, MenuActivity.class);
                    startActivity(intentHome);
                    finish();
                }
            }
        });
    }

    //methode qui envois les données sur firebase
    private void saveUserModel() {
        final String pseudo = mEditPseudo.getText().toString();

        if (mUri == null){

            if (mLink != null){
                String profilPic = mLink;
                if (java.isChecked()) {
                    js.setChecked(false);
                    language = "Java";
                }
                else if (js.isChecked()) {
                    java.setChecked(false);
                    language = "Javascript";
                }
                String annee = editAnnee.getText().toString();
                if (fevrier.isChecked()) {
                    septembre.setChecked(false);
                    promo = "Février " + annee;
                }
                else if (septembre.isChecked()) {
                    fevrier.setChecked(false);
                    promo = "Septembre " + annee;
                }
                UserModel userModel = new UserModel(pseudo, profilPic, language, promo);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).child("Profil").setValue(userModel);
            }
            else{
                String profilPic = mUrlSave;
                if (java.isChecked()) {
                    js.setChecked(false);
                    language = "Java";
                }
                else if (js.isChecked()) {
                    java.setChecked(false);
                    language = "Javascript";
                }
                String annee = editAnnee.getText().toString();
                if (fevrier.isChecked()) {
                    septembre.setChecked(false);
                    promo = "Février " + annee;
                }
                else if (septembre.isChecked()) {
                    fevrier.setChecked(false);
                    promo = "Septembre " + annee;
                }
                UserModel userModel = new UserModel(pseudo, profilPic, language, promo);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).child("Profil").setValue(userModel);
            }


        }
        else {
            StorageReference filePath = mStorageReference.child("profilPicture").child(mUri.getLastPathSegment());
            filePath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String profilPic = downloadUrl.toString();
                    if (java.isChecked()) {
                        js.setChecked(false);
                        language = "Java";
                    }
                    else if (js.isChecked()) {
                        java.setChecked(false);
                        language = "Javascript";
                    }
                    String annee = editAnnee.getText().toString();
                    if (fevrier.isChecked()) {
                        septembre.setChecked(false);
                        promo = "Février " + annee;
                    }
                    else if (septembre.isChecked()) {
                        fevrier.setChecked(false);
                        promo = "Septembre " + annee;
                    }
                    UserModel userModel = new UserModel(pseudo, profilPic, language, promo);
                    FirebaseUser user = mAuth.getCurrentUser();
                    mDatabaseReference = mDatabase.getReference("User");
                    mDatabaseReference.child(user.getUid()).child("Profil").setValue(userModel);
                }
            });
        }



    }

    //Methode qui convertis les photo de l'appareil et de la gallerie en Uri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    Glide.with(CreateProfilActivity.this).load(mUri).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);

                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    mUri = imageReturnedIntent.getData();
                    Glide.with(CreateProfilActivity.this).load(mUri).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);

                }
                break;
        }
    }

}