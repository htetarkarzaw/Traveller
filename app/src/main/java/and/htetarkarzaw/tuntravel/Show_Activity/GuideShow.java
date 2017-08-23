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
import and.htetarkarzaw.tuntravel.Model.GuideModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 6/29/2017.
 */

public class GuideShow extends Fragment implements View.OnClickListener {
    public static final String KEY = "key";
    private static final String ITEMS_COUNT = "items_count";
    private String key;
    private long itemsCount = 0;
    TextView tvGName,tvGNRCNo,tvGLicenceNo,tvGAddress,tvGPhNo,tvGRemark,currentTrip,startDate,endDate,tvGuideFree;
    Button btnShowGuideEdit,btnShowGuideDelete,btnShowGuideCancel;
    LinearLayout guideLoadingLayout;
    private ValueEventListener valueEventListener;
    private StorageReference storageReference;
    private String imageFolder;
    private DatabaseReference databaseReference, databaseReference2;
    ImageView ivGuideShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.guide_show, container, false);
        guideLoadingLayout = (LinearLayout) v.findViewById(R.id.guideLoadingLayout);
        tvGName= (TextView) v.findViewById(R.id.tvGName);
        tvGNRCNo= (TextView) v.findViewById(R.id.tvGNRCNo);
        tvGLicenceNo= (TextView) v.findViewById(R.id.tvGLicenceNo);
        tvGAddress= (TextView) v.findViewById(R.id.tvGAddress);
        tvGPhNo= (TextView) v.findViewById(R.id.tvGPhNo);
        tvGRemark= (TextView) v.findViewById(R.id.tvGRemark);
        btnShowGuideEdit= (Button) v.findViewById(R.id.btnShowGuideEdit);
        btnShowGuideDelete= (Button) v.findViewById(R.id.btnShowGuideDelete);
        btnShowGuideCancel= (Button) v.findViewById(R.id.btnShowGuideCancel);
        ivGuideShow = (ImageView) v.findViewById(R.id.ivGuide);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String driverName=tvGName.getText().toString();
        String driverNRC=tvGNRCNo.getText().toString();
        String driverLicence=tvGLicenceNo.getText().toString();
        String driverAddress=tvGAddress.getText().toString();
        String driverPh=tvGPhNo.getText().toString();
        String driverRemark=tvGRemark.getText().toString();
        outState.putString(TunTravel.Guides.Keys.GUIDE_NAME,driverName);
        outState.putString(TunTravel.Guides.Keys.GUIDE_NRC,driverNRC);
        outState.putString(TunTravel.Guides.Keys.GUIDE_LICENCE_NO,driverLicence);
        outState.putString(TunTravel.Guides.Keys.GUIDE_ADDRESS,driverAddress);
        outState.putString(TunTravel.Guides.Keys.GUIDE_PH_NO,driverPh);
        outState.putString(TunTravel.Guides.Keys.GUIDE_REMARK,driverRemark);
        outState.putString(KEY, key);
        outState.putLong(ITEMS_COUNT, itemsCount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            tvGName.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_NAME));
            tvGNRCNo.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_NRC));
            tvGLicenceNo.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_LICENCE_NO));
            tvGAddress.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_ADDRESS));
            tvGPhNo.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_PH_NO));
            tvGRemark.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_REMARK));
            this.key = savedInstanceState.getString(KEY);
            this.itemsCount = savedInstanceState.getLong(ITEMS_COUNT);
            this.imageFolder = savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_NRC);
        }else{
            guideLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShowGuideDelete.setEnabled(false);
        btnShowGuideEdit.setOnClickListener(this);
        btnShowGuideDelete.setOnClickListener(this);
        btnShowGuideCancel.setOnClickListener(this);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GuideModel guideModel = dataSnapshot.getValue(GuideModel.class);
                    tvGName.setText(guideModel.getGuideName());
                    tvGNRCNo.setText(guideModel.getGuideNRC());
                    tvGLicenceNo.setText(guideModel.getGuideLicenceNo());
                    tvGAddress.setText(guideModel.getGuideAddress());
                    tvGPhNo.setText(guideModel.getGuidePhNo());
                    tvGRemark.setText(guideModel.getGuideRemark());
                    guideLoadingLayout.setVisibility(View.VISIBLE);
                    imageFolder = guideModel.getGuideNRC();
                    listImageFiles();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDES);
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
        storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Guides.GUIDE_IMAGES);
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
        btnShowGuideDelete.setEnabled(true);
        try {
            Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivGuideShow);
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
            case R.id.btnShowGuideEdit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.GUIDE_FRAG);
                intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.GUIDE_EDIT);
                intent.putExtra(AddActivity.KEY, key);
                intent.putExtra(AddActivity.IMAGE_NAME,TunTravel.Guides.Image_Names.IMAGE_ONE);
                startActivity(intent);
                getActivity().finish();
                break;

            case R.id.btnShowGuideDelete:
                if (valueEventListener != null) {
                    databaseReference.child(key).removeEventListener(valueEventListener);
                }
                databaseReference.child(key).removeValue();
                databaseReference2 = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDE_COUNTS);
                databaseReference2.setValue(itemsCount - 1);
                if (storageReference == null) {
                    storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Guides.GUIDE_IMAGES);
                }
                storageReference.child(imageFolder).child(TunTravel.Conductor.Image_Names.IMAGE_ONE).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                getActivity().finish();
                break;

            case R.id.btnShowGuideCancel:
                getActivity().finish();
                break;
        }
    }

}
