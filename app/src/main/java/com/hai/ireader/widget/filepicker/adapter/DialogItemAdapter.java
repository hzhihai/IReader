package com.hai.ireader.widget.filepicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hai.ireader.R;

public class DialogItemAdapter extends BaseAdapter {

    private ViewHolder holder;
    public String[] list;
    LayoutInflater inflater;
    private static Boolean[] isFocused;
    private static int whichClick = -1;
    private static int temp = 0;

    public DialogItemAdapter(Context context, String[] list,int state){
        this.list = list;
        inflater = LayoutInflater.from(context);
        isFocused = new Boolean[list.length];
        for (int i=0;i<list.length;i++){
            isFocused[i]=false;
        }

        isFocused[state]=true;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int i) {
        if (i == getCount()||list == null) {
            return null;
        }
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void selectorkState(int position){
        isFocused[whichClick==-1?0:whichClick]=false;
        temp = whichClick;
        whichClick = position;
        isFocused[position]=true;
        notifyDataSetChanged();
    }




    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_item, null);
            holder.typeTextView = (TextView)convertView.findViewById(R.id.typeTextview);
            holder.stateImageView=(ImageView)convertView.findViewById(R.id.stateImageView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.stateImageView.setSelected(isFocused[position]);
        holder.typeTextView.setText(getItem(position));
        return convertView;
    }

    public static class ViewHolder{
        public TextView typeTextView;
        public ImageView stateImageView;
    }
}
