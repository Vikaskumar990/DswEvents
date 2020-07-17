package com.sodhotuition.dswevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class EventDetailsFragment extends Fragment {
  public static final String DATA = "bundle_data";
  private EditText text_name, txt_contact, txt_email_no, txt_venu, txt_desc;
  private TextView textStartDate, textEndDate, textStartTime, textEndTime;
  private EventData eventData = new EventData();
  private DatabaseReference mReference;
  private StorageReference mStorageReference;
  private ImageView ivUploadImage;
  private ManageEventActivity activity;
  private Uri eventImage;
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

  private int hourOfDay = 7;
  private int minute = 0;
  private View.OnClickListener timeClickListener = v -> {
    TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
        (view, hourOfDay, minute) -> {
          EventDetailsFragment.this.hourOfDay = hourOfDay;
          EventDetailsFragment.this.minute = minute;
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

  public EventDetailsFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getArguments();
    if (bundle != null) {
      eventData = bundle.getParcelable(DATA);
    }
    mReference = FirebaseDatabase.getInstance().getReference();
    mStorageReference = FirebaseStorage.getInstance().getReference();
    activity = (ManageEventActivity) getActivity();
    if (activity != null && activity.getSupportActionBar() != null) {
      activity.getSupportActionBar().setTitle(TextUtils.isEmpty(eventData.e_name) ? getResources().getString(R.string.event_detail) : eventData.e_name);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_event_details, container, false);
    initiate(view);
    updateDataView();
    return view;
  }

  private void updateDataView() {
    if (eventData != null) {
      text_name.setText(eventData.e_name);
      textStartDate.setText(eventData.e_start_date);
      textEndDate.setText(eventData.e_end_date);
      textStartTime.setText(eventData.e_start_time);
      textEndTime.setText(eventData.e_end_time);
      txt_contact.setText(eventData.e_contact);
      txt_email_no.setText(eventData.e_email_id);
      txt_venu.setText(eventData.e_venue);
      txt_desc.setText(eventData.e_desc);
    }
  }

  private void initiate(View view) {
    text_name = view.findViewById(R.id.txtename);
    ivUploadImage = view.findViewById(R.id.ivUploadImage);
    textStartDate = view.findViewById(R.id.textViewStartDate);
    textEndDate = view.findViewById(R.id.textViewEnddate);
    textStartTime = view.findViewById(R.id.textViewStartTime);
    textEndTime = view.findViewById(R.id.textViewEndTime);
    txt_contact = view.findViewById(R.id.editTxtContact);
    txt_email_no = view.findViewById(R.id.editTxtEmailId);
    txt_venu = view.findViewById(R.id.txt_venu);
    txt_desc = view.findViewById(R.id.txt_desc);
    view.findViewById(R.id.btUpdateEvent).setOnClickListener(v -> updateEvent());
    ivUploadImage.setOnClickListener(v -> activity.uploadFile((bitmap, uri) -> {
      if (bitmap != null) {
        eventImage = uri;
        ivUploadImage.setImageBitmap(bitmap);
      }
    }));
    textStartDate.setOnClickListener(dateClickListener);
    textEndDate.setOnClickListener(dateClickListener);
    textStartTime.setOnClickListener(timeClickListener);
    textEndTime.setOnClickListener(timeClickListener);
    mStorageReference.child(eventData.getKey()).getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(getActivity())
        .load(uri)
        .fit()
        .error(R.drawable.blank_img)
        .placeholder(R.drawable.blank_img)
        .into(ivUploadImage));
  }

  private void updateEvent() {
    activity.showLoadingProgress("Updating...");
    EventData eventData = new EventData(text_name.getText().toString(), textStartDate.getText().toString(),
        textEndDate.getText().toString(), textStartTime.getText().toString(), textEndTime.getText().toString(),
        txt_contact.getText().toString(), txt_email_no.getText().toString(), txt_venu.getText().toString(),
        txt_desc.getText().toString());
    mReference.child("events_tbl").child(this.eventData.getKey()).setValue(eventData);
    if (eventImage != null) {
      mStorageReference.child(this.eventData.getKey()).putFile(eventImage).addOnSuccessListener(
          taskSnapshot -> {
            activity.hideProgressDialog();
            Log.i("TAGAG", "onSuccess: " + taskSnapshot.getUploadSessionUri());
            Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
            if (getActivity() != null && getActivity() instanceof ManageEventActivity)
              getActivity().onBackPressed();
          })
          .addOnFailureListener(e -> {
            activity.hideProgressDialog();
            Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
          });
    } else {
      activity.hideProgressDialog();
      Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
      if (getActivity() != null && getActivity() instanceof ManageEventActivity)
        getActivity().onBackPressed();
    }
  }
}
