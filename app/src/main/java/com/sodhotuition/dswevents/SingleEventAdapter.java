package com.sodhotuition.dswevents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sodhotuition.dswevents.Model.EventData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int EMPTY_VIEW = 1;
  private final Activity activity;
  private List<EventData> eventDataList;
  private MyClickListener listener;

  public SingleEventAdapter(FragmentActivity activity, List<EventData> eventDataList) {
    this.eventDataList = eventDataList;
    this.activity = activity;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_single_event, parent, false);
    if (viewType == EMPTY_VIEW) {
      rowView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.layout_empty_list, parent, false);
      return new EmptyViewHolder(rowView);
    }
    return new ViewHolder(rowView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    if (holder instanceof EmptyViewHolder) {
      EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
      viewHolder.emptyListTextView.setText("No event available to manage");
    } else {
      ViewHolder viewHolder = (ViewHolder) holder;
      EventData eventData = eventDataList.get(position);
      if (eventData != null) {
        viewHolder.ivEventImage.setImageResource(R.drawable.dsw_icon);
        viewHolder.tvEventId.setText("#0" + (position + 1));
        viewHolder.tvEventName.setText(eventData.e_name);
        viewHolder.tvEventDateTime.setText(eventData.e_start_date + " - " + eventData.e_end_date + " | "
            + eventData.e_start_time + " - " + eventData.e_end_time);
        viewHolder.tvEventLocation.setText(eventData.e_venue);
        if (eventData.getKey() != null && eventData.getHasImage()) {
          StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
          mStorageReference.child(eventData.getKey()).getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(activity)
              .load(uri)
              .fit()
              .error(R.drawable.blank_img)
              .placeholder(R.drawable.blank_img)
              .into(viewHolder.ivEventImage));
        }
      }
    }
  }

  @Override
  public int getItemCount() {
    Log.i("TAGGA", "getOrderItemCount: " + eventDataList.size());
    return eventDataList.size() > 0 ? eventDataList.size() : 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (eventDataList.size() == 0) {
      return EMPTY_VIEW;
    }
    return super.getItemViewType(position);
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final AppCompatTextView tvEventId, tvEventName, tvEventDateTime, tvEventLocation;
    final CircleImageView ivEventImage;

    ViewHolder(View view) {
      super(view);
      tvEventId = view.findViewById(R.id.tvEventId);
      tvEventName = view.findViewById(R.id.tvEventName);
      tvEventDateTime = view.findViewById(R.id.tvEeventDateTime);
      tvEventLocation = view.findViewById(R.id.tvEventLocation);
      ivEventImage = view.findViewById(R.id.ivEventImage);
      view.setOnClickListener(v -> {
        if (listener != null) {
          listener.setItemClick(v, getAdapterPosition());
        }
      });
    }
  }

  class EmptyViewHolder extends RecyclerView.ViewHolder {
    private final TextView emptyListTextView;

    EmptyViewHolder(View itemView) {
      super(itemView);
      emptyListTextView = itemView.findViewById(R.id.emptyListTextView);
      Button btnCreateNewEvent = itemView.findViewById(R.id.btnCreateNewEvent);
      btnCreateNewEvent.setOnClickListener(v -> {
        if (listener != null) {
          listener.setItemClick(v, getAdapterPosition());
        }
      });
    }
  }

  public void addClickListener(MyClickListener listener) {
    this.listener = listener;
  }
}
