package com.daylab.g2048.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

public class BMapUtil {
    	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	public static boolean saveBitmapToPng(Bitmap bitmap,String filepath) {
		File file = new File(filepath);
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) 
            {
                out.flush();
                out.close();
                return true;
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); 
        }
        return false;
	}
	
    public static Bitmap takeScreenShot(Activity activity) { 
        // View是你需要截图的View 
        View view = activity.getWindow().getDecorView(); 
        view.setDrawingCacheEnabled(true); 
        view.buildDrawingCache(); 
        Bitmap b1 = view.getDrawingCache(); 
   
        // 获取状态栏高度 
        Rect frame = new Rect(); 
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame); 
        int statusBarHeight = frame.top; 
   
        // 获取屏幕长和高 
        int width = activity.getWindowManager().getDefaultDisplay().getWidth(); 
        int height = activity.getWindowManager().getDefaultDisplay() 
                .getHeight(); 
        // 去掉标题栏 
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height 
                - statusBarHeight); 
        view.destroyDrawingCache(); 
        return b; 
    }  
}
