package and.htetarkarzaw.tuntravel.Add_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import and.htetarkarzaw.tuntravel.Model.PassengerModel;
import and.htetarkarzaw.tuntravel.R;

/**
 * Created by Htet Arkar Zaw on 7/6/2017.
 */

public class PassengerInfo extends Fragment implements View.OnClickListener {
    private int position;
    private EditText passengerName,passengerNRC,passengerPhno,passengerRemark;
    Button passengerSave,passengerCancel;
    private PassengerModel passengerModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.passenger_info, container, false);
        passengerName= (EditText) v.findViewById(R.id.passengerName);
        passengerNRC= (EditText) v.findViewById(R.id.passengerNRC);
        passengerPhno= (EditText) v.findViewById(R.id.passengerPhno);
        passengerRemark= (EditText) v.findViewById(R.id.passengerRemark);
        passengerSave= (Button) v.findViewById(R.id.passengerSave);
        passengerCancel= (Button) v.findViewById(R.id.passengerCancel);
        return v;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity().getIntent().hasExtra(AddActivity.PASSENGER_MODEL)) {
            passengerModel = getActivity().getIntent().getParcelableExtra(AddActivity.PASSENGER_MODEL);
            passengerName.setText(passengerModel.getPassengerName());
            passengerNRC.setText(passengerModel.getPassengerNRC());
            passengerPhno.setText(passengerModel.getPassengerPhno());
            passengerRemark.setText(passengerModel.getRemark());
        }
        passengerSave.setOnClickListener(this);
        passengerCancel.setOnClickListener(this);

    }

    public void setPosition(int position){
        this.position=position;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.passengerSave:
                if(checkValuesNotNull()) {
                    passengerModel = new PassengerModel(passengerName.getText().toString(), passengerNRC.getText().toString(), passengerPhno.getText().toString(), position + "", passengerRemark.getText().toString());
                    Intent i = new Intent();
                    i.putExtra("PassengerModel", passengerModel);
                    getActivity().setResult(123, i);
                    getActivity().finish();
                }
                break;

            case R.id.passengerCancel:

                break;
        }
    }

    private boolean checkValuesNotNull() {
        boolean notNull = true;
        if(passengerName.getText().toString().equals("")){
            notNull = false;
            passengerName.setError("Required");
        }
        if(passengerNRC.getText().toString().equals("")){
            notNull = false;
            passengerNRC.setError("Required");
        }
        return notNull;
    }
}
