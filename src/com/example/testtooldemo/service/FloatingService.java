package com.example.testtooldemo.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testtooldemo.DetailActivity;
import com.example.testtooldemo.R;
import com.example.testtooldemo.util.ConfigUtil;
import com.example.testtooldemo.util.Util;


public class FloatingService extends Service {

	// 进行所有数据的获取：耗电量，当前cpu占用率，当前应用程序占用内存，WiFi，3g.
	// 数据的显示和存储

	boolean isShow = true;
	private View view;
	private WindowManager windowManager;
	private DisplayMetrics metric;
	private WindowManager.LayoutParams layoutParams;
	private TextView tv_memory, tv_traffic, tv_cpu, tv_battery;
	private Button btn_close;
//	private LinearLayout ll;
	public boolean viewAdded = false;
	private long action_down_time;
	private long action_up_time;
	private int statusBarHeight;
	final int FLAG_ACTIVITY_NEW_TASK = Intent.FLAG_ACTIVITY_NEW_TASK;
	private Handler handler = new Handler();

	private SetData setdata;
	private String battery;
	private long traffic_wifi = 0;
	private long traffic_3g = 0;
	
	private Util util = Util.getUtil();
	private ConfigUtil configutil;
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		setdata = new SetData(getApplicationContext());
		
		configutil = new ConfigUtil(this);
		if (isShow) {
			view = LayoutInflater.from(this).inflate(R.layout.floating1, null);
			windowManager = (WindowManager) this
					.getSystemService(WINDOW_SERVICE);
			metric = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(metric);
			/*
			 * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
			 * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
			 * PixelFormat.TRANSPARENT：悬浮窗透明
			 */
			layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR,
					LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
			layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;

			InitView();

			// windowManager.addView(view, layoutParams);

			view.setOnTouchListener(new OnTouchListener() {
				float[] temp = new float[] { 0f, 0f };

				public boolean onTouch(View v, MotionEvent event) {
					layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
					int eventaction = event.getAction();
					switch (eventaction) {
					case MotionEvent.ACTION_DOWN:
						temp[0] = event.getX();
						temp[1] = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						refreshView((int) (event.getRawX() - temp[0]),
								(int) (event.getRawY() - temp[1]));
						break;
					case MotionEvent.ACTION_UP:

						if ((event.getRawX() - temp[0]) > (metric.widthPixels / 2)) {
							refreshView(metric.widthPixels,
									(int) (event.getRawY() - temp[1]));
						} else {
							refreshView(0, (int) (event.getRawY() - temp[1]));
						}
						break;
					}
					return false;
				}
			});

		}
	}

	// 移动刷新view
	private void refreshView(int x, int y) {
		if (statusBarHeight == 0) {
			View rootView = view.getRootView();
			Rect r = new Rect();
			rootView.getWindowVisibleDisplayFrame(r);
			statusBarHeight = r.top;
		}
		layoutParams.x = x;
		layoutParams.y = y - statusBarHeight;// STATUS_HEIGHT;
		refresh();
	}

	private void InitView() {
		tv_memory = (TextView) view.findViewById(R.id.tv_memory);
		tv_traffic = (TextView) view.findViewById(R.id.tv_traffic);
		tv_cpu = (TextView) view.findViewById(R.id.tv_cpu);
		tv_battery = (TextView) view.findViewById(R.id.tv_battery);
		btn_close = (Button) view.findViewById(R.id.close);

		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stop();
				Intent intent = new Intent();
				// intent.putExtra("appname", util.getAPPname());
				// intent.putExtra("packagename", packagename);
				intent.setClass(FloatingService.this, DetailActivity.class);
				intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

//		ll = (LinearLayout) view.findViewById(R.id.ll_float2);

		// 闪烁变换颜色

		// h.sendEmptyMessageDelayed(RED, changeColor_delay);

		// h.sendEmptyMessageDelayed(WHITE, changeColor_delay * 5 * 10);
	}

	private void stop() {
		stopSelf();

		// intent = new Intent();
		// intent.setClass(this, FloatingService.class);
		//
		// stopService(intent);
		// cpuText.setText(getResources().getText(R.string.result));
		// ramText.setText(getResources().getText(R.string.result));
		// grafficText.setText(getResources().getText(R.string.result));
		// batteryText.setText(getResources().getText(R.string.result));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (isShow) {
			refresh();
			// if (TextUtils.isEmpty(util.getPACKAGENAME()))
			// RefreshUI_specail(getResources().getString(R.string.no_process));
			// else
			// RefreshUI_specail(getResources().getString(
			// R.string.loading_data));
		}

		// 更新界面
		handler.post(refreshData);

		return super.onStartCommand(intent, flags, startId);
	}

	private void refresh() {
		if (viewAdded) {
			windowManager.updateViewLayout(view, layoutParams);
		} else {
			windowManager.addView(view, layoutParams);
			viewAdded = true;
		}
	}

	public void onDestroy() {
		// logToast();
		// util.setServiceRunning(false);
		// util.setFLAG(0);
		removeView();
		// unregisterReceiver(MyBatteryReceiver);
		// handler.removeCallbacks(refreshData);
		super.onDestroy();
	}

	private void removeView() {
		if (viewAdded) {
			windowManager.removeView(view);
			viewAdded = false;
		}
	}

	/**
	 * 实现数据的更新和存储
	 * 
	 * 每次先获取数据，并进行存储 更新界面显示
	 */

	private Runnable refreshData = new Runnable() {
		@Override
		public void run() {

			// 获取数据
			int result = setdata.startSetData();
			if (result == -5) {
				// util.setFLAG(2);
				if (isShow) {
					RefreshUI_specail("您所选的程序还没有运行");
				}
			} else if (result == 0) {
				// util.setFLAG(1);
				if (isShow) {

					// 数据的显示
					RefreshUI();
				}

				// 离线模式下没有的
				// if (isLog) {
				// long currentTime = System.currentTimeMillis();
				// long tmpTime = currentTime - time;
				// LogName = DataGlobal.LOG_TYPE + "_" + packagename + "_"
				// + logTime + "_" + logNum + ".log";
				//
				// if (logFirst == 0 && !util.isOffline()) {
				// LogFile logFile = new LogFile();
				// logFile.setLogTime(String.valueOf(logTime));
				// logFile.setUserId(configutil.getUID());
				// logFile.setIsUpload(0);
				// logFile.setLogName(LogName);
				// logFile.setLogAppName(util.getAPPname());
				// logFile.setLogPath(LogPath + uid + "/" + LogName);
				// logMessageDao.insert(logFile);
				// logFirst = 1;
				// }
				//
				// if (tmpTime > DataGlobal.AUTOUPLOAD_FREC) {
				// logNum++;
				// time = currentTime;
				//
				// LogName = DataGlobal.LOG_TYPE + "_" + packagename + "_"
				// + logTime + "_" + logNum + ".log";
				//
				// // 判断是否为离线，离线模式不插入数据库不自动上传
				// if (!util.isOffline()) {
				// // 数据库插入数据
				// LogFile logFile = new LogFile();
				// logFile.setLogTime(String.valueOf(logTime));
				// logFile.setUserId(configutil.getUID());
				// logFile.setIsUpload(0);
				// logFile.setLogName(LogName);
				// logFile.setLogAppName(util.getAPPname());
				// logFile.setLogPath(LogPath + uid + "/" + LogName);
				// logMessageDao.insert(logFile);
				//
				// // 发广播自动上传
				// intent = new Intent();
				// intent.setAction(DataGlobal.ADD_LOG);
				// intent.putExtra(DataGlobal.LOG_NUM, logNum - 1);
				// intent.putExtra(DataGlobal.PACKAGENAME, packagename);
				// intent.putExtra(DataGlobal.TIME, logTime);
				// sendBroadcast(intent);
				// }
				// }
				//
				// util.setLOGnum(logNum + 1);
				//
				// // 判断uid是否为空，不为空存log
				// if (!TextUtils.isEmpty(uid)) {
				// logutil.SaveLog(
				// LogPath + uid + "/",
				// LogName,
				// System.currentTimeMillis() + "|"
				// + util.getProcessCpuRatio() + "|"
				// + util.getPSS_MEM() + "|" + log_battery
				// + "|" + util.getTRAFFIC_WIFI() + "|"
				// + util.getTRAFFIC_3G() + "\n");
				// } else {
				// // 判断是否为离线模式
				// if (util.isOffline()) {
				//
				// } else {
				// Toast.makeText(FloatingService.this,
				// getResources().getString(R.string.no_uid),
				// Toast.LENGTH_LONG).show();
				// }
				// }
				// }
				// }
				//
				// DetailActivity detailActivity = util.getDetailActivity();
				// if (detailActivity != null) {
				// detailActivity.refreshUI();
				// }

				handler.postDelayed(refreshData, 3000);
				System.gc();
			}
		}
	};

	private void RefreshUI_specail(String spc) {
		tv_memory.setText(spc);
		tv_cpu.setVisibility(View.GONE);
//		ll.setVisibility(View.GONE);
	}

	private void RefreshUI() {
		// 判断是否充电中
		if (util.isCharging()) {
			battery = "充电中";
			// log_battery = 0;
		} else {
			battery = "voltage" + util.getVoltage() + "mv";
			// log_battery = util.getVoltage();
		}

		tv_cpu.setVisibility(View.VISIBLE);
//		ll.setVisibility(View.VISIBLE);
		tv_memory.setText("memory" + util.transSize(util.getPSS_MEM()) + "\r");

		traffic_wifi = Long.parseLong(util.getTRAFFIC_WIFI());
		traffic_3g = Long.parseLong(util.getTRAFFIC_3G());

		if (configutil.getFLOW_TYPE() == 1) {
			// 显示总流量数
			tv_traffic.setText("wifi:" + util.transSize(traffic_wifi) + "\r"
					+ "3G" + util.transSize(traffic_3g));
		} else {
			tv_traffic.setText("wifi" + util.transSpeed(traffic_wifi) + "\r"
					+ "3G" + util.transSpeed(traffic_3g));
		}

		tv_cpu.setText("cpu:" + util.getProcessCpuRatio());
		tv_battery.setText(battery);

	}

}
