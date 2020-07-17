package com.sodhotuition.dswevents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sodhotuition.dswevents.Model.Organization;

public class UpdateUserActivity extends BaseActivity {
  private TextView tvStName;
  private EditText etEventName, etEventContact, etEventEmail, etEventDes;
  private TextView tvMemberCount;
  private AppCompatButton btnProfileUpdate;
  private Organization organization = null;
  private DatabaseReference databaseReference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_update_user);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    tvStName = findViewById(R.id.tvStName);
    etEventName = findViewById(R.id.etEventName);
    etEventContact = findViewById(R.id.etEventContact);
    etEventEmail = findViewById(R.id.etEventEmail);
    etEventDes = findViewById(R.id.etEventDes);
    tvMemberCount = findViewById(R.id.tvMemberCount);
    btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
    btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateUser(v);
      }
    });
    databaseReference = FirebaseDatabase.getInstance().getReference();
    organization = getPrefObject(AppConstant.SIGNED_USER, Organization.class);
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle("");
      if (organization != null)
        tvStName.setText(organization.name);
    }
    dataInitiate();
  }

  private void dataInitiate() {
    if (organization != null) {
      etEventName.setText(organization.name);
      etEventContact.setText(organization.phone);
      etEventEmail.setText(organization.email);
      etEventDes.setText(organization.description);
      tvMemberCount.setText(organization.members);
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  public void updateUser(View view) {
    Organization or = new Organization();
    or.name = etEventName.getText().toString();
    or.email = etEventEmail.getText().toString();
    or.phone = etEventContact.getText().toString();
    or.description = etEventDes.getText().toString();
    or.members = organization.members;
    or.password = this.organization.password;
    or.setKey(organization.getKey());
    Log.i("KLKL", "updateUser: " + organization.getKey());
    if (organization.getKey() != null)
      databaseReference.child("organization").child(organization.getKey()).setValue(or);
    storeOrUpdateObject(AppConstant.SIGNED_USER, or);
    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
    startActivity(new Intent(this, ManageEventActivity.class));
    finish();
  }

}
