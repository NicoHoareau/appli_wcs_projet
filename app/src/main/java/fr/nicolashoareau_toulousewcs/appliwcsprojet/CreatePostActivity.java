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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mCreatePostRef;
    private String mUid;

    private ImageView mAddPhoto;
    private String mGetImageUrl = "";
    private String mCurrentPhotoPath;
    private Uri mFileUri = null;
    public final static int GALLERY = 123;
    public final static int APP_PHOTO = 456;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAddPhoto = findViewById(R.id.iv_add_photo);
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
                builder.setTitle("aaa").setMessage("aaaa")
                        .setPositiveButton("camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dispatchTakePictureIntent();
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY);
                            }
                        })
                        .show();
            }
        });

        final EditText etDescriptionPost = findViewById(R.id.et_desc_post);

        TextView dateText = findViewById(R.id.tv_date_post);
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = formatter.format(currentTime);
        dateText.setText(date);
        final long dateLong = currentTime.getTime();

        Button btnValidatePost = findViewById(R.id.btn_validate_post);
        btnValidatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textDescriptionPost = etDescriptionPost.getText().toString();
                mCreatePostRef = mDatabase.getReference("Post");
                mCreatePostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!mGetImageUrl.equals("") && mGetImageUrl != null) {
                            StorageReference avatarRef = FirebaseStorage.getInstance().getReference("PhotoPost");
                            avatarRef.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                                    final String avatarUrl = downloadUri.toString();

                                    //todo : NAME
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference userRef = mDatabase.getReference("User").child(mUid).child("Profil");
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                            String pseudo =  userModel.getPseudo();
                                            String urlPhotoUser = userModel.getProfilPic();
                                            ActualityModel actualityModel = new ActualityModel(pseudo, textDescriptionPost, avatarUrl, urlPhotoUser, dateLong);
                                            mCreatePostRef.push().setValue(actualityModel);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(CreatePostActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = CreatePostActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePicture = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(CreatePostActivity.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mFileUri = FileProvider.getUriForFile(getApplicationContext(),
                        "fr.nicolashoareau_toulousewcs.appliwcsprojet.fileprovider",
                        photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(takePicture, APP_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case APP_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        mGetImageUrl = mFileUri.getPath();
                        Glide.with(getApplicationContext()).load(mFileUri).apply(RequestOptions.circleCropTransform()).into(mAddPhoto);
                    } else {
                        //nothing
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GALLERY:
                try {
                    if (resultCode == RESULT_OK) {
                        mFileUri = data.getData();
                        mGetImageUrl = mFileUri.getPath();
                        Glide.with(getApplicationContext()).load(mFileUri).apply(RequestOptions.circleCropTransform()).into(mAddPhoto);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }






}
