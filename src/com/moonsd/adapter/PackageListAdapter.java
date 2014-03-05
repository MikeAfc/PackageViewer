package com.moonsd.adapter;

import java.util.List;

import com.moonsd.entities.AppInfo;
import com.moonsd.packageviewer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PackageListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<AppInfo> appList;
	
	public PackageListAdapter(Context ctx, List<AppInfo> list){
		mInflater = LayoutInflater.from(ctx);
		appList = list;
	}
	
	@Override
	public int getCount() {
		return appList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return appList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppItem item = null;
		if(convertView == null){
			item = new AppItem();
			convertView = mInflater.inflate(R.layout.listitem_app, null);
			item.appIcon = (ImageView)convertView.findViewById(R.id.app_icon);
			item.appName = (TextView)convertView.findViewById(R.id.app_name);
			item.appType = (TextView)convertView.findViewById(R.id.app_type);
			item.pkgName = (TextView)convertView.findViewById(R.id.app_pkg);
			convertView.setTag(item);
		}else{
			item = (AppItem)convertView.getTag();
		}
		
		AppInfo info = appList.get(position);
		item.appIcon.setBackgroundDrawable(info.getAppIcon());
		item.appName.setText(info.getAppName());
		if(info.isSystemApp()){
			item.appType.setText("系统应用");
		}else {
			item.appType.setText("用户应用");
		}
		item.pkgName.setText(info.getPkgName());
		
		return convertView;
	}
	
	private final class AppItem{
		public ImageView appIcon;
		public TextView appName;
		public TextView appType;
		public TextView pkgName;
	}

}
