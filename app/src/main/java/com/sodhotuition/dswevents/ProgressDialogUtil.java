package com.sodhotuition.dswevents;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;

public class ProgressDialogUtil {

  public Dialog createDialog(Context context, String message) {
    final AppCompatDialog progressDialog = new AppCompatDialog(context);
    progressDialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    progressDialog.setContentView(R.layout.custom_progress_dialog);
    LinearLayout llProgress = progressDialog.findViewById(R.id.llProgress);
    if (progressDialog.getWindow()!= null)
      progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    progressDialog.setCancelable(false);
    progressDialog.setCanceledOnTouchOutside(false);
    if (message != null) {
      ((AppCompatTextView) progressDialog.findViewById(R.id.tv_progress_text)).setText(message);
      llProgress.setBackgroundColor(context.getResources().getColor(android.R.color.white));
      progressDialog.findViewById(R.id.tv_progress_text).setVisibility(View.VISIBLE);
    }
    else {
      llProgress.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
      progressDialog.findViewById(R.id.tv_progress_text).setVisibility(View.GONE);
    }
    return progressDialog;
  }

}
