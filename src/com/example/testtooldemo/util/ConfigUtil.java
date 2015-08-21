package com.example.testtooldemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ConfigUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private String name = "my_config";
	private final String PROCESS = "process";
	private final String isUpdate = "isUpdate";
	private final String DELAY = "Delay";
	private final String USERNAME = "Username";
	private final String LOG_TIME = "LogTime";
	private final int Default_DELAY = 3000;
	private final String PASSWORD = "Password";
	private final String SAVE_PWD = "Save_Password";
	private final String UID = "uid";
	private final String AUTO_UPLOAD = "autoUpload";
	private final String TOKEN = "Token";
	private final String PHONE_MODEL = "phone_model";
	private final String RELEASE_VERSION = "release_version";
	private final String FLOW_TYPE = "flow_type";

	public int getFLOW_TYPE() {
		return sp.getInt(FLOW_TYPE, 0);
	}

	public void setFLOW_TYPE(int fLOW_TYPE) {
		editor.putInt(FLOW_TYPE, fLOW_TYPE);
		editor.commit();
	}

	public String getPHONE_MODEL() {
		return sp.getString(PHONE_MODEL, "");
	}

	public void setPHONE_MODEL(String pHONE_MODEL) {
		editor.putString(PHONE_MODEL, pHONE_MODEL);
		editor.commit();
	}

	public String getRELEASE_VERSION() {
		return sp.getString(RELEASE_VERSION, "");
	}

	public void setRELEASE_VERSION(String rELEASE_VERSION) {
		editor.putString(RELEASE_VERSION, rELEASE_VERSION);
		editor.commit();
	}

	public String getTOKEN() {
		return sp.getString(TOKEN, "");
	}

	public void setTOKEN(String tOKEN) {
		editor.putString(TOKEN, tOKEN);
		editor.commit();
	}

	public boolean getAutoUpload() {
		return sp.getBoolean(AUTO_UPLOAD, true);
	}

	public void setAutoUpload(boolean isAutoUpload) {
		editor.putBoolean(AUTO_UPLOAD, isAutoUpload);
		editor.commit();
	}

	public String getUID() {
		return sp.getString(UID, "123456789");
	}

	public void setUID(String uid) {
		editor.putString(UID, uid);
		editor.commit();
	}

	public boolean isSavaPWD() {
		return sp.getBoolean(SAVE_PWD, false);
	}

	public void setSavaPWD(boolean savaPWD) {
		editor.putBoolean(SAVE_PWD, savaPWD);
		editor.commit();
	}

	public String getPassword() {
		return sp.getString(PASSWORD, "");
	}

	public void setPassword(String password) {
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	public long getLogTime() {
		return sp.getLong(LOG_TIME, -1);
	}

	public void setLogTime(long logTime) {
		editor.putLong(LOG_TIME, logTime);
		editor.commit();
	}

	public String getUserName() {
		return sp.getString(USERNAME, "");
	}

	public void setUserName(String userName) {
		editor.putString(USERNAME, userName);
		editor.commit();
	}

	public int getDELAY() {
		return sp.getInt(DELAY, Default_DELAY);
	}

	public void setDELAY(int dELAY) {
		editor.putInt(DELAY, dELAY);
		editor.commit();
	}

	public void setUpdate(boolean update) {
		editor.putBoolean(isUpdate, update);
		editor.commit();
	}

	public boolean isUpdate() {
		return sp.getBoolean(isUpdate, false);
	}

	@SuppressLint("CommitPrefEdits")
	public ConfigUtil(Context context) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public String getProcess() {
		return sp.getString(PROCESS, "");
	}

	public void setProcess(String process) {
		editor.putString(PROCESS, process);
		editor.commit();
	}

}
