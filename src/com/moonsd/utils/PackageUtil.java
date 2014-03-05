package com.moonsd.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.moonsd.entities.AppInfo;

public class PackageUtil {
	public static List<AppInfo> getPackageList(Context ctx){
		List<AppInfo> appList = new ArrayList<AppInfo>();
		PackageManager packageManager = ctx.getPackageManager();
		List<PackageInfo> list = packageManager.getInstalledPackages(0);
		for(PackageInfo info : list){
			AppInfo appInfo = new AppInfo();
			appInfo.setAppName(packageManager.getApplicationLabel(info.applicationInfo).toString());
			appInfo.setPkgName(info.applicationInfo.packageName);
			appInfo.setAppIcon(packageManager.getApplicationIcon(info.applicationInfo));
			if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0){
				appInfo.setSystemApp(true);
			}
			appList.add(appInfo);
		}
		return appList;
	}
}
