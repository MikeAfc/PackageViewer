package com.moonsd.utils;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.moonsd.entities.AppInfo;

public class PackageUtil {
	public static List<AppInfo> getPackageList(Context ctx) {
		List<AppInfo> appList = new ArrayList<AppInfo>();
		PackageManager packageManager = ctx.getPackageManager();
		List<PackageInfo> list = packageManager.getInstalledPackages(0);
		for (PackageInfo info : list) {
			AppInfo appInfo = new AppInfo();
			appInfo.setAppName(packageManager.getApplicationLabel(
					info.applicationInfo).toString());
			appInfo.setPkgName(info.applicationInfo.packageName);
			appInfo.setApplicationInfo(info.applicationInfo);
			appInfo.setChinesePinYin(getChinesePinYin(appInfo.getAppName()));
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				appInfo.setSystemApp(true);
			}
			appList.add(appInfo);
		}
		
		
		return sortList(appList);
	}

	public static Drawable getDrawable(PackageManager packageManager,
			ApplicationInfo applicationInfo) {
		return packageManager.getApplicationIcon(applicationInfo);
	}

	private static List<AppInfo> sortList(List<AppInfo> list) {
		List<AppInfo> resultList = new ArrayList<AppInfo>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				
			}
		}
		return resultList;
	}

	private static String getChinesePinYin(String str) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

		char[] array = str.toCharArray();
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			// "[\u4E00-\u9FA5]+" 可匹配所有汉字
			if (Character.toString(array[i]).matches("[\u4E00-\u9FA5]+")) {
				String[] temp = null;
				try {
					temp = PinyinHelper.toHanyuPinyinStringArray(array[i],
							format);
					result.append(temp[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else
				result.append(Character.toString(array[i]));
		}

		return result.toString();
	}
}
