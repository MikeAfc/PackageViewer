package com.moonsd.packageviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.moonsd.adapter.PackageListAdapter;
import com.moonsd.utils.PackageUtil;

public class MainActivity extends Activity {

	private ListView packageListView;
	private PackageListAdapter packageListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		packageListView = (ListView)findViewById(R.id.app_list);
		packageListAdapter = new PackageListAdapter(this, PackageUtil.getPackageList(this));
		packageListView.setAdapter(packageListAdapter);
		
		packageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
