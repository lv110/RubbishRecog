package com.example.rubbishrecog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowResult extends BaseActivity {

    private static final String TAG = "ShowResult";

    private TextView textView;
    private String finalResult;
    private String finalRelative;
    private String s;
    private String[] resultList;
    private String[] relativeList;
    private List<Rubbish> rubbishList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        textView = (TextView)findViewById(R.id.text1);
        textView.setTypeface(MainActivity.typeface);
        Intent intent = getIntent();
        finalResult = intent.getStringExtra("finalResult");
        finalRelative = intent.getStringExtra("finalRelative");

        //Toast.makeText(ShowResult.this,finalRelative,Toast.LENGTH_LONG).show();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if(!finalResult.equals("")) {
            resultList = finalResult.split("\n");
            relativeList = finalRelative.split("\n");
            //Toast.makeText(ShowResult.this,""+relativeList.length,Toast.LENGTH_LONG).show();
            initRubbish();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RubbishAdapter adapter = new RubbishAdapter(rubbishList);
            recyclerView.setAdapter(adapter);
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            Toast.makeText(ShowResult.this,"无法识别，请多试几次 :(",Toast.LENGTH_SHORT).show();
        }
    }
    private void initRubbish() {
//        Toast.makeText(ShowResult.this,resultList.length,Toast.LENGTH_LONG).show();
        int id;
        String[] split;
        for (int i = 0; i < resultList.length; i++) {
            if(!resultList[i].equals("")) {
                split = resultList[i].split("  ");
//                Log.d(TAG, "initRubbish: "+split[1]);
//                Log.d(TAG, "initRubbish: "+split[1].indexOf("干"));
                id = findID(split[1]);
                s = String.format("名称：%s \n类别：%s \n可信度：%s",split[0],split[1],split[2]);

                //Rubbish rubbish = new Rubbish(resultList[i], id);
                Rubbish rubbish = new Rubbish(s, id);
                rubbishList.add(rubbish);
            }
        }

        for (int i = 0; i < relativeList.length; i++) {
            if(!relativeList[i].equals("null")) {
                //Toast.makeText(ShowResult.this,relativeList[i],Toast.LENGTH_LONG).show();
                Log.d(TAG, "initRubbish: "+relativeList[i]);
                split = relativeList[i].split("  ");
                id = findID(split[1]);
                s = String.format("名称：%s \n类别：%s",split[0],split[1]);
                Rubbish rubbish = new Rubbish(s, id);
                rubbishList.add(rubbish);
            }
        }
    }
    private int findID(String s){
        if (s.indexOf("大")==0){
            return R.drawable.bigrubbish;
        }
        if(s.indexOf("干")==0){
            return R.drawable.other;
        }
        if(s.indexOf("有")==0){
            return R.drawable.harmful;
        }
        if(s.indexOf("湿")==0){
            return R.drawable.foodscrap;
        }
        if(s.indexOf("非")==0){
            return R.drawable.notrubbish;
        }
        return R.drawable.recycle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        takephoto3.instance.finish();
        takephoto3.instance=null;
    }
}
