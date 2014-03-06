package com.moonsd.packageviewer;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.moonsd.adapter.PackageListAdapter;
import com.moonsd.entities.AppInfo;
import com.moonsd.utils.PackageUtil;

public class MainActivity extends Activity {

	private ListView packageListView;
	private PackageListAdapter packageListAdapter;
	private List<AppInfo> appList;

	private Dialog dialog;

	private AppInfo currSelectItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appList = PackageUtil.getPackageList(this);

		packageListView = (ListView) findViewById(R.id.app_list);
		packageListAdapter = new PackageListAdapter(this, appList);
		packageListView.setAdapter(packageListAdapter);
		initDialog();

		packageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!dialog.isShowing()) {
					currSelectItem = appList.get(arg2);
					dialog.setTitle(currSelectItem.getAppName());
					dialog.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initDialog() {
		dialog = new Dialog(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_click,
				null);
		dialog.setContentView(view);
		Button detailsBtn = (Button) view.findViewById(R.id.dialog_appdetails);
		Button shareBtn = (Button) view.findViewById(R.id.dialog_appshare);
		Button cancelBtn = (Button) view.findViewById(R.id.dialog_appcancel);

		detailsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currSelectItem != null) {
					Uri uri = Uri.parse("package:"
							+ currSelectItem.getPkgName());
					Intent intent = new Intent(
							Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
					startActivity(intent);
				}
			}
		});

		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();  
				sendIntent.setAction(Intent.ACTION_SEND);  
				String str;
				if(currSelectItem.isSystemApp())
					str = "是";
				else
					str = "否";
				sendIntent.putExtra(Intent.EXTRA_TEXT, "以下信息来自 PackageViewer: \n应用名称: " +
						currSelectItem.getAppName() + "\n应用包名: " + currSelectItem.getPkgName() + 
						"\n是否系统应用: " + str);  
				sendIntent.setType("text/plain");  
				startActivity(sendIntent);  
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog.isShowing())
					dialog.dismiss();
			}
		});
	}

}
