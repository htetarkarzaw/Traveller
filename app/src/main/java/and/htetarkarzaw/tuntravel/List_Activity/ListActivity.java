package and.htetarkarzaw.tuntravel.List_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.Show_Activity.CarShow;
import and.htetarkarzaw.tuntravel.Show_Activity.ConductorShow;
import and.htetarkarzaw.tuntravel.Show_Activity.DriverShow;
import and.htetarkarzaw.tuntravel.Show_Activity.GuideShow;
import and.htetarkarzaw.tuntravel.UserInfo.UserInfo;

/**
 * Created by Htet Arkar Zaw on 7/4/2017.
 */

public class ListActivity extends AppCompatActivity {

    RelativeLayout FragHolder;
    FragmentManager fm;
    private String type;
    private Car car;
    private Conductor conductor;
    private Driver driver;
    private Guide guide;
    private Trip trip;
    private Passenger passenger;
    private UserInfo userInfo;
    public static final String FRAG_TYPE = "fragType";
    public static final String CAR_FRAG = "carFrag";
    public static final String CONDUCTOR_FRAG = "conductorFrag";
    public static final String DRIVER_FRAG = "driverFrag";
    public static final String GUIDE_FRAG = "guideFrag";
    public static final String TRIP_FRAG="tripFrag";
    public static final String PASSENGER_FRAG="passengerFrag";
    public static final String USER_FRAG="userFrag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        getSupportActionBar().hide();
        FragHolder= (RelativeLayout) findViewById(R.id.FragHolder);
        fm=getSupportFragmentManager();
        type=getIntent().getStringExtra(FRAG_TYPE);
        switch (type){
            case TRIP_FRAG:
                if( fm.findFragmentByTag(TRIP_FRAG)==null ){
                    trip = new Trip();
                    fm.beginTransaction().add(R.id.FragHolder, trip, TRIP_FRAG).commit();
                }else{
                    trip = (Trip) fm.findFragmentByTag(TRIP_FRAG);
                }
                break;
            case CAR_FRAG:
                if( fm.findFragmentByTag(CAR_FRAG)==null ){
                    car = new Car();
                    fm.beginTransaction().add(R.id.FragHolder, car, CAR_FRAG).commit();
                }else{
                    car = (Car) fm.findFragmentByTag(CAR_FRAG);
                }
                break;

            case CONDUCTOR_FRAG:
                if( fm.findFragmentByTag(CONDUCTOR_FRAG)==null ){
                    conductor = new Conductor();
                    fm.beginTransaction().add(R.id.FragHolder, conductor, CONDUCTOR_FRAG).commit();
                }else{
                    conductor = (Conductor) fm.findFragmentByTag(CONDUCTOR_FRAG);
                }
                break;
            case DRIVER_FRAG:
                if( fm.findFragmentByTag(DRIVER_FRAG)==null ){
                    driver = new Driver();
                    fm.beginTransaction().add(R.id.FragHolder, driver, DRIVER_FRAG).commit();
                }else{
                    driver = (Driver) fm.findFragmentByTag(DRIVER_FRAG);
                }
                break;
            case GUIDE_FRAG:
                if( fm.findFragmentByTag(GUIDE_FRAG)==null ){
                    guide = new Guide();
                    fm.beginTransaction().add(R.id.FragHolder, guide, GUIDE_FRAG).commit();
                }else{
                    guide = (Guide) fm.findFragmentByTag(GUIDE_FRAG);
                }
                break;

            case PASSENGER_FRAG:
                if( fm.findFragmentByTag(PASSENGER_FRAG)==null ){
                    passenger = new Passenger();
                    fm.beginTransaction().add(R.id.FragHolder, passenger, PASSENGER_FRAG).commit();
                }else{
                    passenger = (Passenger) fm.findFragmentByTag(PASSENGER_FRAG);
                }
                break;
            case USER_FRAG:
                if( fm.findFragmentByTag(PASSENGER_FRAG)==null ){
                    userInfo = new UserInfo();
                    fm.beginTransaction().add(R.id.FragHolder, userInfo, USER_FRAG).commit();
                }else{
                    userInfo = (UserInfo) fm.findFragmentByTag(USER_FRAG);
                }
                break;
        }
    }
}
