package com.example.testtooldemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testtooldemo.service.FloatingService;
import com.example.testtooldemo.util.GetAppLaunchTime;
import com.example.testtooldemo.util.Util;

public class DetailActivity extends Activity {

	private String appname;
	private String packagename;
	private String mainActivity;
	TextView title_text, battery_text, cpu_text, ram_text, traffic_text;
	Button control_btn;
	private GetAppLaunchTime getAppLaunchTime = null;
	
	private Util util = Util.getUtil();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);

		Intent data = this.getIntent();
		appname = data.getStringExtra("appname");
		packagename = data.getStringExtra("packagename");
		mainActivity = data.getStringExtra("mainactivity");
		initView();
		initData();
		getAppLaunchTime = new GetAppLaunchTime();
		initListener();

	}

	private void initView() {
		title_text = (TextView) findViewById(R.id.title_text);
		battery_text = (TextView) findViewById(R.id.battery_text);
		cpu_text = (TextView) findViewById(R.id.cpu_text);
		ram_text = (TextView) findViewById(R.id.ram_text);
		traffic_text = (TextView) findViewById(R.id.traffic_text);
		control_btn = (Button) findViewById(R.id.control_btn);
	}

	private void initData() {
		title_text.setText(appname + "");
	}

	private void initListener() {

		// 点击按钮开始对app进行检测
		control_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				util.setPACKAGENAME(packagename);
				util.setAPPname(appname);
				
				
				// 开启显示悬浮窗Service
				start_flow();
				
				// 显示界面数据
//				refreshUI();
				

				startApp();

			}
		});

	}
	
	private void start_flow() {
		Intent intent = new Intent();
		intent.setClass(this, FloatingService.class);
//		util.setDetailActivity(this);
//		time = System.currentTimeMillis();
//		configutil.setLogTime(time);
//		configutil.setProcess(packagename);
		startService(intent);
	}
	
	private void startApp() {

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if ("com.qtest.appmonitor".equals(packagename)) {
			// 如果是自己就不kill
			return; 
		} else {
			am.killBackgroundProcesses(packagename);

			Intent launchIntent = new Intent();
			launchIntent.setComponent(new ComponentName(packagename,
					mainActivity));

//			if (DebugLog.DEBUG)
//				DebugLog.e(mainActivity);

			// thisTime即是App的启动时间
			long thisTime = getAppLaunchTime
					.startActivityWithTime(launchIntent);
			Toast.makeText(
					DetailActivity.this,
					"启动程序耗时" + thisTime
							+ "毫秒", Toast.LENGTH_LONG)
					.show();
		}
	
	}

}
