package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter.ActualityAdapter;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ActualityModel;

public class ProfilActivity extends AppCompatActivity {

    public final static int GALLERY = 123;
    public final static int GALLERY_MODIF_PHOTO = 798;
    public final static int APP_PHOTO = 456;
    public final static int APP_MODIF_PHOTO = 333;

    private FirebaseDatabase mDatabase;
    private String mUid, mCurrentPhotoPath, mKeyPostModif;
    private DatabaseReference mPathID, mPostID, mProfileRef;
    private Uri mFileUri = null;
    private String mGetImageUrl = "";
    private ImageView mProfilPix;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, MenuActivity.class));
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mPathID = FirebaseDatabase.getInstance().getReference("User").child(mUid);
        mPostID = FirebaseDatabase.getInstance().getReference("Post");

        mPathID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String urlSave = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                mProfilPix = findViewById(R.id.iv_profil_pic);
                Glide.with(getApplicationContext()).load(urlSave)
                        .apply(RequestOptions.circleCropTransform()).into(mProfilPix);

                if (urlSave == null || urlSave.isEmpty()) {
                    Glide.with(getApplicationContext()).load(R.drawable.logo_user2).apply(RequestOptions.centerCropTransform()).into(mProfilPix);
                }

                String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);
                TextView tvPseudo = findViewById(R.id.tv_pseudo);
                tvPseudo.setText(pseudo);

                String language = dataSnapshot.child("Profil").child("language").getValue(String.class);
                ImageView logoLanguage = findViewById(R.id.iv_logo_language);
                if (language.equals("Java")) {
                    Glide.with(getApplicationContext()).load(R.drawable.java_logo).into(logoLanguage);
                } else {
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

        final ListView listProfile = findViewById(R.id.listview_profile);
        final ArrayList<ActualityModel> actualityModelArrayList = new ArrayList<>();
        final ActualityAdapter adapter = new ActualityAdapter(ProfilActivity.this, actualityModelArrayList);
        listProfile.setAdapter(adapter);
        mProfileRef = mDatabase.getReference("Post");
        mProfileRef.orderByChild("datePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actualityModelArrayList.clear();
                for (final DataSnapshot listActualitySnapshot : dataSnapshot.getChildren()) {
                    final ActualityModel actualityModel = listActualitySnapshot.getValue(ActualityModel.class);
                    String idUserPost = actualityModel.getIdUser();
                    if (idUserPost.equals(mUid)) {
                        actualityModelArrayList.add(actualityModel);
                    }

                    listProfile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                            mKeyPostModif = listActualitySnapshot.getKey();
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfilActivity.this);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(true)
                                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            DatabaseReference databaseReference = mProfileRef.child(mKeyPostModif);
                                            databaseReference.removeValue();
                                            dialog.cancel();
                                            Toast.makeText(ProfilActivity.this, R.string.post_delete, Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .setNegativeButton(R.string.edit_post, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View view = inflater.inflate(R.layout.modify_post_dialog, null);
                                            builder.setView(view);
                                            final AlertDialog dialogModify = builder.create();
                                            //contains modif post
                                            final ImageView ivPost = view.findViewById(R.id.iv_take_pix);
                                            final EditText etPost = view.findViewById(R.id.et_edit_description);
                                            TextView tvdate = view.findViewById(R.id.tv_date);
                                            Button btSendModif = view.findViewById(R.id.btn_send_modif);

                                            String oldImageUrl = actualityModel.getUrlPhoto();
                                            String oldDesc = actualityModel.getDescription();
                                            Glide.with(getApplicationContext()).load(oldImageUrl).apply(RequestOptions.centerCropTransform()).into(ivPost);
                                            etPost.setHint(oldDesc);
                                            final Date currentTime = Calendar.getInstance().getTime();
                                            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                            String date = formatter.format(currentTime);
                                            tvdate.setText(date);
                                            final long dateLong = currentTime.getTime();
                                            ivPost.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    modifImagePost();

                                                }
                                            });
                                            mProfileRef.child(mKeyPostModif).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final ActualityModel actualityModel2 = dataSnapshot.getValue(ActualityModel.class);
                                                    String newImageUrl = actualityModel2.getUrlPhoto();
                                                    Glide.with(getApplicationContext()).load(newImageUrl).into(ivPost);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            btSendModif.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mProfileRef.child(mKeyPostModif).child("description").setValue(etPost.getText().toString());
                                                    mProfileRef.child(mKeyPostModif).child("datePost").setValue(dateLong);
                                                    dialogModify.cancel();
                                                }
                                            });


                                            dialogModify.show();

                                        }
                                    });

                            // create alert dialog
                            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                            return true;
                        }
                    });
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
                Intent intent = new Intent(ProfilActivity.this, ConnectionActivity.class);
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
        lp.setMargins(16, 0, 16, 0);
        input.setLayoutParams(lp);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
        builder.setTitle(R.string.edit_pseudo)
                .setView(input)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
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
                                    for (DataSnapshot post : dataSnapshot.getChildren()) {//pour chaque enfant
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
        builder.setTitle(R.string.edit_pix)
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

    private void modifImagePost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
        builder.setTitle(R.string.edit_pix)
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
                                startActivityForResult(intent, APP_MODIF_PHOTO);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.galery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_MODIF_PHOTO);
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


            case APP_MODIF_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        mGetImageUrl = mFileUri.getPath();
                    }
                    saveModifImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GALLERY_MODIF_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        mFileUri = data.getData();
                        mGetImageUrl = mFileUri.getPath();
                    }
                    saveModifImage();
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
                            for (DataSnapshot post : dataSnapshot.getChildren()) {
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

    private void saveModifImage() {
        if (!mGetImageUrl.equals("") && mGetImageUrl != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("Post").child(mUid).child("avatar.jpg");
            ref.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference("Post")
                            .child(mKeyPostModif).child("urlPhoto").setValue(downloadUri.toString());


                }
            });
        }
    }

}
