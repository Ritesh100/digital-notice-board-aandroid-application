package com.example.noticeboardproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FinalPage extends AppCompatActivity {
    EditText userName,password;
    Button login;

    @Override
    public void onBackPressed() {
        FinalPage.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().toString().equals("admin")&&password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"Redirecting....",Toast.LENGTH_SHORT).show();
                    adminlogin();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
            private void adminlogin(){
                Intent intent = new Intent(getApplicationContext(), adminpanel.class);
                startActivity(intent);
            }

        });

    }
}