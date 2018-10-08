package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.Date;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ActualityModel;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.UserModel;

public class CreatePostActivity extends AppCompatActivity {

    public final static int GALLERY = 123;
    public final static int APP_PHOTO = 456;
    FirebaseDatabase mDatabase;
    DatabaseReference mCreatePostRef;
    private String mUid;
    private ImageView mAddPhoto;
    private String mGetImageUrl = "";
    private String mCurrentPhotoPath;
    private Uri mFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MenuActivity.class));
            }
        });

        mAddPhoto = findViewById(R.id.iv_take_pix);
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
                builder.setTitle(R.string.add_new_pic)
                        .setPositiveButton(getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dispatchTakePictureIntent();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY);
                            }
                        })
                        .show();
            }
        });

        final EditText etDescriptionPost = findViewById(R.id.et_edit_description);

        TextView dateText = findViewById(R.id.tv_date);
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final String date = formatter.format(currentTime);
        dateText.setText(date);
        final long dateLong = currentTime.getTime();

        Button btnValidatePost = findViewById(R.id.btn_validate_post);
        btnValidatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textDescriptionPost = etDescriptionPost.getText().toString();
                if (textDescriptionPost.isEmpty()) {
                    Toast.makeText(CreatePostActivity.this, R.string.enter_description, Toast.LENGTH_SHORT).show();
                } else {
                    mCreatePostRef = mDatabase.getReference("Post");
                    mCreatePostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!mGetImageUrl.equals("") && mGetImageUrl != null) {
                                Date date1 = new Date();
                                StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child("PhotoPost").child(mUid).child("post" + date1.getTime());
                                avatarRef.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                                        final String avatarUrl = downloadUri.toString();

                                        mDatabase = FirebaseDatabase.getInstance();
                                        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference userRef = mDatabase.getReference("User").child(mUid).child("Profil");
                                        userRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                String pseudo = userModel.getPseudo();
                                                String urlPhotoUser = userModel.getProfilPic();
                                                ActualityModel actualityModel = new ActualityModel(pseudo, textDescriptionPost, avatarUrl, urlPhotoUser, dateLong, mUid);
                                                mCreatePostRef.push().setValue(actualityModel);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });

                                    }
                                });
                                Intent intent = new Intent(CreatePostActivity.this, MenuActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CreatePostActivity.this, R.string.no_image, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


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
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        Glide.with(getApplicationContext()).load(mFileUri).into(mAddPhoto);
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
                        Glide.with(getApplicationContext()).load(mFileUri).into(mAddPhoto);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}
