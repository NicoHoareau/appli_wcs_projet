package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

    private EditText mEditPseudo;
    private ImageView mImgProfilPic;
    private RadioButton mJava;
    private RadioButton mJs;
    private String mLanguage;
    private String mLink;
    private RadioButton mFevrier;
    private RadioButton septembre;
    private EditText mEditAnnee;
    private String mPromo;
    private String mUrlSave;
    private Uri mUri = null;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profil);

        ImageView btnCamera = findViewById(R.id.btn_camera);
        ImageView btnGallery = findViewById(R.id.btn_gallery);
        Button btnValidModif = findViewById(R.id.btn_valid_modif);

        mImgProfilPic = findViewById(R.id.iv_profil_pic);
        mEditPseudo = findViewById(R.id.et_enter_pseudo);
        mJava = findViewById(R.id.radiobutton_java);
        mJs = findViewById(R.id.radiobutton_js);
        mFevrier = findViewById(R.id.radiobutton_fev);
        septembre = findViewById(R.id.radiobutton_sept);
        mEditAnnee = findViewById(R.id.et_year);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Rappel du pseudo et de la profilPic sur la page si existant
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        btnValidModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mEditPseudo.getText().toString();
                if (firstName.isEmpty()) {
                    Toast.makeText(CreateProfilActivity.this, "Entrez un pseudo", Toast.LENGTH_SHORT).show();
                } else {
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

        if (mUri == null) {

            if (mLink != null) {
                String profilPic = mLink;
                if (mJava.isChecked()) {
                    mJs.setChecked(false);
                    mLanguage = "Java";
                } else if (mJs.isChecked()) {
                    mJava.setChecked(false);
                    mLanguage = "Javascript";
                }
                String annee = mEditAnnee.getText().toString();
                if (mFevrier.isChecked()) {
                    septembre.setChecked(false);
                    mPromo = "Février " + annee;
                } else if (septembre.isChecked()) {
                    mFevrier.setChecked(false);
                    mPromo = "Septembre " + annee;
                }
                UserModel userModel = new UserModel(pseudo, profilPic, mLanguage, mPromo);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).child("Profil").setValue(userModel);
            } else {
                String profilPic = mUrlSave;
                if (mJava.isChecked()) {
                    mJs.setChecked(false);
                    mLanguage = "Java";
                } else if (mJs.isChecked()) {
                    mJava.setChecked(false);
                    mLanguage = "Javascript";
                }
                String annee = mEditAnnee.getText().toString();
                if (mFevrier.isChecked()) {
                    septembre.setChecked(false);
                    mPromo = "Février " + annee;
                } else if (septembre.isChecked()) {
                    mFevrier.setChecked(false);
                    mPromo = "Septembre " + annee;
                }
                UserModel userModel = new UserModel(pseudo, profilPic, mLanguage, mPromo);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).child("Profil").setValue(userModel);
            }
        } else {
            StorageReference filePath = mStorageReference.child("profilPicture").child(mUri.getLastPathSegment());
            filePath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String profilPic = downloadUrl.toString();
                    if (mJava.isChecked()) {
                        mJs.setChecked(false);
                        mLanguage = "Java";
                    } else if (mJs.isChecked()) {
                        mJava.setChecked(false);
                        mLanguage = "Javascript";
                    }
                    String annee = mEditAnnee.getText().toString();
                    if (mFevrier.isChecked()) {
                        septembre.setChecked(false);
                        mPromo = "Février " + annee;
                    } else if (septembre.isChecked()) {
                        mFevrier.setChecked(false);
                        mPromo = "Septembre " + annee;
                    }
                    UserModel userModel = new UserModel(pseudo, profilPic, mLanguage, mPromo);
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
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Glide.with(CreateProfilActivity.this).load(mUri).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    mUri = imageReturnedIntent.getData();
                    Glide.with(CreateProfilActivity.this).load(mUri).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);
                }
                break;
        }
    }

}