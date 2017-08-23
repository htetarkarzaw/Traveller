package and.htetarkarzaw.tuntravel.UserInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import and.htetarkarzaw.tuntravel.R;

/**
 * Created by Htet Arkar Zaw on 7/8/2017.
 */

public class User extends Fragment {
    Button btnSCar,addUser;
    EditText etSearchUser;
    RecyclerView rcUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user, container, false);
        btnSCar= (Button) v.findViewById(R.id.btnSUser);
        addUser= (Button) v.findViewById(R.id.addUser);
        etSearchUser= (EditText) v.findViewById(R.id.etSearchUser);
        rcUser= (RecyclerView) v.findViewById(R.id.rcUser);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
