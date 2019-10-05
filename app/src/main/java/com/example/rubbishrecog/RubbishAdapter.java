package com.example.rubbishrecog;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RubbishAdapter extends RecyclerView.Adapter<RubbishAdapter.ViewHolder> {

    private List<Rubbish> mRubbishList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rubbishImage;
        TextView rubbishName;
        LinearLayout view;
        public ViewHolder(View view){
            super(view);
            rubbishImage =(ImageView) view.findViewById(R.id.rubbish_image);
            rubbishName = (TextView) view.findViewById(R.id.rubbish_name);
            this.view=(LinearLayout) view.findViewById(R.id.item_background);
        }
    }

    public RubbishAdapter(List<Rubbish> rubbishList){
        mRubbishList = rubbishList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rubbish_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rubbish rubbish = mRubbishList.get(position);
        holder.rubbishImage.setImageResource(rubbish.getImageId());
        holder.rubbishName.setText(rubbish.getName());
        holder.rubbishName.setTypeface(MainActivity.typeface);

        int color=Color.rgb(0,0,0);
        switch(rubbish.getImageId()){
            case R.drawable.recycle :
                color=Color.rgb(0,0,205);
                break;
            case R.drawable.other :
                color=Color.rgb(128,128,128);
                break;
            case R.drawable.harmful :
                color=Color.rgb(255,69,0);
                break;
            case R.drawable.foodscrap :
                color=Color.rgb(34,139,34);
                break;
            case R.drawable.bigrubbish :
                color=Color.rgb(64,224,208);
                break;
            case R.drawable.notrubbish :
                color=Color.rgb(255,215,0);
                break;
            default :
                break;
        }
        holder.view.setBackgroundColor(color);
        holder.view.getBackground().setAlpha(30);
    }

    @Override
    public int getItemCount() {
        return mRubbishList.size();
    }
}
