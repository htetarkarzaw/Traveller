package and.htetarkarzaw.tuntravel.Show_Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Model.ConductorModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 6/29/2017.
 */

public class ConductorShow extends Fragment implements View.OnClickListener {
    public static final String KEY = "key";
    private static final String ITEMS_COUNT = "items_count";
    private String key;
    private long itemsCount = 0;
    TextView tvCName, tvCNRCNo, tvCLicenceNo, tvCAddress, tvCPhNo, tvCRemark, currentTrip, startDate, endDate, tvConductorFree;
    Button btnShowConductorEdit, btnShowConductorDelete, btnShowConductorCancel;
    LinearLayout conductorLoadingLayout;
    private ValueEventListener valueEventListener;
    private StorageReference storageReference;
    private String imageFolder;
    private DatabaseReference databaseReference, databaseReference2;
    ImageView ivConductorShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conductor_show, container, false);
        conductorLoadingLayout = (LinearLayout) v.findViewById(R.id.conductorLoadingLayout);
        tvCName = (TextView) v.findViewById(R.id.tvCName);
        tvCNRCNo = (TextView) v.findViewById(R.id.tvCNRCNo);
        tvCLicenceNo = (TextView) v.findViewById(R.id.tvCLicenceNo);
        tvCAddress = (TextView) v.findViewById(R.id.tvCAddress);
        tvCPhNo = (TextView) v.findViewById(R.id.tvCPhNo);
        tvCRemark = (TextView) v.findViewById(R.id.tvCRemark);
        btnShowConductorEdit = (Button) v.findViewById(R.id.btnShowConductorEdit);
        btnShowConductorDelete = (Button) v.findViewById(R.id.btnShowConductorDelete);
        btnShowConductorCancel = (Button) v.findViewById(R.id.btnShowConductorCancel);
        ivConductorShow = (ImageView) v.findViewById(R.id.conductorShowImage);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String conductorName = tvCName.getText().toString();
        String conductorNRC = tvCNRCNo.getText().toString();
        String conductorLicence = tvCLicenceNo.getText().toString();
        String conductorAddress = tvCAddress.getText().toString();
        String conductorPhNo = tvCPhNo.getText().toString();
        String conductorRemark = tvCRemark.getText().toString();

        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_NAME, conductorName);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_NRC, conductorNRC);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_LICENCE_NO, conductorLicence);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_ADDRESS, conductorAddress);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO, conductorPhNo);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_REMARK, conductorRemark);
        outState.putString(KEY, key);
        outState.putLong(ITEMS_COUNT, itemsCount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            tvCName.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_NAME));
            tvCNRCNo.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_NRC));
            tvCLicenceNo.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_LICENCE_NO));
            tvCAddress.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_ADDRESS));
            tvCPhNo.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO));
            tvCRemark.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_REMARK));
            this.key = savedInstanceState.getString(KEY);
            this.itemsCount = savedInstanceState.getLong(ITEMS_COUNT);
            this.imageFolder = savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_NRC);
        } else {
            conductorLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShowConductorDelete.setEnabled(false);
        btnShowConductorEdit.setOnClickListener(this);
        btnShowConductorDelete.setOnClickListener(this);
        btnShowConductorCancel.setOnClickListener(this);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ConductorModel conductorModel = dataSnapshot.getValue(ConductorModel.class);
                    tvCName.setText(conductorModel.getConductorName());
                    tvCNRCNo.setText(conductorModel.getConductorNRC());
                    tvCLicenceNo.setText(conductorModel.getConductorLicenceNo());
                    tvCAddress.setText(conductorModel.getConductorAddress());
                    tvCPhNo.setText(conductorModel.getConductorPhNo());
                    tvCRemark.setText(conductorModel.getConductorRemark());
                    conductorLoadingLayout.setVisibility(View.VISIBLE);
                    imageFolder = conductorModel.getConductorNRC();
                    listImageFiles();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTORS);
        if (savedInstanceState == null) {
            databaseReference.child(key).addValueEventListener(valueEventListener);
        } else {
            if (imageFolder == null) {
                databaseReference.child(key).addValueEventListener(valueEventListener);
            } else {
                listImageFiles();
            }
        }
    }

    public void listImageFiles() {
        storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_IMAGES);
        if (imageFolder == null) {
            databaseReference.child(key).addValueEventListener(valueEventListener);
        } else {
            storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    adaptAndDownload();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void adaptAndDownload() {
        btnShowConductorDelete.setEnabled(true);
        try {
            Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivConductorShow);
        }catch (NullPointerException ne){

        }
    }

    public void setKey(String key, long itemsCount) {
        this.key = key;
        this.itemsCount = itemsCount;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (valueEventListener != null) {
            databaseReference.child(key).removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowConductorEdit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.CONDUCTOR_FRAG);
                intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.CONDUCTOR_EDIT);
                intent.putExtra(AddActivity.KEY, key);
                intent.putExtra(AddActivity.IMAGE_NAME,TunTravel.Conductor.Image_Names.IMAGE_ONE);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnShowConductorDelete:
                if (valueEventListener != null) {
                    databaseReference.child(key).removeEventListener(valueEventListener);
                }
                databaseReference.child(key).removeValue();
                databaseReference2 = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_COUNTS);
                databaseReference2.setValue(itemsCount - 1);
                if (storageReference == null) {
                    storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_IMAGES);
                }
                storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                getActivity().finish();
                break;

            case R.id.btnShowConductorCancel:
                getActivity().finish();
                break;
        }
    }
}
