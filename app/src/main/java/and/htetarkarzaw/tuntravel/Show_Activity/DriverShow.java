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
import and.htetarkarzaw.tuntravel.Model.DriverModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 6/29/2017.
 */

public class DriverShow extends Fragment implements View.OnClickListener {
    public static final String KEY = "key";
    private static final String ITEMS_COUNT = "items_count";
    private String key;
    private long itemsCount = 0;
    TextView tvDName,tvDNRCNo,tvDLicenceNo,tvDAddress,tvDPhNo,tvDRemark,currentTrip,startDate,endDate,tvDriverFree;
    Button btnShowDriverEdit,btnShowDriverCancel,btnShowDriverDelete;
    LinearLayout driverLoadingLayout;
    private ValueEventListener valueEventListener;
    private StorageReference storageReference;
    private String imageFolder;
    private DatabaseReference databaseReference, databaseReference2;
    ImageView ivDriverShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.driver_show, container, false);
        tvDName= (TextView) v.findViewById(R.id.tvDName);
        tvDNRCNo= (TextView) v.findViewById(R.id.tvDNRCNo);
        tvDLicenceNo= (TextView) v.findViewById(R.id.tvDLicenceNo);
        tvDAddress= (TextView) v.findViewById(R.id.tvDAddress);
        tvDPhNo= (TextView) v.findViewById(R.id.tvDPhNo);
        tvDRemark= (TextView) v.findViewById(R.id.tvDRemark);
        ivDriverShow = (ImageView) v.findViewById(R.id.ivDriver);
        driverLoadingLayout = (LinearLayout) v.findViewById(R.id.driverLoadingLayout);
        btnShowDriverEdit= (Button) v.findViewById(R.id.btnShowDriverEdit);
        btnShowDriverCancel= (Button) v.findViewById(R.id.btnShowDriverCancel);
        btnShowDriverDelete = (Button) v.findViewById(R.id.btnShowDriverDelete);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        String driverName=tvDName.getText().toString();
        String driverNRC=tvDNRCNo.getText().toString();
        String driverLicence=tvDLicenceNo.getText().toString();
        String driverAddress=tvDAddress.getText().toString();
        String driverPh=tvDPhNo.getText().toString();
        String driverRemark=tvDRemark.getText().toString();

        outState.putString(TunTravel.Driver.Keys.DRIVER_NAME,driverName);
        outState.putString(TunTravel.Driver.Keys.DRIVER_NRC,driverNRC);
        outState.putString(TunTravel.Driver.Keys.DRIVER_LICENCE_NO,driverLicence);
        outState.putString(TunTravel.Driver.Keys.DRIVER_ADDRESS,driverAddress);
        outState.putString(TunTravel.Driver.Keys.DRIVER_PH_NO,driverPh);
        outState.putString(TunTravel.Driver.Keys.DRIVER_REMARK,driverRemark);
        outState.putString(KEY, key);
        outState.putLong(ITEMS_COUNT, itemsCount);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            tvDName.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_NAME));
            tvDNRCNo.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_NRC));
            tvDLicenceNo.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_LICENCE_NO));
            tvDAddress.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_ADDRESS));
            tvDPhNo.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_PH_NO));
            tvDRemark.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_REMARK));
            this.imageFolder = savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_NRC);
            this.key = savedInstanceState.getString(KEY);
            this.itemsCount = savedInstanceState.getLong(ITEMS_COUNT);
        }else{
            driverLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShowDriverDelete.setEnabled(false);
        btnShowDriverEdit.setOnClickListener(this);
        btnShowDriverCancel.setOnClickListener(this);
        btnShowDriverDelete.setOnClickListener(this);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    DriverModel driverModel = dataSnapshot.getValue(DriverModel.class);
                    tvDName.setText(driverModel.getDriverName());
                    tvDNRCNo.setText(driverModel.getDriverNRC());
                    tvDLicenceNo.setText(driverModel.getDriverLicenceNo());
                    tvDAddress.setText(driverModel.getDriverAddress());
                    tvDPhNo.setText(driverModel.getDriverPhNo());
                    tvDRemark.setText(driverModel.getDriverRemark());
                    driverLoadingLayout.setVisibility(View.VISIBLE);
                    imageFolder = driverModel.getDriverNRC();
                    listImageFiles();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVERS);
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
        storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Driver.DRIVER_IMAGES);
        if (imageFolder == null) {
            databaseReference.child(key).addValueEventListener(valueEventListener);
        } else {
            storageReference.child(imageFolder).child(TunTravel.Driver.Image_Names.IMAGE_ONE).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        btnShowDriverDelete.setEnabled(true);
        try {
            Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivDriverShow);
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
        switch (v.getId()){
            case R.id.btnShowDriverEdit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.DRIVER_FRAG);
                intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.DRIVER_EDIT);
                intent.putExtra(AddActivity.KEY, key);
                intent.putExtra(AddActivity.IMAGE_NAME,TunTravel.Driver.Image_Names.IMAGE_ONE);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnShowDriverCancel:
                getActivity().finish();
                break;
            case R.id.btnShowDriverDelete:
                if (valueEventListener != null) {
                    databaseReference.child(key).removeEventListener(valueEventListener);
                }
                databaseReference.child(key).removeValue();
                databaseReference2 = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVER_COUNTS);
                databaseReference2.setValue(itemsCount - 1);
                if (storageReference == null) {
                    storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Driver.DRIVER_IMAGES);
                }
                storageReference.child(imageFolder).child(TunTravel.Driver.Image_Names.IMAGE_ONE).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                getActivity().finish();
                break;
        }
    }
}
