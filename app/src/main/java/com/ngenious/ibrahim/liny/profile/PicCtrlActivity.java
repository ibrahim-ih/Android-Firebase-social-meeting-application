package com.ngenious.ibrahim.liny.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PicCtrlActivity extends AppCompatActivity {
//    picturegallerycapturesaveButton
private static final String TAG = "PicCtrlActivity";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static int RESULT_LOAD_IMG = 2;
    @BindView(R.id.picture)ImageView _picture;
    @BindView(R.id.gallery)ImageButton _gallery;
    @BindView(R.id.capture)ImageButton _capture;
    @BindView(R.id.saveButton)Button _saveButton;
    private FirebaseAuth mAuth;
    private Context mContext;
    String uid,name,email,mCurrentPhotoPath,gender,mAge;
    Uri photoUrl;
    FirebaseUser user;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @BindView(R.id.toolbar)Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_ctrl);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = mFirebaseInstance.getReference("users").child(uid);
        mContext = getApplicationContext();


    }
    private void snackbarAlert(String msg) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.contentLayout), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void uploadPictureToDb(Uri photoUrl, String namePic){
        String photoStringUrl = photoUrl.toString();
        String name = namePic;
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        Log.d(TAG, "name : "+name+"\n photoStringUrl : "+photoStringUrl+"\n photoUrl : "+photoUrl+"\n tsLong : "+tsLong+"\n timestamp : "+timestamp);
//        String name, String small, String medium, String large, String timestamp
        final Image image = new Image(name, photoStringUrl, photoStringUrl, photoStringUrl, timestamp);
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase
                        .child("gallery")
                        .push()
                        .setValue(image);
                    Intent intent = new Intent(PicCtrlActivity.this, SimpleProfileActivity.class);
                    startActivity(intent);

            }
        });


    }

    @OnClick(R.id.capture)
    public void camera() {
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ngenious.ibrahim.liny",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @OnClick(R.id.gallery)
    public void loadImagefromGallery(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            galleryAddPic();
        }else if(requestCode==RESULT_LOAD_IMG && null!=data)
        { try {
            final Uri imageUri = data.getData();
//            uploadPicture(imageUri);
            final InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            _picture.setImageBitmap(selectedImage);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            Uri ui = Uri.parse(imageEncoded);
            uploadPicture(ui);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG,"onActivityResult:"+"something went wrong");

        }

        }else {
            Log.i(TAG,"onActivityResult:"+"You havent picked Image");

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Environment.getExternalStoragePublicDirectory for public picture
        //Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) for private picture (deleted when uninstall the app)
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Log.i(TAG,"contentUri"+contentUri);
        _picture.setImageURI(contentUri);


        uploadPicture(contentUri);
    }

    private void uploadPicture(Uri contentUri){

        Uri file = contentUri;
        final String namePic = UUID.randomUUID().toString();
        FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("gallery")
                .child(namePic)
                .putFile(file)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");

                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                snackbarAlert("unsuccessful uploads");
            }
        })

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful uploads on complete
                        photoUrl =  taskSnapshot.getDownloadUrl();
                        //Log.i(TAG,"pictureURL : "+pictureUrl);
                        uploadPictureToDb(photoUrl,namePic);
                        snackbarAlert("success");
                    }
                });
    }

    //changer le chemin de photo et add to database , fill the image model plz
}
