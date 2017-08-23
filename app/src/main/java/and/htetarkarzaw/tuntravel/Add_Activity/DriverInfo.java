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

import and.htetarkarzaw.tuntravel.Model.DriverModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Htet Arkar Zaw on 6/18/2017.
 */

public class DriverInfo extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private String key;
    private TextInputEditText etDriverName,etDriverNRC,etDriverLicenceNo,etDriverAddress,
    etDriverPhNo,etDriverRemark;
    private Button btnAddDriverSave,btnAddDriverCancel;
    private DatabaseReference databaseReference;
    private String driverName,driverNRC,driverLicenceNo,driverAddress,driverPhNo,driverRemark;
    private ImageView btnUpload;
    private ImageButton deleteOne;
    private Uri filePath;
    private LoadingDialog loadingDialog;
    private long driverCounts = 0;
    private static final int PICK_IMAGE_REQUEST_ONE = 2341;
    private StorageReference storageReference;
    private String driverImage;
    private boolean isExistEditImage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.driver_info,container,false);
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVERS);
        }
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Driver.DRIVER_IMAGES);
        }
        init(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Driver.Keys.DRIVER_NAME,etDriverName.getText().toString());
        outState.putString(TunTravel.Driver.Keys.DRIVER_NRC,etDriverNRC.getText().toString());
        outState.putString(TunTravel.Driver.Keys.DRIVER_LICENCE_NO,etDriverLicenceNo.getText().toString());
        outState.putString(TunTravel.Driver.Keys.DRIVER_ADDRESS,etDriverAddress.getText().toString());
        outState.putString(TunTravel.Driver.Keys.DRIVER_PH_NO,etDriverPhNo.getText().toString());
        outState.putString(TunTravel.Driver.Keys.DRIVER_REMARK,etDriverRemark.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            etDriverName.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_NAME));
            etDriverNRC.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_NRC));
            etDriverLicenceNo.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_LICENCE_NO));
            etDriverAddress.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_ADDRESS));
            etDriverPhNo.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_PH_NO));
            etDriverRemark.setText(savedInstanceState.getString(TunTravel.Driver.Keys.DRIVER_REMARK));
        }
    }

    private void init(View view) {
        etDriverName = (TextInputEditText) view.findViewById(R.id.etDriverName);
        etDriverNRC = (TextInputEditText) view.findViewById(R.id.etDriverNRC);
        etDriverLicenceNo = (TextInputEditText) view.findViewById(R.id.etDriverLicenceNo);
        etDriverAddress = (TextInputEditText) view.findViewById(R.id.etDriverAddress);
        etDriverPhNo = (TextInputEditText) view.findViewById(R.id.etDriverPhNo);
        etDriverRemark = (TextInputEditText) view.findViewById(R.id.etDriverRemark);
        btnUpload = (ImageView) view.findViewById(R.id.Upload);
        deleteOne = (ImageButton) view.findViewById(R.id.deleteOne);
        btnAddDriverSave = (Button) view.findViewById(R.id.btnAddDriverSave);
        btnAddDriverCancel = (Button) view.findViewById(R.id.btnAddDriverCancel);
        btnUpload.setOnClickListener(this);
        deleteOne.setOnClickListener(this);
        btnAddDriverCancel.setOnClickListener(this);
        btnAddDriverSave.setOnClickListener(this);
        etDriverNRC.setOnFocusChangeListener(this);
        etDriverNRC.addTextChangedListener(this);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(key==null){
            btnAddDriverSave.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddDriverSave:
                if (checkValuesNotNull()) {
                    saveDatasToServer();
                }else{
                    Snackbar.make(this.getView(),"Required", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAddDriverCancel:
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
        DriverModel driverModel = new DriverModel();
        driverModel.setDriverName(driverName);
        driverModel.setDriverNRC(driverNRC);
        driverModel.setDriverLicenceNo(driverLicenceNo);
        driverModel.setDriverAddress(driverAddress);
        driverModel.setDriverPhNo(driverPhNo);
        driverModel.setDriverRemark(driverRemark);
        driverModel.setStartTripDate("");
        driverModel.setEndTripDate("");
        driverModel.setCurrentTrip("");
        if(key==null) {
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVER_COUNTS);
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        driverCounts = (long) dataSnapshot.getValue();
                    }
                    if (driverCounts != 0) {
                        databaseReference1.setValue(driverCounts + 1);
                    } else {
                        databaseReference1.setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(driverModel);
            uploadImage(driverNRC);
        }else{
            Map<String,Object> tasks = new HashMap<>();
            tasks.put(TunTravel.Driver.Keys.DRIVER_NAME,driverName);
            tasks.put(TunTravel.Driver.Keys.DRIVER_ADDRESS,driverAddress);
            tasks.put(TunTravel.Driver.Keys.DRIVER_LICENCE_NO,driverLicenceNo);
            tasks.put(TunTravel.Driver.Keys.DRIVER_PH_NO,driverPhNo);
            tasks.put(TunTravel.Driver.Keys.DRIVER_REMARK,driverRemark);
            databaseReference.child(key).updateChildren(tasks);
            uploadImage(driverNRC);
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
            uploadTask = storageReference.child(folderName).child(TunTravel.Driver.Image_Names.IMAGE_ONE).putFile(filePath);
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

    private boolean checkValuesNotNull() {
        boolean notNull = true;
        driverName = etDriverName.getText().toString();
        driverNRC = etDriverNRC.getText().toString();
        driverLicenceNo = etDriverLicenceNo.getText().toString();
        driverAddress = etDriverAddress.getText().toString();
        driverPhNo = etDriverPhNo.getText().toString();
        driverRemark = etDriverRemark.getText().toString();
        if(driverName.equals("")){
            etDriverName.setError("Required");
            notNull = false;
        }
        if(driverNRC.equals("")){
            etDriverNRC.setError("Required");
            notNull = false;
        }
        if(driverLicenceNo.equals("")){
            etDriverLicenceNo.setError("Required");
            notNull = false;
        }
        if(driverAddress.equals("")){
            etDriverAddress.setError("Required");
            notNull = false;
        }
        if(driverPhNo.equals("")){
            etDriverPhNo.setError("Required");
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

    public void setKey(String key,String driverImage){
        this.key = key;
        this.driverImage = driverImage;
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVERS);
        }
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etDriverNRC.setEnabled(false);
                DriverModel driverModel = dataSnapshot.getValue(DriverModel.class);
                etDriverName.setText(driverModel.getDriverName());
                etDriverNRC.setText(driverModel.getDriverNRC());
                etDriverAddress.setText(driverModel.getDriverAddress());
                etDriverLicenceNo.setText(driverModel.getDriverLicenceNo());
                etDriverPhNo.setText(driverModel.getDriverPhNo());
                etDriverRemark.setText(driverModel.getDriverRemark());
                setImageForUpdate(driverModel.getDriverNRC());
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
        Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(folderName).child(driverImage)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(btnUpload);
        deleteOne.setVisibility(View.VISIBLE);
        isExistEditImage = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String conductorNRC = etDriverNRC.getText().toString();
            if(!conductorNRC.equals("")){
                databaseReference.orderByChild(TunTravel.Conductor.Keys.CONDUCTOR_NRC).equalTo(conductorNRC).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==0){
                            etDriverNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.correct,0);
                            btnAddDriverSave.setEnabled(true);
                        }else{
                            etDriverNRC.setError("Car No already exists");
                            btnAddDriverSave.setEnabled(false);
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
        etDriverNRC.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
