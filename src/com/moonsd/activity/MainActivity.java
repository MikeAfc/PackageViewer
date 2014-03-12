package com.moonsd.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.moonsd.adapter.PackageListAdapter;
import com.moonsd.entities.AppInfo;
import com.moonsd.packageviewer.R;
import com.moonsd.utils.PackageUtil;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements OnDismissCallback {

	private ListView packageListView;
	private PackageListAdapter packageListAdapter;
	private AnimationAdapter animAdapter;
	private List<AppInfo> appList;
	private List<AppInfo> systemList;
	private List<AppInfo> userList;

	private Dialog dialog;

	private AppInfo currSelectItem;
	private int currTab = 0;
	private static final String TAG = "PackageViewer";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		systemList = new ArrayList<AppInfo>();
		userList = new ArrayList<AppInfo>();
		refreshData();

		MobclickAgent.updateOnlineConfig(this);

		packageListView = (ListView) findViewById(R.id.app_list);
		packageListAdapter = new PackageListAdapter(this, appList);
		animAdapter = new SwingRightInAnimationAdapter(packageListAdapter);
		SwipeDismissAdapter adapter = new SwipeDismissAdapter(animAdapter, this);
		adapter.setAbsListView(packageListView);
		packageListView.setAdapter(adapter);

		initDialog();

		packageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!dialog.isShowing()) {
					if (currTab == 0)
						currSelectItem = appList.get(arg2);
					else if (currTab == 1)
						currSelectItem = systemList.get(arg2);
					else if (currTab == 2)
						currSelectItem = userList.get(arg2);
					MobclickAgent.onEvent(MainActivity.this, "DialogShow");
					dialog.setTitle(currSelectItem.getAppName());
					dialog.show();
				}
			}
		});
	}

	private void refreshData() {
		if (appList != null)
			appList.clear();
		appList = PackageUtil.getPackageList(this);
		systemList.clear();
		userList.clear();
		for (AppInfo info : appList) {
			if (info.isSystemApp())
				systemList.add(info);
			else
				userList.add(info);

		}
		if (packageListAdapter != null) {
			if (currTab == 0) {
				packageListAdapter.setData(appList);
			} else if (currTab == 1) {
				packageListAdapter.setData(systemList);
			} else if (currTab == 2) {
				packageListAdapter.setData(userList);
			}
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
		MobclickAgent.onEvent(this, "Menuclick");
		switch (item.getItemId()) {
		case 0:
			packageListAdapter.setData(appList);
			string = "共安装了 " + appList.size() + " 款应用";
			currTab = 0;
			break;
		case 1:
			packageListAdapter.setData(systemList);
			string = "共安装了 " + systemList.size() + " 款系统应用";
			currTab = 1;
			break;
		case 2:
			packageListAdapter.setData(userList);
			string = "共安装了 " + userList.size() + " 款用户应用";
			currTab = 2;
			break;
		default:
			break;
		}
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
		packageListAdapter.notifyDataSetChanged();
		animAdapter.notifyDataSetChanged();

		return super.onOptionsItemSelected(item);
	}

	private void initDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_click,
				null);
		dialog = new AlertDialog.Builder(this).setView(view).create();
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
					if (dialog.isShowing())
						dialog.dismiss();
					MobclickAgent.onEvent(MainActivity.this, "AppDetailsClick");
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
					str = "???";
				else
					str = "???";
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						"以下信息来自 " + R.string.app_name + ": \n应用名称: "
								+ currSelectItem.getAppName() + "\n应用包名: "
								+ currSelectItem.getPkgName() + "\n是否系统应用: "
								+ str);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				if (dialog.isShowing())
					dialog.dismiss();
				MobclickAgent.onEvent(MainActivity.this, "AppSharedClick");
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
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(installReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	BroadcastReceiver installReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals("android.intent.action.PACKAGE_ADDED")) {
				refreshData();
				packageListAdapter.notifyDataSetChanged();
			}

			if (intent.getAction().equals(
					"android.intent.action.PACKAGE_REMOVED")) {
				for (AppInfo info : appList) {
					if(info.getPkgName().equals(intent.getDataString().replace("package:", ""))){
						break;
					}
				}
				
				refreshData();
				packageListAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onDismiss(AbsListView arg0, int[] reverseSortedPositions) {
		AppInfo tmp = null;
		for (int position : reverseSortedPositions) {
			if (currTab == 0)
				tmp = appList.get(position);
			else if (currTab == 1)
				tmp = systemList.get(position);
			else if (currTab == 2)
				tmp = userList.get(position);
			if (tmp != null) {
				
				Uri uri = Uri.parse("package:" + tmp.getPkgName());
				Intent intent = new Intent(Intent.ACTION_DELETE, uri);
				startActivity(intent);
			}
		}

	}

}
