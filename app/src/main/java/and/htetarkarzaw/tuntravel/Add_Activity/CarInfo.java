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
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import and.htetarkarzaw.tuntravel.Model.CarModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Htet Arkar Zaw on 6/18/2017.
 */

public class CarInfo extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private ArrayList<ImageView> ivArr;
    private ArrayList<ImageView> deleteArr;
    private Map<String,Boolean> existImagesMap = new HashMap<>();
    private String key;
    private ArrayList<String> carImagesArr;
    private TextInputEditText etCarNo, etCarType, etCountSeats, etCarOwnerName, etCarOwnerPhNo,
            etCarOwnerAddress, etCarOwnerNRC, etCurrentLine, etCarRemark;
    private Button btnCarInfoSave, btnCarInfoCancel;
    private String carNo, carType, countSeats, carOwnerName, carOwnerPhNo, carOwnerAddress, carOwnerNRC, currentLine, carRemark;
    private DatabaseReference databaseReference;
    private long carCounts = 0;
    private ImageView btnUpload, btnUploadTwo, btnUploadThree, btnUploadFour;
    private StorageReference storageReference;
    private ImageButton dOne, dTwo, dThree, dFour;
    private Uri filePath, filePathTwo, filePathThree, filePathFour;
    private LoadingDialog loadingDialog;
    private boolean isUploadedImageOne, isUploadedImageTwo, isUploadedImageThree, isUploadedImageFour;
    private static final int PICK_IMAGE_REQUEST_ONE = 2341;
    private static final int PICK_IMAGE_REQUEST_TWO = 2342;
    private static final int PICK_IMAGE_REQUEST_THREE = 2343;
    private static final int PICK_IMAGE_REQUEST_FOUR = 2344;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.car_info, container, false);
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS);
        }
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES);
        }
        init(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Car.Keys.CAR_NO, etCarNo.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_TYPE, etCarType.getText().toString());
        outState.putString(TunTravel.Car.Keys.COUNT_SEATS, etCountSeats.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_NAME, etCarOwnerName.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_PH_NUM, etCarOwnerPhNo.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_ADDRESS, etCarOwnerAddress.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_NRC, etCarOwnerNRC.getText().toString());
        outState.putString(TunTravel.Car.Keys.CURRENT_LINE, etCurrentLine.getText().toString());
        outState.putString(TunTravel.Car.Keys.CAR_REMARK, etCarRemark.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            etCarNo.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_NO));
            etCarType.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_TYPE));
            etCountSeats.setText(savedInstanceState.getString(TunTravel.Car.Keys.COUNT_SEATS));
            etCarOwnerName.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_NAME));
            etCarOwnerPhNo.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_PH_NUM));
            etCarOwnerAddress.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_ADDRESS));
            etCarOwnerNRC.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_NRC));
            etCurrentLine.setText(savedInstanceState.getString(TunTravel.Car.Keys.CURRENT_LINE));
            etCarRemark.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_REMARK));
        }
    }

    private void init(View view) {
        isUploadedImageOne = false;
        isUploadedImageTwo = false;
        isUploadedImageThree = false;
        isUploadedImageFour = false;
        etCarNo = (TextInputEditText) view.findViewById(R.id.etCarNo);
        etCarType = (TextInputEditText) view.findViewById(R.id.etCarType);
        etCountSeats = (TextInputEditText) view.findViewById(R.id.etCountSeats);
        etCarOwnerName = (TextInputEditText) view.findViewById(R.id.etCarOwnerName);
        etCarOwnerPhNo = (TextInputEditText) view.findViewById(R.id.etCarOwnerPhNo);
        etCarOwnerAddress = (TextInputEditText) view.findViewById(R.id.etCarOwnerAddress);
        etCarOwnerNRC = (TextInputEditText) view.findViewById(R.id.etCarOwnerNRC);
        etCurrentLine = (TextInputEditText) view.findViewById(R.id.etCurrentLine);
        etCarRemark = (TextInputEditText) view.findViewById(R.id.etCarRemark);
        btnCarInfoSave = (Button) view.findViewById(R.id.btnCarInfoSave);
        btnCarInfoCancel = (Button) view.findViewById(R.id.btnCarInfoCancel);
        btnCarInfoSave.setOnClickListener(this);
        btnCarInfoCancel.setOnClickListener(this);

        btnUpload = (ImageView) view.findViewById(R.id.Upload);
        btnUploadTwo = (ImageView) view.findViewById(R.id.UploadTwo);
        btnUploadThree = (ImageView) view.findViewById(R.id.UploadThree);
        btnUploadFour = (ImageView) view.findViewById(R.id.UploadFour);
        btnCarInfoSave.setOnClickListener(this);
        btnCarInfoCancel.setOnClickListener(this);

        btnUploadTwo.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnUploadFour.setOnClickListener(this);
        btnUploadThree.setOnClickListener(this);

        dOne = (ImageButton) view.findViewById(R.id.deleteOne);
        dTwo = (ImageButton) view.findViewById(R.id.deleteTwo);
        dFour = (ImageButton) view.findViewById(R.id.deleteFour);
        dThree = (ImageButton) view.findViewById(R.id.deleteThree);
        dOne.setOnClickListener(this);
        dTwo.setOnClickListener(this);
        dThree.setOnClickListener(this);
        dFour.setOnClickListener(this);
        etCarNo.setOnFocusChangeListener(this);
        etCarNo.addTextChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(key==null){
            btnCarInfoSave.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpload) {
            showFileChooser("one");
        } else if (v == btnUploadTwo) {
            showFileChooser("two");
        } else if (v == btnUploadThree) {
            showFileChooser("three");
        } else if (v == btnUploadFour) {
            showFileChooser("four");
        }
        switch (v.getId()) {
            case R.id.btnCarInfoSave:
                if (checkValuesToNotNull()) {
                    saveDatasToServer();
                } else {
                    Snackbar.make(this.getView(), "Required", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCarInfoCancel:
                getActivity().finish();
                break;

            case R.id.deleteOne:
                filePath = null;
                btnUpload.setImageResource(R.drawable.ic_add_black_24dp);
                dOne.setVisibility(View.INVISIBLE);
                if(existImagesMap.get(TunTravel.Car.Image_Names.IMAGE_ONE) != null){
                    existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_ONE,false);
                }
                Log.d("ExistImage",0+" = false");
                break;

            case R.id.deleteTwo:
                filePathTwo = null;
                btnUploadTwo.setImageResource(R.drawable.ic_add_black_24dp);
                dTwo.setVisibility(View.INVISIBLE);
                if(existImagesMap.get(TunTravel.Car.Image_Names.IMAGE_TWO) != null){
                    existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_TWO,false);
                }
                Log.d("ExistImage",1+" = false");
                break;
            case R.id.deleteThree:
                filePathThree = null;
                dThree.setVisibility(View.INVISIBLE);
                btnUploadThree.setImageResource(R.drawable.ic_add_black_24dp);
                if(existImagesMap.get(TunTravel.Car.Image_Names.IMAGE_THREE) != null){
                    existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_THREE,false);
                }
                Log.d("ExistImage",2+" = false");
                break;
            case R.id.deleteFour:
                filePathFour = null;
                btnUploadFour.setImageResource(R.drawable.ic_add_black_24dp);
                dFour.setVisibility(View.INVISIBLE);
                if(existImagesMap.get(TunTravel.Car.Image_Names.IMAGE_FOUR) != null){
                    existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_FOUR,false);
                }
                Log.d("ExistImage",3+" = false");
                break;
        }
    }

    private void saveDatasToServer() {
        CarModel carModel = new CarModel();
        carModel.setCarNo(carNo);
        carModel.setCarType(carType);
        carModel.setCountSeats(countSeats);
        carModel.setCarOwnerName(carOwnerName);
        carModel.setCarOwnerPhNo(carOwnerPhNo);
        carModel.setCarOwnerAddress(carOwnerAddress);
        carModel.setCarOwnerNRC(carOwnerNRC);
        carModel.setCurrentLine(currentLine);
        carModel.setCarRemark(carRemark);
        carModel.setStartTripDate("");
        carModel.setEndTripDate("");
        carModel.setCurrentTrip("");
        if (this.key == null) {
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS_COUNTS);
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        carCounts = (long) dataSnapshot.getValue();
                    }

                    if (carCounts != 0) {
                        databaseReference1.setValue(carCounts + 1);
                    } else if (carCounts == 0) {
                        databaseReference1.setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(carModel);
            uploadImages(carModel.getCarNo());
        } else {
            Map<String, Object> tasks = new HashMap<>();
            tasks.put(TunTravel.Car.Keys.CAR_TYPE, carType);
            tasks.put(TunTravel.Car.Keys.COUNT_SEATS, countSeats);
            tasks.put(TunTravel.Car.Keys.CAR_OWNER_NAME, carOwnerName);
            tasks.put(TunTravel.Car.Keys.CAR_OWNER_PH_NUM, carOwnerPhNo);
            tasks.put(TunTravel.Car.Keys.CAR_OWNER_ADDRESS, carOwnerAddress);
            tasks.put(TunTravel.Car.Keys.CAR_OWNER_NRC, carOwnerNRC);
            tasks.put(TunTravel.Car.Keys.CURRENT_LINE, currentLine);
            tasks.put(TunTravel.Car.Keys.CAR_REMARK, carRemark);
            databaseReference.child(this.key).updateChildren(tasks);
            for(Map.Entry<String,Boolean> set : existImagesMap.entrySet()){
                if(set.getValue() == false){
                    storageReference.child(carNo).child(set.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            }
            uploadImages(carNo);
        }
    }

    private void uploadImages(String fileName) {
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
            uploadTask = storageReference.child(fileName).child(TunTravel.Car.Image_Names.IMAGE_ONE).putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isUploadedImageOne = true;
                    CompleteUploadDatas();
                }
            });
        } else {
            isUploadedImageOne = true;
            CompleteUploadDatas();
        }
        if (filePathTwo != null) {
            uploadTask = storageReference.child(fileName).child(TunTravel.Car.Image_Names.IMAGE_TWO).putFile(filePathTwo);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isUploadedImageTwo = true;
                    CompleteUploadDatas();
                }
            });
        } else {
            isUploadedImageTwo = true;
            CompleteUploadDatas();
        }
        if (filePathThree != null) {
            uploadTask = storageReference.child(fileName).child(TunTravel.Car.Image_Names.IMAGE_THREE).putFile(filePathThree);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isUploadedImageThree = true;
                    CompleteUploadDatas();
                }
            });
        } else {
            isUploadedImageThree = true;
            CompleteUploadDatas();
        }
        if (filePathFour != null) {
            uploadTask = storageReference.child(fileName).child(TunTravel.Car.Image_Names.IMAGE_FOUR).putFile(filePathFour);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isUploadedImageFour = true;
                    CompleteUploadDatas();
                }
            });
        } else {
            isUploadedImageFour = true;
            CompleteUploadDatas();
        }
    }

    private void CompleteUploadDatas() {
        if (isUploadedImageOne && isUploadedImageTwo && isUploadedImageThree && isUploadedImageFour) {
            loadingDialog.dismiss();
            getActivity().finish();
        }
    }

    private boolean checkValuesToNotNull() {
        boolean notNull = true;
        carNo = etCarNo.getText().toString();
        carType = etCarType.getText().toString();
        countSeats = etCountSeats.getText().toString();
        carOwnerName = etCarOwnerName.getText().toString();
        carOwnerPhNo = etCarOwnerPhNo.getText().toString();
        carOwnerAddress = etCarOwnerAddress.getText().toString();
        carOwnerNRC = etCarOwnerNRC.getText().toString();
        currentLine = etCurrentLine.getText().toString();
        carRemark = etCarRemark.getText().toString();

        if (carNo.equals("")) {
            etCarNo.setError("Required");
            notNull = false;
        }
        if (carType.equals("")) {
            etCarType.setError("Required");
            notNull = false;
        }
        if (countSeats.equals("")) {
            etCountSeats.setError("Required");
            notNull = false;
        }
        if (carOwnerName.equals("")) {
            etCarOwnerName.setError("Required");
            notNull = false;
        }
        if (carOwnerPhNo.equals("")) {
            etCarOwnerPhNo.setError("Required");
            notNull = false;
        }
        if (carOwnerAddress.equals("")) {
            etCarOwnerAddress.setError("Required");
            notNull = false;
        }
        if (carOwnerNRC.equals("")) {
            etCarOwnerNRC.setError("Required");
            notNull = false;
        }
        if (currentLine.equals("")) {
            etCurrentLine.setError("Required");
            notNull = false;
        }

        if (checkImagesNotNull()) {
            Snackbar.make(getView(), "Required, at least one car image", Snackbar.LENGTH_SHORT).show();
            notNull = false;
        }

        return notNull;
    }

    private boolean checkImagesNotNull() {
        if (filePath == null && filePathTwo == null && filePathThree == null && filePathFour == null) {
            if (key != null) {
                boolean isAllFalse = true;
                for (Map.Entry<String,Boolean> set:existImagesMap.entrySet()){
                    if(set.getValue() == true){
                        isAllFalse = false;
                    }
                }
                if(isAllFalse){
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void setKey(String key, ArrayList<String> carImagesArr) {
        this.key = key;
        this.carImagesArr = carImagesArr;
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS);
        }
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etCarNo.setEnabled(false);
                CarModel carModel = dataSnapshot.getValue(CarModel.class);
                etCarNo.setText(carModel.getCarNo());
                etCarType.setText(carModel.getCarType());
                etCountSeats.setText(carModel.getCountSeats());
                etCarOwnerName.setText(carModel.getCarOwnerName());
                etCarOwnerPhNo.setText(carModel.getCarOwnerPhNo());
                etCarOwnerAddress.setText(carModel.getCarOwnerAddress());
                etCarOwnerNRC.setText(carModel.getCarOwnerNRC());
                etCurrentLine.setText(carModel.getCurrentLine());
                etCarRemark.setText(carModel.getCarRemark());
                setImagesForUpdate(carModel.getCarNo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setImagesForUpdate(String carNo) {
        ivArr = new ArrayList<>();
        deleteArr = new ArrayList<>();
        ivArr.add(btnUpload);
        ivArr.add(btnUploadTwo);
        ivArr.add(btnUploadThree);
        ivArr.add(btnUploadFour);
        deleteArr.add(dOne);
        deleteArr.add(dTwo);
        deleteArr.add(dThree);
        deleteArr.add(dFour);
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES);
        }
        for (int i = 0; i < carImagesArr.size(); i++) {
            if(carImagesArr.get(i).equals(TunTravel.Car.Image_Names.IMAGE_ONE)) {
                Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(carNo).child(carImagesArr.get(i))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivArr.get(0));
                deleteArr.get(0).setVisibility(View.VISIBLE);
                existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_ONE,true);
                Log.d("ExistImage",0+" = true");
            }
            if(carImagesArr.get(i).equals(TunTravel.Car.Image_Names.IMAGE_TWO)) {
                Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(carNo).child(carImagesArr.get(i))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivArr.get(1));
                deleteArr.get(1).setVisibility(View.VISIBLE);
                existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_TWO,true);
                Log.d("ExistImage",1+" = true");
            }
            if(carImagesArr.get(i).equals(TunTravel.Car.Image_Names.IMAGE_THREE)) {
                Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(carNo).child(carImagesArr.get(i))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivArr.get(2));
                deleteArr.get(2).setVisibility(View.VISIBLE);
                existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_THREE,true);
                Log.d("ExistImage",2+" = true");
            }
            if(carImagesArr.get(i).equals(TunTravel.Car.Image_Names.IMAGE_FOUR)) {
                Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference.child(carNo).child(carImagesArr.get(i))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivArr.get(3));
                deleteArr.get(3).setVisibility(View.VISIBLE);
                existImagesMap.put(TunTravel.Car.Image_Names.IMAGE_FOUR,true);
                Log.d("ExistImage",3+" = true");
            }
        }
    }

    private void showFileChooser(String some) {
        switch (some) {
            case "one": {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_ONE);
            }
            break;
            case "two": {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_TWO);
            }
            break;
            case "three": {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_THREE);
            }
            break;
            case "four": {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_FOUR);
            }
            break;
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_ONE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            dOne.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                btnUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_IMAGE_REQUEST_TWO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathTwo = data.getData();
            dTwo.setVisibility(View.VISIBLE);
            Bitmap bitmapTwo = null;
            try {
                bitmapTwo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathTwo);
                btnUploadTwo.setImageBitmap(bitmapTwo);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_IMAGE_REQUEST_THREE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathThree = data.getData();
            dThree.setVisibility(View.VISIBLE);
            Bitmap bitmapThree = null;
            try {
                bitmapThree = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathThree);
                btnUploadThree.setImageBitmap(bitmapThree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_FOUR && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFour = data.getData();
            dFour.setVisibility(View.VISIBLE);
            Bitmap bitmapFour = null;

            try {
                bitmapFour = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathFour);
                btnUploadFour.setImageBitmap(bitmapFour);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String carNo = etCarNo.getText().toString();
            if(!carNo.equals("")){
                databaseReference.orderByChild(TunTravel.Car.Keys.CAR_NO).equalTo(carNo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==0){
                            etCarNo.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.correct,0);
                            btnCarInfoSave.setEnabled(true);
                        }else{
                            etCarNo.setError("Car No already exists");
                            btnCarInfoSave.setEnabled(false);
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
        etCarNo.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
