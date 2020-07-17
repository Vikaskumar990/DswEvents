package com.sodhotuition.dswevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sodhotuition.dswevents.Model.EventData;

import java.util.Calendar;


public class CreateEventForm extends Fragment {
  private EditText txtename, editTxtContact, editTxtEmailId, txt_venu, txt_desc;
  private TextView textStartDate, textEndDate, textStartTime, textEndTime;
  private DatabaseReference mReference;
  private StorageReference mStorageReference;
  private ManageEventActivity activity;
  private ImageView ivUploadImage;
  private Button btRemoveImage;
  private Uri eventImage;
  private int hourOfDay = 7, minute = 0;
  private Calendar startDateCalendar = Calendar.getInstance();
  private Calendar endDateCalendar = Calendar.getInstance();
  private View.OnClickListener dateClickListener = v -> {
    Calendar calendar = v.getId() == R.id.textViewStartDate ? startDateCalendar : endDateCalendar;
    int mYear = calendar.get(Calendar.YEAR);
    int mMonth = calendar.get(Calendar.MONTH);
    int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog pickerDialog = new DatePickerDialog(activity,
        (view, year, month, dayOfMonth) -> {
          calendar.set(year, month, dayOfMonth);
          if (v.getId() == R.id.textViewStartDate) {
            textStartDate.setText(activity.getDateInSpecificFormat(calendar));
            startDateCalendar = calendar;
          } else {
            textEndDate.setText(activity.getDateInSpecificFormat(calendar));
            endDateCalendar = calendar;
          }
        }, mYear, mMonth, mDayOfMonth);
    pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    pickerDialog.show();
  };

  private View.OnClickListener timeClickListener = v -> {
    TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
        (view, hourOfDay, minute) -> {
          CreateEventForm.this.hourOfDay = hourOfDay;
          CreateEventForm.this.minute = minute;
          String time = "";
          int hr = hourOfDay > 12 ? hourOfDay % 12 : hourOfDay;
          time += hr > 10 ? hr : "0" + hr;
          time += " : " + (minute > 10 ? minute : "0" + minute);
          time += hourOfDay > 12 ? " PM" : " AM";

          if (v.getId() == R.id.textViewStartTime) {
            textStartTime.setText(time);
          } else {
            textEndTime.setText(time);
          }
        }, hourOfDay, minute, false);
    timePickerDialog.show();
  };

  @Override
  public void onStart() {
    super.onStart();
    if (getActivity() instanceof ManageEventActivity)
      activity = (ManageEventActivity) getActivity();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_create_event, container, false);
    mReference = FirebaseDatabase.getInstance().getReference();
    mStorageReference = FirebaseStorage.getInstance().getReference();
    initiateView(view);
    return view;
  }


  private void initiateView(View view) {
    txtename = view.findViewById(R.id.txtename);
    ivUploadImage = view.findViewById(R.id.ivUploadImage);
    btRemoveImage = view.findViewById(R.id.btRemoveImage);
    textStartDate = view.findViewById(R.id.textViewStartDate);
    textEndDate = view.findViewById(R.id.textViewEnddate);
    textStartTime = view.findViewById(R.id.textViewStartTime);
    textEndTime = view.findViewById(R.id.textViewEndTime);
    editTxtContact = view.findViewById(R.id.editTxtContact);
    editTxtEmailId = view.findViewById(R.id.editTxtEmailId);
    txt_venu = view.findViewById(R.id.txt_venu);
    txt_desc = view.findViewById(R.id.txt_desc);
//    spStartTime = view.findViewById(R.id.spStartTime);
//    spEndTime = view.findViewById(R.id.spEndTime);
    textStartDate.setOnClickListener(dateClickListener);
    textEndDate.setOnClickListener(dateClickListener);
    textStartTime.setOnClickListener(timeClickListener);
    textEndTime.setOnClickListener(timeClickListener);

    view.findViewById(R.id.bt_new_event).setOnClickListener(view1 -> createEvent());
    view.findViewById(R.id.buttonUpload).setOnClickListener(v -> activity.uploadFile((bitmap, uri) -> {
      if (bitmap != null) {
        eventImage = uri;
        btRemoveImage.setVisibility(View.VISIBLE);
        ivUploadImage.setImageBitmap(bitmap);
      }
    }));
    btRemoveImage.setOnClickListener(v -> {
      eventImage = null;
      btRemoveImage.setVisibility(View.GONE);
      ivUploadImage.setImageResource(R.drawable.blank_img);
    });
  }

  private void createEvent() {
    EventData eventData = new EventData(txtename.getText().toString(), textStartDate.getText().toString(),
        textEndDate.getText().toString(), textStartTime.getText().toString(), textEndTime.getText().toString(),
        editTxtContact.getText().toString(), editTxtEmailId.getText().toString(), txt_venu.getText().toString(),
        txt_desc.getText().toString());
    DatabaseReference refTemp = mReference.child("events_tbl").push();
    refTemp.setValue(eventData);
    if (refTemp.getKey() != null && eventImage != null) {
      mStorageReference.child(refTemp.getKey()).putFile(eventImage)
          .addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(activity, "Event created successfully", Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
          })
          .addOnFailureListener(Throwable::printStackTrace);
    } else {
      Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
      if (getActivity() != null && getActivity() instanceof ManageEventActivity)
        getActivity().onBackPressed();
    }

  }
}