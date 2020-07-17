//package com.sodhotuition.dswevents;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.sodhotuition.dswevents.Model.EventData;
//
//public class CreateNewEvent extends AppCompatActivity {
//  private static final int RESULT_LOAD_IMAGE = 1;
//  private EditText txtename,txt_start_date,txt_end_date,txt_start_time,txt_end_time,txt_contact,txt_email_no,txt_venu,txt_desc;
//  private Spinner spStartTime,spEndTime;
//  private DatabaseReference mReference;
//
//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_create_event);
//    Log.i("TATAT", "onCreate: sss");
//    initiateView();
//    mReference = FirebaseDatabase.getInstance().getReference();
//  }
//
//
//  private void initiateView() {
//    txtename = findViewById(R.id.txtename);
//    txt_start_date = findViewById(R.id.txt_start_date);
//    txt_end_date = findViewById(R.id.txt_end_date);
//    txt_start_time = findViewById(R.id.txt_start_time);
//    txt_end_time = findViewById(R.id.txt_end_time);
//    txt_contact = findViewById(R.id.txt_contact);
//    txt_email_no = findViewById(R.id.txt_email_no);
//    txt_venu = findViewById(R.id.txt_venu);
//    txt_desc = findViewById(R.id.txt_desc);
//    spStartTime = findViewById(R.id.spStartTime);
//    spEndTime = findViewById(R.id.spEndTime);
//  }
//
//  public void uploadFile(View view) {
//    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//    startActivityForResult(i, RESULT_LOAD_IMAGE);
//  }
//
//  @Override
//  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//  }
//
//  public void createEvent(View view) {
//    EventData  eventData = new EventData(txtename.getText().toString(),txt_start_date.getText().toString(),txt_end_date.getText().toString(),
//        txt_start_time.getText().toString(),txt_end_time.getText().toString(),txt_contact.getText().toString(),txt_email_no.getText().toString(),
//        txt_venu.getText().toString(),txt_desc.getText().toString());
//    //String id = mReference.child("events_tbl").push().toString();
//    //eventData.setId(id);
//    //Log.i("TAG", "createEvent: "+id);
//    mReference.child("events_tbl").push().setValue(eventData);
//    Toast.makeText(this,"Event created successfully",Toast.LENGTH_SHORT).show();
//    onBackPressed();
//  }
//}
