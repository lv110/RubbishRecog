package com.example.rubbishrecog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Translation {
    //将结果翻译为中文结果，并确定相关垃圾分类
    private String[] resource;  //原结果
    private String[][] relative;//与其相关的垃圾分类

    public Translation() {
    }

    public String[] getResource() {
        return resource;
    }

    public String[][] getRelative() {
        return relative;
    }

    //最终结果处理函数
    public void finalresult(String result, Context context) {
        //读取翻译和垃圾类别

        ArrayList<String> labels = new ArrayList<String>();
        String actualFilename = "c.txt";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(actualFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
                //Log.d(TAG, "finalresult: "+line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading file!", e);
        }

        String[] group = result.substring(1, result.length() - 1).split(",");

        Log.d(TAG, "group: "+group.length);

        resource = new String[group.length];
        relative = new String[group.length][20];

        String id_str = new String();
        String probability = new String();
        String line = new String();
        String[] label;

        for (int i = 0; i < group.length; i++) {
            //提取有关信息
            id_str = group[i].substring(group[i].indexOf('[') + 1, group[i].indexOf(']'));
            probability = group[i].substring(group[i].indexOf('(') + 1, group[i].indexOf(')'));
            line = labels.get(Integer.valueOf(id_str));
            label = line.split(",");

            //原分类信息
            resource[i] = new String("" + label[0] + "  " + label[1] + "  " + probability);
            //相关分类信息
            for (int j = 2; j < 19&&j<label.length-1; j = j + 2) {
                relative[i][j / 2 - 1] = new String("" + label[j] + "  " + label[j + 1]);
            }
        }

    }
}
