package com.sodhotuition.dswevents;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sodhotuition.dswevents.Model.Organization;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

  private DatabaseReference ref;
  private List<Organization> orgList = new ArrayList<>();
  private boolean doubleBackToExitPressedOnce = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ref = FirebaseDatabase.getInstance().getReference(); //initialiazed
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //read data from firebase
        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Organization").getChildren()) {
          //single datasnapshot for one student obj
          Organization org = dataSnapshot1.getValue(Organization.class);
          orgList.add(org);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    setContentView(R.layout.activity_login);

       /* database = FirebaseDatabase.getInstance();
        ref = database.getReference("Organization");

        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        btnlogin =   findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(),
                        password.getText().toString());
            }
        });*/
  }

  /*  private void login(final String email,final String password) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(email).exists()){
                    if (!email.isEmpty()){
                        Organization login = dataSnapshot.child(email).getValue(Organization.class);
                        if (login.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this,"LoginActivity Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,Register.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this,"Password is Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this,"Email not Valid",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

  public void btn_register(View view) {
    startActivity(new Intent(getApplicationContext(), Register.class));
  }


  public void btn_login(View view) {
    Log.i("TAGG", "login: " + orgList);
    final EditText email = findViewById(R.id.email);
    final EditText password = findViewById(R.id.password);
    if (!email.getText().toString().trim().isEmpty()) {
      Log.i("TAGG", "login: user Input");
      if (!password.getText().toString().trim().isEmpty()) {
        Log.i("TAGG", "login: pass input");
        showLoadingProgress(null);
        ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //read data from firebase
            hideProgressDialog();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("organization").getChildren()) {
              //single datasnapshot for one student obj
              Log.i("TAGGA", "onDataChange: " + dataSnapshot1.getValue());
              Organization org = dataSnapshot1.getValue(Organization.class);
              if (org != null)
                org.setKey(dataSnapshot1.getKey());
              orgList.add(org);
            }
            for (Organization org : orgList) {
              if (org.getEmail().equalsIgnoreCase(email.getText().toString())) {
                Log.i("TAGG", "login: userId present" + org.getPassword() + "::" + password.getText().toString());
                if (password.getText().toString().equalsIgnoreCase(org.getPassword())) {
                  storeOrUpdateObject(AppConstant.SIGNED_USER, org);
                  storeOrUpdateBoolean(AppConstant.IS_SIGNED, true);
                  Toast.makeText(LoginActivity.this, "LoginActivity successfully!", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(getApplicationContext(), ManageEventActivity.class));
                  finish();
                } else {
                  password.setText("");
                  Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
                break;
              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        });

      } else
        Toast.makeText(this, "Password can't be blank", Toast.LENGTH_SHORT).show();
    } else
      Toast.makeText(this, "Email can't be blank", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      super.onBackPressed();
      return;
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        doubleBackToExitPressedOnce = false;
      }
    }, 2000);
  }
}
