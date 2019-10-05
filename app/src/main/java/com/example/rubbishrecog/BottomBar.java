package com.example.rubbishrecog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BottomBar extends LinearLayout implements View.OnClickListener {

    private static AppCompatActivity appCompatActivity;
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    public static boolean STATE1=false;
    public static boolean STATE2=false;
    public static boolean STATE3=false;


    public static void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        BottomBar.appCompatActivity = appCompatActivity;
    }

    public BottomBar(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.bottombar_layout,this);
        button1 = (ImageButton) findViewById(R.id.bottom_button_1);
        button2 = (ImageButton) findViewById(R.id.bottom_button_2);
        button3 = (ImageButton) findViewById(R.id.bottom_button_3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    //更换碎片
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bottom_button_1:
                if(!STATE1) {
                    addFragment(new takephoto2());
                    button1.setImageResource(R.drawable.photo_2);
                    STATE1=true;
                    button2.setImageResource(R.drawable.search_1);
                    STATE2=false;
                    button3.setImageResource(R.drawable.new_other_1);
                    STATE3=false;
                }
                break;
            case R.id.bottom_button_2:
                if(!STATE2) {
                    addFragment(new query());
                    button1.setImageResource(R.drawable.photo_1);
                    STATE1=false;
                    button2.setImageResource(R.drawable.search_2);
                    STATE2=true;
                    button3.setImageResource(R.drawable.new_other_1);
                    STATE3=false;
                }
                break;
            case R.id.bottom_button_3:
                if (!STATE3) {
                    addFragment(new other());
                    button1.setImageResource(R.drawable.photo_1);
                    STATE1=false;
                    button2.setImageResource(R.drawable.search_1);
                    STATE2=false;
                    button3.setImageResource(R.drawable.new_other_2);
                    STATE3=true;
                }
                break;
        }
    }

    //添加碎片
    public static void addFragment(Fragment fragment){
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.view_fragment,fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
