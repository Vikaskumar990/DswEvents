package com.sodhotuition.dswevents;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import static com.sodhotuition.dswevents.AppConstant.IS_SIGNED;

public class FlashActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent homeIntent = new Intent(FlashActivity.this, getPrefBoolean(IS_SIGNED) ?
            ManageEventActivity.class : LoginActivity.class);
        startActivity(homeIntent);
        finish();
      }
    }, 2000);
  }
}
