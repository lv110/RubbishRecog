package com.example.rubbishrecog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class takephoto3 extends AppCompatActivity {
    public static AppCompatActivity instance = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takephoto3_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.view_fragment2,new takephoto());
        //transaction.addToBackStack(null);
        transaction.commit();
        instance=this;
    }

    @Override
    protected void onPause() {
        super.onPause();
       // this.finish();
    }
}
