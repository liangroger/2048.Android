package com.daylab.g2048.base;
/**
 * 配置
 * @author Administrator
 *
 */

public class Config {
	
	//==================设备基本参数======================
	/**当前设备的密度*/
	public static float DENSITY;
	/**当前程序屏幕宽度*/
	public static int SCREEN_WIDTH;
	/**当前程序屏幕高度*/
	public static int SCREEN_HEIGHT;
	/**当前设备真实屏幕宽度*/
	public static int EXACT_SCREEN_WIDTH;
	/**当前设备真实屏幕高度*/
	public static int EXACT_SCREEN_HEIGHT;
	//======================END==========================
	
//      模拟器上测试 会得出以下两组数据	
//		320*480: density:1.0   width:320   height:480
//		480*800: density:1.5   width:320   height:533
//		480*854: density:1.5   width:320   height:569
//		
//		320*480: density:1.0   width:320   height:480
//		480*800: density:1.5   width:480   height:800
//		480*854: density:1.5   width:480   height:854
	
	
	// dip转化为像素
	public static int getPx(int dip) {
		if (Config.DENSITY > 1.0 && Config.EXACT_SCREEN_WIDTH > 320)
			return (int) (dip * Config.DENSITY);
		else
			return dip;
	}

	// 像素转化为dip
	public static int getDip(int px) {
		if (Config.DENSITY > 1.0 && Config.EXACT_SCREEN_WIDTH > 320)
			return (int) (px / Config.DENSITY);
		else
			return px;
	}
	
	public static String IMEI = null;
}
