package and.htetarkarzaw.tuntravel.Show_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import and.htetarkarzaw.tuntravel.R;
/**
 * Created by Htet Arkar Zaw on 6/19/2017.
 */

public class ShowActivity extends AppCompatActivity  {
    public static String PASSENGER_RC_SHOW_FRAG = "passengerRcShowFrag";
    RelativeLayout FragHolder;
    FragmentManager fm;
    private String type;
    private String key = null,passengerKey = null;
    private long itemsCount = 0;
    private CarShow carShow;
    private ConductorShow conductorShow;
    private DriverShow driverShow;
    private GuideShow guideShow;
    private TripShow tripShow;
    private PassengerShow passengerShow;
    private PassengerListShow passengerListShow;
    public static final String FRAG_TYPE = "fragType";
    public static final String CAR_SHOW_FRAG = "carShowFrag";
    public static final String CONDUCTOR_SHOW_FRAG = "conductorShowFrag";
    public static final String DRIVER_SHOW_FRAG = "driverShowFrag";
    public static final String GUIDE_SHOW_FRAG = "guideShowFrag";
    public static final String TRIP_SHOW_FRAG = "tripShowFrag";
    public static final String KEY = "key";
    public static final String ITEMS_COUNT = "itemsCount";
    public static final String PASSENGER_LIST_SHOW_FRAG = "passengerListShow";
    public static final String PASSENGER_SHOW_FRAG = "passengerShowFrag";
    public static final String PASSENGER_KEY = "passengerKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_main);
        getSupportActionBar().hide();
        FragHolder= (RelativeLayout) findViewById(R.id.FragHolder);
        fm=getSupportFragmentManager();
        type=getIntent().getStringExtra(FRAG_TYPE);
        key = getIntent().getStringExtra(KEY);
        itemsCount = getIntent().getLongExtra(ITEMS_COUNT,0);
        passengerKey = getIntent().getStringExtra(PASSENGER_KEY);
        switch (type){
            case CAR_SHOW_FRAG:
                if( fm.findFragmentByTag(CAR_SHOW_FRAG)==null ){
                    carShow = new CarShow();
                    carShow.setKey(key,itemsCount);
                    fm.beginTransaction().add(R.id.FragHolder, carShow, CAR_SHOW_FRAG).commit();
                }else{
                    carShow = (CarShow) fm.findFragmentByTag(CAR_SHOW_FRAG);
                    carShow.setKey(key,itemsCount);
                }
                break;
            case CONDUCTOR_SHOW_FRAG:
                if( fm.findFragmentByTag(CONDUCTOR_SHOW_FRAG)==null ){
                    conductorShow = new ConductorShow();
                    conductorShow.setKey(key,itemsCount);
                    fm.beginTransaction().add(R.id.FragHolder, conductorShow, CONDUCTOR_SHOW_FRAG).commit();
                }else{
                    conductorShow = (ConductorShow) fm.findFragmentByTag(CONDUCTOR_SHOW_FRAG);
                    conductorShow.setKey(key,itemsCount);
                }
                break;
            case DRIVER_SHOW_FRAG:
                if( fm.findFragmentByTag(DRIVER_SHOW_FRAG)==null ){
                    driverShow = new DriverShow();
                    driverShow.setKey(key,itemsCount);
                    fm.beginTransaction().add(R.id.FragHolder, driverShow, DRIVER_SHOW_FRAG).commit();
                }else{
                    driverShow = (DriverShow) fm.findFragmentByTag(DRIVER_SHOW_FRAG);
                    driverShow.setKey(key,itemsCount);
                }
                break;
            case GUIDE_SHOW_FRAG:
                if( fm.findFragmentByTag(GUIDE_SHOW_FRAG)==null ){
                    guideShow = new GuideShow();
                    guideShow.setKey(key,itemsCount);
                    fm.beginTransaction().add(R.id.FragHolder, guideShow, GUIDE_SHOW_FRAG).commit();
                }else{
                    guideShow = (GuideShow) fm.findFragmentByTag(GUIDE_SHOW_FRAG);
                    guideShow.setKey(key,itemsCount);
                }
                break;
            case TRIP_SHOW_FRAG:
                if( fm.findFragmentByTag(TRIP_SHOW_FRAG)==null ){
                    tripShow = new TripShow();
                    tripShow.setKey(key,itemsCount);
                    fm.beginTransaction().add(R.id.FragHolder, tripShow, TRIP_SHOW_FRAG).commit();
                }else{
                    tripShow = (TripShow) fm.findFragmentByTag(TRIP_SHOW_FRAG);
                    tripShow.setKey(key,itemsCount);
                }
                break;
            case PASSENGER_LIST_SHOW_FRAG:
                if( fm.findFragmentByTag(PASSENGER_LIST_SHOW_FRAG)==null ){
                    passengerListShow = new PassengerListShow();
                    passengerListShow.setKey(key);
                    fm.beginTransaction().add(R.id.FragHolder, passengerListShow, PASSENGER_LIST_SHOW_FRAG).commit();
                }else{
                    passengerListShow = (PassengerListShow) fm.findFragmentByTag(PASSENGER_LIST_SHOW_FRAG);
                    passengerListShow.setKey(key);
                }
                break;
            case PASSENGER_SHOW_FRAG:
                if( fm.findFragmentByTag(PASSENGER_LIST_SHOW_FRAG)==null ){
                    passengerShow = new PassengerShow();
                    passengerShow.setKey(key,passengerKey);
                    fm.beginTransaction().add(R.id.FragHolder, passengerShow, PASSENGER_SHOW_FRAG).commit();
                }else{
                    passengerShow = (PassengerShow) fm.findFragmentByTag(PASSENGER_SHOW_FRAG);
                    passengerShow.setKey(key,passengerKey);
                }
                break;
        }
    }
}
