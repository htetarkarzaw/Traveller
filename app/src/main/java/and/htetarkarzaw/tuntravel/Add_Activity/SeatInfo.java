package and.htetarkarzaw.tuntravel.Add_Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Add_Activity.RegularTripInfo;
import and.htetarkarzaw.tuntravel.Model.PassengerModel;
import and.htetarkarzaw.tuntravel.R;


/**
 * Created by Htet Arkar Zaw on 7/6/2017.
 */

public class SeatInfo extends Fragment {
    RecyclerView rcView;
    SeatAdapter seatAdapter;
    ArrayList<String> seatNo = new ArrayList();
    private static final int PASSENGER_RESULT = 123;
    ArrayList<PassengerModel> passengerList = new ArrayList<>();
    PassengerModel passengerModel;
    private AddActivity context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seat_info, container, false);
        rcView = (RecyclerView) v.findViewById(R.id.rcSeat);

        for (int i = 1; i < 51; i++) {
            seatNo.add("Seat " + i);
        }

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seatAdapter = new SeatAdapter(seatNo, passengerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rcView.setLayoutManager(layoutManager);
        rcView.setAdapter(seatAdapter);


    }

    @Override
    public void onAttach(Context context) {
        if(getActivity().getIntent().getParcelableArrayListExtra(AddActivity.PASSENGER_LIST)!=null) {
            passengerList = getActivity().getIntent().getParcelableArrayListExtra(AddActivity.PASSENGER_LIST);
            Toast.makeText(getActivity(), "Trip:"+passengerList.size(), Toast.LENGTH_SHORT).show();        }
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        seatAdapter = new SeatAdapter(seatNo, passengerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rcView.setLayoutManager(layoutManager);
        rcView.setAdapter(seatAdapter);

    }


    @Override
    public void onPause() {
        Toast.makeText(getActivity(), "OnDestroy:"+passengerList.size(), Toast.LENGTH_SHORT).show();
        Intent i=new Intent();
        i.putExtra("PassengerList",passengerList);
        getActivity().setResult(RegularTripInfo.SEAT_RESULT,i);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            passengerModel = data.getParcelableExtra("PassengerModel");
            for (int i = 0; i < passengerList.size(); i++) {
                if (passengerList.get(i).getSeatNo().equals(passengerModel.getSeatNo())) {
                    passengerList.remove(i);
                }
            }
            passengerList.add(passengerModel);

        }
    }


    private class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.MyHolder> {
        ArrayList<String> seatNo;
        ArrayList<PassengerModel> pModel;

        public SeatAdapter(ArrayList<String> seatNo, ArrayList<PassengerModel> pModel) {
            this.seatNo = seatNo;
            this.pModel = pModel;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.seat_view, null);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {

            for (int i = 0; i < pModel.size(); i++) {
                if (position == Integer.parseInt(pModel.get(i).getSeatNo())) {
                    holder.Name.setText(pModel.get(i).getPassengerName());
                    holder.passengerModel=pModel.get(i);
                }
            }
            String temp = seatNo.get(position);
            holder.seatNo.setText(temp);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.PASSENGER_ADD);
                    intent.putExtra(AddActivity.SEAT_NO, position);
                    if(holder.passengerModel!=null) {
                        intent.putExtra(AddActivity.PASSENGER_MODEL, holder.passengerModel);
                    }
                    startActivityForResult(intent, PASSENGER_RESULT);

                }
            });

        }

        @Override
        public int getItemCount() {
            return seatNo.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView seatNo, Name;
            PassengerModel passengerModel=null;

            public MyHolder(View itemView) {
                super(itemView);
                seatNo = (TextView) itemView.findViewById(R.id.seatNo);
                Name = (TextView) itemView.findViewById(R.id.name);
            }
        }
    }

    public void getPassengerModel(PassengerModel passengerModel) {
        this.passengerModel = passengerModel;
        if (passengerModel != null) {
            passengerList.add(passengerModel);
        }
    }
}
