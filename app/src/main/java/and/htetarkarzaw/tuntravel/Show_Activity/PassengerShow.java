package and.htetarkarzaw.tuntravel.Show_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import and.htetarkarzaw.tuntravel.Model.PassengerModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Eiron on 7/8/17.
 */

public class PassengerShow extends Fragment implements View.OnClickListener {
    String key,passengerKey;
    TextView tvPassengerName,tvPassengerNRC,tvPassengerPhNo,tvPassengerRemark;
    Button btnPassengerTripShow;
    private DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passenger_show,container,false);
        tvPassengerName = (TextView) view.findViewById(R.id.tvPassengerName);
        tvPassengerNRC = (TextView) view.findViewById(R.id.tvPassengerNRC);
        tvPassengerPhNo = (TextView) view.findViewById(R.id.tvPassengerPhNo);
        tvPassengerRemark = (TextView) view.findViewById(R.id.tvPassengerRemark);
        btnPassengerTripShow = (Button) view.findViewById(R.id.btnPassengerTripShow);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPassengerTripShow.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Passenger.PASSENGERS).child(key).child(passengerKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PassengerModel passengerModel = dataSnapshot.getValue(PassengerModel.class);
                tvPassengerName.setText(passengerModel.getPassengerName());
                tvPassengerNRC.setText(passengerModel.getPassengerNRC());
                tvPassengerPhNo.setText(passengerModel.getPassengerPhno());
                tvPassengerRemark.setText(passengerModel.getRemark());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setKey(String key, String passengerKey) {
        this.key = key;
        this.passengerKey = passengerKey;
    }

    @Override
    public void onClick(View v) {

    }
}
