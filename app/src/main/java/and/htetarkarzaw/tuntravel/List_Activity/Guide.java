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
import and.htetarkarzaw.tuntravel.Model.GuideModel;
import and.htetarkarzaw.tuntravel.OnLoadMoreListener;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.ShowActivity;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 5/31/2017.
 */

public class Guide extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    Button addGuide;
    ImageButton searchGuide;
    RecyclerView rcGuide;
    EditText etSearchGuide;
    private DatabaseReference databaseReference, databaseReference1;
    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3;
    private ValueEventListener searchForGuideName, searchForGuidePhNum;
    private WrapLinearLayoutManager manager;
    private GuideRcAdapter adapterForList;
    private GuideSearchAdapter adapterForSearch;
    private long totalServerItemsCount = 0;
    private long currentItemCount = 0;
    private ArrayList<GuideModel> guideModels, reverseModels;
    private ArrayList<String> keys, reverseKeys;
    private ArrayList<GuideModel> searchedGuideModels = new ArrayList<>();
    private ArrayList<String> searchedKeys = new ArrayList<>();
    private boolean isDoneGuideName, isDoneGuidePhNum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.guide, container, false);
        addGuide = (Button) v.findViewById(R.id.addGuide);
        rcGuide = (RecyclerView) v.findViewById(R.id.rcGuide);
        searchGuide = (ImageButton) v.findViewById(R.id.btnSGuide);
        etSearchGuide = (EditText) v.findViewById(R.id.etSGuide);
        keys = new ArrayList<>();
        reverseKeys = new ArrayList<>();
        guideModels = new ArrayList<>();
        reverseModels = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDES);
        manager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcGuide.setLayoutManager(manager);
        addGuide.setOnClickListener(this);
        searchGuide.setOnClickListener(this);
        etSearchGuide.setOnFocusChangeListener(this);
        etSearchGuide.addTextChangedListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (rcGuide.getAdapter() != null) {
                    rcGuide.setAdapter(null);
                }
                getItemsCountFromServer(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDE_COUNTS);
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
        if (!etSearchGuide.getText().toString().equals("")) {
            etSearchGuide.setText("");
        }
        if(rcGuide.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
        }
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(GuideModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                guideModels.clear();
                keys.clear();
                Log.d("Resume", "Enter Download FirstData");
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i != -1; i--) {
                    guideModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                if(rcGuide.getAdapter()==null){
                    adapterForList = new GuideRcAdapter();
                }
                adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
                rcGuide.setAdapter(adapterForList);
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
                adapterForList.notifyItemRemoved(guideModels.size() - 1);
                guideModels.remove(guideModels.size() - 1);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    reverseModels.add(dataSnapshot1.getValue(GuideModel.class));
                    reverseKeys.add(dataSnapshot1.getKey());
                }
                guideModels.remove(guideModels.size() - 1);
                keys.remove(keys.size() - 1);
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (; i >= 0; i--) {
                    guideModels.add(reverseModels.get(i));
                    keys.add(reverseKeys.get(i));
                }
                for (int j = 0; j < keys.size(); j++) {
                    Log.d("Keys", "key = " + keys.get(j));
                }
                Log.d("children", dataSnapshot.getChildrenCount() + "");
                reverseModels.clear();
                reverseKeys.clear();
                Toast.makeText(getContext(), "onLoad = " + guideModels.size(), Toast.LENGTH_SHORT).show();
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
        if(rcGuide.getAdapter()!=null){
            adapterForList.setOnLoadMoreListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addGuide:
                Intent i=new Intent(this.getContext(),AddActivity.class);
                i.putExtra(AddActivity.FRAG_TYPE,AddActivity.GUIDE_FRAG);
                startActivity(i);
                break;

            case R.id.btnSGuide:
                String searchText = etSearchGuide.getText().toString();
                searchedKeys.clear();
                searchedGuideModels.clear();
                if (!searchText.equals("")) {
                    isDoneGuideName = false;
                    isDoneGuidePhNum = false;
                    adapterForSearch = new GuideSearchAdapter();
                    rcGuide.setAdapter(adapterForSearch);
                    databaseReference.orderByChild(TunTravel.Guides.Keys.GUIDE_NAME).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForGuideName);
                    databaseReference.orderByChild(TunTravel.Guides.Keys.GUIDE_PH_NO).startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(searchForGuidePhNum);
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
            searchForGuideName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedGuideModels.add(dataSnapshot1.getValue(GuideModel.class));
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
            searchForGuidePhNum = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            searchedGuideModels.add(dataSnapshot1.getValue(GuideModel.class));
                            searchedKeys.add(dataSnapshot1.getKey());
                        }
                    }
                    isDoneGuidePhNum = true;
                    setNotifyDataChange();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
    }

    private void setNotifyDataChange() {
        if (isDoneGuideName && isDoneGuidePhNum ) {
            if (searchedGuideModels.size() > 1) {
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
                searchedGuideModels.remove(i);
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
            adapterForList = new GuideRcAdapter();
            adapterForList.setOnLoadMoreListener(new ScrollOnLoadMoreListener());
            rcGuide.setAdapter(adapterForList);
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

    private class GuideRcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        public GuideRcAdapter() {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) rcGuide.getLayoutManager();
            rcGuide.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                View view = inflater.inflate(R.layout.rc_view_guide, null);
                return new MyHolderItem(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MyHolderItem) {
                GuideModel model = guideModels.get(position);
                ((MyHolderItem) holder).tvGuidePhNum.setText(model.getGuidePhNo());
                ((MyHolderItem) holder).tvGuideName.setText(model.getGuideName());
                ((MyHolderItem) holder).tvGuideNRC.setText(model.getGuideNRC());
                ((MyHolderItem) holder).clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShowActivity.class);
                        intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.GUIDE_SHOW_FRAG);
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
            return guideModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return guideModels.get(position) == null ? 1 : 0;
        }
    }


    private class GuideSearchAdapter extends RecyclerView.Adapter<MyHolderItem> {
        @Override
        public MyHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rc_view_guide, null);
            return new MyHolderItem(view);
        }

        @Override
        public void onBindViewHolder(MyHolderItem holder, final int position) {
            GuideModel guideMo = searchedGuideModels.get(position);
            holder.tvGuideName.setText(guideMo.getGuideName());
            holder.tvGuidePhNum.setText(guideMo.getGuidePhNo());
            holder.tvGuideNRC.setText(guideMo.getGuideNRC());
            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra(ShowActivity.FRAG_TYPE, ShowActivity.GUIDE_SHOW_FRAG);
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
            return searchedGuideModels.size();
        }
    }

    private class MyHolderItem extends RecyclerView.ViewHolder {
        TextView tvGuideName, tvGuidePhNum, tvGuideNRC;
        FrameLayout clickLayout;

        public MyHolderItem(View itemView) {
            super(itemView);
            tvGuideName = (TextView) itemView.findViewById(R.id.tvGuideName);
            tvGuidePhNum = (TextView) itemView.findViewById(R.id.tvGuidePhoneNo);
            tvGuideNRC = (TextView) itemView.findViewById(R.id.tvGuideNRC);
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
            guideModels.add(null);
            adapterForList.notifyItemInserted(guideModels.size() - 1);
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
