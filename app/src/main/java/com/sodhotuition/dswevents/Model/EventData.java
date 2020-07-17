package com.sodhotuition.dswevents.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventData implements Parcelable {
  //txtename,txt_start_date,txt_end_date,txt_start_time,txt_end_time,txt_contact,txt_email_no,txt_venu,txt_desc;
  public String e_name;
  public String e_start_date;
  public String e_end_date;
  public String e_start_time;
  public String e_end_time;
  public String e_contact;
  public String e_email_id;
  public String e_venue;
  public String e_desc;
  public String e_id;
  public String e_banner;
  private String key;
  private boolean hasImage = false;

  public EventData() {
  }

  public EventData(String e_name, String e_start_date, String e_end_date,
                   String e_start_time, String e_end_time, String e_contact, String e_email_id, String e_venue, String e_desc) {
    this.e_name = e_name;
    this.e_start_date = e_start_date;
    this.e_end_date = e_end_date;
    this.e_start_time = e_start_time;
    this.e_end_time = e_end_time;
    this.e_contact = e_contact;
    this.e_email_id = e_email_id;
    this.e_venue = e_venue;
    this.e_desc = e_desc;
  }

  protected EventData(Parcel in) {
    e_name = in.readString();
    e_start_date = in.readString();
    e_end_date = in.readString();
    e_start_time = in.readString();
    e_end_time = in.readString();
    e_contact = in.readString();
    e_email_id = in.readString();
    e_venue = in.readString();
    e_desc = in.readString();
    e_id = in.readString();
    e_banner = in.readString();
  }

  public static final Creator<EventData> CREATOR = new Creator<EventData>() {
    @Override
    public EventData createFromParcel(Parcel in) {
      return new EventData(in);
    }

    @Override
    public EventData[] newArray(int size) {
      return new EventData[size];
    }
  };

  public void setId(String id) {
    this.e_id = id;
  }

  public String getId() {
    return e_id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(e_name);
    dest.writeString(e_start_date);
    dest.writeString(e_end_date);
    dest.writeString(e_start_time);
    dest.writeString(e_end_time);
    dest.writeString(e_contact);
    dest.writeString(e_email_id);
    dest.writeString(e_venue);
    dest.writeString(e_desc);
    dest.writeString(e_id);
    dest.writeString(e_banner);
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setHasImage(boolean hasImage) {
    this.hasImage = hasImage;
  }

  public boolean getHasImage() {
    return hasImage;
  }
}
