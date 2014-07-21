package com.daylab.g2048;

import java.util.List;

import com.daylab.g2048.base.Config;
import com.daylab.g2048.base.Score;
import com.daylab.g2048.ui.ScaleImageView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_score);
		mGridView = (GridView)findViewById(R.id.gridView1);
		mGridView.setAdapter(new MyAdapter(this));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( "Score" );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( "Score" );
		MobclickAgent.onPause(this);
	}	
	
	public class MyAdapter extends BaseAdapter {
		private Context mContext;
		private List<Score> mScoreData;
		public MyAdapter(Context context) {
			mContext = context;
			mScoreData = Score.getAll();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mScoreData.size()*3;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
	            convertView = LinearLayout.inflate(mContext, R.layout.adapter_score_item, null);
	            holder = new ViewHolder();
	            holder.mText = (TextView) convertView.findViewById(R.id.grid_textView);
	            holder.mImageBg = (ScaleImageView)convertView.findViewById(R.id.scale_imageView);
	            convertView.setTag(holder);
	            int width = (Config.EXACT_SCREEN_WIDTH-50)/5;
	            holder.mImageBg.setImageWidth(width);
	            holder.mImageBg.setImageHeight(width);
	        } else {
	            holder = (ViewHolder) convertView.getTag();
	        }
			
			int col = position %3;
			int line = position/3;
			Score score = mScoreData.get(line);
			holder.mText.setVisibility(View.VISIBLE);
			holder.mImageBg.setVisibility(View.GONE);
			if(col == 0) {
				holder.mText.setText(line+1+"");
			}
			if(col == 1) {
				if(score.gridvalue == 0)
					holder.mText.setText(score.maxvalue+"");
				else if(score.gridvalue == 1) {
					holder.mText.setText(PaneInfo.getValueText(score.gridvalue, score.maxvalue));
				}else if(score.gridvalue == 2) {
					holder.mText.setText(PaneInfo.getValueText(score.gridvalue, score.maxvalue));
//					holder.mText.setVisibility(View.INVISIBLE);
//					holder.mImageBg.setVisibility(View.VISIBLE);
//					holder.mImageBg.setImageResource(PaneInfo.getValueBgRes(score.maxvalue));
				}
			}
			if(col == 2) {
				holder.mText.setText(score.score+"");
			}			
			return convertView;
		}
		
		private class ViewHolder{
			ScaleImageView mImageBg;
			TextView mText;
		}
		
	}
}
