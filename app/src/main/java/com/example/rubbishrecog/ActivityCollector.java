package com.example.rubbishrecog;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

//活动池
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //结束所有活动
    public static void finishAll() {
        //Log.d(TAG, "finishAll: finish");
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}