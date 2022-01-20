package com.example.androidforegroundservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
                startForegroundService(foreGroundServiceIntent);
            }
        });

        binding.btnStopService.setOnClickListener(view -> {
            if (isMyServiceRunning(MyForeGroundService.class)){
                foreGroundServiceIntent.setAction("stop");
                startForegroundService(foreGroundServiceIntent);
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