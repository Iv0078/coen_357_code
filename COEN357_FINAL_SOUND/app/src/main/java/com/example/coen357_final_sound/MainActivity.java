package com.example.coen357_final_sound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleName = findViewById(R.id.toolbar_title);
        titleName.setText("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this,SignIn.class);
        startActivity(intent);
        if(id == R.id.sign_out){

        }


        return super.onOptionsItemSelected(item);


    }
}