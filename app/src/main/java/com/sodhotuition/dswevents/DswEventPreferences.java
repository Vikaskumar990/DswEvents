package com.sodhotuition.dswevents;

import android.content.Context;
import android.content.SharedPreferences;

class DswEventPreferences {

  private static final String PREFERENCE_FILE = "dswevent";
  private static DswEventPreferences appPreferences;
  private final SharedPreferences mSharedPreferences;
  private final SharedPreferences.Editor mSharedPreferencesEditor;

  private DswEventPreferences(Context context, String permissionFile) {
    mSharedPreferences = context.getSharedPreferences(permissionFile, Context.MODE_PRIVATE);
    mSharedPreferencesEditor = mSharedPreferences.edit();
    mSharedPreferencesEditor.apply();
  }

  public static DswEventPreferences getInstance(Context context) {
    if (appPreferences == null) {
      synchronized (DswEventPreferences.class) {
        appPreferences = new DswEventPreferences(context, PREFERENCE_FILE);
      }
    }
    return appPreferences;
  }
  public String getString(String key){
    return  mSharedPreferences.getString(key,null);
  }

  public void storeOrUpdateSting(String key, String value){
    synchronized (this) {
      mSharedPreferencesEditor.putString(key, value);
      mSharedPreferencesEditor.apply();
    }
  }

  public boolean getBoolean(String key){
    return  mSharedPreferences.getBoolean(key,false);
  }

  public void storeOrUpdateBoolean(String key, boolean value){
    synchronized (this) {
      mSharedPreferencesEditor.putBoolean(key, value);
      mSharedPreferencesEditor.apply();
    }
  }

  public void storeOrUpdateInt(String key, int value) {
    synchronized (this) {
      mSharedPreferencesEditor.putInt(key, value);
      mSharedPreferencesEditor.apply();
    }
  }
}
