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

import and.htetarkarzaw.tuntravel.Model.ConductorModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Htet Arkar Zaw on 6/18/2017.
 */

public class ConductorInfo extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private String key;
    private TextInputEditText etConductorName,etConductorNRC,etConductorLicenceNo,
    etConductorAddress,etConductorPhNo,etConductorRemark;
    private Button btnConductorSave,btnConductorCancel;
    private DatabaseReference databaseReference;
    private String conductorName,conductorNRC,conductorLicenceNo,conductorAddress,conductorPhNo,conductorRemark;
    private ImageView btnUpload;
    private ImageButton deleteOne;
    private Uri filePath;
    private LoadingDialog loadingDialog;
    private long conductorCounts = 0;
    private static final int PICK_IMAGE_REQUEST_ONE = 2341;
    private StorageReference storageReference;
    private String conductorImage;
    private boolean isExistEditImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.conductor_info,container,false);
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTORS);
        }
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_IMAGES);
        }
        init(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_NAME,etConductorName.getText().toString());
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_NRC,etConductorNRC.getText().toString());
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_LICENCE_NO,etConductorLicenceNo.getText().toString());
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_ADDRESS,etConductorAddress.getText().toString());
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO,etConductorPhNo.getText().toString());
        outState.putString(TunTravel.Conductor.Keys.CONDUCTOR_REMARK,etConductorRemark.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            etConductorName.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_NAME));
            etConductorNRC.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_NRC));
            etConductorLicenceNo.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_LICENCE_NO));
            etConductorAddress.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_ADDRESS));
            etConductorPhNo.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO));
            etConductorRemark.setText(savedInstanceState.getString(TunTravel.Conductor.Keys.CONDUCTOR_REMARK));
        }
    }

    private void init(View view) {
        etConductorName = (TextInputEditText) view.findViewById(R.id.etConductorName);
        etConductorNRC = (TextInputEditText) view.findViewById(R.id.etConductorNRC);
        etConductorLicenceNo = (TextInputEditText) view.findViewById(R.id.etConductorLicenceNo);
        etConductorAddress = (TextInputEditText) view.findViewById(R.id.etConductorAddress);
        etConductorPhNo = (TextInputEditText) view.findViewById(R.id.etConductorPhNo);
        etConductorRemark = (TextInputEditText) view.findViewById(R.id.etConductorRemark);
        btnConductorSave = (Button) view.findViewById(R.id.btnAddConductorSave);
        btnConductorCancel = (Button) view.findViewById(R.id.btnAddConductorCancel);
        btnUpload= (ImageView) view.findViewById(R.id.Upload);
        btnUpload.setOnClickListener(this);
        deleteOne = (ImageButton) view.findViewById(R.id.deleteOne);
        deleteOne.setOnClickListener(this);
        btnConductorSave.setOnClickListener(this);
        btnConductorCancel.setOnClickListener(this);
        etConductorNRC.setOnFocusChangeListener(this);
        etConductorNRC.addTextChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(key==null){
            btnConductorSave.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddConductorSave:
                if(checkValuesToNotNull()){
                    SaveDatasToServer();
                }else{
                    Snackbar.make(this.getView(),"Required", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAddConductorCancel:
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

    private void SaveDatasToServer() {
        ConductorModel conductorModel = new ConductorModel();
        conductorModel.setConductorName(conductorName);
        conductorModel.setConductorNRC(conductorNRC);
        conductorModel.setConductorLicenceNo(conductorLicenceNo);
        conductorModel.setConductorAddress(conductorAddress);
        conductorModel.setConductorPhNo(conductorPhNo);
        conductorModel.setConductorRemark(conductorRemark);
        conductorModel.setStartTripDate("");
        conductorModel.setEndTripDate("");
        conductorModel.setCurrentTrip("");
        if(key==null) {
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_COUNTS);
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        conductorCounts = (long) dataSnapshot.getValue();
                    }
                    if (conductorCounts != 0) {
                        databaseReference1.setValue(conductorCounts + 1);
                    } else {
                        databaseReference1.setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(conductorModel);
            uploadImage(conductorModel.getConductorNRC());
        }else {
            Map<String,Object> tasks = new HashMap<>();
            tasks.put(TunTravel.Conductor.Keys.CONDUCTOR_NAME,conductorName);
            tasks.put(TunTravel.Conductor.Keys.CONDUCTOR_ADDRESS,conductorAddress);
            tasks.put(TunTravel.Conductor.Keys.CONDUCTOR_LICENCE_NO,conductorLicenceNo);
            tasks.put(TunTravel.Conductor.Keys.CONDUCTOR_PH_NO,conductorPhNo);
            tasks.put(TunTravel.Conductor.Keys.CONDUCTOR_REMARK,conductorRemark);
            databaseReference.child(key).updateChildren(tasks);
            uploadImage(conductorNRC);
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
            uploadTask = storageReference.child(folderName).child(TunTravel.Conductor.Image_Names.IMAGE_ONE).putFile(filePath);
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
        conductorName = etConductorName.getText().toString();
        conductorNRC = etConductorNRC.getText().toString();
        conductorLicenceNo = etConductorLicenceNo.getText().toString();
        conductorAddress = etConductorAddress.getText().toString();
        conductorPhNo = etConductorPhNo.getText().toString();
        conductorRemark = etConductorRemark.getText().toString();
        if(conductorName.equals("")){
            etConductorName.setError("Required");
            notNull = false;
        }
        if(conductorNRC.equals("")){
            etConductorNRC.setError("Required");
            notNull = false;
        }
        if(conductorLicenceNo.equals("")){
            etConductorLicenceNo.setError("Required");
            notNull = false;
        }
        if(conductorAddress.equals("")){
            etConductorAddress.setError("Required");
            notNull = false;
        }
        if(conductorPhNo.equals("")){
            etConductorPhNo.setError("Required");
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

    public void setKey(String key,String conductorImage){
        this.key = key;
        this.conductorImage = conductorImage;
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTORS);
        }
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etConductorNRC.setEnabled(false);
                ConductorModel conductorModel = dataSnapshot.getValue(ConductorModel.class);
                etConductorName.setText(conductorModel.getConductorName());
                etConductorNRC.setText(conductorModel.getConductorNRC());
                etConductorAddress.setText(conductorModel.getConductorAddress());
                etConductorLicenceNo.setText(conductorModel.getConductorLicenceNo());
                etConductorPhNo.setText(conductorModel.getConductorPhNo());
                etConductorRemark.setText(conductorModel.getConductorRemark());
                setImageForUpdate(conductorModel.getConductorNRC());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setImageForUpdate(String folderName) {
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Conductor.CONDUCTOR_IMAGES);
        }
        Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(folderName).child(conductorImage)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(btnUpload);
        deleteOne.setVisibility(View.VISIBLE);
        isExistEditImage = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String conductorNRC = etConductorNRC.getText().toString();
            if(!conductorNRC.equals("")){
                databaseReference.orderByChild(TunTravel.Conductor.Keys.CONDUCTOR_NRC).equalTo(conductorNRC).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==0){
                            etConductorNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.correct,0);
                            btnConductorSave.setEnabled(true);
                        }else{
                            etConductorNRC.setError("Car No already exists");
                            btnConductorSave.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        etConductorNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
