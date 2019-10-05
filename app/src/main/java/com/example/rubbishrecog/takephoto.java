package com.example.rubbishrecog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

//照相碎片
public class takephoto extends Fragment {
    private CameraView cameraView;//相机
    private ImageButton btnToggleCamera;
    private ImageButton btnDetectObject;
    private ProgressBar progressBar;
    private String englishResult;
    private String finalResult;
    private String finalRelative;
    private Intent intent;
    private Bitmap bitmap;
    private static Handler handler;
    private static final int RECOGNIZE_PHOTO =1; //Message用
    private LinearLayout window;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.takephoto_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cameraView=getActivity().findViewById(R.id.cameraView);
//        if(cameraView!=null){
//            Toast.makeText(getContext(),"OK",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(getContext(),"NO",Toast.LENGTH_LONG).show();
//        }
        btnDetectObject = (ImageButton)getActivity().findViewById(R.id.button_photo);
        btnToggleCamera = (ImageButton)getActivity().findViewById(R.id.button_toggle);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        window=getActivity().findViewById(R.id.window);



        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RECOGNIZE_PHOTO:
                        ImageClassification imageClassification = new ImageClassification();
                        englishResult = imageClassification.imageclassification(bitmap, getContext());
                        if (englishResult != null&&!englishResult.equals("[]")) {
                            finalResult="";
                            finalRelative="";

                            Toast.makeText(getContext(), englishResult, Toast.LENGTH_LONG).show();

                            Translation translation = new Translation();
                            translation.finalresult(englishResult,getContext());
                            for(int i = 0;i<translation.getResource().length;i++){
                                finalResult += translation.getResource()[i]+"\n";
                            }

                            for (int i =0;i<translation.getRelative()[0].length;i++){
                                finalRelative += translation.getRelative()[0][i]+"\n";
                            }
                        } else {
                            //Toast.makeText(getContext(), "nothing", Toast.LENGTH_LONG).show();
                            finalResult="";
                            finalRelative="";
                        }


                        intent = new Intent(getContext(), ShowResult.class);
                        intent.putExtra("finalResult",finalResult);
                        intent.putExtra("finalRelative",finalRelative);
                        startActivity(intent);
                        break;
                }
            }

        };

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) { }
            @Override
            public void onError(CameraKitError cameraKitError) { }
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                progressBar.setVisibility(View.VISIBLE);
                bitmap = cameraKitImage.getBitmap();
                new Thread(new Runnable() {
                     @Override
                     public void run() {
                         Message message = new Message();
                         message.what=RECOGNIZE_PHOTO;
                         handler.sendMessage(message);
                     }
                 }).start();
                cameraView.stop();
            }
            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) { }
        });

        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }

        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
