package com.example.coen357_final_sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button startMeasurebtn;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TO do:
//        startActivity(new Intent(MainActivity.this,SignIn.class));

        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String accountType = sh.getString("ACCOUNTTYPE","");
        if(accountType.equals("CUSTOM") ){
            userName = sh.getString("CUSTOMNAME","");
        }
        else if(accountType.equals("PUBLIC") ){
            userName = sh.getString("PUBLICNAME","");
        }
        else{
            startActivity(new Intent(MainActivity.this,SignIn.class));
        }

        startMeasurebtn = (Button) findViewById(R.id.measurebtn);
        startMeasurebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this,SoundMeasuring.class);
                startActivity(intent);

            }

        });



    }


}