package and.htetarkarzaw.tuntravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import and.htetarkarzaw.tuntravel.Add_Activity.AddActivity;
import and.htetarkarzaw.tuntravel.Custom_fonts.MyButton;
import and.htetarkarzaw.tuntravel.List_Activity.ListActivity;

/**
 * Created by Zinmin2K on 7/4/2017.
 */

public class Grid_View extends AppCompatActivity {
    TextView trip, car, conductor, driver, guide, developer;
    MyButton setting, logout;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);
        getSupportActionBar().hide();
        trip = (TextView) findViewById(R.id.btnTrip);
        car = (TextView) findViewById(R.id.btnCar);
        conductor = (TextView) findViewById(R.id.btnConductor);
        driver = (TextView) findViewById(R.id.btnDriver);
        guide = (TextView) findViewById(R.id.btnGuide);
        setting = (MyButton) findViewById(R.id.btnSetting);
        logout = (MyButton) findViewById(R.id.btnLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(Grid_View.this, "Are You Sure ");
            }
        });

    }

    public void GridView(View v) {
        switch (v.getId()) {
            case R.id.btnTrip:
                Intent trip = new Intent(Grid_View.this, ListActivity.class);
                trip.putExtra(AddActivity.FRAG_TYPE, ListActivity.TRIP_FRAG);
                startActivity(trip);
                break;
            case R.id.btnCar:
                Intent car = new Intent(Grid_View.this, ListActivity.class);
                car.putExtra(AddActivity.FRAG_TYPE, ListActivity.CAR_FRAG);
                startActivity(car);
                break;
            case R.id.btnConductor:
                Intent conductor = new Intent(Grid_View.this, ListActivity.class);
                conductor.putExtra(AddActivity.FRAG_TYPE, ListActivity.CONDUCTOR_FRAG);
                startActivity(conductor);
                break;
            case R.id.btnDriver:
                Intent driver = new Intent(Grid_View.this, ListActivity.class);
                driver.putExtra(AddActivity.FRAG_TYPE, ListActivity.DRIVER_FRAG);
                startActivity(driver);
                break;
            case R.id.btnGuide:
                Intent guide = new Intent(Grid_View.this, ListActivity.class);
                guide.putExtra(AddActivity.FRAG_TYPE, ListActivity.GUIDE_FRAG);
                startActivity(guide);
                break;
            case R.id.btnPassenger:
                Intent passenger = new Intent(Grid_View.this, ListActivity.class);
                passenger.putExtra(AddActivity.FRAG_TYPE, ListActivity.PASSENGER_FRAG);
                startActivity(passenger);

                break;

            case R.id.btnSetting:

                Intent setting = new Intent(Grid_View.this, ListActivity.class);
                setting.putExtra(AddActivity.FRAG_TYPE, ListActivity.USER_FRAG);
                startActivity(setting);
                break;


        }


    }
}
