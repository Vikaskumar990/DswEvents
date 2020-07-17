package com.sodhotuition.dswevents;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.sodhotuition.dswevents.Model.Organization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class ManageEventActivity extends BaseActivity implements ValueEventListener, MenuItem.OnMenuItemClickListener {
  private static final int RESULT_LOAD_IMAGE = 1;
  private final int MULTIPLE_PERMISSIONS = 10;
  private String[] permissionsList = new String[]{
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
  };
  private AppBarConfiguration mAppBarConfiguration;
  private NavController navController;
  private boolean doubleBackToExitPressedOnce = false;
  private DrawerLayout drawer;
  private View head;
  private TextView tvOrName, tvOrEmail;
  private AppCompatButton btnUserUpdate;
  private MyBitmapListener listener = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_event);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.getMenu().findItem(R.id.nav_create_event).setOnMenuItemClickListener(this);
    navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(this);
    navigationView.setItemIconTintList(null);
    head = navigationView.getHeaderView(0);
    initiateHeader();
    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    mReference.addValueEventListener(this);
    mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_conducted_event, R.id.nav_organizer_details,
        R.id.nav_tools, R.id.nav_share, R.id.nav_help)
        .setDrawerLayout(drawer)
        .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }

  private void initiateHeader() {
    tvOrName = head.findViewById(R.id.tvOrName);
    tvOrEmail = head.findViewById(R.id.tvOrEmail);
    head.findViewById(R.id.btnUserUpdate).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(ManageEventActivity.this, UpdateUserActivity.class));
      }
    });
    Organization organization = getPrefObject(AppConstant.SIGNED_USER, Organization.class);
    if (organization != null) {
      tvOrName.setText(organization.name);
      tvOrEmail.setText(organization.email);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.manage_event, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
        || super.onSupportNavigateUp();
  }

  public void switchFragments(int id, Bundle agrs) {
    navController.navigate(id, agrs);
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }

  /**
   * Called when a menu item has been invoked.  This is the first code
   * that is executed; if it returns true, no other callbacks will be
   * executed.
   *
   * @param item The menu item that was invoked.
   * @return Return true to consume this click and prevent others from
   * executing.
   */
  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav_logout:
        storeOrUpdateBoolean(AppConstant.IS_SIGNED, false);
        startActivity(new Intent(ManageEventActivity.this, LoginActivity.class));
        finish();
        break;
      case R.id.nav_create_event:
        switchFragments(R.id.nav_create_new, null);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
          drawer.closeDrawer(GravityCompat.START);
        break;
    }
    return true;
  }

  private boolean checkPermissions() {
    int result;
    List<String> listPermissionsNeeded = new ArrayList<>();
    for (String p : permissionsList) {
      result = ContextCompat.checkSelfPermission(this, p);
      if (result != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(p);
      }
    }
    if (!listPermissionsNeeded.isEmpty()) {
      ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
      return false;
    }
    return true;
  }

  public void uploadFile(MyBitmapListener listener) {
    this.listener = listener;
    if (checkPermissions()) {
      Intent i = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
      startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
      try {
        Bitmap bitmap = null;
        if (data != null) {
          bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
          bitmap = rotateImage(data, bitmap);
          if (listener != null) listener.onResultData(bitmap, data.getData());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param data   //Intent data from storage.
   * @param bitmap //Default Bitmap from intent data.
   * @return Modified Bitmap
   */
  private Bitmap rotateImage(@NonNull Intent data, Bitmap bitmap) {
    // Get selected gallery image
    Uri selectedPicture = data.getData();
    // Get and resize profile image
    if (selectedPicture != null) {
      String[] filePathColumn = {MediaStore.Images.Media.DATA};
      String picturePath = null;
      Cursor cursor = getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();
      }
      if (TextUtils.isEmpty(picturePath)) {
        picturePath = selectedPicture.getPath();
      }
      Bitmap loadedBitmap = null;
      if (data.getDataString() != null && data.getDataString().contains("docs.file")) {
        try {
          InputStream inputStream = getContentResolver().openInputStream(selectedPicture);
          loadedBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
      if (loadedBitmap == null)
        loadedBitmap = BitmapFactory.decodeFile(picturePath);
      ExifInterface exif = null;
      try {
        if (picturePath != null) {
          File pictureFile = new File(picturePath);
          exif = new ExifInterface(pictureFile.getAbsolutePath());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      int orientation = ExifInterface.ORIENTATION_NORMAL;

      if (exif != null)
        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          bitmap = rotateBitmap(loadedBitmap, 90);
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          bitmap = rotateBitmap(loadedBitmap, 180);
          break;

        case ExifInterface.ORIENTATION_ROTATE_270:
          bitmap = rotateBitmap(loadedBitmap, 270);
          break;
      }
    }
    return bitmap;
  }

  private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
    Matrix matrix = new Matrix();
    matrix.postRotate(degrees);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == MULTIPLE_PERMISSIONS) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        uploadFile(listener);
      } else {
        showAlert(this, getString(R.string.ok), (dialogInterface, i) -> {
          dialogInterface.dismiss();
          checkPermissions();
        }, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
      }
    }
  }

  private void showAlert(Context context, CharSequence positiveButtonText,
                         DialogInterface.OnClickListener positiveListener, CharSequence negativeButtonText,
                         DialogInterface.OnClickListener negativeListener) {
    AlertDialog.Builder dlg = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
    dlg.setIcon(android.R.drawable.ic_dialog_alert);
    dlg.setTitle(Html.fromHtml(String.format("<font color='#212121'>%s</font>", context.getString(R.string.permission_alert))));
    dlg.setPositiveButton(positiveButtonText, positiveListener);
    dlg.setNegativeButton(negativeButtonText, negativeListener);
    dlg.setMessage("Allow all permission are require, Do you want try again").setCancelable(false).create();
    dlg.show();
  }

  public String getDateInSpecificFormat(@NotNull Calendar currentCalDate) {
    String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
    DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMM yyyy", Locale.getDefault());
    return dateFormat.format(currentCalDate.getTime());
  }

  @NotNull
  private String getDayNumberSuffix(int dayOfMonth) {
    if (dayOfMonth >= 11 && dayOfMonth <= 13) {
      return "th";
    }
    switch (dayOfMonth % 10) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }


//  @Override
//  public void onBackPressed() {
//    Log.i("TAGAG", "onBackPressed: " + navController.popBackStack());
//    if (doubleBackToExitPressedOnce ||
//          navController.getCurrentDestination().getId() == R.id.nav_create_new ||
//          navController.getCurrentDestination().getId() == R.id.nav_event_detail) {
//      super.onBackPressed();
//    } else {
//
//      this.doubleBackToExitPressedOnce = true;
//      Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//      new Handler().postDelayed(new Runnable() {
//
//        @Override
//        public void run() {
//          doubleBackToExitPressedOnce = false;
//        }
//      }, 2000);
//    }
//  }
}
