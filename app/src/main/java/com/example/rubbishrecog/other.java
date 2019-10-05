package com.example.rubbishrecog;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class other extends Fragment{
    private ImageButton quit;
    private TextView textView;
    private TextView quitText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView = (TextView) getActivity().findViewById(R.id.text2);
        textView.setTypeface(MainActivity.typeface);
        quitText=(TextView) getActivity().findViewById(R.id.quit_text);
        quitText.setTypeface(MainActivity.typeface);
        quit = (ImageButton)getActivity().findViewById(R.id.button_quit);

        quit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    try {
                        Thread.sleep(300);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    quit.setImageResource(R.drawable.button1);
                    try {
                        Thread.sleep(300);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ActivityCollector.finishAll();

                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    quit.setImageResource(R.drawable.button2);
                }
                return false;
            }
        });
    }
}
