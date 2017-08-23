package and.htetarkarzaw.tuntravel.UserInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import and.htetarkarzaw.tuntravel.R;

/**
 * Created by Htet Arkar Zaw on 7/7/2017.
 */

public class UserInfo extends Fragment {
    EditText etUserName,etPassword;
    Button btnUserSave,btnUserCancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_info, container, false);
        etUserName= (EditText) v.findViewById(R.id.etUserName);
        etPassword= (EditText) v.findViewById(R.id.etPassword);
        btnUserSave= (Button) v.findViewById(R.id.btnUserSave);
        btnUserCancel= (Button) v.findViewById(R.id.btnUserCancel);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
