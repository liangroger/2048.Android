package com.daylab.g2048.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 
 * @author dailiang
 */
public class NoScrollGridView extends GridView{

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NoScrollGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
}
