package com.sodhotuition.dswevents;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.StringReader;
import java.lang.reflect.Type;

import static com.sodhotuition.dswevents.ApplicationController.preferences;

public class BaseActivity extends AppCompatActivity {
  private Dialog pDialog;

  public void showLoadingProgress(String message) {
    if (pDialog == null) {
      pDialog = new ProgressDialogUtil().createDialog(this, message);
    }
    pDialog.show();
  }

  public void hideProgressDialog() {
    if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
  }
  protected String getPrefString(String key) {
    return preferences.getString(key);
  }

  protected boolean getPrefBoolean(String key) {
    return preferences.getBoolean(key);
  }

  protected <T> T getPrefObject(String key, @NonNull Class<T> valueType) {
    String s = preferences.getString(key);
    return new Gson().fromJson(s, valueType);
  }

  protected Object getPrefObject(String key, @NonNull Type typeToken) {
    String s = preferences.getString(key);
    StringReader reader = new StringReader(s);
    return new Gson().fromJson(reader, typeToken);
  }

  protected void storeOrUpdateObject(String key, Object object) {
    String s = new Gson().toJson(object);
    preferences.storeOrUpdateSting(key, s);
  }

  protected void storeOrUpdateBoolean(String key, boolean object) {
    preferences.storeOrUpdateBoolean(key, object);
  }

  protected void storeOrUpdateInt(String key, int object) {
    preferences.storeOrUpdateInt(key, object);
  }
}
