package com.example.androidforegroundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.androidforegroundservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent foreGroundServiceIntent = new Intent(this, MyForeGroundService.class);

        binding.btnStartService.setOnClickListener(view -> {
            if (!isMyServiceRunning(MyForeGroundService.class)){
                foreGroundServiceIntent.setAction("start");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(foreGroundServiceIntent);
                }else {
                    startService(foreGroundServiceIntent);
                }
            }
        });

        binding.btnStopService.setOnClickListener(view -> {
            if (isMyServiceRunning(MyForeGroundService.class)){
                foreGroundServiceIntent.setAction("stop");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(foreGroundServiceIntent);
                }else {
                    startService(foreGroundServiceIntent);
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}