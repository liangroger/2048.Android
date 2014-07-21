package com.daylab.g2048;

import com.umeng.analytics.MobclickAgent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	private ListPreference grid;
	private ListPreference disName;
	
	private int oldValue0;
	private int oldValue1;
	private boolean change;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置视图
		addPreferencesFromResource(R.xml.pref);
		grid = (ListPreference)findPreference("list_preference");
		grid.setOnPreferenceChangeListener(this);
		disName = (ListPreference)findPreference("list_preference2");
		disName.setOnPreferenceChangeListener(this);
		
		// 得到我们的存储Preferences值的对象，然后对其进行相应操作  
		{
			SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
			String value = shp.getString("list_preference", "0"); 
			int intValue = oldValue0 = Integer.parseInt(value);
			String[] array=this.getResources().getStringArray(R.array.list_preference);
			grid.setSummary(array[intValue]);
		}
		{
			SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
			String value = shp.getString("list_preference2", "0"); 
			int intValue = oldValue1 = Integer.parseInt(value); 
			String[] array=this.getResources().getStringArray(R.array.list_preference2);
			disName.setSummary(array[intValue]);
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if(preference.getKey().equals("list_preference")) {
			SharedPreferences sf = preference.getSharedPreferences(); 
		}
		if(preference.getKey().equals("list_preference2")) {
			
		}
		String value = prefs.getString("edittext_preference", "unset");// 通过key值取Value值

		System.out.println("value-->" + value);
		return false;
	}

	// 当Preference的值发生改变时触发该事件，true则以新值更新控件的状态，false则do noting
	@Override
	public boolean onPreferenceChange(Preference preference, Object objValue) {
		Log.i("", "onPreferenceChange----->"+String.valueOf(preference.getKey())+" "+String.valueOf(objValue));  
		if(preference == grid) {
			String value = String.valueOf(objValue);
			int intValue = Integer.parseInt(value);
			String[] array=this.getResources().getStringArray(R.array.list_preference);
			grid.setSummary(array[intValue]);
			change = !(intValue == oldValue0);
			
			MobclickAgent.onEvent(this, "grid", ""+intValue);
			return true;
		}
		if(preference == disName) {
			String value = String.valueOf(objValue);
			int intValue = Integer.parseInt(value);
			String[] array=this.getResources().getStringArray(R.array.list_preference2);
			disName.setSummary(array[intValue]);
			change = !(intValue == oldValue1);
			
			MobclickAgent.onEvent(this, "disName", ""+intValue);
			return true;
		}
		return false;
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
        	setResult(change?RESULT_OK:RESULT_CANCELED);
        	finish();
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( "Setting" );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( "Setting" );
		MobclickAgent.onPause(this);
	}
}
