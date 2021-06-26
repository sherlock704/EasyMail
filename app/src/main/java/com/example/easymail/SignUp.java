package com.example.easymail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SignUp extends AppCompatActivity {
    private TextInputEditText name,email,password;
    Button login;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    String AppID = "easymail-ekjlt";
    App app;
    User user;
    private ProgressBar simpleProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=(TextInputEditText)findViewById(R.id.username);
        email=(TextInputEditText)findViewById(R.id.email);
        password=(TextInputEditText)findViewById(R.id.password);
        login=findViewById(R.id.submit);
        simpleProgressBar=findViewById(R.id.simple);
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppID).build());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(password.getText().toString())){

                    Toast.makeText(SignUp.this,"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(SignUp.this,"Please enter name",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(SignUp.this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(SignUp.this,"Please enter a valid email address",Toast.LENGTH_LONG).show();

                    return;
                }
                app.getEmailPassword().registerUserAsync(email.getText().toString(),password.getText().toString(),it->{

           if (it.isSuccess()){
               simpleProgressBar.setVisibility(View.VISIBLE);
               Log.v("User","User Registered Successfully");

               Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(SignUp.this, HomeActivity.class);
               startActivity(intent);
               finish();
               simpleProgressBar.setVisibility(View.INVISIBLE);
            }else{
               Toast.makeText(getApplicationContext(),"Logged In Failed",Toast.LENGTH_SHORT).show();
                Log.v("User","User Registration Failed");
            }

        });
            }
        });
    }
    public void signup(View view) {
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}