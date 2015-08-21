package com.example.testtooldemo.adapter;

import java.util.ArrayList;
import java.util.HashMap;



import com.example.testtooldemo.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class testtoolAdapter extends BaseAdapter {

	private Context mContext;
	ArrayList<HashMap<String, Object>> allProcess;
	private int mResId;
	private LayoutInflater inflater;

	public testtoolAdapter(Context context, int resId,
			ArrayList<HashMap<String, Object>> allProcess) {
		this.mContext = context;
		this.mResId = resId;
		inflater = LayoutInflater.from(context);
		setData(allProcess);
	}

	private void setData(ArrayList<HashMap<String, Object>> allProcess) {
		if (allProcess == null) {
			allProcess = new ArrayList<HashMap<String, Object>>();
		}
		this.allProcess = allProcess;
	}

	@Override
	public int getCount() {
		return allProcess.size();
	}

	@Override
	public Object getItem(int position) {
		return allProcess.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder;
		View view;
		if (convertView == null) {
			holder = new Holder();
			view = inflater.inflate(mResId, null);
			holder.imgView = (ImageView) view.findViewById(R.id.app_img);
			holder.appnameText = (TextView) view
					.findViewById(R.id.app_name);
			holder.packageText = (TextView) view
					.findViewById(R.id.app_packagename);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (Holder) view.getTag();
		}

		holder.imgView.setImageDrawable((Drawable) allProcess.get(position)
				.get("appimage"));
		holder.appnameText.setText(allProcess.get(position).get("appname")
				.toString());
		holder.packageText.setText(allProcess.get(position)
				.get("packagename").toString());

		return view;
	}
	
	
	public class Holder {
		ImageView imgView;
		TextView appnameText;
		TextView packageText;
	}

}
