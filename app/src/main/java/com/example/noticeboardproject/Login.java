package com.example.noticeboardproject;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText userName,password;
    Button login;
    TextView register,forgotPassword;
    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login_btn);
        register = (TextView) findViewById(R.id.registerLink);
        forgotPassword = (TextView) findViewById(R.id.forgetpsw);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        currentUser = mAuth.getCurrentUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AllowUserToLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegister();
            }
        });
        //if user forgets the password then to reset it
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordUser();
            }
        });
    }

    private void resetPasswordUser() {
        String email = userName.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(Login.this,"Please enter your email id",Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Reset Email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToRegister() {
        //When user wants to create a new account send user to Register Activity
        Intent registerIntent = new Intent(Login.this,SignUp.class);
        startActivity(registerIntent);
    }

    private void AllowUserToLogin() {
        String email = userName.getText().toString().trim();
        String pwd = password.getText().toString();
//
//        if(userName.getEditableText().equals("admin@admin")&&password.getEditableText().equals("admin10")){
//            Toast.makeText(getApplicationContext(),"Redirecting....",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), FinalPage.class);
//            startActivity(intent);
//        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(Login.this,"Please enter email id",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pwd))
        {
            Toast.makeText(Login.this,"Please enter password",Toast.LENGTH_SHORT).show();
        }

        else
        {
            //When both email and password are available log in to the account
            //Show the progress on Progress Dialog
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait ,Because Good things always take time");
            mAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())//If account login successful print message and send user to main Activity
                            {
                               // sendToMainActivity();
                                Toast.makeText(Login.this,"Welcome",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                userView();


                            }
                            else//Print the error message incase of failure
                            {
                                String msg = task.getException().toString();
                                Toast.makeText(Login.this,"Error: "+msg,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }


    protected void onStart() {
        //Check if user has already signed in if yes send to mainActivity
        //This to avoid signing in everytime you open the app.
        super.onStart();
        if(currentUser!=null)
        {
           // sendToMainActivity();
        }
    }
    private void userView(){
        Intent intent = new Intent(Login.this, Userview.class);
        startActivity(intent);
    }

//    private void sendToMainActivity() {
//        //This is to send user to MainActivity
//        Intent  MainIntent = new Intent(Login.this, FinalPage.class);
//        startActivity(MainIntent);
//    }
}