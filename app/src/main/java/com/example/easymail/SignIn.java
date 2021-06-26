package com.example.easymail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SignIn extends AppCompatActivity {

    private TextInputEditText email,password;
    Button login;
    String emailStr,passStr;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    String AppID = "easymail-ekjlt";
    App app;
    TextView texta;
    User user;
    private ProgressBar simpleProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email=(TextInputEditText)findViewById(R.id.username1);
        password=(TextInputEditText)findViewById(R.id.password1);
        login=findViewById(R.id.signup);
        texta=findViewById(R.id.textss);

        texta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        simpleProgressBar=findViewById(R.id.simple);
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppID).build());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailStr = email.getText().toString();
                passStr = password.getText().toString();
                login(emailStr,passStr);
            }
        });
    }

    private void login(String emailStr, String passStr) {
        if(TextUtils.isEmpty(emailStr)){

            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(passStr)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_LONG).show();

            return;
        }
        Credentials credentials = Credentials.emailPassword(emailStr, passStr);

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {

                if (result.isSuccess()) {
                    simpleProgressBar.setVisibility(View.VISIBLE);
                    Log.v("User", "Logged In Successfully");
                    user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("easydb");

                    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("User");

                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    simpleProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(),"Logged In Failed",Toast.LENGTH_SHORT).show();
                    Log.v("User", "Logged In Failed");
                }

            }
        });
    }
  /*  private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // RC_SIGN_IN lets onActivityResult identify the result of THIS call
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authorizationCode = account.getServerAuthCode();
            String idToken=account.getIdToken();
            Credentials googleCredentials = Credentials.google(idToken);
            app.loginAsync(googleCredentials, it -> {
                if (it.isSuccess()) {
                    Intent intent = new Intent(SignIn.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Log.v("AUTH", "Successfully logged in to MongoDB Realm using Google OAuth.");
                } else {
                    Log.e("AUTH", "Failed to log in to MongoDB Realm", it.getError());
                }
            });
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
        }
    }*/

}