package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ProfilActivity extends AppCompatActivity {

    public final static int GALLERY = 123;
    public final static int APP_PHOTO = 456;

    private String mUid;
    private DatabaseReference mPathID;
    private DatabaseReference mPostID;
    private Uri mFileUri = null;
    private String mGetImageUrl = "";
    private ImageView mProfilPix;
    private String mCurrentPhotoPath;
    private FirebaseAuth mAuth;

    FirebaseDatabase mDatabase;
    DatabaseReference mProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mPathID = FirebaseDatabase.getInstance().getReference("User").child(mUid);
        mPostID = FirebaseDatabase.getInstance().getReference("Post");

        mPathID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String urlSave = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                mProfilPix = findViewById(R.id.iv_profilPic);
                Glide.with(getApplicationContext()).load(urlSave)
                        .apply(RequestOptions.circleCropTransform()).into(mProfilPix);

                String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);
                TextView tvPseudo = findViewById(R.id.tv_pseudo);
                tvPseudo.setText(pseudo);

                String language = dataSnapshot.child("Profil").child("language").getValue(String.class);
                ImageView logoLanguage = findViewById(R.id.iv_logo_language);
                if (language.equals("Java")) {
                    Glide.with(getApplicationContext()).load(R.drawable.java_logo).into(logoLanguage);
                }
                else {
                    Glide.with(getApplicationContext()).load(R.drawable.js_logo).into(logoLanguage);
                }


                String promo = dataSnapshot.child("Profil").child("promo").getValue(String.class);
                TextView tvPromo = findViewById(R.id.tv_promo);
                tvPromo.setText(promo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ListView listProfile = findViewById(R.id.listview_profile);
        final ArrayList<ActualityModel> actualityModelArrayList = new ArrayList<>();
        final ActualityAdapter adapter = new ActualityAdapter(ProfilActivity.this, actualityModelArrayList);
        listProfile.setAdapter(adapter);
        mProfileRef = mDatabase.getReference("Post");
        mProfileRef.orderByChild("datePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actualityModelArrayList.clear();
                for (DataSnapshot listActualitySnapshot : dataSnapshot.getChildren()){
                    ActualityModel actualityModel = listActualitySnapshot.getValue(ActualityModel.class);
                    String idUserPost = actualityModel.getIdUser();
                    if (idUserPost.equals(mUid)){
                        actualityModelArrayList.add(actualityModel);
                    }
                }
                adapter.notifyDataSetChanged();
                Collections.reverse(actualityModelArrayList);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_name:
                editName();
                return true;
            case R.id.edit_pix:
                editPix();
                return true;
            case R.id.disconnect:
                mAuth.signOut();
                Intent intent = new Intent(ProfilActivity.this, ConnexionActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editName() {
        final EditText input = new EditText(ProfilActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
        builder.setTitle(R.string.edit_my_name)
                .setView(input)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText() != null) {
                            final String pseudo = input.getText().toString();
                            mPathID.child("Profil").child("pseudo").setValue(pseudo);

                            //on cherche dans post, classé par idUser = mUid
                            final DatabaseReference postRef = mDatabase.getReference("Post");
                            postRef.orderByChild("idUser").equalTo(mUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot post : dataSnapshot.getChildren()){//pour chaque enfant
                                        ActualityModel actualityModel = post.getValue(ActualityModel.class);
                                        actualityModel.setPseudoUser(pseudo);//on créé un nouveau de la nouvelle valeur modif
                                        postRef.child(post.getKey()).setValue(actualityModel);//on réinjecte le model modifié pour la clé correspondante

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }
                })
                .show();
    }

    private void editPix() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
        builder.setTitle(R.string.edit_my_pix)
                .setMessage(R.string.select_source)
                .setPositiveButton(R.string.app_photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {

                            }

                            if (photoFile != null) {
                                mFileUri = FileProvider.getUriForFile(ProfilActivity.this,
                                        "fr.nicolashoareau_toulousewcs.appliwcsprojet.fileprovider",
                                        photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                                startActivityForResult(intent, APP_PHOTO);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.galery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY);
                    }
                })
                .show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case APP_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        mGetImageUrl = mFileUri.getPath();
                    }
                    saveCaptureImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GALLERY:
                try {
                    if (resultCode == RESULT_OK) {
                        mFileUri = data.getData();
                        mGetImageUrl = mFileUri.getPath();
                    }
                    saveCaptureImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void saveCaptureImage() {
        if (!mGetImageUrl.equals("") && mGetImageUrl != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(mUid).child("avatar.jpg");
            ref.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(mUid).child("Profil").child("profilPic").setValue(downloadUri.toString());

                    final DatabaseReference postRef = mDatabase.getReference("Post");
                    postRef.orderByChild("idUser").equalTo(mUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot post : dataSnapshot.getChildren()){
                                ActualityModel actualityModel = post.getValue(ActualityModel.class);
                                actualityModel.setUrlPhotoUser(downloadUri.toString());
                                postRef.child(post.getKey()).setValue(actualityModel);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

}
