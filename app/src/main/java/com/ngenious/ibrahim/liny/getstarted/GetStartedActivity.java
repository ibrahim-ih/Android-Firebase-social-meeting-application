package com.ngenious.ibrahim.liny.getstarted;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.activity.friends.FriendsListActivity;
import com.ngenious.ibrahim.liny.main.MainActivity;

public class GetStartedActivity extends AppCompatActivity {
    AppCompatButton button;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(GetStartedActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_get_started);

        button = (AppCompatButton) findViewById(R.id.btn_launch_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });
    }
}
