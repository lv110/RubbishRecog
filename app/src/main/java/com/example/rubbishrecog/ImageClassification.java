package com.example.rubbishrecog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

public class ImageClassification {
    /*
    基于inception v3的图像分类器
    在gradle中添加如下代码
    implementation 'org.tensorflow:tensorflow-android:+'
    在main文件夹下创建assests，并放入：
    imagenet_comp_graph_label_strings.txt
    tensorflow_inception_graph.pb
    创建对象实例，调用imageclassification即可使用
    imageclassification的第一个参数为Bitmap格式的图像
    第二个参数为上下文，返回字符串结果
    其他参数说明见注释
    */
    private static final String TAG = "ImageClassification";

    private int input_size_height = 224;   //输入图片尺寸
    private int input_size_width = 224;
    private String input_name = "input";    //输入tensor名称
    private String output_name = "output";  //输出tensor名称
    private String[] outpur_names = new String[]{output_name};
    private String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb"; //模型存放的地址
    private String labelFilename = "file:///android_asset/imagenet_comp_graph_label_strings.txt";//存放标签的地址
    private int[] intValues = new int[input_size_width*input_size_height];//用于存放图片信息的数组
    private float[] floatValues = new float[input_size_width*input_size_height*3]; //归一化后的数据
    private float imageMean = 117; //假定的平均像素值
    private float imageStd = 1;  //用于归一化
    private float THRESHOLD = 0.1f;//置信度
    private int MAX_RESULTS = 3; //最多输出的结果个数

    //构造函数

    public ImageClassification(){}

    public ImageClassification(int input_size_height, int input_size_width, String input_name, String output_name, String[] outpur_names, String MODEL_FILE, String labelFilename, int[] intValues, float[] floatValues, float imageMean, float imageStd, float THRESHOLD, int MAX_RESULTS) {
        this.input_size_height = input_size_height;
        this.input_size_width = input_size_width;
        this.input_name = input_name;
        this.output_name = output_name;
        this.outpur_names = outpur_names;
        this.MODEL_FILE = MODEL_FILE;
        this.labelFilename = labelFilename;
        this.intValues = intValues;
        this.floatValues = floatValues;
        this.imageMean = imageMean;
        this.imageStd = imageStd;
        this.THRESHOLD = THRESHOLD;
        this.MAX_RESULTS = MAX_RESULTS;
    }

    //用于辅助识别的内部类
    class Recognition {
        private final String id;
        private final String title;
        private final Float confidence;
        private RectF location;
        public Recognition(
                final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }
        public String getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }
        public Float getConfidence() {
            return confidence;
        }
        public RectF getLocation() {
            return new RectF(location);
        }
        public void setLocation(RectF location) {
            this.location = location;
        }
        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }
            if (title != null) {
                resultString += title + " ";
            }
            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }
            if (location != null) {
                resultString += location + " ";
            }
            return resultString.trim();
        }
    }

    //将bitmap格式的图片进行压缩
    private Bitmap changebitmap(Bitmap bitmap,int newWidth,int newHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e("width","width:"+width);
        Log.e("height","height:"+height);
        //设置想要的大小
        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;
        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();
        Log.e("newWidth","newWidth"+bitmap.getWidth());
        Log.e("newHeight","newHeight"+bitmap.getHeight());
        return bitmap;
    }

    //识别图片的主要函数
    public String imageclassification(Bitmap bitmap, Context context){

        bitmap = changebitmap(bitmap,input_size_width,input_size_height);//变更图片尺寸
        TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface(
                context.getAssets(),MODEL_FILE);            //读取已经训练完成的模型

        //将图片信息存入数组中
        bitmap.getPixels(intValues, 0, input_size_width, 0, 0, input_size_width, input_size_height);
        //图片归一化
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
        }

        //计算输出类别的个数
        Operation operation = inferenceInterface.graphOperation(output_name);
        int numClasses = (int) operation.output(0).shape().size(1);
        float[] outputs = new float[numClasses];//用于存放结果的数组

        //运行模型
        inferenceInterface.feed(input_name, floatValues, 1, input_size_width, input_size_height, 3);
        inferenceInterface.run(outpur_names,false);
        inferenceInterface.fetch(output_name, outputs);

        Vector<String> labels = new Vector<String>();
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        Log.i(TAG, "Reading labels from: " + actualFilename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(actualFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading label file!", e);
        }

        //找出类别
        PriorityQueue<Recognition> pq =
                new PriorityQueue<Recognition>(
                        3,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });
        for (int i = 0; i < outputs.length; ++i) {
            if (outputs[i] > THRESHOLD) {
                pq.add(
                        new Recognition(
                                "" + i, labels.size() > i ? labels.get(i) : "unknown", outputs[i], null));
            }
        }
        final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }

        return recognitions.toString();
    }
}
