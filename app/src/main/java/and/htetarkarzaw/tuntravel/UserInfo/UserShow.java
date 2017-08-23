package and.htetarkarzaw.tuntravel.UserInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import and.htetarkarzaw.tuntravel.R;

/**
 * Created by Htet Arkar Zaw on 7/8/2017.
 */

public class UserShow extends Fragment {
    TextView tvUserName,tvPassword;
    Button btnUserEdit,btnUserDelete,btnUserCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_show, container, false);
        tvUserName= (TextView) v.findViewById(R.id.tvUserName);
        tvPassword= (TextView) v.findViewById(R.id.tvPassword);
        btnUserEdit= (Button) v.findViewById(R.id.btnUserEdit);
        btnUserDelete= (Button) v.findViewById(R.id.btnUserDelete);
        btnUserCancel= (Button) v.findViewById(R.id.btnUserCancel);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
