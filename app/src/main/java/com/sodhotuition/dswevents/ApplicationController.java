package com.sodhotuition.dswevents;

import android.app.Application;
public class ApplicationController extends Application {
  public static DswEventPreferences preferences;

  @Override
  public void onCreate() {
    super.onCreate();
    preferences = DswEventPreferences.getInstance(this);
  }

  public DswEventPreferences getPreferences() {
    return preferences;
  }
}
