package com.ngenious.ibrahim.liny.activity.authentication.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.activity.authentication.SignupActivity;
import com.ngenious.ibrahim.liny.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedInfoFragment extends Fragment {
    static final int REQUEST_TAKE_PHOTO = 1;
    private static int RESULT_LOAD_IMG = 2;
    private static String TAG = "Error";
    @BindView(R.id.pictureImageView)
    CircleImageView _mImageView;
    @BindView(R.id.takePicImageButton)
    ImageButton _takePic;
    @BindView(R.id.cameraImageButton)
    ImageButton _camera;
    @BindView(R.id.dateOfBirthImageButton)
    ImageButton _dateOfBirth;
    @BindView(R.id.datePicked)
    EditText _datePicked;
    @BindView(R.id.genderRadioGroup)
    RadioGroup _genderGroup;
    /* @BindView(R.id.maleRadioButton)
     RadioButton _male;
     @BindView(R.id.femaleRadioButton)
     RadioButton _female;*/
    @BindView(R.id.manchecked)
    ImageView _manChecked;
    @BindView(R.id.womanchecked)
    ImageView _womanChecked;
    @BindView(R.id.saveButton)
    Button _save;
    private RadioButton _gender_choice;
    private FragmentListener mListener;
    String mCurrentPhotoPath;
    String imgDecodableString;
    String gender;
    Uri pictureUrl;
    private int mYear, mMonth, mDay,age;
    private StorageReference mStorageRef;
    public AdvancedInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            snackbarAlert("something went wrong");
            Log.i(TAG,"user is signed out");

        }
        View view = inflater.inflate(R.layout.fragment_advanced_info, container, false);
        ButterKnife.bind(this, view);
        if (user != null) {
            Log.i(TAG,"user is signed"+user.getUid());

        }

        _genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.femaleRadioButton:
                        // switch to fragment 1
                        _womanChecked.setVisibility(View.VISIBLE);
                        _manChecked.setVisibility(View.GONE);
                        gender = "female";
                        break;
                    case R.id.maleRadioButton:
                        _manChecked.setVisibility(View.VISIBLE);
                        _womanChecked.setVisibility(View.GONE);
                        gender = "male";
                        break;
                }
            }
        });
        //
        mStorageRef = FirebaseStorage.getInstance().getReference();


        return view;
    }

    @OnClick(R.id.saveButton)
    public void finishSignup()
    {
        done();
    }

    @OnClick(R.id.cameraImageButton)
    public void camera() {
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.ngenious.ibrahim.liny",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @OnClick(R.id.takePicImageButton)
    public void loadImagefromGallery(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, RESULT_LOAD_IMG);
    }


    @OnClick(R.id.dateOfBirthImageButton)
    public void takeBirthDate(){


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR) - 18;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(mYear < year )
                        {snackbarAlert("You can't subscribe cause your age under 18 ! ");}
                        else{
                            _datePicked.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            age = getAge(year, monthOfYear,dayOfMonth);
                            _datePicked.setVisibility(View.VISIBLE);}
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        // String ageS = ageInt.toString();

        return ageInt;
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
            uploadPicture(imageUri);
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                _mImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            Log.i(TAG,"onActivityResult:"+"something went wrong");

            }

    }else {
            Log.i(TAG,"onActivityResult:"+"You havent picked Image");

        }
        }



    ////
    private void snackbarAlert(String msg) {

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.advanderinfoLayout), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      //  if (!(context instanceof FragmentListener)) throw new AssertionError();
        mListener=(FragmentListener)context;



    }

    private void done(){
        String dateOfBirth= _datePicked.getText().toString();
        age = Integer.valueOf(age);

        User user = new User(pictureUrl, dateOfBirth, age, gender);
        Log.i(TAG,"user : picture"+pictureUrl+"dateOfBirth"+dateOfBirth+"age"+age+"gender"+gender);

        mListener.onSignupFinish(user);

    }
    public interface FragmentListener{
        void onSignupFinish(User user);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Environment.getExternalStoragePublicDirectory for public picture
        //Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) for private picture (deleted when uninstall the app)
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        getActivity().sendBroadcast(mediaScanIntent);
        Log.i(TAG,"contentUri"+contentUri);
        _mImageView.setImageURI(contentUri);


        uploadPicture(contentUri);
    }

    private void uploadPicture(Uri contentUri){

       Uri file = contentUri;

        FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(UUID.randomUUID().toString())
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
                pictureUrl =  taskSnapshot.getDownloadUrl();
                //Log.i(TAG,"pictureURL : "+pictureUrl);
                snackbarAlert("success");
            }
        });
    }

}
