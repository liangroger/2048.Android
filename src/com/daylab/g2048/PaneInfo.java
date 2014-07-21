package com.daylab.g2048;

import java.io.Serializable;

public class PaneInfo implements Serializable{
	public int position;
	public int value = 1;//1,2,4,8,16,32,64......
	public int oldPosition;
	public boolean isNew;
	public boolean join;
	
	public String toString() {
		return value+"";
	}
	
	private static String[] texts = {"","夏","商","周","秦","汉","晋",//64
									"南北朝","隋","唐",//512
									"五代十国","宋","元","明",//8192
								    "清","民国","天朝"};//65536
	
	private static String[] texts2 = {"","子鼠","丑牛","寅虎","卯兔","辰龙","巳蛇",
		"午马","未羊","申猴",
		"酉鸡","戌狗","亥猪"};//4096
	
	public PaneInfo getClone() {
		PaneInfo info = new PaneInfo();
		info.position = position;
		info.value = value;
		info.oldPosition = oldPosition;
		info.isNew = isNew;
		info.join = join;
		return info;
	}
	
	private static int getValueIndex(int value) {
		int index = 0;
		switch(value) {
			case 1: index = 0; break;
			case 2: index = 1; break;
			case 4: index = 2; break;
			case 8: index = 3; break;
			case 16: index = 4; break;
			case 32: index = 5; break;	
			case 64: index = 6; break;
			case 128: index = 7; break;
			case 256: index = 8; break;
			case 512: index = 9; break;
			case 1024: index = 10; break;
			case 2048: index = 11; break;	
			case 4096: index = 12; break;
			case 8192: index = 13; break;
			case 16384: index = 14; break;
			case 32768: index = 15; break;	
			case 65536: index = 16; break;	
			case 131072: index = 17; break;
			case 262144: index = 18; break;
			case 524288: index = 19; break;
			case 1048576: index = 20; break;	
		}
		return index;
	}
	
	public String getValueText(int type) {
		return getValueText(type,value);
	}
	
	public static String getValueText(int type,int value) {
		String s = "";
		if(type == 1) {
			if(getValueIndex(value) < texts.length)
				s = texts[getValueIndex(value)];
		}else if(type == 2) {
			if(getValueIndex(value) < texts2.length)
				s = texts2[getValueIndex(value)];
		}else {
			switch(value) {
			case 1:
				s = "0";
				break;
			default:
				s = value + "";
				break;
			}
		}
		return s;
	}
	
	public String getValueColor() {
		String color = "#f0e3da";
		switch(value) {
		case 1: color="#f0e3da"; break;
		case 2: color = "#f0e3da"; break;
		case 4: color = "#efe0cb"; break;
		case 8: color = "#ff9966"; break;
		case 16: color = "#ff9900"; break;
		case 32: color = "#ff6600"; break;	
		case 64: color = "#cc6633"; break;
		
		case 128: color = "#FA5858"; break;
		case 256: color = "#FE2E2E"; break;
		case 512: color = "#cc0000"; break;
		
		case 1024: color = "#2E9AFE"; break;
		case 2048: color = "#0174DF"; break;	
		case 4096: color = "#0040FF"; break;
		case 8192: color = "#0099cc"; break;
		
		case 16384: color = "#04B45F"; break;
		case 32768: color = "#298A08"; break;	
		case 65536: color = "#31B404"; break;	
		
		case 131072: color = "#D358F7"; break;
		case 262144: color = "#9A2EFE"; break;
		case 524288: color = "#8A0886"; break;
		case 1048576: color = "#9933cc"; break;	
		}
		return color;
	}
	
	public int getValueBgRes() {
		return getValueBgRes(value);
	}
	
	public static int getValueBgRes(int value) {
		int res = R.drawable.sx2;
		switch(value) {
		case 1: res = R.drawable.sx2; break;
		case 2: res = R.drawable.sx2; break;
		case 4: res = R.drawable.sx4; break;
		case 8: res = R.drawable.sx8; break;
		case 16: res = R.drawable.sx16; break;
		case 32: res = R.drawable.sx32; break;	
		case 64: res = R.drawable.sx64; break;
		
		case 128: res = R.drawable.sx128; break;
		case 256: res = R.drawable.sx256; break;
		case 512: res = R.drawable.sx512; break;
		
		case 1024: res = R.drawable.sx1024; break;
		case 2048: res = R.drawable.sx2048; break;	
		case 4096: res = R.drawable.sx4096; break;
		}
		return res;
		
	}
}
