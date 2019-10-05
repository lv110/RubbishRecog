package com.example.rubbishrecog;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;


public class MainActivity extends BaseActivity {
    public static Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        typeface = Typeface.createFromAsset(getAssets(),"font/cartoon.ttf");
//        getSupportActionBar().hide();
        BottomBar.setAppCompatActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.view_fragment,new takephoto2());
        //transaction.addToBackStack(null);
        transaction.commit();

        ImageButton imageButton = (ImageButton)findViewById(R.id.bottom_button_1);
        imageButton.setImageResource(R.drawable.photo_2);
        Toast.makeText(this,"本应用仅供参考和学习！\n具体分类标准请咨询专业人士！\n:D",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(takephoto3.instance!=null) {
            takephoto3.instance.finish();
            takephoto3.instance = null;
        }
    }
}
