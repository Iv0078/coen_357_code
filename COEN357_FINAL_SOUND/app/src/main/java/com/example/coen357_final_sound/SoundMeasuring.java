package com.example.coen357_final_sound;


import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.DecimalFormat;
import java.text.Format;

import androidx.appcompat.widget.Toolbar;

public class SoundMeasuring extends AppCompatActivity {

    public static final int APP_PERMISSIONS_RECORD_AUDIO =1;
    private final static String LOG_TAG="main";
    private ScreenVisualization screenVisualization;
    private MediaRecorder recorder;
    private Handler handler = new Handler();
    TextView mainDb, comparissonDb;


    private static double ambient = 0.0;
    static final private double filter = 0.6;


    final Runnable updater = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,50);
            if(recorder != null && screenVisualization != null){
                int maxAmplitude =0;
                try{
                    maxAmplitude = recorder.getMaxAmplitude();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(maxAmplitude>0){
                    mainDb.setText("maxAmplitude");
                    screenVisualization.addAmplitude(maxAmplitude);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_measuring);


        screenVisualization = (ScreenVisualization) findViewById(R.id.visualization);


        screenVisualization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSoundListening();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleName = findViewById(R.id.toolbar_title);
        titleName.setText("Sound Meter");

        mainDb = findViewById(R.id.db_val);
        comparissonDb = findViewById(R.id.db_comp);

        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
            foregroundServiceRunning();

        }
    }

    private void RequestPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this,"Permissions required for this functionality to work",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},APP_PERMISSIONS_RECORD_AUDIO);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){

        }
        ActivityCompat.requestPermissions(SoundMeasuring.this,new String[]{
            Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},APP_PERMISSIONS_RECORD_AUDIO);
    }

    public boolean checkPermissions(){
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED && result2 ==  PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case APP_PERMISSIONS_RECORD_AUDIO:{
                if(grantResults.length>0){
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(permissionToRecord && permissionToStore){
                        Toast.makeText(getApplicationContext(),"Permissions Granted",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Permissions Denied",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }

    private void startSoundListening() {
        if(checkPermissions()){
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat((MediaRecorder.OutputFormat.THREE_GPP));
            recorder.setAudioEncoder((MediaRecorder.AudioEncoder.AMR_NB));
            File file = null;
            try{
                file = File.createTempFile("temp","audio",getApplication().getCacheDir());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    recorder.setOutputFile(file);
                }
            }
            catch(IOException e)
            {e.printStackTrace();}
            try{
                Toast.makeText(getApplicationContext(),"Sound detector ready",Toast.LENGTH_SHORT).show();
                recorder.prepare();
                Toast.makeText(getApplicationContext(),"Listening",Toast.LENGTH_SHORT).show();
                recorder.start();
                Toast.makeText(getApplicationContext(),"Sound detector ready",Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            RequestPermissions();
        }


    }


    public double soundDb(double ampl){

        double dbSPL = 20 * Math.log10(presSoundPA() / ampl);

        if (dbSPL < 0){
            return 0;
        }
        else {
            return dbSPL;
        }

    }
    public double soundAmbient() {
        if (recorder != null)
            return  (recorder.getMaxAmplitude());
        else
            return 0;

    }
    public double presSoundPA() {
        double amp =  soundAmbient();
        ambient = filter * amp + (1.0 - filter) * ambient;
        return ambient;
    }

    public void soundDb(){
        DecimalFormat df1 = new DecimalFormat("####.0");
        double decib = soundDb(20);
        String decib1 = df1.format(decib);
        mainDb.setText(decib1 + " dB");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updater);
        if(recorder != null ){
            try{
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        handler.post(updater);
    }

   public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager)  getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(MyForegroundService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
   }



}