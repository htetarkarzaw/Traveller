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

import java.util.ArrayList;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Model.ConductorModel;
import and.htetarkarzaw.tuntravel.OnLoadMoreListener;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.ShowActivity;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 5/31/2017.
 */

public class Conductor extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    Button addConductor;
    ImageButton searchConductor;
    RecyclerView rcConductor;
    EditText etSearchConductor;
    private DatabaseReference databaseReference, databaseReference1;
    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3;
    private ValueEventListener searchForConductorName, searchForConductorPhNum;
    private WrapLinearLayoutManager manager;
    private ConductorRcAdapter adapterForList;
    private ConductorSearchAdapter adapterForSearch;
    private long totalServerItemsCount = 0;
    private long currentItemCount = 0;
    private ArrayList<ConductorModel> conductorModels, reverseModels;
    private ArrayList<String> keys, reverseKeys;
    private ArrayList<ConductorModel> searchedConductorModels = new ArrayList<>();
    private ArrayList<String> searchedKeys = new ArrayList<>();
    private boolean isDoneConductorName, isDoneConductorPhNum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conductor, container, false);
        addConductor = (Button) v.findViewById(R.id.addConductor);
        rcConductor = (RecyclerView) v.findViewById(R.id.rcConductor);
        searchConductor = (ImageButton) v.findViewById(R.id.btnSConductor);
        etSearchConductor = (EditText) v.findViewById(R.id.etSConductor);
        keys = new ArrayList<>();
        reverseKeys = new ArrayList<>();
        conductorModels = new ArrayList<>();
        reverseModels = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTORS);
        manager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcConductor.setLayoutManager(manager);
        addConductor.setOnClickListener(this);
        searchConductor.setOnClickListener(this);
        etSearchConductor.setOnFocusChangeListener(this);
        etSearchConductor.addTextChangedListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (rcConductor.getAdapter() != null) {
                    rcConductor.setAdapter(null);
                }
                getItemsCountFromServer(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_COUNTS);
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
        if (!etSearchConductor.getText().toString().equals("")) {
            etSearchConductor.setText("");
        }
        if(rcConductor.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
        }
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(ConductorModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                conductorModels.clear();
                keys.clear();
                Log.d("Resume", "Enter Download FirstData");
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i != -1; i--) {
                    conductorModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                if(rcConductor.getAdapter()==null){
                    adapterForList = new ConductorRcAdapter();
                }
                adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
                rcConductor.setAdapter(adapterForList);
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
                adapterForList.notifyItemRemoved(conductorModels.size() - 1);
                conductorModels.remove(conductorModels.size() - 1);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(ConductorModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                conductorModels.remove(conductorModels.size() - 1);
                keys.remove(keys.size() - 1);
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i >= 0; i--) {
                    conductorModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                for (int j = 0; j < keys.size(); j++) {
                    Log.d("Keys", "key = " + keys.get(j));
                }
                Log.d("children", dataSnapshot.getChildrenCount() + "");
                reverseModels.clear();
                reverseKeys.clear();
                Toast.makeText(getContext(), "onLoad = " + conductorModels.size(), Toast.LENGTH_SHORT).show();
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
        if(rcConductor.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addConductor:
                Intent i=new Intent(this.getContext(),AddActivity.class);
                i.putExtra(AddActivity.FRAG_TYPE,AddActivity.CONDUCTOR_FRAG);
                startActivity(i);
                break;
            case R.id.btnSConductor:
                String searchText = etSearchConductor.getText().toString();
                searchedKeys.clear();
                searchedConductorModels.clear();
                if (!searchText.equals("")) {
                    isDoneConductorName = false;
                    isDoneConductorPhNum = false;
                    adapterForSearch = new ConductorSearchAdapter();
                    rcConductor.setAdapter(adapterForSearch);
                    databaseReference.orderByChild(TunTravel.Conductor.Keys.CONDUCTOR_NAME).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForConductorName);
                    databaseReference.orderByChild(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForConductorPhNum);
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
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            searchForConductorName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedConductorModels.add(dataSnapshot1.getValue(ConductorModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneConductorName = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            searchForConductorPhNum = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedConductorModels.add(dataSnapshot1.getValue(ConductorModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneConductorPhNum = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
    }

    private void setNotifyDataChange() {
        if (isDoneConductorName && isDoneConductorPhNum ) {
            if (searchedConductorModels.size() > 1) {
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
                searchedConductorModels.remove(i);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals("")) {
            adapterForList = new ConductorRcAdapter();
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
            rcConductor.setAdapter(adapterForList);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class WrapLinearLayoutManager extends LinearLayoutManager{
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

    private class ConductorRcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        public ConductorRcAdapter() {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) rcConductor.getLayoutManager();
            rcConductor.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                View view = inflater.inflate(R.layout.rc_view_conductor, null);
                return new MyHolderItem(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MyHolderItem) {
                ConductorModel model = conductorModels.get(position);
                ((MyHolderItem) holder).tvConductorPhNum.setText(model.getConductorPhNo());
                ((MyHolderItem) holder).tvConductorName.setText(model.getConductorName());
                ((MyHolderItem) holder).tvConductorNRC.setText(model.getConductorNRC());
                ((MyHolderItem) holder).clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShowActivity.class);
                        intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.CONDUCTOR_SHOW_FRAG);
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
            return conductorModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return conductorModels.get(position) == null ? 1 : 0;
        }
    }

    private class ConductorSearchAdapter extends RecyclerView.Adapter<MyHolderItem>{
        @Override
        public MyHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rc_view_conductor, null);
            return new MyHolderItem(view);
        }

        @Override
        public void onBindViewHolder(MyHolderItem holder, final int position) {
            ConductorModel conductorModel = searchedConductorModels.get(position);
            holder.tvConductorName.setText(conductorModel.getConductorName());
            holder.tvConductorPhNum.setText(conductorModel.getConductorPhNo());
            holder.tvConductorNRC.setText(conductorModel.getConductorNRC());
            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.CONDUCTOR_SHOW_FRAG);
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
            return searchedConductorModels.size();
        }
    }

    private class MyHolderItem extends RecyclerView.ViewHolder {
        TextView tvConductorName, tvConductorPhNum, tvConductorNRC;
        FrameLayout clickLayout;

        public MyHolderItem(View itemView) {
            super(itemView);
            tvConductorName = (TextView) itemView.findViewById(R.id.tvConductorName);
            tvConductorPhNum = (TextView) itemView.findViewById(R.id.tvConductorPhNum);
            tvConductorNRC = (TextView) itemView.findViewById(R.id.tvConductorNRC);
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

    private class ScrollOnLoadMoreListener implements OnLoadMoreListener {
        @Override
        public void onLoadMore() {
            conductorModels.add(null);
            adapterForList.notifyItemInserted(conductorModels.size() - 1);
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
