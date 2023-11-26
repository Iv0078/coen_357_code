package com.example.coen357_final_sound;


import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
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

public class SoundMeasuring extends AppCompatActivity {

    public static final int APP_PERMISSIONS_RECORD_AUDIO =1;
    private final static String LOG_TAG="main";
    private ScreenVisualization screenVisualization;
    private MediaRecorder recorder;
    private Handler handler = new Handler();
    TextView txtName;


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
                    screenVisualization.addAmplitude(maxAmplitude);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_sound_measuring);
        screenVisualization = (ScreenVisualization) findViewById(R.id.visualization);


        screenVisualization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSoundListening();
            }
        });
        

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


}