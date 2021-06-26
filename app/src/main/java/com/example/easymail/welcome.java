package com.example.easymail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class welcome extends AppCompatActivity {
    String AppID = "easymail-ekjlt";
    App app;
    User user;
    @Override
    protected void onStart() {
        super.onStart();
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppID).build());
        user = app.currentUser();
        if(user!=null){
            Intent intent=new Intent(welcome.this,HomeActivity.class);
            startActivity(intent);
            finish();

        }else{
            Intent intent=new Intent(welcome.this,SignIn.class);
            startActivity(intent);
            finish();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}