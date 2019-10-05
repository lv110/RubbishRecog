package com.example.rubbishrecog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class takephoto2 extends Fragment {

    private ImageButton totakephoto;
    private Intent intent;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.takephoto2_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        totakephoto = (ImageButton)getActivity().findViewById(R.id.totakephoto);
        textView = (TextView)getActivity().findViewById(R.id.text);
        textView.setTypeface(MainActivity.typeface);

        totakephoto.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    totakephoto.setImageResource(R.drawable.totakephoto1);
                    try {
                        Thread.sleep(300);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    intent = new Intent(getContext(),takephoto3.class);
                    startActivity(intent);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    totakephoto.setImageResource(R.drawable.totakephoto2);
                }
                return false;
            }
        });

    }
}
