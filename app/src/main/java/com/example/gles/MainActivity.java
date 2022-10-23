package com.example.gles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MainView mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        setContentView(R.layout.activity_main);
        mainView = (MainView) findViewById(R.id.mainView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainView.onPause();
    }


}
