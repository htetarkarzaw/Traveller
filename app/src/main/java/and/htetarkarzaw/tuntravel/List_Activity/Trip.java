package and.htetarkarzaw.tuntravel.List_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Model.TripModel;
import and.htetarkarzaw.tuntravel.OnLoadMoreListener;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.ShowActivity;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 4/3/2017.
 */
public class Trip extends Fragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    Button addRegularTrip,addOrderTrip;
    ImageButton btnSearchTrip;
    RecyclerView rcTrip;
    EditText etSTrip;
    private DatabaseReference databaseReference, databaseReference1;
    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3;
    private ValueEventListener searchForTripName, searchForTripStartDate,searchForTripEndDate, searchForCarNo, searchForDriverName, searchForConductorName1,searchForConductorName2,searchForGuideName;
    private WrapLinearLayoutManager manager;
    private TripRcAdapter adapterForList;
    private TripSearchAdapter adapterForSearch;
    private long totalServerItemsCount = 0;
    private long currentItemCount = 0;
    private ArrayList<TripModel> tripModels, reverseModels;
    private ArrayList<String> keys, reverseKeys;
    private ArrayList<TripModel> searchedTripModels = new ArrayList<>();
    private ArrayList<String> searchedKeys = new ArrayList<>();
    private boolean isDoneTripName, isDoenStartDateTrip,isDoneEndDateTrip, isDoneCarNo, isDoneDriverName, isDoneConductorName1,isDoneConductorName2,isDoneGuideName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trip,container,false);
        rcTrip= (RecyclerView) v.findViewById(R.id.rcTrip);
        btnSearchTrip = (ImageButton) v.findViewById(R.id.btnSTrip);
        etSTrip= (EditText) v.findViewById(R.id.etSearchTrip);
        etSTrip.clearFocus();
        addRegularTrip = (Button) v.findViewById(R.id.addTripRegular);
        addOrderTrip = (Button) v.findViewById(R.id.addTripOrder);
        keys = new ArrayList<>();
        reverseKeys = new ArrayList<>();
        tripModels = new ArrayList<>();
        reverseModels = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIPS);
        manager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcTrip.setLayoutManager(manager);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRegularTrip.setOnClickListener(this);
        addOrderTrip.setOnClickListener(this);
        etSTrip.setOnFocusChangeListener(this);
        etSTrip.addTextChangedListener(this);
        btnSearchTrip.setOnClickListener(this);
        valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (rcTrip.getAdapter() != null) {
                    rcTrip.setAdapter(null);
                }
                getItemsCountFromServer(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIP_COUNTS);
        databaseReference1.addValueEventListener(valueEventListener3);
    }

    private void getItemsCountFromServer(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue()!=null && (long) dataSnapshot.getValue() > 0) {
            totalServerItemsCount = 0;
            totalServerItemsCount = (long) dataSnapshot.getValue();
            Toast.makeText(getActivity(), "Total ServerCount = "+totalServerItemsCount + "", Toast.LENGTH_SHORT).show();
            currentItemCount = 0;
            Log.d("Resume", "CurrentItemCount = " + currentItemCount);
            Log.d("Resume", "TotalServerItemCount = " + totalServerItemsCount);
        } else {
            return;
        }
        if (totalServerItemsCount >= 10) {
            databaseReference.orderByKey().limitToLast(10).addListenerForSingleValueEvent(valueEventListener);
            currentItemCount += 10;
        } else {
            databaseReference.orderByKey().limitToLast(Integer.parseInt(String.valueOf(totalServerItemsCount))).addListenerForSingleValueEvent(valueEventListener);
            currentItemCount += totalServerItemsCount;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!etSTrip.getText().toString().equals("")) {
            etSTrip.setText("");
        }
        if(rcTrip.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
        }
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(TripModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                tripModels.clear();
                keys.clear();
                Log.d("Resume", "Enter Download FirstData");
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i != -1; i--) {
                    tripModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                if(rcTrip.getAdapter()==null){
                    adapterForList = new TripRcAdapter();
                }
                adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
                rcTrip.setAdapter(adapterForList);
                reverseModels.clear();
                reverseKeys.clear();
                databaseReference.removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapterForList.notifyItemRemoved(tripModels.size() - 1);
                tripModels.remove(tripModels.size() - 1);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(TripModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                tripModels.remove(tripModels.size() - 1);
                keys.remove(keys.size() - 1);
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i >= 0; i--) {
                    tripModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                for (int j = 0; j < keys.size(); j++) {
                    Log.d("Keys", "key = " + keys.get(j));
                }
                Log.d("children", dataSnapshot.getChildrenCount() + "");
                reverseModels.clear();
                reverseKeys.clear();
                Toast.makeText(getContext(), "onLoad = " + tripModels.size(), Toast.LENGTH_SHORT).show();
                adapterForList.notifyDataSetChanged();
                adapterForList.setLoaded();
                databaseReference.removeEventListener(valueEventListener2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if(rcTrip.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(null);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addTripRegular:
                Intent intent = new Intent(getActivity(),AddActivity.class);
                intent.putExtra(AddActivity.FRAG_TYPE,AddActivity.REGULAR_TRIP_FRAG);
                startActivity(intent);
                break;
            case R.id.addTripOrder:
                Intent intent2 = new Intent(getActivity(),AddActivity.class);
                intent2.putExtra(AddActivity.FRAG_TYPE,AddActivity.ORDER_TRIP_FRAG);
                startActivity(intent2);
                break;
            case R.id.btnSTrip:
                String searchText = etSTrip.getText().toString();
                searchedKeys.clear();
                searchedTripModels.clear();
                if (!etSTrip.getText().toString().equals("")) {
                    isDoneTripName = false;
                    isDoenStartDateTrip = false;
                    isDoneEndDateTrip = false;
                    isDoneCarNo = false;
                    isDoneConductorName1 = false;
                    isDoneConductorName2 = false;
                    isDoneDriverName = false;
                    isDoneGuideName = false;
                    adapterForSearch = new TripSearchAdapter();
                    rcTrip.setAdapter(adapterForSearch);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.TRIP_NAME).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForTripName);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.STR_START_DATE).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForTripStartDate);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.STR_END_DATE).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForTripEndDate);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.CAR_NO).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForCarNo);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.DRIVER_NAME).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForDriverName);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.CONDUCTOR_NAME_1).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForConductorName1);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.CONDUCTOR_NAME_2).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForConductorName2);
                    databaseReference.orderByChild(TunTravel.Trip.Keys.GUIDE_NAME).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForGuideName);
                }
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
        if (valueEventListener2 != null) {
            databaseReference.removeEventListener(valueEventListener2);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (valueEventListener3 != null) {
            databaseReference1.removeEventListener(valueEventListener3);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals("")) {
            adapterForList = new TripRcAdapter();
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
            rcTrip.setAdapter(adapterForList);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            searchForCarNo = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneCarNo = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForTripName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneTripName = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForTripStartDate = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoenStartDateTrip = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForTripEndDate = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() != 0){
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneEndDateTrip = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForConductorName1 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneConductorName1 = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForConductorName2 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneConductorName2 = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForDriverName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneDriverName = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForGuideName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedTripModels.add(dataSnapshot1.getValue(TripModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneGuideName = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
    }

    private void setNotifyDataChange() {
        if (isDoneTripName && isDoenStartDateTrip && isDoneEndDateTrip && isDoneCarNo && isDoneConductorName1 && isDoneConductorName2 && isDoneDriverName && isDoneGuideName) {
            if (searchedTripModels.size() > 1) {
                Log.d("RemoveKey", "Enter SearchCar grethar than 1");
                String tempKey = searchedKeys.get(0);
                loopForRemove(tempKey, 1);
            }
            for (int i = 0; i < searchedKeys.size(); i++) {
                Log.d("SearchedKeys", searchedKeys.get(i) + "");
            }
            adapterForSearch.notifyDataSetChanged();
        }
    }

    private void loopForRemove(String tempKey, int currentIndex) {
        for (int i = currentIndex; i < searchedKeys.size(); ++i) {
            if (tempKey.equals(searchedKeys.get(i))) {
                Log.d("SearchKey", "TempKey = " + tempKey + "  RemoveKey = " + searchedKeys.get(i));
                searchedTripModels.remove(i);
                searchedKeys.remove(i);
                if (currentIndex != 0) {
                    currentIndex -= 1;
                }
            }
        }

        if (currentIndex < searchedKeys.size()) {
            loopForRemove(searchedKeys.get(currentIndex), currentIndex + 1);
        }
    }

    private class TripRcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        public TripRcAdapter() {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) rcTrip.getLayoutManager();
            rcTrip.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && layoutManager instanceof WrapLinearLayoutManager) {
                        if (onLoadMoreListener != null) {
                            if (currentItemCount < totalServerItemsCount) {
                                onLoadMoreListener.onLoadMore();
                            }
                        }
                        loading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.onLoadMoreListener = mOnLoadMoreListener;
        }

        public void setLoaded() {
            loading = false;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_LOADING) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.on_scroll_loading_rc, parent, false);
                return new MyHolderLoading(view);
            } else if (viewType == VIEW_TYPE_ITEM) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.rc_view_trip, null);
                return new MyHolderItem(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MyHolderItem) {
                TripModel tripModel = tripModels.get(position);
                ((MyHolderItem) holder).tvTripName.setText(tripModel.getTripName());
                ((MyHolderItem) holder).tvTripDate.setText(tripModel.getStrStartDate()+" -- "+tripModel.getStrEndDate());
                ((MyHolderItem) holder).clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShowActivity.class);
                        intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.TRIP_SHOW_FRAG);
                        intent.putExtra(ShowActivity.KEY, keys.get(position));
                        intent.putExtra(ShowActivity.ITEMS_COUNT, totalServerItemsCount);
                        startActivity(intent);
                        databaseReference.child(keys.get(position)).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                Toast.makeText(getActivity(), "ChildChanged", Toast.LENGTH_SHORT).show();
                                databaseReference1.addValueEventListener(valueEventListener3);
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getActivity(), "Click = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (holder instanceof MyHolderLoading) {
                MyHolderLoading loading = (MyHolderLoading) holder;
                loading.progressBar.setIndeterminate(true);
            }


        }

        @Override
        public int getItemCount() {
            return tripModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return tripModels.get(position) == null ? 1 : 0;
        }
    }

    private class MyHolderItem extends RecyclerView.ViewHolder {
        TextView tvTripName, tvTripDate;
        FrameLayout clickLayout;

        public MyHolderItem(View itemView) {
            super(itemView);
            tvTripName = (TextView) itemView.findViewById(R.id.tvTripName);
            tvTripDate = (TextView) itemView.findViewById(R.id.tvTripTime);
            clickLayout = (FrameLayout) itemView.findViewById(R.id.clickLayout);
        }
    }

    private class MyHolderLoading extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public MyHolderLoading(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    class WrapLinearLayoutManager extends LinearLayoutManager {

        public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

    private class TripSearchAdapter extends RecyclerView.Adapter<MyHolderItem> {

        @Override
        public MyHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rc_view_trip, null);
            return new MyHolderItem(view);
        }

        @Override
        public void onBindViewHolder(MyHolderItem holder, final int position) {
            TripModel tripModel = searchedTripModels.get(position);
            holder.tvTripName.setText(tripModel.getTripName());
            holder.tvTripDate.setText(tripModel.getStrStartDate()+" -- "+tripModel.getStrEndDate());
            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.TRIP_SHOW_FRAG);
                    intent.putExtra(ShowActivity.KEY, searchedKeys.get(position));
                    intent.putExtra(ShowActivity.ITEMS_COUNT, totalServerItemsCount);
                    startActivity(intent);
                    databaseReference.child(searchedKeys.get(position)).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Toast.makeText(getActivity(), "ChildChanged", Toast.LENGTH_SHORT).show();
                            databaseReference1.addValueEventListener(valueEventListener3);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(getActivity(), "Click = " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchedTripModels.size();
        }
    }

    private class ScrollOnLoadMoreListener implements OnLoadMoreListener {
        @Override
        public void onLoadMore() {
            tripModels.add(null);
            adapterForList.notifyItemInserted(tripModels.size() - 1);
            if (totalServerItemsCount >= currentItemCount + 10) {
                currentItemCount += 10;
                databaseReference.orderByKey().endAt(keys.get(keys.size() - 1)).limitToLast(10 + 1).addValueEventListener(valueEventListener2);
            } else {
                int temp = (int) (totalServerItemsCount - currentItemCount);
                String key = keys.get(keys.size() - 1);
                databaseReference.orderByKey().endAt(key).limitToLast(temp + 1).addValueEventListener(valueEventListener2);
                currentItemCount += temp;

            }
        }
    }
}
