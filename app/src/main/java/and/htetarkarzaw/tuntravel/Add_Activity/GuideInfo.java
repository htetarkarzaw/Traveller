package and.htetarkarzaw.tuntravel.Add_Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import and.htetarkarzaw.tuntravel.Model.GuideModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Htet Arkar Zaw on 6/18/2017.
 */

public class GuideInfo extends Fragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private String key;
    private TextInputEditText etGuideName,etGuideNRC,etGuideLicenceNo,etGuideAddress,
    etGuidePhNo,etGuideRemark;
    private Button btnAddGuideSave,btnAddGuideCancel;
    private DatabaseReference databaseReference;
    private String guideName,guideNRC,guideLicenceNo,guideAddress,guidePhNo,guideRemark;
    private Uri filePath;
    private LoadingDialog loadingDialog;
    private long guideCounts = 0;
    private static final int PICK_IMAGE_REQUEST_ONE = 2341;
    private StorageReference storageReference;
    private String guideImage;
    private boolean isExistEditImage;
    ImageView btnUpload;
    ImageButton deleteOne;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.guide_info,container,false);
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDES);
        }
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Guides.GUIDE_IMAGES);
        }
        init(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Guides.Keys.GUIDE_NAME,guideName);
        outState.putString(TunTravel.Guides.Keys.GUIDE_NRC,guideNRC);
        outState.putString(TunTravel.Guides.Keys.GUIDE_LICENCE_NO,guideLicenceNo);
        outState.putString(TunTravel.Guides.Keys.GUIDE_ADDRESS,guideAddress);
        outState.putString(TunTravel.Guides.Keys.GUIDE_PH_NO,guidePhNo);
        outState.putString(TunTravel.Guides.Keys.GUIDE_REMARK,guideRemark);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            etGuideName.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_NAME));
            etGuideNRC.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_NRC));
            etGuideLicenceNo.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_LICENCE_NO));
            etGuideAddress.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_ADDRESS));
            etGuidePhNo.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_PH_NO));
            etGuideRemark.setText(savedInstanceState.getString(TunTravel.Guides.Keys.GUIDE_REMARK));
        }
    }

    private void init(View view) {
        etGuideName = (TextInputEditText) view.findViewById(R.id.etGuideName);
        etGuideNRC = (TextInputEditText) view.findViewById(R.id.etGuideNRC);
        etGuideLicenceNo = (TextInputEditText) view.findViewById(R.id.etGuideLicenceNo);
        etGuideAddress = (TextInputEditText) view.findViewById(R.id.etGuideAddress);
        etGuidePhNo = (TextInputEditText) view.findViewById(R.id.etGuidePhNo);
        etGuideRemark = (TextInputEditText) view.findViewById(R.id.etGuideRemark);
        btnAddGuideSave = (Button) view.findViewById(R.id.btnAddGuideSave);
        btnAddGuideCancel = (Button) view.findViewById(R.id.btnAddGuideCancel);
        btnUpload = (ImageView) view.findViewById(R.id.Upload);
        deleteOne = (ImageButton) view.findViewById(R.id.deleteOne);
        btnUpload.setOnClickListener(this);
        deleteOne.setOnClickListener(this);
        btnAddGuideSave.setOnClickListener(this);
        btnAddGuideCancel.setOnClickListener(this);
        etGuideNRC.setOnFocusChangeListener(this);
        etGuideNRC.addTextChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(key==null){
            btnAddGuideSave.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddGuideSave:
                if (checkValuesToNotNull()) {
                    saveDatasToServer();
                }else{
                    Snackbar.make(this.getView(),"Required", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAddGuideCancel:
                getActivity().finish();
                break;
            case R.id.Upload:
                showFileChooser();
                break;
            case R.id.deleteOne:
                if(key!=null){
                    isExistEditImage = false;
                }
                filePath=null;
                btnUpload.setImageResource(R.drawable.ic_add_black_24dp);
                deleteOne.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_ONE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_ONE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            deleteOne.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                btnUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void saveDatasToServer() {
        GuideModel guideModel = new GuideModel();
        guideModel.setGuideName(guideName);
        guideModel.setGuideNRC(guideNRC);
        guideModel.setGuideLicenceNo(guideLicenceNo);
        guideModel.setGuideAddress(guideAddress);
        guideModel.setGuidePhNo(guidePhNo);
        guideModel.setGuideRemark(guideRemark);
        guideModel.setStartTripDate("");
        guideModel.setEndTripDate("");
        guideModel.setCurrentTrip("");
        if(key==null) {
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDE_COUNTS);
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        guideCounts = (long) dataSnapshot.getValue();
                    }
                    if (guideCounts != 0) {
                        databaseReference1.setValue(guideCounts + 1);
                    } else {
                        databaseReference1.setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(guideModel);
            uploadImage(guideNRC);
        }else{
            Map<String,Object> tasks = new HashMap<>();
            tasks.put(TunTravel.Guides.Keys.GUIDE_NAME,guideName);
            tasks.put(TunTravel.Guides.Keys.GUIDE_ADDRESS,guideAddress);
            tasks.put(TunTravel.Guides.Keys.GUIDE_LICENCE_NO,guideLicenceNo);
            tasks.put(TunTravel.Guides.Keys.GUIDE_PH_NO,guidePhNo);
            tasks.put(TunTravel.Guides.Keys.GUIDE_REMARK,guideRemark);
            databaseReference.child(key).updateChildren(tasks);
            uploadImage(guideNRC);
        }
    }

    private void uploadImage(String folderName) {
        loadingDialog = (LoadingDialog) getActivity().getSupportFragmentManager().findFragmentByTag("LoadingFrag");
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
            loadingDialog.show(getActivity().getSupportFragmentManager(), "LoadingFrag");
        } else {
            loadingDialog.show(getActivity().getSupportFragmentManager(), "LoadingFrag");
        }
        loadingDialog.setCancelable(false);
        UploadTask uploadTask;
        if (filePath != null) {
            uploadTask = storageReference.child(folderName).child(TunTravel.Guides.Image_Names.IMAGE_ONE).putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    CompleteUploadDatas();
                }
            });
        }
    }

    private void CompleteUploadDatas() {
        loadingDialog.dismiss();
        getActivity().finish();
    }

    private boolean checkValuesToNotNull() {
        boolean notNull = true;
        guideName = etGuideName.getText().toString();
        guideNRC = etGuideNRC.getText().toString();
        guideLicenceNo = etGuideLicenceNo.getText().toString();
        guideAddress = etGuideAddress.getText().toString();
        guidePhNo = etGuidePhNo.getText().toString();
        guideRemark = etGuideRemark.getText().toString();
        if(guideName.equals("")){
            etGuideName.setError("Required");
            notNull = false;
        }
        if(guideNRC.equals("")){
            etGuideNRC.setError("Required");
            notNull = false;
        }
        if(guideLicenceNo.equals("")){
            etGuideLicenceNo.setError("Required");
            notNull = false;
        }
        if(guideAddress.equals("")){
            etGuideAddress.setError("Required");
            notNull = false;
        }

        if (checkImagesNotNull()) {
            Snackbar.make(getView(), "Required, at least one car image", Snackbar.LENGTH_SHORT).show();
            notNull = false;
        }
        return notNull;
    }

    private boolean checkImagesNotNull() {
        if(filePath==null){
            if(key!=null){
                if(isExistEditImage==false){
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    public void setKey(String key,String guideImage){
        this.key = key;
        this.guideImage = guideImage;
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDES);
        }
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etGuideNRC.setEnabled(false);
                GuideModel guideModel = dataSnapshot.getValue(GuideModel.class);
                etGuideName.setText(guideModel.getGuideName());
                etGuideNRC.setText(guideModel.getGuideNRC());
                etGuideAddress.setText(guideModel.getGuideAddress());
                etGuideLicenceNo.setText(guideModel.getGuideLicenceNo());
                etGuidePhNo.setText(guideModel.getGuidePhNo());
                etGuideRemark.setText(guideModel.getGuideRemark());
                setImageForUpdate(guideModel.getGuideNRC());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setImageForUpdate(String folderName) {
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Guides.GUIDE_IMAGES);
        }
        Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(folderName).child(guideImage)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(btnUpload);
        deleteOne.setVisibility(View.VISIBLE);
        isExistEditImage = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        etGuideNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String guideNRC = etGuideNRC.getText().toString();
            if(!guideNRC.equals("")){
                databaseReference.orderByChild(TunTravel.Guides.Keys.GUIDE_NRC).equalTo(guideNRC).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==0){
                            etGuideNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.correct,0);
                            btnAddGuideSave.setEnabled(true);
                        }else{
                            etGuideNRC.setError("Car No already exists");
                            btnAddGuideSave.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
