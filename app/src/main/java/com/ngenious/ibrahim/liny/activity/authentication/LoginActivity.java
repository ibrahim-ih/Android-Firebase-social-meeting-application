package com.ngenious.ibrahim.liny.activity.authentication;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.main.MainActivity;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.model.Users;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.BuildConfig;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.DefaultLogger;
import io.fabric.sdk.android.Fabric;
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;
        private static final int RC_SIGN_IN = 007;

    @BindView(R.id.input_email) EditText _emailText;
        @BindView(R.id.input_password) EditText _passwordText;
        @BindView(R.id.btn_login) Button _loginButton;

        @BindView(R.id.facebookButton)ImageButton _facebookButton;
        @BindView(R.id.twitterButton)ImageButton _twitterButton;
        @BindView(R.id.googlePlusButton)ImageButton _googlePlusButton;
        @BindView(R.id.simpleSignupButton)ImageButton _simpleSignupButton;
        @BindView(R.id.loginFacebookButton)LoginButton _loginFacebookButton;
       // private FirebaseDatabase database = FirebaseDatabase.getInstance();
       // private DatabaseReference userDatabaseRef = database.getReference("users");
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;

        private GoogleApiClient mGoogleApiClient;
        private CallbackManager mCallbackManager;

        private TwitterLoginButton mLoginButton;
        private String uid,personName,personGivenName,personFamilyName,personEmail,personId;
        private Uri personPhoto, profilePic;

        FirebaseUser user;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);




            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("users");

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _simpleSignupButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };

            intializeFacebookButton();
           _facebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _loginFacebookButton.performClick();

                }
            });

            // *** twitter ****//
            mLoginButton = (TwitterLoginButton) findViewById(R.id.button_twitter_login);

            _twitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoginButton.performClick();

                }
            });

            initializeTwitter();

        }




    public void login() {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }

            _loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.Theme_AppCompat_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            signInWithEmailAndPassword(email,password);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                Log.d(TAG, "GoogleSignInResult:" + result.getSignInAccount());

                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount acct = result.getSignInAccount();

                    firebaseAuthWithGoogle(acct);

                } else {
                    //ici en peut essayer de verifier d'autre methode de signin
                    // Google Sign In failed, update UI appropriately

                    snackbarAlert("Google Sign In failed");
                }
            }else if (requestCode == REQUEST_SIGNUP)
            {
                this.finish();
            }else {
                mLoginButton.onActivityResult(requestCode, resultCode, data);

                //facebook callback
                mCallbackManager.onActivityResult(requestCode, resultCode, data);


            }

        }

        @Override
        public void onBackPressed() {
            // disable going back to the GetStartedActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("enter a valid email address");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _passwordText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }

                                    //*********************//
                                    //*****SignIn**Methods//
                                    //*********************//

        private void signInWithEmailAndPassword(String email,String password){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            snackbarAlert("Welcome back");

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                                snackbarAlert("Sign In Failed !");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException){
                                snackbarAlert("Invalid Password");
                            }else if(e instanceof FirebaseAuthInvalidUserException){
                                snackbarAlert("No account with this email");
                            }else{
                                snackbarAlert(e.getLocalizedMessage());
                            }
                        }
                    });
        }

        private void snackbarAlert(String msg){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_activity), msg, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

                                        //*********************//
                                        //*****GooglePlus//
                                        //*********************//

    @OnClick(R.id.googlePlusButton)
    public void signIn() {
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ///////

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        personName = acct.getDisplayName();
                        personGivenName = acct.getGivenName();
                        personFamilyName = acct.getFamilyName();
                        personEmail = acct.getEmail();
                        personId = acct.getId();
                        personPhoto = acct.getPhotoUrl();
                        uid = task.getResult().getUser().getUid();
                        //uid = user.getUid();
                      /*  createGooglePlusDbUser(uid, personName, personEmail, personGivenName, personFamilyName,
                                personId, personPhoto);*/



                       // startMainActivity();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());

                            snackbarAlert("Authentication failed.");

                        }else {
                            Log.d(TAG, "signInWithCustomToken:success");




                           // uid = mAuth.getCurrentUser().getUid();

                            Log.i(TAG, "uid : "+uid);
                            String picture = personPhoto.toString();
                            final Users createGooglePlusDbUser = new Users(personGivenName,picture, personEmail, personName, personFamilyName,null,null,personId);

                            mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        startMainActivity();
                                    }else {
                                        updateDatabase(createGooglePlusDbUser);
                                        startMainActivity();
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




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    //*********************//
    //*****On Start//
    //*********************//

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //*********************//
    //*****On Stop//
    //*********************//

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //*********************//
    //*****startMainActivity//
    //*********************//
    private void startMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


            //***********************//
            //*****Facebook*********//
            //*********************//
    private void intializeFacebookButton(){
        // Initialize Facebook Login button

        mCallbackManager = CallbackManager.Factory.create();


        _loginFacebookButton.setReadPermissions("email", "public_profile");
        _loginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                _loginFacebookButton.setEnabled(false);

                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

// ...

    }

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        personName = Profile.getCurrentProfile().getName();
                        personGivenName = Profile.getCurrentProfile().getFirstName();
                        personFamilyName = Profile.getCurrentProfile().getLastName();
                        personEmail = Profile.getCurrentProfile().getId();
                        personId = Profile.getCurrentProfile().getId();
                        //Uri personPhoto = acct.getPhotoUrl();
                        profilePic = Profile.getCurrentProfile().getProfilePictureUri(64,64);
                        Log.d(TAG, "Profile name:" + personName);

                        //uid = user.getUid();
                        uid = mAuth.getCurrentUser().getUid();

                        Log.i(TAG, "uid : "+uid);
                        String picture = profilePic.toString();
                        final Users userFacebook = new Users(personGivenName,picture, personEmail, personName, personFamilyName,personId,null,null);
                        Log.i(TAG,"userfacebook : "+"name"+personGivenName+"picture"+picture+"email"+personEmail+"Name"+personName+"family name"+personFamilyName+"id"+personId+"les nulls"+null+null);



                       mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()){
                                   startMainActivity();
                               }else {
                                   updateDatabase(userFacebook);
                                   startMainActivity();
                               }
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });

                        /*updateDatabase(userFacebook);
                        startMainActivity();*/

                        //createFacebookDbUser(uid, personName, personEmail, personGivenName, personFamilyName,personId, profilePic);


                     //  startMainActivity();


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());

                            snackbarAlert("Authentication failed.");
                        }}

                    // ...
                });

    }


    //***********************//
    //*****Twitter*********//
    //*********************//

    private void initializeTwitter() {

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.twitterConsumerKey),
                getString(R.string.twitterConsumerSecret));

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new TwitterCore(authConfig))
                .logger(new DefaultLogger(Log.DEBUG))
                .debuggable(true)
                .build();

        Fabric.with(fabric);

        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
                //updateUI(null);
            }
        });
    }
    private void handleTwitterSession(final TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                            Log.i(TAG, "TwitterDisplayName :"+task.getResult().getUser().getDisplayName()+task.getResult().getUser().getEmail());
                            ////////////////////////////


                            uid = mAuth.getCurrentUser().getUid();

                            Log.i(TAG, "uid : "+uid);
                            String picture = task.getResult().getUser().getPhotoUrl().toString();
                            final Users userTwitter = new Users(task.getResult().getUser().getDisplayName(),picture,task.getResult().getUser().getEmail(), task.getResult().getUser().getDisplayName(), task.getResult().getUser().getDisplayName(), null,task.getResult().getUser().getProviderId(),null);
                            Log.i(TAG,"userfacebook : "+"name"+personGivenName+"picture"+picture+"email"+personEmail+"Name"+personName+"family name"+personFamilyName+"id"+personId+"les nulls"+null+null);



                            mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        startMainActivity();
                                    }else {
                                        updateDatabase(userTwitter);
                                        startMainActivity();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });






                            //////////////////

                            startMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            snackbarAlert("Authentication failed.");
                        }

                        // ...
                    }
                });
    }
/*    private void createFacebookDbUser(final String uid, final String personGivenName, final String personEmail,
                                      final String personName, final String personFamilyName, final String personId,
                                      final Uri profilePic) {

        userDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    snackbarAlert("Welcome again");

                }else {
                    String picture = profilePic.toString();
                    Users userFacebook = new Users(personGivenName,picture, personEmail, personName, personFamilyName,personId,null,null);
                    userDatabaseRef.child(uid).setValue(userFacebook);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }*/

/*    private void createGooglePlusDbUser(final String uid, final String personGivenName, final String personEmail,
                                        final String personName, final String personFamilyName, final String personId,
                                        final Uri profilePic) {

        userDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    snackbarAlert("Welcome again");

                }else {
                   String picture = profilePic.toString();
                    Users userGplus = new Users(personGivenName,picture, personEmail, personName, personFamilyName,null,null,personId);
                    userDatabaseRef.child(uid).setValue(userGplus);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }*/

    private boolean updateDatabase(Users simpleUser){
        //user is the firebaseUser
        Log.i(TAG , "uid update"+uid);
        if (mAuth.getCurrentUser() == null) {
            Log.e(TAG, "updateDatabase:no currentUser");
            return false;
        }
        else
            return mFirebaseDatabase.child(uid)
                    .setValue(simpleUser).isSuccessful();
    }


}
