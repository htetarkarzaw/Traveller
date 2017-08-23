package and.htetarkarzaw.tuntravel.Add_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import and.htetarkarzaw.tuntravel.R;

/**
 * Created by Eiron on 6/24/17.
 */

public class LoadingDialog extends DialogFragment {
    TextView tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_loading_dialog,null);
        tv = (TextView) view.findViewById(R.id.tvLoading);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
