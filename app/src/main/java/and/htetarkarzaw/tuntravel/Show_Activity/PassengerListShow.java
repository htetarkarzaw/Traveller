package and.htetarkarzaw.tuntravel.Show_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import and.htetarkarzaw.tuntravel.Model.PassengerModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Eiron on 7/8/17.
 */

public class PassengerListShow extends Fragment {

    RecyclerView rcPassengerList;
    private String key;
    private ArrayList<PassengerModel> passengerModels = new ArrayList<>();
    private PassengerListAdapter passengerListAdapter;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passenger_list_show,container,false);
        rcPassengerList = (RecyclerView) view.findViewById(R.id.rcPassengerList);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        passengerListAdapter = new PassengerListAdapter();
        Toast.makeText(getActivity(), "Downloading Data", Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Passenger.PASSENGERS).child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), "downloaded Data", Toast.LENGTH_SHORT).show();
                if(dataSnapshot!=null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        passengerModels.add(dataSnapshot1.getValue(PassengerModel.class));
                        Toast.makeText(getActivity(), dataSnapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                    rcPassengerList.setLayoutManager(manager);
                    rcPassengerList.setAdapter(passengerListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setKey(String key) {
        this.key = key;
    }

    private class PassengerListAdapter extends RecyclerView.Adapter<MyListItem>{

        @Override
        public MyListItem onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rc_view_passenger,null);
            return new MyListItem(view);
        }

        @Override
        public void onBindViewHolder(MyListItem holder, int position) {
            final PassengerModel model = passengerModels.get(position);
            holder.tvName.setText(model.getPassengerName());
            holder.tvNRC.setText(model.getPassengerNRC());
            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ShowActivity.class);
                    intent.putExtra(ShowActivity.FRAG_TYPE,ShowActivity.PASSENGER_SHOW_FRAG);
                    intent.putExtra(ShowActivity.KEY,key);
                    intent.putExtra(ShowActivity.PASSENGER_KEY,model.getSeatNo());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return passengerModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class MyListItem extends RecyclerView.ViewHolder{
        TextView tvName,tvNRC;
        FrameLayout clickLayout;

        public MyListItem(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvPassengerName);
            tvNRC = (TextView) itemView.findViewById(R.id.tvPassengerNRC);
            clickLayout = (FrameLayout) itemView.findViewById(R.id.clickLayout);
        }
    }
}
