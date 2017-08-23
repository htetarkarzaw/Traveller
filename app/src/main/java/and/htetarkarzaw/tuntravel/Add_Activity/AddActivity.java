package and.htetarkarzaw.tuntravel.Add_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import and.htetarkarzaw.tuntravel.Model.PassengerModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.CarShow;

/**
 * Created by Htet Arkar Zaw on 6/18/2017.
 */

public class AddActivity extends AppCompatActivity {
    private String frag_type;
    private String behavior_type;
    private RelativeLayout FragHolder;
    private FragmentManager fm;
    private String key;
    private String imageName;
    private ArrayList<String> carImagesArr;
    public static final String KEY = "key";
    public static final String IMAGE_NAME = "imageName";
    public static final String CAR_FRAG = "carAdd";
    public static final String DRIVER_FRAG = "driverAdd";
    public static final String CONDUCTOR_FRAG = "conductorAdd";
    public static final String GUIDE_FRAG = "guideAdd";
    public static final String CAR_EDIT = "carEdit";
    public static final String DRIVER_EDIT = "driverEdit";
    public static final String CONDUCTOR_EDIT = "conductorEdit";
    public static final String GUIDE_EDIT = "guideEdit";
    public static final String FRAG_TYPE = "fragType";
    public static final String BEHAVIOR_TYPE = "behavior_type";
    public static final String REGULAR_TRIP_FRAG = "regularTripAdd";
    public static final String ORDER_TRIP_FRAG = "orderTripAdd";
    public static final String REGULAR_TRIP_EDIT = "regularTripEdit";
    public static final String ORDER_TRIP_EDIT = "orderTripEdit";
    public static final String SEAT_INFO="seatInfo";
    public static final String PASSENGER_ADD = "passengerAdd";
    public static final String SEAT_NO="seatNo";
    public static final String PASSENGER_MODEL="passengerModel";
    public static final String PASSENGER_LIST="passengerList";
    private CarInfo carInfo;
    private ConductorInfo conInfo;
    private DriverInfo driverInfo;
    private GuideInfo guideInfo;
    private RegularTripInfo regularTripInfo;
    private OrderTripInfo orderTripInfo;
    private SeatInfo seatInfo;
    private PassengerInfo passengerInfo;
    private int temp;
    PassengerModel passengerModel=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_main);
        FragHolder = (RelativeLayout) findViewById(R.id.FragHolder);
        fm = getSupportFragmentManager();
        frag_type = getIntent().getStringExtra(FRAG_TYPE);
        behavior_type = getIntent().getStringExtra(BEHAVIOR_TYPE);
        key = getIntent().getStringExtra(KEY);
        imageName = getIntent().getStringExtra(IMAGE_NAME);
        temp=getIntent().getIntExtra(SEAT_NO,0);
        carImagesArr = getIntent().getStringArrayListExtra(CarShow.CAR_IMAGES_ARR);
        switch (frag_type) {
            case CAR_FRAG:
                if (fm.findFragmentByTag(CAR_FRAG) == null) {
                    carInfo = new CarInfo();
                    fm.beginTransaction().add(R.id.FragHolder, carInfo,CAR_FRAG).commit();
                    if (behavior_type != null) {
                        carInfo.setKey(key,carImagesArr);
                    }
                }else{
                    carInfo = (CarInfo) fm.findFragmentByTag(CAR_FRAG);
                    if (behavior_type != null) {
                        carInfo.setKey(key,carImagesArr);
                    }
                }
                break;
            case DRIVER_FRAG:
                if (fm.findFragmentByTag(DRIVER_FRAG) == null) {
                    driverInfo = new DriverInfo();
                    fm.beginTransaction().add(R.id.FragHolder, driverInfo,DRIVER_FRAG).commit();
                    if (behavior_type != null) {
                        driverInfo.setKey(key,imageName);
                    }
                }else{
                    driverInfo = (DriverInfo) fm.findFragmentByTag(DRIVER_FRAG);
                    if (behavior_type != null) {
                        driverInfo.setKey(key,imageName);
                    }
                }
                break;

            case CONDUCTOR_FRAG:
                if (fm.findFragmentByTag(CONDUCTOR_FRAG) == null) {
                    conInfo = new ConductorInfo();
                    fm.beginTransaction().add(R.id.FragHolder, conInfo,CONDUCTOR_FRAG).commit();
                    if (behavior_type != null) {
                        conInfo.setKey(key,imageName);
                    }
                }else{
                    conInfo = (ConductorInfo) fm.findFragmentByTag(CONDUCTOR_FRAG);
                    if (behavior_type != null) {
                        conInfo.setKey(key,imageName);
                    }
                }
                break;

            case GUIDE_FRAG:
                if (fm.findFragmentByTag(GUIDE_FRAG) == null) {
                    guideInfo = new GuideInfo();
                    fm.beginTransaction().add(R.id.FragHolder, guideInfo,GUIDE_FRAG).commit();
                    if (behavior_type != null) {
                        guideInfo.setKey(key,imageName);
                    }
                }else{
                    guideInfo = (GuideInfo) fm.findFragmentByTag(GUIDE_FRAG);
                    if (behavior_type != null) {
                        conInfo.setKey(key,imageName);
                    }
                }
                break;
            case REGULAR_TRIP_FRAG:
                if (fm.findFragmentByTag(REGULAR_TRIP_FRAG) == null) {
                    regularTripInfo = new RegularTripInfo();
                    fm.beginTransaction().add(R.id.FragHolder, regularTripInfo, REGULAR_TRIP_FRAG).commit();
                    if(behavior_type != null){
                        regularTripInfo.setKey(key);
                    }
                }else{
                    regularTripInfo = (RegularTripInfo) fm.findFragmentByTag(REGULAR_TRIP_FRAG);
                    if(behavior_type != null){
                        regularTripInfo.setKey(key);
                    }
                }
                break;
            case ORDER_TRIP_FRAG:
                if (fm.findFragmentByTag(ORDER_TRIP_FRAG) == null) {
                    orderTripInfo = new OrderTripInfo();
                    fm.beginTransaction().add(R.id.FragHolder, orderTripInfo, ORDER_TRIP_FRAG).commit();
                    if(behavior_type != null){
                        orderTripInfo.setKey(key);
                    }
                }else{
                    orderTripInfo = (OrderTripInfo) fm.findFragmentByTag(ORDER_TRIP_FRAG);
                    if(behavior_type != null){
                        orderTripInfo.setKey(key);
                    }
                }
                break;
            case PASSENGER_ADD:
                if (fm.findFragmentByTag(PASSENGER_ADD) == null) {
                    passengerInfo = new PassengerInfo();
                    passengerInfo.setPosition(temp);
                    fm.beginTransaction().add(R.id.FragHolder, passengerInfo, PASSENGER_ADD).commit();

                }
                break;

            case SEAT_INFO:
                if (fm.findFragmentByTag(SEAT_INFO) == null) {
                    seatInfo = new SeatInfo();
                    seatInfo.getPassengerModel(passengerModel);
                    fm.beginTransaction().add(R.id.FragHolder, seatInfo, SEAT_INFO).commit();
                }
                break;
        }

    }
}
