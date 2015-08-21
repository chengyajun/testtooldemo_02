package com.example.testtooldemo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class ListTools {
	
	
	// 获取系统中所有应用的方法
	public ArrayList<HashMap<String, Object>> getItems(Context context) {

		PackageManager pckMan = context.getPackageManager();
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pckMan.queryIntentActivities(
				mainIntent, 0);

		//根据name进行排序
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(
				pckMan));

		for (ResolveInfo reInfo : resolveInfos) {

			if ((reInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
					&& (reInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
				HashMap<String, Object> item = new HashMap<String, Object>();

				item.put("uid", reInfo.activityInfo.applicationInfo.uid);
				item.put("appimage",
						reInfo.activityInfo.applicationInfo.loadIcon(pckMan));
				item.put("appname", reInfo.activityInfo.applicationInfo
						.loadLabel(pckMan).toString());
				item.put("packagename", reInfo.activityInfo.packageName);

				item.put("mainactivity", reInfo.activityInfo.name);

				items.add(item);
			}

		}

		return items;
	}
}
