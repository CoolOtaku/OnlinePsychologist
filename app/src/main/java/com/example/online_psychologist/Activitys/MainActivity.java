package com.example.online_psychologist.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.online_psychologist.App;
import com.example.online_psychologist.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Intent in = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setLogo(R.drawable.logo).build(), App.SIGN_IN_CODE);
        }else {
            StartNext();
        }
    }
    private void StartNext(){
        in.setClass(MainActivity.this, ChatActivity.class);
        startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == App.SIGN_IN_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(MainActivity.this, "Ви авторизовані!", Toast.LENGTH_SHORT).show();
                recreate();
            }
        }
    }

}