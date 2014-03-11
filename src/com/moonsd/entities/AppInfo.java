package com.moonsd.entities;

import java.lang.ref.SoftReference;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppInfo {
	private String appName;
	private String pkgName;
	private Drawable appIcon;
	private boolean isSystemApp;
	private ApplicationInfo applicationInfo;
	private String chinesePinYin;
	public String getChinesePinYin() {
		return chinesePinYin;
	}
	public void setChinesePinYin(String chinesePinYin) {
		this.chinesePinYin = chinesePinYin;
	}
	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}
	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}
	
}
