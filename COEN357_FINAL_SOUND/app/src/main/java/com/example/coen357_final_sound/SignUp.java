package com.example.coen357_final_sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




    }

    public void registerEvent(View view){
        TextView passView = (TextView) findViewById(R.id.password2);
        TextView repassView = (TextView) findViewById(R.id.repassword);

        String pass = passView.getText().toString();
        String repass = repassView.getText().toString();

        if(!pass.equals(repass)){
            Toast.makeText(SignUp.this,"Passwords don't match", Toast.LENGTH_SHORT).show();
        }else{
            TextView usernameView = (TextView) findViewById(R.id.username2);
            TextView emailView = (TextView) findViewById(R.id.email);

            String username = usernameView.getText().toString();
            String email = emailView.getText().toString();

            SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sh.edit();

            myEdit.putString("CUSTOMNAME",username);
            myEdit.putString("CUSTOMPASS",pass);
            myEdit.putString("EMAIL",email);
            myEdit.putString("ACCOUNTTYPE","CUSTOM");
            myEdit.commit();

            Toast.makeText(SignUp.this,"Account Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUp.this,MainActivity.class));


        }

    }
}