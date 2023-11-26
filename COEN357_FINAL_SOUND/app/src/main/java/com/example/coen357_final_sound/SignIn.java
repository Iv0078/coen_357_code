package com.example.coen357_final_sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void signInEvent(View view){
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        String userName = sh.getString("CUSTOMNAME","");
        String password = sh.getString("CUSTOMPASS","");

        TextView inputUserNameView =  findViewById(R.id.username);
        TextView inputPassView = findViewById(R.id.password);

        String inputUserName = inputUserNameView.getText().toString();
        String inputPass = inputPassView.getText().toString();

        if(userName.equals(inputUserName) && password.equals(inputPass) && !inputUserName.isEmpty()){
            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.putString("ACCOUNTTYPE","CUSTOM");
            myEdit.commit();
            startActivity(new Intent(SignIn.this,MainActivity.class));
        }
        else{
            Toast.makeText(SignIn.this, "Login error!!",Toast.LENGTH_SHORT).show();
        }
    }

    public void guestEvent(View view){
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        String guestName = "Guest26214";
        // gives public name
        myEdit.putString("PUBLICNAME",guestName);
        // tells app that the name is public
        myEdit.putString("ACCOUNTTYPE","PUBLIC");
        myEdit.commit();

        startActivity(new Intent(SignIn.this,MainActivity.class));
    }

    public void newAccountEvent(View view){
        // goes new create a new account
        startActivity(new Intent(SignIn.this,SignUp.class));

    }

}