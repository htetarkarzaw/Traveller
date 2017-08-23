package and.htetarkarzaw.tuntravel.Show_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Custom_fonts.MyButton;
import and.htetarkarzaw.tuntravel.Model.TripModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Eiron on 7/7/17.
 */

public class TripShow extends Fragment implements View.OnClickListener {
    public static final String KEY = "key";
    private static final String ITEMS_COUNT = "items_count";
    private String key;
    private long itemsCount = 0;
    TextView tvTripType,tvPassengerCounts,tvTripName,tvClientName,tvClientPhNum,tvClientComapny, tvStartDate, tvEndDate, tvRenderCharge,
            tvRoadFee,tvCarNo,tvConductorName1,tvConductorName2,tvGuideName,tvDriverName, tvCostForFood, tvGeneralExpense, tvDebit, tvProfit;
    String strCarKey,strConductorName1Key,strConductorName2Key,strDriverKey,strGuideKey;
    private MyButton btnTripShowEdit, btnTripShowDelete, btnTripShowCancel , btnShowPassengers;
    LinearLayout tripLoadingLayout,layoutForOrderTrip,layoutForRegularTrip;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference, databaseReference2,databaseReferenceForPassenger;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trip_show,container,false);
        tvTripType = (TextView) view.findViewById(R.id.tvTripType);
        tvPassengerCounts = (TextView) view.findViewById(R.id.tvPassengerCounts);
        tvTripName = (TextView) view.findViewById(R.id.tvTripName);
        tvClientName = (TextView) view.findViewById(R.id.tvClientName);
        tvClientPhNum = (TextView) view.findViewById(R.id.tvClientPhNum);
        tvClientComapny = (TextView) view.findViewById(R.id.tvClientCompany);
        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        tvRenderCharge = (TextView) view.findViewById(R.id.tvRenderCharge);
        tvRoadFee = (TextView) view.findViewById(R.id.tvRoadFee);
        tvCarNo = (TextView) view.findViewById(R.id.tvCarNo);
        tvConductorName1 = (TextView) view.findViewById(R.id.tvConductorName1);
        tvConductorName2 = (TextView) view.findViewById(R.id.tvConductorName2);
        tvGuideName = (TextView) view.findViewById(R.id.tvGuideName);
        tvDriverName = (TextView) view.findViewById(R.id.tvDriverName);
        tvCostForFood = (TextView) view.findViewById(R.id.tvCostForFood);
        tvGeneralExpense = (TextView) view.findViewById(R.id.tvGeneralExpense);
        tvDebit = (TextView) view.findViewById(R.id.tvDebit);
        tvProfit = (TextView) view.findViewById(R.id.tvProfit);
        btnTripShowEdit = (MyButton) view.findViewById(R.id.btnTripShowEdit);
        btnTripShowDelete = (MyButton) view.findViewById(R.id.btnTripShowDelete);
        btnTripShowCancel = (MyButton) view.findViewById(R.id.btnTripShowCancel);
        btnShowPassengers = (MyButton) view.findViewById(R.id.btnShowPassengers);
        tripLoadingLayout = (LinearLayout) view.findViewById(R.id.tripLoadingLayout);
        layoutForRegularTrip = (LinearLayout) view.findViewById(R.id.layoutForRegularTrip);
        layoutForOrderTrip = (LinearLayout) view.findViewById(R.id.layoutForOrderTrip);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Trip.Keys.TRIP_TYPE,tvTripType.getText().toString());
        outState.putString(TunTravel.Trip.Keys.TRIP_NAME,tvTripName.getText().toString());
        outState.putString(TunTravel.Trip.Keys.START_DATE,tvStartDate.getText().toString());
        outState.putString(TunTravel.Trip.Keys.END_DATE,tvEndDate.getText().toString());
        outState.putString(TunTravel.Trip.Keys.RENDER_CHARGE,tvRenderCharge.getText().toString());
        outState.putString(TunTravel.Trip.Keys.ROAD_FEE,tvRoadFee.getText().toString());
        outState.putString(TunTravel.Trip.Keys.COST_FOR_FOOD,tvCostForFood.getText().toString());
        outState.putString(TunTravel.Trip.Keys.GENERAL_EXPENSE,tvGeneralExpense.getText().toString());
        outState.putString(TunTravel.Trip.Keys.DEBIT,tvDebit.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CAR_NO,tvCarNo.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CONDUCTOR_NAME_1,tvConductorName1.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CONDUCTOR_NAME_2,tvConductorName2.getText().toString());
        outState.putString(TunTravel.Trip.Keys.DRIVER_NAME,tvDriverName.getText().toString());
        outState.putString(TunTravel.Trip.Keys.GUIDE_NAME,tvGuideName.getText().toString());
        outState.putString(TunTravel.Trip.Keys.PROFIT,tvProfit.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CLIENT_NAME,tvClientName.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CLIENT_PH_NUM,tvClientPhNum.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CLIENT_COMPANY,tvClientComapny.getText().toString());
        outState.putString(TunTravel.Trip.Keys.PASSENGERS_COUNT,tvPassengerCounts.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CAR_KEY,strCarKey);
        outState.putString(TunTravel.Trip.Keys.CONDUCTOR_KEY_1,strConductorName1Key);
        outState.putString(TunTravel.Trip.Keys.CONDUCTOR_KEY_2,strConductorName2Key);
        outState.putString(TunTravel.Trip.Keys.DRIVER_KEY,strDriverKey);
        outState.putString(TunTravel.Trip.Keys.GUIDE_KEY,strGuideKey);
        outState.putString(KEY, key);
        outState.putLong(ITEMS_COUNT, itemsCount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            tvTripType.setText(savedInstanceState.getString(TunTravel.Trip.Keys.TRIP_TYPE));
            tvTripName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.TRIP_NAME));
            tvStartDate.setText(savedInstanceState.getString(TunTravel.Trip.Keys.START_DATE));
            tvEndDate.setText(savedInstanceState.getString(TunTravel.Trip.Keys.END_DATE));
            tvRenderCharge.setText(savedInstanceState.getString(TunTravel.Trip.Keys.RENDER_CHARGE));
            tvRoadFee.setText(savedInstanceState.getString(TunTravel.Trip.Keys.ROAD_FEE));
            tvCostForFood.setText(savedInstanceState.getString(TunTravel.Trip.Keys.COST_FOR_FOOD));
            tvGeneralExpense.setText(savedInstanceState.getString(TunTravel.Trip.Keys.GENERAL_EXPENSE));
            tvDebit.setText(savedInstanceState.getString(TunTravel.Trip.Keys.DEBIT));
            tvProfit.setText(savedInstanceState.getString(TunTravel.Trip.Keys.PROFIT));
            tvCarNo.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CAR_NO));
            tvConductorName1.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CONDUCTOR_NAME_1));
            tvConductorName2.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CONDUCTOR_NAME_2));
            tvClientName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_NAME));
            tvClientPhNum.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_PH_NUM));
            tvClientComapny.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_COMPANY));
            tvDriverName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.DRIVER_NAME));
            tvGuideName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.GUIDE_NAME));
            tvPassengerCounts.setText(savedInstanceState.getString(TunTravel.Trip.Keys.PASSENGERS_COUNT));
            strCarKey = savedInstanceState.getString(TunTravel.Trip.Keys.CAR_KEY);
            strConductorName1Key = savedInstanceState.getString(TunTravel.Trip.Keys.CONDUCTOR_KEY_1);
            strConductorName2Key = savedInstanceState.getString(TunTravel.Trip.Keys.CONDUCTOR_KEY_2);
            strDriverKey = savedInstanceState.getString(TunTravel.Trip.Keys.DRIVER_KEY);
            strGuideKey = savedInstanceState.getString(TunTravel.Trip.Keys.GUIDE_KEY);
            this.key = savedInstanceState.getString(KEY);
            this.itemsCount = savedInstanceState.getLong(ITEMS_COUNT);
        }else{
            tripLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTripShowDelete.setEnabled(false);
        btnTripShowEdit.setOnClickListener(this);
        btnTripShowDelete.setOnClickListener(this);
        btnTripShowCancel.setOnClickListener(this);
        btnShowPassengers.setOnClickListener(this);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm");
                    TripModel tripModel = dataSnapshot.getValue(TripModel.class);
                    tvTripType.setText(tripModel.getTripType());
                    tvTripName.setText(tripModel.getTripName());
                    Date startDate = new Date(Long.parseLong(tripModel.getStartDate()));
                    tvStartDate.setText(dateFormat.format(startDate)+" "+(startDate.getHours()>12?"PM":"AM"));
                    Date endDate = new Date(Long.parseLong(tripModel.getEndDate()));
                    tvEndDate.setText(dateFormat.format(endDate)+" "+(endDate.getHours()>12?"PM":"AM"));
                    tvCarNo.setText(tripModel.getCarNo());
                    tvConductorName1.setText(tripModel.getConductorName1());
                    tvConductorName2.setText(tripModel.getConductorName2());
                    tvGuideName.setText(tripModel.getGuideName());
                    tvDriverName.setText(tripModel.getDriverName());
                    tvClientName.setText(tripModel.getClientName());
                    tvClientPhNum.setText(tripModel.getClientPhNo());
                    tvClientComapny.setText(tripModel.getClientCompany());
                    tvRenderCharge.setText(tripModel.getRenderCharge());
                    tvRoadFee.setText(tripModel.getRoadFee());
                    tvCostForFood.setText(tripModel.getCostForFood());
                    tvGeneralExpense.setText(tripModel.getGeneralExpense());
                    tvDebit.setText(tripModel.getDebit());
                    tvProfit.setText(tripModel.getProfit());
                    if(tripModel.getTripType().equals(TunTravel.Trip.ORDER_TRIP_TYPE)){
                        layoutForOrderTrip.setVisibility(View.VISIBLE);
                        layoutForRegularTrip.setVisibility(View.GONE);
                    }
                    btnTripShowDelete.setEnabled(true);
                    tripLoadingLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIPS);
        databaseReferenceForPassenger = FirebaseDatabase.getInstance().getReference(TunTravel.Passenger.PASSENGERS).child(key);
        if (savedInstanceState == null) {
            databaseReference.child(key).addValueEventListener(valueEventListener);
            databaseReferenceForPassenger.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null) {
                        tvPassengerCounts.setText(dataSnapshot.getChildrenCount()+"");
                    }else{
                        tvPassengerCounts.setText(0+"");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void setKey(String key, long itemsCount) {
        this.key = key;
        this.itemsCount = itemsCount;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTripShowEdit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                switch (tvTripType.getText().toString()){
                    case TunTravel.Trip.REG_TRIP_TYPE:
                        intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.REGULAR_TRIP_FRAG);
                        intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.REGULAR_TRIP_EDIT);
                        break;
                    case TunTravel.Trip.ORDER_TRIP_TYPE:
                        intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.ORDER_TRIP_FRAG);
                        intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.ORDER_TRIP_EDIT);
                        break;
                }
                intent.putExtra(AddActivity.KEY, key);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnTripShowDelete:
                if (valueEventListener != null) {
                    databaseReference.child(key).removeEventListener(valueEventListener);
                }
                databaseReference.child(key).removeValue();
                databaseReference2 = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIP_COUNTS);
                databaseReference2.setValue(itemsCount - 1);
                databaseReferenceForPassenger.child(key).removeValue();
                getActivity().finish();
                break;

            case R.id.btnTripShowCancel:
                getActivity().finish();
                break;
            case R.id.btnShowPassengers:
                Intent intent1 = new Intent(getActivity(),ShowActivity.class);
                intent1.putExtra(ShowActivity.KEY,key);
                intent1.putExtra(ShowActivity.FRAG_TYPE,ShowActivity.PASSENGER_LIST_SHOW_FRAG);
                startActivity(intent1);
                break;
        }
    }
}
