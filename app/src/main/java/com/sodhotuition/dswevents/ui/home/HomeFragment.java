package com.sodhotuition.dswevents.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sodhotuition.dswevents.EventDetailsFragment;
import com.sodhotuition.dswevents.ManageEventActivity;
import com.sodhotuition.dswevents.Model.EventData;
import com.sodhotuition.dswevents.MyClickListener;
import com.sodhotuition.dswevents.R;
import com.sodhotuition.dswevents.SingleEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyClickListener {
  private ManageEventActivity activity;
  private SwipeRefreshLayout srlEventList;
  private RecyclerView rvEventList;
  private DatabaseReference mReference;
  private StorageReference mStorageReference;
  private List<EventData> eventDataList = new ArrayList<>();
  private SingleEventAdapter adapter;
  private Button buttonCreateEvent;

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onPause() {
    super.onPause();
    adapter = null;
    eventDataList = new ArrayList<>();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getActivity() instanceof ManageEventActivity)
      activity = (ManageEventActivity) getActivity();
    String EVENTS_TABLE = "events_tbl";
    mReference = FirebaseDatabase.getInstance().getReference(EVENTS_TABLE);
    mStorageReference = FirebaseStorage.getInstance().getReference();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    Log.i(HomeFragment.class.getSimpleName(), "onCreateView: ");
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    buttonCreateEvent = root.findViewById(R.id.btn_create_event);
    srlEventList = root.findViewById(R.id.srlEventList);
    buttonCreateEvent.setOnClickListener(v -> createNewEvent());
    rvEventList = root.findViewById(R.id.rvEventList);
    rvEventList.setHasFixedSize(false);
    rvEventList.setLayoutManager(new LinearLayoutManager(activity));
    fetchData();
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    srlEventList.setOnRefreshListener(this::fetchData);
  }

  private void fetchData() {
    if (mReference != null) {
      if (eventDataList.size() == 0) activity.showLoadingProgress("Fetching event...");
      mReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          eventDataList.clear();
          if (srlEventList.isRefreshing())
            srlEventList.setRefreshing(false);
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            try {
              EventData eventData = snapshot.getValue(EventData.class);
              if (eventData != null) eventData.setKey(snapshot.getKey());
              eventDataList.add(eventData);
            } catch (Error | Exception e) {
              e.printStackTrace();
              break;
            }
          }
          setImagesToEventList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }
  }

  private void setImagesToEventList() {
    List<String> keys = new ArrayList<>();
    mStorageReference.listAll().addOnSuccessListener(listResult -> {
      for (StorageReference reference : listResult.getItems()) {
        keys.add(reference.getName());
      }
      if (keys.size() > 0 && eventDataList.size() > 0) {
        for (EventData data : eventDataList) {
          data.setHasImage(keys.contains(data.getKey()));
        }
      }
      activity.hideProgressDialog();
      if (adapter != null)
        adapter.notifyDataSetChanged();
      else {
        adapter = new SingleEventAdapter(getActivity(), eventDataList);
        adapter.addClickListener(HomeFragment.this);
        rvEventList.setAdapter(adapter);
      }
      buttonCreateEvent.setVisibility(eventDataList.size() > 0 ? View.VISIBLE : View.GONE);
    }).addOnFailureListener(e -> activity.hideProgressDialog());

  }

  private void createNewEvent() {
    activity.switchFragments(R.id.nav_create_new, null);
  }

  @Override
  public void setItemClick(View v, int adapterPosition) {
    if (v.getId() == R.id.btnCreateNewEvent)
      createNewEvent();
    else {
      Bundle bundle = new Bundle();
      bundle.putParcelable(EventDetailsFragment.DATA, eventDataList.get(adapterPosition));
      activity.switchFragments(R.id.nav_event_detail, bundle);
    }
  }
}