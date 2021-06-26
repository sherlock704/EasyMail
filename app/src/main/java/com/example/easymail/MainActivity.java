package com.example.easymail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText to,cc,subject,mailbody;
    Button send,left,right,center,b,i,u,no;
    private RadioGroup radioGroup;
    private String shedule = null;
    private FloatingActionButton btnFloatCat;
    BottomNavigationView bottomNavigationView;
    User user;
    String AppID = "easymail-ekjlt";
    App app;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFloatCat = findViewById(R.id.addCategory_btn);
        bottomNavigationView=findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppID).build());
        btnFloatCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.mail, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                to=dialogView.findViewById(R.id.to);
                cc=dialogView.findViewById(R.id.cc);
                subject=dialogView.findViewById(R.id.sub);
                mailbody=dialogView.findViewById(R.id.mailbody);

                no=dialogView.findViewById(R.id.none);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringTExt = mailbody.getText().toString();
                        mailbody.setText(stringTExt);
                    }
                });
                left=dialogView.findViewById(R.id.left);
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mailbody.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                        Spannable spannable = new SpannableStringBuilder(mailbody.getText());
                        mailbody.setText(spannable);

                    }
                });
                center=dialogView.findViewById(R.id.center);
                center.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mailbody.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        Spannable spannable = new SpannableStringBuilder(mailbody.getText());
                        mailbody.setText(spannable);

                    }
                });
                right=dialogView.findViewById(R.id.right);
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mailbody.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                        Spannable spannable = new SpannableStringBuilder(mailbody.getText());
                        mailbody.setText(spannable);

                    }
                });

                b=dialogView.findViewById(R.id.bolds);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spannable spannable = new SpannableStringBuilder(mailbody.getText());
                        spannable.setSpan(new StyleSpan(Typeface.BOLD),
                                mailbody.getSelectionStart(),
                                mailbody.getSelectionEnd(),
                                0);

                        mailbody.setText(spannable);
                    }
                });
                i=dialogView.findViewById(R.id.ital);
                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spannable spannable = new SpannableStringBuilder(mailbody.getText());
                        spannable.setSpan(new StyleSpan(Typeface.ITALIC),
                                mailbody.getSelectionStart(),
                                mailbody.getSelectionEnd(),
                                0);

                        mailbody.setText(spannable);
                    }
                });

                radioGroup =dialogView.findViewById(R.id.radioGroup_payment);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId)
                        {
                            case R.id.recursive:
                                shedule = "recursive";
                                break;

                            case R.id.week:
                                shedule = "week";
                                break;

                            case R.id.month:
                                shedule = "month";
                                break;
                            case R.id.year:
                                shedule = "year";
                                break;
                        }

                    }
                });
                send=dialogView.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(to.getText().toString())){

                            Toast.makeText(MainActivity.this,"Please enter TO",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(cc.getText().toString())){
                            Toast.makeText(MainActivity.this,"Please enter CC",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(subject.getText().toString())){
                            Toast.makeText(MainActivity.this,"Please enter subject",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(mailbody.getText().toString())){
                            Toast.makeText(MainActivity.this,"Please enter mailbody",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(shedule)){
                            Toast.makeText(MainActivity.this,"Please enter shedule",Toast.LENGTH_LONG).show();
                            return;
                        }
                        user = app.currentUser();
                        mongoClient = user.getMongoClient("mongodb-atlas");
                        mongoDatabase = mongoClient.getDatabase("easydb");

                        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("mails");

                        mongoCollection.insertOne(new Document("userid", user.getId()).append("TO", to.getText().toString()).append("cc",cc.getText().toString()).
                                append("subject",subject.getText().toString()).
                                append("mailbody",mailbody.getText().toString()).
                                append("shedule",shedule)).getAsync(result1 -> {
                            if (result1.isSuccess()) {
                                alertDialog.dismiss();
                                Log.v("Data", "Data Added Successfully");

                            } else {

                                Log.v("Data", "Error: " + result1.getError().toString());

                            }
                        });}
                });

                alertDialog.show();

            }
        });

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            item -> {

                switch(item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(MainActivity.this,HomeActivity.class);

                        startActivity(intent1);
                        break;

                    case R.id.history:


                        break;


                }

                return false;
            };


    public void logout(View view) {
        new android.app.AlertDialog.Builder(this)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Do you want to Logout?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    user = app.currentUser();
                    user.logOutAsync( result -> {
                        if (result.isSuccess()) {
                            Log.v("AUTH", "Successfully logged out.");
                            Intent intent = new Intent(MainActivity.this, SignIn.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("AUTH", result.getError().toString());
                        }
                    });


                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                    dialog.cancel();
                    // finish();
                    // startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                })
                .show();


    }
}