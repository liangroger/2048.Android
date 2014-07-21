package com.daylab.g2048;

import java.util.ArrayList;

import com.daylab.g2048.base.Config;
import com.daylab.g2048.ui.ScaleImageView;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<PaneInfo> mInfos;
	
	public GridAdapter(Context context,ArrayList<PaneInfo> data) {
		mContext = context;
		mInfos = data;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int width = (Config.EXACT_SCREEN_WIDTH-50)/5;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.adapter_grid_item, null);
            holder = new ViewHolder();
            holder.mText = (TextView) convertView.findViewById(R.id.grid_textView);
            holder.mImageBg = (ScaleImageView)convertView.findViewById(R.id.scale_imageView);
            convertView.setTag(holder);
            
            holder.mImageBg.setImageWidth(width);
            holder.mImageBg.setImageHeight(width);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        PaneInfo info = mInfos.get(position);
        if(info.value > 1) {
        	holder.mText.setVisibility(View.VISIBLE);
        	if(MainActivity.displayType == 2) {
        		holder.mText.setText("");
        		holder.mText.setBackgroundResource(info.getValueBgRes());
        	}else {
	        	holder.mText.setText(info.getValueText(MainActivity.displayType));
	        	holder.mText.setBackgroundColor(Color.parseColor(info.getValueColor()));
	        	if(info.value > 4) {
	        		holder.mText.setTextColor(Color.WHITE);
	        	}else {
	        		holder.mText.setTextColor(Color.GRAY);	
	        	}
        	}
        	if(info.isNew) {
        		info.isNew = false;
        		AlphaAnimation alpha = new AlphaAnimation(0, 1); 
        		alpha.setDuration(1000);
        		convertView.startAnimation(alpha);
        	}
        	if(info.join) {
        		info.join = false;
        		RotateAnimation rotate =new RotateAnimation(360f,0f,width/2,width/2); 
        		rotate.setDuration(500);
        		convertView.startAnimation(rotate);
        	}
        }else {
        	holder.mText.setVisibility(View.INVISIBLE);
        }
        return convertView;
	}
	
	private class ViewHolder{
		ScaleImageView mImageBg;
		TextView mText;
	}
}
