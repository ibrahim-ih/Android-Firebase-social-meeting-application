package com.ngenious.ibrahim.liny.foursquare.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.foursquare.PlacePickerActivity;
import com.ngenious.ibrahim.liny.foursquare.adapter.PlacePickerAdapter;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareGroup;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareJSON;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareResponse;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareResults;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareService;
import com.ngenious.ibrahim.liny.foursquare.models.FoursquareVenue;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDestFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    // The client object for connecting to the Google API
    private GoogleApiClient mGoogleApiClient;

    // The TextView for displaying the current location
    private TextView snapToPlace;

    // The RecyclerView and associated objects for displaying the nearby coffee spots
    private RecyclerView placePicker;
    private LinearLayoutManager placePickerManager;
    private RecyclerView.Adapter placePickerAdapter;

    // The base URL for the Foursquare API
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";

    // The client ID and client secret for authenticating with the Foursquare API
    private String foursquareClientID;
    private String foursquareClientSecret;

    private FoursquareService foursquare;
    private String userLL;
    private double userLLAcc;
    private Toolbar toolbar;

    @BindView(R.id.foodImageButton)ImageButton _foodImageButton;
    @BindView(R.id.coffeeImageButton)ImageButton _coffeeImageButton;
    @BindView(R.id.nidhtlifeImageButton)ImageButton _nightlifeImageButton;
    @BindView(R.id.funImageButton)ImageButton _funImageButton;
    @BindView(R.id.buttonsGroupLayout)LinearLayout _buttonsGroup;
    @BindView(R.id.activity_place_picker)LinearLayout _contentLayout;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;

    private Context mContext;
    public ChooseDestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_dest, container, false);
        ButterKnife.bind(this, v);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        // The visible TextView and RecyclerView objects
        snapToPlace = (TextView)v.findViewById(R.id.snapToPlace);
        placePicker = (RecyclerView)v.findViewById(R.id.coffeeList);

        // Sets the dimensions, LayoutManager, and dividers for the RecyclerView
        placePicker.setHasFixedSize(true);
        placePickerManager = new LinearLayoutManager(mContext);
        placePicker.setLayoutManager(placePickerManager);
        placePicker.addItemDecoration(new DividerItemDecoration(placePicker.getContext(), placePickerManager.getOrientation()));

        // Creates a connection to the Google API for location services
        mGoogleApiClient = new GoogleApiClient
                .Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Gets the stored Foursquare API client ID and client secret from XML
        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coffeeImageButton:
                coffeeTaked();
                changeButtonColor(v);
                changeButtonsGroupOrientation();
                break;
            case R.id.foodImageButton:
                foodTaked();
                changeButtonColor(v);
                changeButtonsGroupOrientation();
                break;
            case R.id.nidhtlifeImageButton:
                nightlifeTaked();
                changeButtonColor(v);
                changeButtonsGroupOrientation();

                break;
            case R.id.funImageButton:
                funTaked();
                changeButtonColor(v);
                changeButtonsGroupOrientation();

                break;

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Checks for location permissions at runtime (required for API >= 23)
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Makes a Google API request for the user's last known location
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
//                startRipple(false);

                // The user's current latitude, longitude, and location accuracy
                userLL = mLastLocation.getLatitude() + "," +  mLastLocation.getLongitude();
                userLLAcc = mLastLocation.getAccuracy();

                // Builds Retrofit and FoursquareService objects for calling the Foursquare API and parsing with GSON
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(foursquareBaseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                foursquare = retrofit.create(FoursquareService.class);

                // Calls the Foursquare API to snap the user's location to a Foursquare venue
                Call<FoursquareJSON> stpCall = foursquare.snapToPlace(
                        foursquareClientID,
                        foursquareClientSecret,
                        userLL,
                        userLLAcc);
                stpCall.enqueue(new Callback<FoursquareJSON>() {
                    @Override
                    public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                        // Gets the venue object from the JSON response
                        FoursquareJSON fjson = response.body();
                        FoursquareResponse fr = fjson.response;
                        List<FoursquareVenue> frs = fr.venues;
                        FoursquareVenue fv = frs.get(0);

                        // Notifies the user of their current location
                        snapToPlace.setText("You're at " + fv.name + ". Here's some ☕ nearby.");
                        mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("city").setValue(fv.name);

                    }

                    @Override
                    public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                       /* Toast.makeText(getApplicationContext(), "You can't connect to Foursquare's servers!", Toast.LENGTH_LONG).show();
                        startRipple(true);*/
//                        PlacePickerActivity.snackIt("You can't connect to Foursquare's servers!");
                        // finish();
                        Log.i("error", "You can't connect to Foursquare's servers!");

                    }
                });
///////
                _coffeeImageButton.setOnClickListener(this);
                _foodImageButton.setOnClickListener(this);
                _funImageButton.setOnClickListener(this);
                _nightlifeImageButton.setOnClickListener(this);

            } else {
             /*   startRipple(true);
                Toast.makeText(getApplicationContext(), "can't determine your current location!", Toast.LENGTH_LONG).show();*/
                //finish();
//                snackIt("can't determine your current location!");
                Log.i("error", "can't determine your current location!");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void changeButtonColor(View v){
        _foodImageButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        _nightlifeImageButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        _coffeeImageButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        _funImageButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        v.setBackgroundColor(Color.RED);
    }
    private void changeButtonsGroupOrientation(){
        _buttonsGroup.setOrientation(LinearLayout.HORIZONTAL);

    }

    protected void coffeeTaked(){
        // Calls the Foursquare API to explore nearby coffee spots

        Call<FoursquareJSON> coffeeCall = foursquare.searchCoffee(
                foursquareClientID,
                foursquareClientSecret,
                userLL,
                userLLAcc);
        coffeeCall.enqueue(new Callback<FoursquareJSON>() {
            @Override
            public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                // Gets the venue object from the JSON response
                FoursquareJSON fjson = response.body();
                FoursquareResponse fr = fjson.response;
                FoursquareGroup fg = fr.group;
                List<FoursquareResults> frs = fg.results;

                // Displays the results in the RecyclerView
                placePickerAdapter = new PlacePickerAdapter(getContext(), frs);
                placePicker.setAdapter(placePickerAdapter);
            }

            @Override
            public void onFailure(Call<FoursquareJSON> call, Throwable t) {
              /*  Toast.makeText(getApplicationContext(), "can't connect to Foursquare's servers!", Toast.LENGTH_LONG).show();
                finish();*/
//                snackIt("can't connect to Foursquare's servers!");
                Log.i("error","can't connect to Foursquare's servers!");

            }
        });
    }

    protected void foodTaked(){
        // Calls the Foursquare API to explore nearby coffee spots

        Call<FoursquareJSON> foodCall = foursquare.searchFood(
                foursquareClientID,
                foursquareClientSecret,
                userLL,
                userLLAcc);
        foodCall.enqueue(new Callback<FoursquareJSON>() {
            @Override
            public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                // Gets the venue object from the JSON response
                FoursquareJSON fjson = response.body();
                FoursquareResponse fr = fjson.response;
                FoursquareGroup fg = fr.group;
                List<FoursquareResults> frs = fg.results;

                // Displays the results in the RecyclerView
                placePickerAdapter = new PlacePickerAdapter(getApplicationContext(), frs);
                placePicker.setAdapter(placePickerAdapter);
            }

            @Override
            public void onFailure(Call<FoursquareJSON> call, Throwable t) {
               /* Toast.makeText(getApplicationContext(), "can't connect to Foursquare's servers!", Toast.LENGTH_LONG).show();
                finish();*/
//                snackIt("can't connect to Foursquare's servers!");
            }
        });
    }

    protected void nightlifeTaked(){
        // Calls the Foursquare API to explore nearby coffee spots

        Call<FoursquareJSON> nightlifeCall = foursquare.searchNightlife(
                foursquareClientID,
                foursquareClientSecret,
                userLL,
                userLLAcc);
        nightlifeCall.enqueue(new Callback<FoursquareJSON>() {
            @Override
            public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                // Gets the venue object from the JSON response
                FoursquareJSON fjson = response.body();
                FoursquareResponse fr = fjson.response;
                FoursquareGroup fg = fr.group;
                List<FoursquareResults> frs = fg.results;

                // Displays the results in the RecyclerView
                placePickerAdapter = new PlacePickerAdapter(getApplicationContext(), frs);
                placePicker.setAdapter(placePickerAdapter);
            }

            @Override
            public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                /*Toast.makeText(getApplicationContext(), "can't connect to Foursquare's servers!", Toast.LENGTH_LONG).show();
                finish();*/
//                snackIt("can't connect to Foursquare's servers!");
                Log.i("error","can't connect to Foursquare's servers!");
            }
        });
    }


    protected void funTaked(){
        // Calls the Foursquare API to explore nearby coffee spots

        Call<FoursquareJSON> funCall = foursquare.searchFun(
                foursquareClientID,
                foursquareClientSecret,
                userLL,
                userLLAcc);
        funCall.enqueue(new Callback<FoursquareJSON>() {
            @Override
            public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                // Gets the venue object from the JSON response
                FoursquareJSON fjson = response.body();
                FoursquareResponse fr = fjson.response;
                FoursquareGroup fg = fr.group;
                List<FoursquareResults> frs = fg.results;

                // Displays the results in the RecyclerView
                placePickerAdapter = new PlacePickerAdapter(getApplicationContext(), frs);
                placePicker.setAdapter(placePickerAdapter);
            }

            @Override
            public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "can't connect to Foursquare's servers!", Toast.LENGTH_LONG).show();
//                snackIt("can't connect to Foursquare's servers!");
                //finish();
                Log.i("error","can't connect to Foursquare's servers!");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reconnects to the Google API
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Disconnects from the Google API
        mGoogleApiClient.disconnect();
    }

}
