package com.example.rubbishrecog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class query extends Fragment {

    private RecyclerView recyclerView;
    private String rubbishText;
    private String jsonText;
    private EditText searchText;
    private List<Rubbish> rubbishList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchText = (EditText) getActivity().findViewById(R.id.search_text);

        jsonText = getJson("trash.json",getActivity());
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view2);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    rubbishList.clear();
                    rubbishText = searchText.getText().toString();

                    parseJSONWithJSONObject(jsonText,rubbishText);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    RubbishAdapter adapter = new RubbishAdapter(rubbishList);

                    //Toast.makeText(getContext(),""+rubbishText,Toast.LENGTH_LONG).show();
                    if(rubbishList.size()==0){
                        Toast.makeText(getContext(),"未找到相关结果，请多试几次:(",Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.setAdapter(adapter);

                }
                return false;
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }
    private void parseJSONWithJSONObject(String jsonData, String key) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            Log.d(TAG, "parseJSONWithJSONObject: " + jsonArray.length());
            String name;
            String category;
            Rubbish rubbish;
            String s;
            String realCategory;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.getString("name");
                category = jsonObject.getString("category");
                realCategory = "";
                int id=R.drawable.recycle;
                if (category.equals("1")) {
                    realCategory = "可回收垃圾";
                    id=R.drawable.recycle;
                } else if (category.equals("2")) {
                    realCategory = "有害垃圾";
                    id=R.drawable.harmful;
                } else if (category.equals("4")) {
                    realCategory = "湿垃圾";
                    id=R.drawable.foodscrap;
                } else if (category.equals("8")) {
                    realCategory = "干垃圾";
                    id=R.drawable.other;
                } else if (category.equals("16")) {
                    realCategory = "大件垃圾";
                    id=R.drawable.bigrubbish;
                }
                if (name.contains(key)) {
                    s = String.format("名称：%s \n类别：%s",name,realCategory);
                    rubbish = new Rubbish(s,id);
                    rubbishList.add(rubbish);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getJson(String fileName, Context context) {
//        将json数据转化为字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
//        获取assets资源管理器
            AssetManager assetManager = context.getAssets();
//        通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
