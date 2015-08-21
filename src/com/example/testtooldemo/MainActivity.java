package com.example.testtooldemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.testtooldemo.adapter.testtoolAdapter;
import com.example.testtooldemo.util.ListTools;

public class MainActivity extends Activity {

	private ListView lv_testtool;
	public ListTools listTools = new ListTools();
	private ArrayList<HashMap<String, Object>> allProcess;
	private testtoolAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}
	
	private void initView() {
		lv_testtool = (ListView) findViewById(R.id.lv_testtool);
	}
	
	private void initData() {
		
		allProcess = listTools.getItems(this);
		adapter = new testtoolAdapter(this, R.layout.list_item, allProcess);
		lv_testtool.setAdapter(adapter);
		
		//点击item，跳转到具体的detailActivity
		lv_testtool.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> tempItem = (HashMap<String, Object>) arg0
						.getAdapter().getItem(arg2);

				String appname = tempItem.get("appname").toString();
				String packagename = tempItem.get("packagename").toString();
				String mainActivity = tempItem.get("mainactivity").toString();

				
				//进入detailActivity，并把应用的信息传过去
				
				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);
				intent.putExtra("appname", appname);
				intent.putExtra("packagename", packagename);
				intent.putExtra("mainactivity", mainActivity);

				MainActivity.this.startActivity(intent);
			}
		});
	}


	
}
