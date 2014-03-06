package com.moonsd.packageviewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.moonsd.adapter.PackageListAdapter;
import com.moonsd.entities.AppInfo;
import com.moonsd.utils.PackageUtil;

public class MainActivity extends Activity {

	private ListView packageListView;
	private PackageListAdapter packageListAdapter;
	private List<AppInfo> appList;
	private List<AppInfo> systemList;
	private List<AppInfo> userList;

	private Dialog dialog;

	private AppInfo currSelectItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		systemList = new ArrayList<AppInfo>();
		userList = new ArrayList<AppInfo>();
		refreshData();

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

	private void refreshData() {
		appList = PackageUtil.getPackageList(this);
		systemList.clear();
		userList.clear();
		for (AppInfo info : appList) {
			if (info.isSystemApp())
				systemList.add(info);
			else
				userList.add(info);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (systemList.size() == 0 && userList.size() == 0) {
			for (AppInfo info : appList) {
				if (info.isSystemApp())
					systemList.add(info);
				else
					userList.add(info);

			}
		}
		menu.add(0, 0, 0, "显示全部应用");
		menu.add(0, 1, 1, "仅显示系统应用");
		menu.add(0, 2, 2, "仅显示用户应用");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String string = "";
		switch (item.getItemId()) {
		case 0:
			packageListAdapter.setData(appList);
			string = "共安装了 " + appList.size() + " 款应用";
			break;
		case 1:
			packageListAdapter.setData(systemList);
			string = "共安装了 " + systemList.size() + " 款系统应用";
			break;
		case 2:
			packageListAdapter.setData(userList);
			string = "共安装了 " + userList.size() + " 款用户应用";
			break;
		default:
			break;
		}
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
		packageListAdapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
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
				if (currSelectItem.isSystemApp())
					str = "是";
				else
					str = "否";
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						"以下信息来自 PackageViewer: \n应用名称: "
								+ currSelectItem.getAppName() + "\n应用包名: "
								+ currSelectItem.getPkgName() + "\n是否系统应用: "
								+ str);
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

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");

		registerReceiver(installReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(installReceiver);
	}

	BroadcastReceiver installReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals("android.intent.action.PACKAGE_ADDED")) {
				refreshData();
			}

			if (intent.getAction().equals(
					"android.intent.action.PACKAGE_REMOVED")) {
				refreshData();
			}
		}
	};

}
