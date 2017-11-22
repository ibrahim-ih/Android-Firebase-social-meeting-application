package com.ngenious.ibrahim.liny.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngenious.ibrahim.liny.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";

    @BindView(R.id.emailEditText)EditText _email;
    @BindView(R.id.passwordEditText)EditText _password;
    @BindView(R.id.deleteAccount)TextView _deleteAccount;
    @BindView(R.id.facebookSwitch)Switch _facebook;
    @BindView(R.id.twitterSwitch)Switch _twitter;
    @BindView(R.id.googlePlusSwitch)Switch _googlePlus;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    AuthCredential credential;
    private String uid, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = mFirebaseInstance.getReference("users").child(uid);

//        mContext = getApplicationContext();

        user = FirebaseAuth.getInstance().getCurrentUser();


    }
private void verifSocialStatut(FirebaseUser user) {
    if (user != null) {
        email = user.getEmail();

        uid = user.getUid();

        for (UserInfo userInfo : user.getProviderData()) {
            if (userInfo.getProviderId().equals("password")) {
                Toast.makeText(SettingActivity.this, "User is signed in with SimpleSignUp", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is signed in with SimpleSignUp");
                _email.setText(userInfo.getEmail());
//                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


            }
            if (userInfo.getProviderId().equals("facebook.com")) {
                Toast.makeText(SettingActivity.this, "User is signed in with Facebook", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is signed in with Facebook");
                _facebook.setChecked(true);
//                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


            }

            if (userInfo.getProviderId().equals("google.com")) {
                Toast.makeText(SettingActivity.this, "You are signed in Google!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is signed in with Google!");
                _googlePlus.setChecked(true);
//                AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);


            }
            if (userInfo.getProviderId().equals("twitter.com")) {
                Toast.makeText(SettingActivity.this, "You are signed in Google!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is signed in with Google!");
                _twitter.setChecked(true);
//                AuthCredential credential = TwitterAuthProvider.getCredential(token.getToken());


            }
        }
    }

}
private void updateLinkAccount(AuthCredential credential)
    {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            verifSocialStatut(user);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(SettingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            verifSocialStatut(null);
                        }

                        // ...
                    }
                });
    }



    private void updateEmail()
    {
        String newEmail = _email.getText().toString();

        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
    }
    private void updatePassword()
    {
        String newPassword = _password.getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }
/*@OnClick(R.id.deleteAccount)
    public void unlinkAuth(final String providerId) {
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    FirebaseAuth.getInstance().getCurrentUser().unlink(providerId)
                            .addOnCompleteListener(SettingActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Auth provider unlinked from account
                                    }
                                }
                            });


                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure to delete your account ?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
}*/


@OnClick(R.id.saveButton)
    public void saveUpdate()
{
    //save password
    if (!_password.getText().toString().isEmpty() && !_email.getText().toString().isEmpty() && _password.getText().toString().length()>5)
    {
        updatePassword();
    }else{
        snackbarAlert("Verif Email or Password");
    }

    //save email

    //save password
    if (!_password.getText().toString().isEmpty() && !_email.getText().toString().isEmpty() && _password.getText().toString().length()<4)
    {
        updateEmail();
    }else{
        snackbarAlert("Verif Email or Password");
    }
}
    private void snackbarAlert(String msg){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_setting), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void deleteAccount(View view) {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }
}
