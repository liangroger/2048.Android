package com.daylab.g2048;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import cn.sharesdk.framework.ShareSDK;

import com.daylab.g2048.base.BMapUtil;
import com.daylab.g2048.base.Config;
import com.daylab.g2048.base.Score;
import com.daylab.g2048.base.SharedPreferencesManager;
import com.daylab.g2048.base.Utils;
import com.daylab.g2048.ui.NoScrollGridView;
import com.umeng.analytics.MobclickAgent;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGestureListener {
	private View mParentView;
	private NoScrollGridView mGridView;
	private GridAdapter mAdapter;

	private ArrayList<PaneInfo> mInfos;
	private ArrayList<PaneInfo> mBeforeInfos;
	private ArrayList<PaneInfo> mTempInfos;
	private boolean canBefore;
	private GestureDetector gd;
	private java.util.Random rand;
	
	private int LINE = 4,COLUME = 4;
	private int ALL = LINE*COLUME;
	private int END_NUMBER = 2048;//游戏完成的分数值
	private int MAX_NUMBER;//当局游戏中最大方块值
	private int gridType=0;
	public static int displayType = 0;//当前进行的版本值
	
	private boolean mSuccessful;//已经成功了，但可以继续
	private int mScoreCur;//每一移动的得分
	private int mScore;
	private int mMove;
	private int mTopScore;//最高分
	private TextView mScoreView;
	private TextView mMoveView;
	private TextView mTopScoreView;
	private TextView mDestView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getDeviceDensity();
		
		setContentView(R.layout.activity_main);
		mParentView = findViewById(R.id.layout_pane);
		mScoreView = (TextView)findViewById(R.id.textView2);
		mMoveView = (TextView)findViewById(R.id.textView1);
		mTopScoreView = (TextView)findViewById(R.id.textView3);
		mDestView = (TextView)findViewById(R.id.textView4);
		findViewById(R.id.topscore_pane).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,ScoreActivity.class));
			}
		});
		
		mGridView = (NoScrollGridView) findViewById(R.id.gridView);
		mGridView.setNumColumns(COLUME);
		mInfos = new ArrayList<PaneInfo>();
		mBeforeInfos = new ArrayList<PaneInfo>();
		mTempInfos = new ArrayList<PaneInfo>();
		mAdapter = new GridAdapter(this, mInfos);
		mGridView.setAdapter(mAdapter);
		gd = new GestureDetector(this);
		
		//初始化数据
		rand = new java.util.Random(); 
		reset(false);
		
		//加载历史数据
		if(SharedPreferencesManager.getBooleanInfo(this, "save")) {
			String progress = SharedPreferencesManager.getStringInfo(this, "progress");
			if(progress.length() > 2 && progress.startsWith("[") && progress.endsWith("]")) {
				String sub = progress.substring(1, progress.length()-1);
				String[] infos = sub.split(",");
				if(infos.length == mInfos.size()) {
					mScore = SharedPreferencesManager.getIntInfo(this, "score");
					mMove = SharedPreferencesManager.getIntInfo(this, "move");
					mSuccessful = SharedPreferencesManager.getBooleanInfo(this, "successful");
					mInfos.clear();
					for(int i=0;i<infos.length;i++) {
						String val = infos[i];
						val = val.trim();
						PaneInfo info = new PaneInfo();
						info.position = info.oldPosition = i;
						info.value = Integer.parseInt(val);
						mInfos.add(info);
					}
					updateView();
				}
			}
		}
		mTopScore = SharedPreferencesManager.getIntInfo(this, "top_score");
		mTopScoreView.setText(mTopScore+"");
		
		MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
		MobclickAgent.updateOnlineConfig(this);
		
		//初始化
		ShareSDK.initSDK(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( "Main" );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( "Main" );
		MobclickAgent.onPause(this);
	}
	
	/**
	* @Title: loadSetting
	* @Description: 加载保存的设置
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	private void loadSetting() {
		{
			SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
			String value = shp.getString("list_preference", "0"); 
			int intValue = Integer.parseInt(value);
			gridType = intValue;
			if(intValue == 0) {
				LINE = 4;
				COLUME = 4;
			}else if(intValue == 1) {
				LINE = 5;
				COLUME = 5;
			}else if(intValue == 2) {
				LINE = 6;
				COLUME = 6;
			}
			mGridView.setNumColumns(COLUME);
			ALL = LINE*COLUME;
		}
		{
			SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
			String value = shp.getString("list_preference2", "0"); 
			int intValue = Integer.parseInt(value);
			displayType = intValue;
			if(intValue == 0) {
				END_NUMBER = 2048;
				mDestView.setText("生成一个2048的数字。");
			}else if(intValue == 1){
				END_NUMBER = 65536;
				mDestView.setText("尽快发展成天朝时代！");
			}else {
				END_NUMBER = 4096;
				mDestView.setText("完成生肖顺序。");
			}
		}
	}
	
	private void reset(boolean clean) {
		if(clean) {
			SharedPreferencesManager.saveBooleanInfo(this, "save", false);
			//这里是每局结束后保存分数的地方
			saveData2Db();
		}
		loadSetting();
		canBefore = false;
		mInfos.clear();
		mScore = 0;
		mMove = 0;
		mSuccessful = false;
		MAX_NUMBER = 2;
		
		int first=rand.nextInt(ALL),second=rand.nextInt(ALL);
		if(first == second) {
			second++;
			if(second == ALL-1)
				second = 0;
		}
		
		for(int i=0;i<ALL;i++) {
			PaneInfo info = new PaneInfo();
			if(i == first || i == second) {
				info.value = 2;
			}
			info.position = info.oldPosition = i;
			mInfos.add(info);
		}
		updateView();
	}
	
	public void restart(View view) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定要重新开始吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				reset(true);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void before(View view) {
		Toast.makeText(this, canBefore?"你采取了反悔一步！":"当前不能反悔", Toast.LENGTH_SHORT).show();
		if(canBefore) {
			mInfos.clear();
			for(PaneInfo pane:mBeforeInfos) {
				mInfos.add(pane);
			}
			canBefore = false;
			updateView();
		}
	}
	
	public void setting(View view) {
		startActivityForResult(new Intent(this,SettingActivity.class),1);
	}
	
	public void share(View view) {
		MobclickAgent.onEvent(this, "share", ""+mScore);
		
		String filepath = Utils.getCacheRootPath(this)+"/screen_2014.png";
		Log.i("", "file:"+filepath);
		Bitmap bitmap = BMapUtil.takeScreenShot(this);//getBitmapFromView(mParentView);
		BMapUtil.saveBitmapToPng(bitmap, filepath);
		
		Utils.showShare(this, "我在玩多样2048，很在趣，一起来玩吧！", "", "", filepath);
//		Intent intent=new Intent(Intent.ACTION_SEND);    
//		 intent.setType("text/plain"); //纯文本
//		 Uri uri = Uri.fromFile(new File(filepath));
//		 intent.putExtra(Intent.EXTRA_STREAM, uri);
//		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//		intent.putExtra(Intent.EXTRA_TEXT,
//				"我在玩多样2048，很在趣，一起来玩吧！");
//		startActivity(Intent.createChooser(intent, getTitle()));
	}
	
	private void save2Before(ArrayList<PaneInfo> infos) {
		mBeforeInfos.clear();
		for(PaneInfo pane:infos) {
			mBeforeInfos.add(pane);
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void finish() {
		super.finish();
		SharedPreferencesManager.saveIntInfo(this, "top_score", mTopScore);
	}
	
	/**
     * 获取当前设备的屏幕密度等基本参数
     */
    protected void getDeviceDensity(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.DENSITY = metrics.density;
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH  = metrics.widthPixels;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
    		AlertDialog.Builder builder = new Builder(this);
    		builder.setMessage("确定退出当前游戏吗？");
    		builder.setTitle("提示");
//    		builder.setNeutralButton("保存并退出", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int arg1) {
//					Log.i("","progress:"+ mInfos.toString());
//					//progress:[1, 1, 4, 4, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 1, 1]
//					SharedPreferencesManager.saveBooleanInfo(MainActivity.this, "save", true);
//					SharedPreferencesManager.saveStringInfo(MainActivity.this, "progress", mInfos.toString());
//					SharedPreferencesManager.saveIntInfo(MainActivity.this, "score", mScore);
//					SharedPreferencesManager.saveIntInfo(MainActivity.this, "move", mMove);
//					SharedPreferencesManager.saveBooleanInfo(MainActivity.this, "successful", mSuccessful);
//    				finish();
//    				dialog.dismiss();
//				}
//			});
    		builder.setPositiveButton("退出", new OnClickListener() {
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
//    				SharedPreferencesManager.saveBooleanInfo(MainActivity.this, "save", false);
    				//自动保存当前进度
					SharedPreferencesManager.saveBooleanInfo(MainActivity.this, "save", true);
					SharedPreferencesManager.saveStringInfo(MainActivity.this, "progress", mInfos.toString());
					SharedPreferencesManager.saveIntInfo(MainActivity.this, "score", mScore);
					SharedPreferencesManager.saveIntInfo(MainActivity.this, "move", mMove);
					SharedPreferencesManager.saveBooleanInfo(MainActivity.this, "successful", mSuccessful);
    				finish();
    				dialog.dismiss();
    			}
    		});
    		builder.setNegativeButton("取消", new OnClickListener() {
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			}
    		});
    		builder.create().show();
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gd.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float absX = Math.abs(e1.getX() - e2.getX());
		float absY = Math.abs(e1.getY() - e2.getY());
		if (absX >= absY) {
			if ((e1.getX() - e2.getX()) > 30) {
//				Log.i("", "to left");
				turnWithType(0);
				return true;
			} else if ((e1.getX() - e2.getX()) < -30) {
//				Log.i("", "to right");
				turnWithType(1);
				return true;
			}
		}
		
		if ((e1.getY() - e2.getY()) > 30) {
//			Log.i("", "to up");
			turnWithType(2);
			return true;
		} else if ((e1.getY() - e2.getY()) < -30) {
//			Log.i("", "to down");
			turnWithType(3);
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private int getNullItemNum() {
		int result = 0;
		for(int i=0;i<ALL;i++) {
			if(mInfos.get(i).value == 1)
				result++;
		}
		return result;
	}
	
	private void newItem() {
		int num = getNullItemNum();//有num个空格可以插入
		if(num > 0) {
			int r = rand.nextInt(num);//新出现的块在随机位置r
			int j=0;
			for(int i=0;i<ALL;i++) {
				if(mInfos.get(i).value == 1) {
					if(j == r) {
						//如果num少于一行半时，会出现4的可能
						int addNum = 2;
						if(num < LINE*3/2) {
							int a = (int)(Math.random()*100);
							boolean four = a%2 == 1;
							if(four)
								addNum = 4;
						}
						mInfos.get(i).position = mInfos.get(i).oldPosition = r;
						mInfos.get(i).value = addNum;
						mInfos.get(i).isNew = true;
						break;
					}
					j++;
				}
			}
		}
		if(num <= 1) {
			//检查是否游戏结束
			if(checkIsEnd())
				showFail();
		}
	}
	
	private boolean deal(ArrayList<PaneInfo> source) {
		ArrayList<PaneInfo> result = new ArrayList<PaneInfo>();
		//复制
		boolean change = false;
		int fore=0;
		for(int i=0;i<source.size();i++) {
			PaneInfo pane = source.get(i);
			result.add(pane);
			
			//判断此次移动是否有方块合并
			if(fore==0)
				fore = pane.value;
			else {
				int cur = pane.value;
				if(fore==1) {
					if(cur > 1) {
						fore = cur;
						change = true;
					}
				}else {
					if(cur == fore) {
						change = true;
					}else {
						fore = cur;
					}
				}
			}
		}
		
		//先消除初始值的块
		for(int i=0;i<result.size();i++) {
			PaneInfo pane = result.get(i);
			if(pane.value == 1) {
				result.remove(i);
				i--;
			}
		}
		
		//合并值
		for(int i=0;i<result.size()-1;i++) {
			PaneInfo pane1 = result.get(i);
			PaneInfo pane2 = result.get(i+1);
			if(pane1.value == pane2.value) {
				pane1.value = pane1.value*2;
				pane1.join = true;
				result.remove(i+1);
				mScoreCur += pane1.value;
				if(MAX_NUMBER < pane1.value )
					MAX_NUMBER = pane1.value;
				if(pane1.value == END_NUMBER && !mSuccessful) {
					showSuccess();
				}
			}
		}
		
		//赋新的位置值
		for(int i=0;i<source.size();i++) {
			if(i<result.size()) {
				source.get(i).value = result.get(i).value;
			}else {
				source.get(i).value = 1;
			}
		}
		
		return change;
	}
	
	private void turnWithType(int type) {
		if(displayType > 0) {
			if(END_NUMBER == MAX_NUMBER) {
				showSuccess();
				return;
			}
		}
		mTempInfos.clear();
		for(PaneInfo pane:mInfos) {
			mTempInfos.add(pane.getClone());
		}
		
		mMove++;
		mScoreCur=0;
		boolean change = false;
		if(type == 0) 
			change = turnLeft();
		else if(type == 1)
			change = turnRight();
		else if(type == 2)
			change = turnUp();
		else
			change = turnDown();
		mScore += mScoreCur;
		if(change) {
			newItem();
			canBefore = true;
			save2Before(mTempInfos);
		}else {
			if(getNullItemNum() == 0 && checkIsEnd()) {
				showFail();
			}
		}
		updateView();
	}
	
	private boolean turnLeft() {
		boolean change = false;
		for(int h=0;h<LINE;h++) {
			ArrayList<PaneInfo> result = new ArrayList<PaneInfo>();
			for(int v=0;v<COLUME;v++) {
				result.add(mInfos.get(LINE*h+v));
			}
			if(deal(result))
				change = true;
		}
		return change;
	}
	
	private boolean turnRight() {
		boolean change = false;
		for(int h=0;h<LINE;h++) {
			ArrayList<PaneInfo> result = new ArrayList<PaneInfo>();
			for(int v=COLUME-1;v>=0;v--) {
				result.add(mInfos.get(LINE*h+v));
			}
			if(deal(result))
				change = true;
		}
		return change;
	}
	
	private boolean turnUp() {
		boolean change = false;
		for(int h=0;h<COLUME;h++) {
			ArrayList<PaneInfo> result = new ArrayList<PaneInfo>();
			for(int v=0;v<LINE;v++) {
				result.add(mInfos.get(LINE*v+h));
			}
			if(deal(result))
				change = true;
		}
		return change;
	}
	
	private boolean turnDown() {
		boolean change = false;
		for(int h=0;h<COLUME;h++) {
			ArrayList<PaneInfo> result = new ArrayList<PaneInfo>();
			for(int v=LINE-1;v>=0;v--) {
				result.add(mInfos.get(LINE*v+h));
			}
			if(deal(result))
				change = true;
		}
		return change;
	}
	
	private boolean checkIsEnd() {
		//横竖列是否有相临相同的值
		for(int h=0;h<LINE;h++) {
			for(int v=0;v<COLUME-1;v++) {
				if(mInfos.get(h*LINE+v).value == mInfos.get(h*LINE+v+1).value)
					return false;
			}
		}
		for(int h=0;h<COLUME;h++) {
			for(int v=0;v<LINE-1;v++) {
				if(mInfos.get(LINE*v+h).value == mInfos.get(LINE*(v+1)+h).value)
					return false;
			}
		}
		return true;
	}
	
	private void updateView() {
		mAdapter.notifyDataSetChanged();
		mMoveView.setText(""+mMove);
		mScoreView.setText(""+mScore);
		if(mTopScore < mScore) {
			mTopScore = mScore;
			mTopScoreView.setText(""+mTopScore);
		}
	}
	
	private void animation(View view) {
		TranslateAnimation animation = new TranslateAnimation(30.0f, -80.0f, 30.0f, 300.0f); 
		animation.setDuration(2000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void showFail() {
		MobclickAgent.onEvent(this, "fail", ""+mScore);
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("游戏失败，你已经不能再移动了");
			
		builder.setTitle("提示");
		builder.setPositiveButton("再来一局", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				reset(true);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("返回", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void showSuccess() {
		MobclickAgent.onEvent(this, "success", ""+mScore);
		
		AlertDialog.Builder builder = new Builder(this);
		if(displayType == 0)
			builder.setMessage("恭喜你，完成游戏目标，要继续冲击吗？");
		else if(displayType == 1)
			builder.setMessage("恭喜你，来到了天朝时代！");
		else {
			builder.setMessage("恭喜你，完成游戏目标！");
		}
		builder.setTitle("提示");
		if(displayType == 0) {
			builder.setPositiveButton("继续", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSuccessful = true;
					dialog.dismiss();
				}
			});
		}
		builder.setNeutralButton("分享", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				share(null);
			}
			
		});
		builder.setNegativeButton("再来一局", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				reset(true);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	if(requestCode == 1 && resultCode == RESULT_OK) {
    		loadSetting();
    		reset(true);
    	}
    }
    
    private void saveData2Db() {
    	if(mScore == 0)
    		return;
    	Score score = new Score();
    	score.username = "default";
    	score.gridtype = gridType;
    	score.gridvalue = displayType;
    	score.maxvalue = MAX_NUMBER;
    	score.score = mScore;
    	score.move = mMove;
    	Date date = new Date();
    	score.time = date.getTime();
    	score.save();
    }
}
