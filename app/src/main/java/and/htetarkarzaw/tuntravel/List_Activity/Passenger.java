package and.htetarkarzaw.tuntravel.List_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.ShowActivity;

/**
 * Created by Htet Arkar Zaw on 7/7/2017.
 */

public class Passenger extends Fragment implements View.OnClickListener {

    Button searchPassenger;
    ImageButton btnSearchPassenger;
    EditText etSearchPassenger;
    RecyclerView rcPassenger;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.passenger, container, false);
        searchPassenger= (Button) v.findViewById(R.id.searchPassenger);
        btnSearchPassenger= (ImageButton) v.findViewById(R.id.btnSearchPassenger);
        etSearchPassenger= (EditText) v.findViewById(R.id.etSearchPassenger);
        rcPassenger= (RecyclerView) v.findViewById(R.id.rcPassenger);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSearchPassenger.setOnClickListener(this);
        searchPassenger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchPassenger:
                Intent i=new Intent(getActivity(), ShowActivity.class);
                i.putExtra(ShowActivity.FRAG_TYPE,ShowActivity.PASSENGER_RC_SHOW_FRAG);
                startActivity(i);
                break;
        }
    }
}
