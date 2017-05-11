package com.ngenious.ibrahim.liny.activity.authentication;


import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.activity.authentication.fragment.AdvancedInfoFragment;
import com.ngenious.ibrahim.liny.activity.authentication.fragment.PersonalInfoFragment;
import com.ngenious.ibrahim.liny.main.MainActivity;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.model.Users;

import android.app.ProgressDialog;
import android.util.Log;

public class SignupActivity extends AppCompatActivity implements PersonalInfoFragment.FragmentListener, AdvancedInfoFragment.FragmentListener {
    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser user ;
    private String displayName;
    private String email;
    private String password;
    private int age;
    private Uri picture;
    private String dateOfBirth;
    private String gender;
    private String uid;
    private String pictureToDb;
/*
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userDatabaseRef = database.getReference("users");
*/

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        //User user = new User("ibrahim","email","password");
        //mFirebaseInstance.getReference("app_title").setValue(user);

        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        if(displayName==null && email==null && password == null)
        {
            Fragment fragment = new PersonalInfoFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.signup_content_frame, fragment)
                    .commit();
        }

    }


    private void snackbarAlert(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.signup_content_frame), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void signUp(){
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

    /*    if(gender != null){
            firebaseUpdateUser();

        }else{

            firebaseSignup(email, password);
            //send email , password and displayName to activity
        }*/
        firebaseSignup(email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        advancedInfo();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void advancedInfo() {
        Fragment advancedInfoFragment = new AdvancedInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.signup_content_frame, advancedInfoFragment)
                .commit();
    }


    public void onSignupSuccess() {
       // _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //mainActivity();
    }



    private void firebaseSignup(final String email, final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                       //     onSignupFailed();
                        }else {
                           // firebaseUpdateUser(name);
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            uid = user.getUid();
                            snackbarAlert("success : email"+email+"password"+password+"uid"+user.getUid());
                        }

                        // ...
                    }
                });

    }


    private void firebaseUpdateUser(){

        user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(picture)
                .build();
        Log.i(TAG,"profileUpdates.getdisplayName"+profileUpdates.getDisplayName());
        user.updateProfile(profileUpdates);


                            Log.d(TAG, "User profile updated.");
                            // sendEmailVerif();
                            pictureToDb = picture.toString();
                            String mAge = String.valueOf(age);
                            final Users simpleUser = new Users(displayName, pictureToDb, email, dateOfBirth, mAge, gender);

/*
                            if (updateDatabase(simpleUser)) {
                                mainActivity();
                            }else snackbarAlert("error database connection");*/




        final ProgressDialog secondeprogressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Dialog);
        secondeprogressDialog.setIndeterminate(true);
        secondeprogressDialog.setMessage("Creating Account...");
        secondeprogressDialog.show();

    /*    if(gender != null){
            firebaseUpdateUser();

        }else{

            firebaseSignup(email, password);
            //send email , password and displayName to activity
        }*/
        firebaseSignup(email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        updateDatabase(simpleUser);
                        mainActivity();
                        secondeprogressDialog.dismiss();
                    }
                }, 3000);









    }


private boolean updateDatabase(Users simpleUser){
    //user is the firebaseUser
    if (user == null) {
        Log.e(TAG, "updateDatabase:no currentUser");
        return false;
    }
    else
    return mFirebaseDatabase.child(uid)
            .setValue(simpleUser).isSuccessful();
}
    private void mainActivity(){
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onFragmentFinish(User user) {
        Log.i(TAG, "onFragmentFinish: "+ user.getEmail() +","+user.getDisplayName()+","+user.getPassword());
        displayName = user.getDisplayName();
        email= user.getEmail();
        password = user.getPassword();

            signUp();
        }

    @Override
    public void onSignupFinish(User user) {
        Log.i(TAG, "onSignupFinish: "+ displayName +","+email+","+password+","+user.getPicture()+","+user.getAge()+","+user.getDateOfBirth()+","+user.getGender());

                picture = user.getPicture();
                age = user.getAge();
                dateOfBirth = user.getDateOfBirth();
                gender = user.getGender();
            firebaseUpdateUser();


    }


}