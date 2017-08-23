package and.htetarkarzaw.tuntravel.Show_Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Model.CarModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 6/19/2017.
 */

public class CarShow extends Fragment implements View.OnClickListener {
    private String[] tempURLS = new String[4];
    public static final String KEY = "key";
    private static final String ITEMS_COUNT = "items_count";
    public static String CAR_IMAGES_ARR = "carImagesArray";
    private RecyclerView rcImageCar;
    private String key;
    private long itemsCount = 0;
    private ValueEventListener valueEventListener;
    private LinearLayout carLoadingLayout;
    private StorageReference storageReference;
    private boolean isGetImageOne, isGetImageTwo, isGetImageThree, isGetImageFour;
    private String imageFolder;
    private ArrayList<String> downloadURLS;
    private TextView tvShowCarNo, tvShowCarType, tvShowCountSeats, tvShowCarOwnerName,
            tvShowCarOwnerPhNo, tvShowCarOwnerAddress, tvShowCarOwnerNRC, tvShowCarCurrentLine, tvShowCarRemark;
    private Button btnShowCarEdit, btnShowCarDelete , btnShowCarCancel;
    private DatabaseReference databaseReference, databaseReference2;
    private ImageView ivCar, ivCarTwo, ivCarThree, ivCarFour;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String carNo = tvShowCarNo.getText().toString();
        String carType = tvShowCarType.getText().toString();
        String countSeats = tvShowCountSeats.getText().toString();
        String carOwnerName = tvShowCarOwnerName.getText().toString();
        String carOwnerPhNo = tvShowCarOwnerPhNo.getText().toString();
        String carOwnerAddress = tvShowCarOwnerAddress.getText().toString();
        String carOwnerNRC = tvShowCarOwnerNRC.getText().toString();
        String carCurrentLine = tvShowCarCurrentLine.getText().toString();
        String carRemark = tvShowCarRemark.getText().toString();
        outState.putString(TunTravel.Car.Keys.CAR_NO, carNo);
        outState.putString(TunTravel.Car.Keys.CAR_TYPE, carType);
        outState.putString(TunTravel.Car.Keys.COUNT_SEATS, countSeats);
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_NAME, carOwnerName);
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_PH_NUM, carOwnerPhNo);
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_ADDRESS, carOwnerAddress);
        outState.putString(TunTravel.Car.Keys.CAR_OWNER_NRC, carOwnerNRC);
        outState.putString(TunTravel.Car.Keys.CURRENT_LINE, carCurrentLine);
        outState.putString(TunTravel.Car.Keys.CAR_REMARK, carRemark);
        outState.putString(KEY, key);
        outState.putLong(ITEMS_COUNT, itemsCount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            tvShowCarNo.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_NO));
            tvShowCarType.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_TYPE));
            tvShowCountSeats.setText(savedInstanceState.getString(TunTravel.Car.Keys.COUNT_SEATS));
            tvShowCarOwnerName.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_NAME));
            tvShowCarOwnerPhNo.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_PH_NUM));
            tvShowCarOwnerAddress.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_ADDRESS));
            tvShowCarOwnerNRC.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_OWNER_NRC));
            tvShowCarCurrentLine.setText(savedInstanceState.getString(TunTravel.Car.Keys.CURRENT_LINE));
            tvShowCarRemark.setText(savedInstanceState.getString(TunTravel.Car.Keys.CAR_REMARK));
            this.key = savedInstanceState.getString(KEY);
            this.itemsCount = savedInstanceState.getLong(ITEMS_COUNT);
            this.imageFolder = savedInstanceState.getString(TunTravel.Car.Keys.CAR_NO);
        } else {
            carLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.car_show, container, false);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            if (getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                rcImageCar = (RecyclerView) v.findViewById(R.id.rcImageCar);
            }
        }

        tvShowCarNo = (TextView) v.findViewById(R.id.tvShowCarNo);
        tvShowCarType = (TextView) v.findViewById(R.id.tvShowCarType);
        tvShowCountSeats = (TextView) v.findViewById(R.id.tvShowCountSeats);
        tvShowCarOwnerName = (TextView) v.findViewById(R.id.tvShowCarOwnerName);
        tvShowCarOwnerPhNo = (TextView) v.findViewById(R.id.tvShowCarOwnerPhNo);
        tvShowCarOwnerAddress = (TextView) v.findViewById(R.id.tvShowCarOwnerAddress);
        tvShowCarOwnerNRC = (TextView) v.findViewById(R.id.tvShowCarOwnerNRC);
        tvShowCarCurrentLine = (TextView) v.findViewById(R.id.tvShowCarCurrentLine);
        tvShowCarRemark = (TextView) v.findViewById(R.id.tvShowCarRemark);
        btnShowCarEdit = (Button) v.findViewById(R.id.btnShowCarEdit);
        btnShowCarDelete = (Button) v.findViewById(R.id.btnShowCarDelete);
        btnShowCarCancel = (Button) v.findViewById(R.id.btnShowCarCancel);
        carLoadingLayout = (LinearLayout) v.findViewById(R.id.carLoadingLayout);
        ivCar = (ImageView) v.findViewById(R.id.iv);
        ivCarTwo = (ImageView) v.findViewById(R.id.iv2);
        ivCarThree = (ImageView) v.findViewById(R.id.iv3);
        ivCarFour = (ImageView) v.findViewById(R.id.iv4);
        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShowCarDelete.setEnabled(false);
        btnShowCarEdit.setOnClickListener(this);
        btnShowCarDelete.setOnClickListener(this);
        btnShowCarCancel.setOnClickListener(this);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CarModel carModel = dataSnapshot.getValue(CarModel.class);
                    tvShowCarNo.setText(carModel.getCarNo());
                    tvShowCarType.setText(carModel.getCarType());
                    tvShowCountSeats.setText(carModel.getCountSeats());
                    tvShowCarOwnerName.setText(carModel.getCarOwnerName());
                    tvShowCarOwnerPhNo.setText(carModel.getCarOwnerPhNo());
                    tvShowCarOwnerAddress.setText(carModel.getCarOwnerAddress());
                    tvShowCarOwnerNRC.setText(carModel.getCarOwnerNRC());
                    tvShowCarCurrentLine.setText(carModel.getCurrentLine());
                    tvShowCarRemark.setText(carModel.getCarRemark());
                    carLoadingLayout.setVisibility(View.VISIBLE);
                    imageFolder = carModel.getCarNo();
                    listImageFiles();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS);
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
        downloadURLS = new ArrayList<>();
        isGetImageOne = false;
        isGetImageTwo = false;
        isGetImageThree = false;
        isGetImageFour = false;
        storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES);
        if (imageFolder == null) {
            databaseReference.child(key).addValueEventListener(valueEventListener);
        } else {
            storageReference.child(imageFolder).child(TunTravel.Car.Image_Names.IMAGE_ONE).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    isGetImageOne = true;
                    tempURLS[0] = TunTravel.Car.Image_Names.IMAGE_ONE;
                    adaptAndDownload();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    isGetImageOne = true;
                    tempURLS[0] = null;
                    adaptAndDownload();
                }
            });
            storageReference.child(imageFolder).child(TunTravel.Car.Image_Names.IMAGE_TWO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    isGetImageTwo = true;
                    tempURLS[1] = TunTravel.Car.Image_Names.IMAGE_TWO;
                    adaptAndDownload();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tempURLS[1] = null;
                    isGetImageTwo = true;
                    adaptAndDownload();
                }
            });
            storageReference.child(imageFolder).child(TunTravel.Car.Image_Names.IMAGE_THREE).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    isGetImageThree = true;
                    tempURLS[2] = TunTravel.Car.Image_Names.IMAGE_THREE;
                    adaptAndDownload();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tempURLS[2] = null;
                    isGetImageThree = true;
                    adaptAndDownload();
                }
            });
            storageReference.child(imageFolder).child(TunTravel.Car.Image_Names.IMAGE_FOUR).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    isGetImageFour = true;
                    tempURLS[3] = TunTravel.Car.Image_Names.IMAGE_FOUR;
                    adaptAndDownload();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tempURLS[3] = null;
                    isGetImageFour = true;
                    adaptAndDownload();
                }
            });
        }
    }

    private void adaptAndDownload() {
        if (isGetImageOne && isGetImageTwo && isGetImageThree && isGetImageFour) {
            Log.d("Download Size ", downloadURLS.size() + "");
            for (int i = 0; i < tempURLS.length; i++) {
                if (tempURLS[i] != null) {
                    downloadURLS.add(tempURLS[i]);
                    Log.d("URL", tempURLS[i] + "");
                }
            }
            btnShowCarDelete.setEnabled(true);
            Activity activity = getActivity();
            if (isAdded() && activity != null) {
                if (getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rcImageCar.setLayoutManager(layoutManager);
                    CarImagesAdapter adapter = new CarImagesAdapter(downloadURLS);
                    rcImageCar.setAdapter(adapter);
                } else {
                    ImageView[] ivArr = {ivCar, ivCarTwo, ivCarThree, ivCarFour};
                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES).child(imageFolder);
                    if (downloadURLS.size() == 1) {
                        ivCarTwo.setVisibility(View.GONE);
                        ivCarThree.setVisibility(View.GONE);
                        ivCarFour.setVisibility(View.GONE);
                    }
                    if (downloadURLS.size() == 2) {
                        ivCarThree.setVisibility(View.GONE);
                        ivCarFour.setVisibility(View.GONE);
                    }
                    if (downloadURLS.size() == 3) {
                        ivCarFour.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < downloadURLS.size(); i++) {
                        Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference1.child(downloadURLS.get(i))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivArr[i]);
                    }
                }
            }
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
            case R.id.btnShowCarEdit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(AddActivity.FRAG_TYPE, AddActivity.CAR_FRAG);
                intent.putExtra(AddActivity.BEHAVIOR_TYPE, AddActivity.CAR_EDIT);
                intent.putExtra(AddActivity.KEY, key);
                intent.putStringArrayListExtra(CAR_IMAGES_ARR, downloadURLS);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnShowCarDelete:
                if (valueEventListener != null) {
                    databaseReference.child(key).removeEventListener(valueEventListener);
                }
                databaseReference.child(key).removeValue();
                databaseReference2 = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS_COUNTS);
                databaseReference2.setValue(itemsCount - 1);
                if (storageReference == null) {
                    storageReference = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES);
                }
                for (int i = 0; i < downloadURLS.size(); i++) {
                    storageReference.child(imageFolder).child(downloadURLS.get(i)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
                getActivity().finish();
                break;
            case R.id.btnShowCarCancel:
                getActivity().finish();
                break;
        }
    }

    private class CarImagesAdapter extends RecyclerView.Adapter<CarImagesAdapter.MyHolder> {

        ArrayList<String> vList = new ArrayList<>();

        public CarImagesAdapter(ArrayList<String> vList) {
            this.vList = vList;
            for (int i = 0; i < vList.size(); i++) {
                Log.d("URL", vList.get(i));
            }
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.card_view, null);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(TunTravel.Car.CARS_IMAGES).child(imageFolder).child(vList.get(position));
            Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference1).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.iv);
        }

        @Override
        public int getItemCount() {
            return vList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            ImageView iv;

            public MyHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.iv);
            }
        }
    }
}
