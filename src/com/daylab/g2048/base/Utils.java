package com.daylab.g2048.base;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

/**
 * 通用工具类
 */
public class Utils {

	public static String getCacheRootPath(Context context) {
		return (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable()) ? Environment
				.getExternalStorageDirectory().getPath() : context
				.getFilesDir().getPath();
	}

	public static String setCachePath(Context context, String uniqueName) {
		return getCacheRootPath(context) + uniqueName;
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return false;
	}

	public static boolean hasFroyo() {
		return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return android.os.Build.VERSION.SDK_INT >= 11;// Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return android.os.Build.VERSION.SDK_INT >= 12;// Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return android.os.Build.VERSION.SDK_INT >= 16;// Build.VERSION_CODES.JELLY_BEAN;
	}

	// public static ArrayList<GamePackage> getPackageInfo(Context context){
	// ArrayList<GamePackage> appList = new ArrayList<GamePackage>();
	// List<PackageInfo> packages =
	// context.getPackageManager().getInstalledPackages(0);
	// for (int i = 0; i < packages.size(); i++) {
	// PackageInfo packageInfo = packages.get(i);
	// // Only display the non-system app info
	// if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==
	// 0)
	// {
	// GamePackage tmpInfo = new GamePackage();
	// tmpInfo.appName =
	// packageInfo.applicationInfo.loadLabel(context.getPackageManager())
	// .toString();
	// tmpInfo.packageName = packageInfo.packageName;
	// tmpInfo.versionName = packageInfo.versionName;
	// tmpInfo.versioncode = packageInfo.versionCode;
	// appList.add(tmpInfo);// 如果非系统应用，则添加至appList
	// }
	// }
	//
	// return appList;
	// }

	public static PackageInfo getPackageInfo(Context context) {
		PackageManager pm = context.getPackageManager();// context为当前Activity上下文
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return pi;
		}
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToastLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 *
	 *
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	public static void showShare(Context context,final String title,final String content,final String url,final String imagefile) {
		final OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
//		oks.setNotification(R.drawable.ic_launcher, getContext().getString(R.string.app_name));
		// address是接收人地址，仅在信息和邮件使用
//		oks.setAddress("12345678901");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title+" "+url);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(title+" "+url);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(MainActivity.TEST_IMAGE);
        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
        // 微信的两个平台、Linked-In支持此字段
//		oks.setImageUrl("http://mmbiz.qpic.cn/mmbiz/aKqTprFAouWwiaqw76Gxm3750HIQVjR5xQcCOBJ3iak68zSRib88zC8oCuj7dm9msIWAIjXh1r5QypJdmykpJamwQ/0");
		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl(url);
//		oks.setFilePath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment(getContext().getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl(url);
		 // venueName是分享社区名称，仅在Foursquare使用
//		oks.setVenueName("ShareSDK");
		// venueDescription是分享社区描述，仅在Foursquare使用
//		oks.setVenueDescription("This is a beautiful place!");
		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
//		oks.setLatitude(23.056081f);
		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
//		oks.setLongitude(113.385708f);
		// 是否直接分享（true则直接分享）
		oks.setSilent(false);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
//		if (platform != null) {
//			oks.setPlatform(platform);
//		}

		// 取消注释，可以实现对具体的View进行截屏分享
//		oks.setViewToShare(getPage());

		// 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();

		// 去除注释，在自动授权时可以禁用SSO方式
//		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		oks.setCallback(new OneKeyShareCallback());
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			
			@Override
			public void onShare(Platform platform, ShareParams ps) {
				//微博text：不能超过140个汉字
				//http://wiki.sharesdk.cn/Android_%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E#QQ.E7.A9.BA.E9.97.B4
				if ("QQ".equals(platform.getName())) {
					//QQ分享支持文字、图片、图文和音乐分享
					//参数说明
					//title：最多30个字符
					//text：最多40个字符
					ps.setTitle(title);
					ps.setTitleUrl(url);
					ps.setText(title+url);
					ps.setImagePath(imagefile);
				}else if("QZone".equals(platform.getName())) {
					//title：最多200个字符
					//text：最多600个字符
					ps.setTitle(title);
					ps.setTitleUrl(url);
					ps.setText(content);
					ps.setSite(title);
					ps.setSiteUrl(url);
				}else if("Wechat".equals(platform.getName())) {
					ps.setShareType(Platform.SHARE_WEBPAGE);//认证
					ps.setImagePath(imagefile);
					ps.setUrl(url);
					
					ps.setTitle(title);
					ps.setText(content);
				}else if("WechatMoments".equals(platform.getName())) {
					ps.setShareType(Platform.SHARE_WEBPAGE);//认证
					ps.setUrl(url);
					
					ps.setTitle(title);
					ps.setText(content);//not use
					ps.setImagePath(imagefile);
				}else {
					//新浪微博，腾讯微博
					ps.setImagePath(imagefile);
				}
			}
		});

		// 去除注释，演示在九宫格设置自定义的图标
//		Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
//		String label = menu.getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
//				Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
//		oks.addHiddenPlatform(SinaWeibo.NAME);
//		oks.addHiddenPlatform(TencentWeibo.NAME);

		oks.show(context);
	}
}
