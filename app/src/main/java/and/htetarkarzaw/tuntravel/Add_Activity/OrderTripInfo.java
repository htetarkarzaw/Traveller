package and.htetarkarzaw.tuntravel.Add_Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import and.htetarkarzaw.tuntravel.Model.CarModel;
import and.htetarkarzaw.tuntravel.Model.ConductorModel;
import and.htetarkarzaw.tuntravel.Model.DriverModel;
import and.htetarkarzaw.tuntravel.Model.ExistingKeyModelForTrip;
import and.htetarkarzaw.tuntravel.Model.GuideModel;
import and.htetarkarzaw.tuntravel.Model.ModelForSpinner;
import and.htetarkarzaw.tuntravel.Model.TripModel;
import and.htetarkarzaw.tuntravel.R;
import and.htetarkarzaw.tuntravel.TunTravel;

/**
 * Created by Htet Arkar Zaw on 7/3/2017.
 */

public class OrderTripInfo extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageButton startPickerButton, endPickerButton;
    private DatePickerDialog pickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Spinner carSpinner, driverSpinner, conductor1Spinner, conductor2Spinner, guideSpinner;
    private SpinnerAdapter carSpinnerAdapter, driverSpinnerAdapter, conductor1SpinnerAdapter,
            conductor2SpinnerAdapter, guideSpinnerAdapter;
    private EditText etClientName,etClientPhNum,etClientCompany,etTripName, etStartDate, etEndDate, etRenderCharge, etRoadFee, etCostForFood,
            etGeneralExpense, etDebit, etProfit;
    private String strTripName, strStartDate, strEndDate, strRenderCharge, strRoadFee, strCostForFood,
            strGeneralExpense, strDebit,strCarKey, strConductor1Key, strConductor2Key, strDriverKey,strGuideKey, strProfit,strClientName,strClientPhNum,strClientCompany;
    private LinearLayout spinnerLayout;
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String TRIP_TYPE = "orderTrip";
    private static final String IS_DONE_START_DATE = "isDoneStartDate";
    private static final String IS_DONE_END_DATE = "isDoneEndDate";
    private static final int DATE_GREATER = 1;
    private static final int DATE_LESS = -1;
    private static final int DATE_EQUAL = 0;
    private DatabaseReference databaseReference;
    private boolean isDoneStartDate = false, isDoneEndDate = false;
    private DatabaseReference databaseReferenceForTripCounts, databaseReferenceForTripData, databaseReferenceForCars,
            databaseReferenceForDriver, databaseReferenceForGuide, databaseReferenceForConductor;
    private Button btnOrderInfoSave, btnOrderInfoCancel;
    private int tripCounts = 0;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private ArrayList<TripModel> tripModels = new ArrayList<>();
    private ValueEventListenerForTripData valueEventListenerForTripData;
    private ArrayList<ExistingKeyModelForTrip> existingKeyModelForTrips = new ArrayList<>();
    private ArrayList<ModelForSpinner> spinnerArrayListForCars = new ArrayList<>();
    private ArrayList<ModelForSpinner> spinnerArrayListForDrivers = new ArrayList<>();
    private ArrayList<ModelForSpinner> spinnerArrayListForConductor = new ArrayList<>();
    private ArrayList<ModelForSpinner> spinnerArrayListForGuides = new ArrayList<>();
    private boolean isDoneCarSpinner, isDoneDriverSpinner, isDoneGuideSpinner, isDoneConductorSpinner;
    private int selectCarSpinner = 0,selectDriverSpinner = 0,selectConductor1Spinner = 0,selectConductor2Spinner = 0,selectGuideSpinner = 0;
    private TripModel tripModel;
    private String key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_trip_info, container, false);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy-hh:mm");
        etTripName = (EditText) v.findViewById(R.id.etTripName);
        etClientName = (EditText) v.findViewById(R.id.etClientName);
        etClientCompany = (EditText) v.findViewById(R.id.etClientCompany);
        etClientPhNum = (EditText) v.findViewById(R.id.etClientPh);
        etRenderCharge = (EditText) v.findViewById(R.id.etRenderCharge);
        etRoadFee = (EditText) v.findViewById(R.id.etRoadFee);
        etCostForFood = (EditText) v.findViewById(R.id.etCostForFood);
        etGeneralExpense = (EditText) v.findViewById(R.id.etGeneralExpense);
        etDebit = (EditText) v.findViewById(R.id.etDebit);
        etProfit = (EditText) v.findViewById(R.id.etProfit);
        etEndDate = (EditText) v.findViewById(R.id.etEndDate);
        etStartDate = (EditText) v.findViewById(R.id.etStartDate);
        endPickerButton = (ImageButton) v.findViewById(R.id.endPickerButton);
        startPickerButton = (ImageButton) v.findViewById(R.id.startPickerButton);
        carSpinner = (Spinner) v.findViewById(R.id.carSpinner);
        driverSpinner = (Spinner) v.findViewById(R.id.driverSpinner);
        conductor1Spinner = (Spinner) v.findViewById(R.id.conductor1Spinner);
        conductor2Spinner = (Spinner) v.findViewById(R.id.conductor2Spinner);
        guideSpinner = (Spinner) v.findViewById(R.id.guideSpinner);
        spinnerLayout = (LinearLayout) v.findViewById(R.id.spinnerLayout);
        btnOrderInfoSave = (Button) v.findViewById(R.id.btnOrderTripInfoSave);
        btnOrderInfoCancel = (Button) v.findViewById(R.id.btnOrderTripInfoCancel);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDbReference();
    }

    private void initDbReference() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIPS);
        }
        if (databaseReferenceForTripCounts == null) {
            databaseReferenceForTripCounts = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIP_COUNTS);
        }

        if (databaseReferenceForTripData == null) {
            databaseReferenceForTripData = FirebaseDatabase.getInstance().getReference(TunTravel.Trip.TRIPS);
        }

        if (valueEventListenerForTripData == null) {
            valueEventListenerForTripData = new ValueEventListenerForTripData();
        }

        if (databaseReferenceForCars == null) {
            databaseReferenceForCars = FirebaseDatabase.getInstance().getReference(TunTravel.Car.CARS);
        }

        if (databaseReferenceForConductor == null) {
            databaseReferenceForConductor = FirebaseDatabase.getInstance().getReference(TunTravel.Conductor.CONDUCTORS);
        }

        if (databaseReferenceForDriver == null) {
            databaseReferenceForDriver = FirebaseDatabase.getInstance().getReference(TunTravel.Driver.DRIVERS);
        }

        if (databaseReferenceForGuide == null) {
            databaseReferenceForGuide = FirebaseDatabase.getInstance().getReference(TunTravel.Guides.GUIDES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TunTravel.Trip.Keys.TRIP_TYPE,TRIP_TYPE);
        outState.putString(TunTravel.Trip.Keys.CLIENT_NAME,etClientName.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CLIENT_PH_NUM,etClientPhNum.getText().toString());
        outState.putString(TunTravel.Trip.Keys.CLIENT_COMPANY,etClientCompany.getText().toString());
        outState.putString(TunTravel.Trip.Keys.TRIP_NAME,etTripName.getText().toString());
        outState.putLong(TunTravel.Trip.Keys.START_DATE,startDate.getTimeInMillis());
        outState.putLong(TunTravel.Trip.Keys.END_DATE,endDate.getTimeInMillis());
        outState.putString(TunTravel.Trip.Keys.RENDER_CHARGE,etRenderCharge.getText().toString());
        outState.putString(TunTravel.Trip.Keys.ROAD_FEE,etRoadFee.getText().toString());
        outState.putString(TunTravel.Trip.Keys.COST_FOR_FOOD,etCostForFood.getText().toString());
        outState.putString(TunTravel.Trip.Keys.GENERAL_EXPENSE,etGeneralExpense.getText().toString());
        outState.putString(TunTravel.Trip.Keys.DEBIT,etDebit.getText().toString());
        outState.putString(TunTravel.Trip.Keys.PROFIT,etProfit.getText().toString());
        outState.putBoolean(IS_DONE_START_DATE,isDoneStartDate);
        outState.putBoolean(IS_DONE_END_DATE,isDoneEndDate);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            initDbReference();
            etClientName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_NAME));
            etClientPhNum.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_PH_NUM));
            etClientCompany.setText(savedInstanceState.getString(TunTravel.Trip.Keys.CLIENT_COMPANY));
            etTripName.setText(savedInstanceState.getString(TunTravel.Trip.Keys.TRIP_NAME));
            etRenderCharge.setText(savedInstanceState.getString(TunTravel.Trip.Keys.RENDER_CHARGE));
            etRoadFee.setText(savedInstanceState.getString(TunTravel.Trip.Keys.ROAD_FEE));
            etCostForFood.setText(savedInstanceState.getString(TunTravel.Trip.Keys.COST_FOR_FOOD));
            etGeneralExpense.setText(savedInstanceState.getString(TunTravel.Trip.Keys.GENERAL_EXPENSE));
            etDebit.setText(savedInstanceState.getString(TunTravel.Trip.Keys.DEBIT));
            etProfit.setText(savedInstanceState.getString(TunTravel.Trip.Keys.PROFIT));
            isDoneStartDate = savedInstanceState.getBoolean(IS_DONE_START_DATE,false);
            isDoneEndDate = savedInstanceState.getBoolean(IS_DONE_END_DATE,false);
            if(isDoneStartDate){
                long startDateMilli = savedInstanceState.getLong(TunTravel.Trip.Keys.START_DATE);
                Date date = new Date(startDateMilli);
                startDate.setTime(date);
                int hours = date.getHours();
                String ampm;
                if (hours > 12) {
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                etStartDate.setText(dateFormatter.format(date)+" "+ampm);
            }
            if(isDoneEndDate){
                long endDateMilli = savedInstanceState.getLong(TunTravel.Trip.Keys.END_DATE);
                Date date = new Date(endDateMilli);
                endDate.setTime(date);
                int hours = date.getHours();
                String ampm;
                if (hours > 12) {
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                etEndDate.setText(dateFormatter.format(date)+" "+ampm);
            }
        }
        if(isDoneStartDate && isDoneEndDate){
            startLoadingSpinners();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerLayout.setVisibility(View.GONE);
        startPickerButton.setOnClickListener(this);
        endPickerButton.setOnClickListener(this);
        btnOrderInfoSave.setOnClickListener(this);
        btnOrderInfoCancel.setOnClickListener(this);
        carSpinner.setOnItemSelectedListener(this);
        conductor1Spinner.setOnItemSelectedListener(this);
        conductor2Spinner.setOnItemSelectedListener(this);
        guideSpinner.setOnItemSelectedListener(this);
        driverSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startPickerButton:
                dateTimePicker(startDate, etStartDate, START_DATE);
                break;
            case R.id.endPickerButton:
                dateTimePicker(endDate, etEndDate, END_DATE);
                break;
            case R.id.btnOrderTripInfoSave:
                if (!checkValuesIfNotNull()) {
                    saveDatasToServer();
                }else{
                    Snackbar.make(this.getView(),"Required Fields To Enter",Snackbar.LENGTH_SHORT);
                }
                break;
            case R.id.btnOrderTripInfoCancel:
                getActivity().finish();
                break;
        }
    }

    private boolean checkValuesIfNotNull() {
        if(tripModel != null){
            tripModel = null;
        }
        tripModel = new TripModel();
        boolean notNull = false;
        strTripName = etTripName.getText().toString();
        strStartDate = etStartDate.getText().toString();
        strEndDate = etEndDate.getText().toString();
        strRenderCharge = etRenderCharge.getText().toString();
        strRoadFee = etRoadFee.getText().toString();
        strCostForFood = etCostForFood.getText().toString();
        strGeneralExpense = etGeneralExpense.getText().toString();
        strDebit = etDebit.getText().toString();
        strProfit = etProfit.getText().toString();
        strClientName = etClientName.getText().toString();
        strClientCompany = etClientCompany.getText().toString();
        strClientPhNum = etClientPhNum.getText().toString();
        tripModel.setClientName(strClientName);
        tripModel.setClientPhNo(strClientPhNum);
        tripModel.setClientCompany(strClientCompany);
        tripModel.setTripType(TRIP_TYPE);
        tripModel.setRenderCharge(strRenderCharge);
        tripModel.setRoadFee(strRoadFee);
        tripModel.setCostForFood(strCostForFood);
        tripModel.setGeneralExpense(strGeneralExpense);
        tripModel.setDebit(strDebit);
        tripModel.setProfit(strProfit);
        if(selectCarSpinner != 0){
            tripModel.setCarNo(spinnerArrayListForCars.get(selectCarSpinner).getFirst());
            tripModel.setCarKey(spinnerArrayListForCars.get(selectCarSpinner).getKey());
        }else{
            tripModel.setCarNo("");
            tripModel.setCarKey("");
        }
        if(selectGuideSpinner != 0 ){
            tripModel.setGuideName(spinnerArrayListForGuides.get(selectGuideSpinner).getFirst());
            tripModel.setGuideKey(spinnerArrayListForGuides.get(selectGuideSpinner).getKey());
        }else{
            tripModel.setGuideName("");
            tripModel.setGuideKey("");
        }
        if(selectConductor1Spinner != 0){
            tripModel.setConductorName1(spinnerArrayListForConductor.get(selectConductor1Spinner).getFirst());
            tripModel.setConductor1Key(spinnerArrayListForConductor.get(selectConductor1Spinner).getKey());
        }else{
            tripModel.setConductorName1("");
            tripModel.setConductor1Key("");
        }
        if(selectConductor2Spinner != 0){
            tripModel.setConductorName2(spinnerArrayListForConductor.get(selectConductor2Spinner).getFirst());
            tripModel.setConductor2Key(spinnerArrayListForConductor.get(selectConductor2Spinner).getKey());

        }else{
            tripModel.setConductorName2("");
            tripModel.setConductor2Key("");
        }
        if(selectDriverSpinner != 0 ){
            tripModel.setDriverName(spinnerArrayListForDrivers.get(selectDriverSpinner).getFirst());
            tripModel.setDriverKey(spinnerArrayListForDrivers.get(selectDriverSpinner).getKey());

        }else{
            tripModel.setDriverName("");
            tripModel.setDriverKey("");
        }
        if(strTripName.equals("")){
            notNull = true;
            etTripName.setError("Required");
        }else{
            tripModel.setTripName(strTripName);
        }
        if (strStartDate.equals("")) {
            notNull = true;
            etStartDate.setError("Required");
        } else {
            tripModel.setStartDate(startDate.getTimeInMillis() + "");
            tripModel.setStrStartDate(etStartDate.getText().toString());
        }
        if (strEndDate.equals("")) {
            notNull = true;
            etEndDate.setError("Required");
        } else {
            tripModel.setEndDate(endDate.getTimeInMillis() + "");
            tripModel.setStrEndDate(etEndDate.getText().toString());
        }
        if(notNull){
            return true;
        }else{
            return false;
        }
    }

    private void saveDatasToServer() {
        if(key==null){
            databaseReferenceForTripCounts.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long tripCounts = 0;
                    if (dataSnapshot.getValue() != null) {
                        tripCounts = (long) dataSnapshot.getValue();
                    }

                    if (tripCounts != 0) {
                        databaseReferenceForTripCounts.setValue(tripCounts + 1);
                    } else if (tripCounts == 0) {
                        databaseReferenceForTripCounts.setValue(1);
                    }
                    String tripKey = databaseReferenceForTripData.push().getKey();
                    databaseReferenceForTripData.child(tripKey).setValue(tripModel);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Map<String,Object> tasks = new HashMap<>();
            tasks.put(TunTravel.Trip.Keys.TRIP_NAME,strTripName);
            tasks.put(TunTravel.Trip.Keys.STR_START_DATE,strStartDate);
            tasks.put(TunTravel.Trip.Keys.START_DATE,startDate.getTimeInMillis()+"");
            tasks.put(TunTravel.Trip.Keys.STR_END_DATE,strEndDate);
            tasks.put(TunTravel.Trip.Keys.END_DATE,endDate.getTimeInMillis()+"");
            tasks.put(TunTravel.Trip.Keys.RENDER_CHARGE,strRenderCharge);
            tasks.put(TunTravel.Trip.Keys.ROAD_FEE,strRoadFee);
            tasks.put(TunTravel.Trip.Keys.COST_FOR_FOOD,strCostForFood);
            tasks.put(TunTravel.Trip.Keys.GENERAL_EXPENSE,strGeneralExpense);
            tasks.put(TunTravel.Trip.Keys.DEBIT,strDebit);
            tasks.put(TunTravel.Trip.Keys.PROFIT,strProfit);
            tasks.put(TunTravel.Trip.Keys.CLIENT_NAME,strClientName);
            tasks.put(TunTravel.Trip.Keys.CLIENT_PH_NUM,strClientPhNum);
            tasks.put(TunTravel.Trip.Keys.CLIENT_COMPANY,strClientCompany);
            if (selectCarSpinner != 0) {
                tasks.put(TunTravel.Trip.Keys.CAR_NO,spinnerArrayListForCars.get(selectCarSpinner).getFirst());
                tasks.put(TunTravel.Trip.Keys.CAR_KEY,spinnerArrayListForCars.get(selectCarSpinner).getKey());
            } else {
                tasks.put(TunTravel.Trip.Keys.CAR_NO,"");
                tasks.put(TunTravel.Trip.Keys.CAR_KEY,"");
            }
            if (selectGuideSpinner != 0) {
                tasks.put(TunTravel.Trip.Keys.GUIDE_NAME,spinnerArrayListForGuides.get(selectGuideSpinner).getFirst());
                tasks.put(TunTravel.Trip.Keys.GUIDE_KEY,spinnerArrayListForGuides.get(selectGuideSpinner).getKey());
            } else {
                tasks.put(TunTravel.Trip.Keys.GUIDE_NAME,"");
                tasks.put(TunTravel.Trip.Keys.GUIDE_KEY,"");
            }
            if (selectConductor1Spinner != 0) {
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_NAME_1,spinnerArrayListForConductor.get(selectConductor1Spinner).getFirst());
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_KEY_1,spinnerArrayListForConductor.get(selectConductor1Spinner).getKey());
            } else {
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_NAME_1,"");
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_KEY_1,"");
            }
            if (selectConductor2Spinner != 0) {
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_NAME_2,spinnerArrayListForConductor.get(selectConductor2Spinner).getFirst());
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_KEY_2,spinnerArrayListForConductor.get(selectConductor2Spinner).getKey());
            } else {
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_NAME_2,"");
                tasks.put(TunTravel.Trip.Keys.CONDUCTOR_KEY_2,"");
            }
            if (selectDriverSpinner != 0) {
                tasks.put(TunTravel.Trip.Keys.DRIVER_NAME,spinnerArrayListForDrivers.get(selectDriverSpinner).getFirst());
                tasks.put(TunTravel.Trip.Keys.DRIVER_KEY,spinnerArrayListForDrivers.get(selectDriverSpinner).getKey());
            } else {
                tasks.put(TunTravel.Trip.Keys.DRIVER_NAME,"");
                tasks.put(TunTravel.Trip.Keys.DRIVER_KEY,"");
            }
            databaseReferenceForTripData.child(key).updateChildren(tasks);
        }
        getActivity().finish();
    }

    private void dateTimePicker(final Calendar calendarDate, EditText date, final String dateConst) {

        Calendar newCalendar = Calendar.getInstance();
        pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(year, monthOfYear, dayOfMonth);
                timePick(calendarDate, dateConst);
            }


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    private void timePick(final Calendar calendarDate, final String dateConst) {
        Calendar timeCalendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendarDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarDate.set(Calendar.MINUTE, minute);
                int hour = hourOfDay;
                Log.d("HourOfDay", hour + "");
                String ampm;
                if (hour > 12) {
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                switch (dateConst) {
                    case START_DATE:
                        etStartDate.setText(dateFormatter.format(calendarDate.getTime()) + " " + ampm);
                        isDoneStartDate = true;
                        break;
                    case END_DATE:
                        etEndDate.setText(dateFormatter.format(calendarDate.getTime()) + " " + ampm);
                        isDoneEndDate = true;
                        break;
                }
                if (spinnerLayout.getVisibility() == View.VISIBLE) {
                    spinnerLayout.setVisibility(View.GONE);
                }
                if (isDoneStartDate && isDoneEndDate) {
                    existingKeyModelForTrips.clear();
                    Log.d("TimeStart", startDate.getTime().toString());
                    Log.d("TimeStart", endDate.getTime().toString());
                    int dateAns = startDate.getTime().compareTo(endDate.getTime());
                    switch (dateAns) {
                        case DATE_GREATER:
                            Toast.makeText(getActivity(), "startDate must be less than endDate", Toast.LENGTH_SHORT).show();
                            break;
                        case DATE_LESS:
                            startLoadingSpinners();
                            Toast.makeText(getActivity(), "start Downloading Data", Toast.LENGTH_SHORT).show();
                            break;
                        case DATE_EQUAL:
                            Toast.makeText(getActivity(), "start Date equal end Date", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }


        }, timeCalendar.get(Calendar.HOUR_OF_DAY), timeCalendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void startLoadingSpinners() {
        checkingTripDataInServer();
    }
    private void checkingTripDataInServer() {
        databaseReferenceForTripCounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tripCounts = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                if (tripCounts != 0) {
                    downloadTripData();
                } else {
                    downloadTripData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadTripData() {
        if (tripCounts >= 50) {
            databaseReferenceForTripData.orderByKey().limitToLast(50).addValueEventListener(valueEventListenerForTripData);
        } else if (tripCounts == 0) {
            databaseReferenceForTripData.addValueEventListener(valueEventListenerForTripData);
        } else {
            databaseReferenceForTripData.orderByKey().limitToLast(tripCounts).addValueEventListener(valueEventListenerForTripData);
        }
    }

    private void searchForGuides() {
        spinnerArrayListForGuides.clear();
        databaseReferenceForGuide.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> tempKeys = new ArrayList<>();
                ArrayList<GuideModel> tempModels = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        tempModels.add(dataSnapshot1.getValue(GuideModel.class));
                        tempKeys.add(dataSnapshot1.getKey());
                    }
                }
                for (int i = 0; i < tempKeys.size(); i++) {
                    String tempKey = tempKeys.get(i);
                    boolean isEqual = false;
                    for (int j = 0; j < existingKeyModelForTrips.size(); j++) {
                        ExistingKeyModelForTrip tempModel = existingKeyModelForTrips.get(j);
                        if (tempKey.equals(tempModel.getGuideKey())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        GuideModel guideModel = tempModels.get(i);
                        spinnerArrayListForGuides.add(new ModelForSpinner(guideModel.getGuideName(), guideModel.getGuidePhNo(), tempKey));
                    }
                }
                if(spinnerArrayListForGuides.size() == 0){
                    spinnerArrayListForGuides.add(0,new ModelForSpinner("You have not currently Available Guides","",""));
                }else{
                    spinnerArrayListForGuides.add(0,new ModelForSpinner("Choose Guides","Ph No",""));
                }
                guideSpinnerAdapter = new SpinnerAdapter(getActivity(), spinnerArrayListForGuides);
                setPinnerMaxSize(guideSpinner);
                guideSpinner.setAdapter(guideSpinnerAdapter);
                if(key!=null && !strGuideKey.equals("")){
                    databaseReferenceForCars.child(strGuideKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CarModel carModel = dataSnapshot.getValue(CarModel.class);
                            spinnerArrayListForGuides.add(1,new ModelForSpinner(carModel.getCarNo(),carModel.getCountSeats(),strCarKey));
                            guideSpinner.setSelection(1);
                            guideSpinnerAdapter.notifyDataSetChanged();
                            isDoneGuideSpinner = true;
                            updateUI();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    isDoneGuideSpinner = true;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setPinnerMaxSize(Spinner spinner) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow pWindow = (ListPopupWindow) popup.get(spinner);
            pWindow.setHeight(1000);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void searchForDrivers() {
        spinnerArrayListForDrivers.clear();
        databaseReferenceForDriver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> tempKeys = new ArrayList<>();
                ArrayList<DriverModel> tempModels = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        tempModels.add(dataSnapshot1.getValue(DriverModel.class));
                        tempKeys.add(dataSnapshot1.getKey());
                    }
                }
                for (int i = 0; i < tempKeys.size(); i++) {
                    String tempKey = tempKeys.get(i);
                    boolean isEqual = false;
                    for (int j = 0; j < existingKeyModelForTrips.size(); j++) {
                        ExistingKeyModelForTrip tempModel = existingKeyModelForTrips.get(j);
                        if (tempKey.equals(tempModel.getDriverKey())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        DriverModel driverModel = tempModels.get(i);
                        spinnerArrayListForDrivers.add(new ModelForSpinner(driverModel.getDriverName(), driverModel.getDriverPhNo(), tempKey));
                    }
                }
                if(spinnerArrayListForDrivers.size() == 0){
                    spinnerArrayListForDrivers.add(0,new ModelForSpinner("You have not currently Available Drivers","",""));
                }else{
                    spinnerArrayListForDrivers.add(0,new ModelForSpinner("Choose Drivers","Ph No",""));
                }
                driverSpinnerAdapter = new SpinnerAdapter(getActivity(), spinnerArrayListForDrivers);
                setPinnerMaxSize(driverSpinner);
                driverSpinner.setAdapter(driverSpinnerAdapter);
                if(key!=null && !strDriverKey.equals("")){
                    databaseReferenceForDriver.child(strDriverKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DriverModel driverModel = dataSnapshot.getValue(DriverModel.class);
                            spinnerArrayListForDrivers.add(1,new ModelForSpinner(driverModel.getDriverName(),driverModel.getDriverPhNo(),strDriverKey));
                            driverSpinner.setSelection(1);
                            driverSpinnerAdapter.notifyDataSetChanged();
                            isDoneDriverSpinner = true;
                            updateUI();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    isDoneDriverSpinner = true;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void searchForConductors() {
        spinnerArrayListForConductor.clear();
        databaseReferenceForConductor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> tempKeys = new ArrayList<>();
                ArrayList<ConductorModel> tempModels = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        tempModels.add(dataSnapshot1.getValue(ConductorModel.class));
                        tempKeys.add(dataSnapshot1.getKey());
                    }
                }
                for (int i = 0; i < tempKeys.size(); i++) {
                    String tempKey = tempKeys.get(i);
                    boolean isEqual = false;
                    for (int j = 0; j < existingKeyModelForTrips.size(); j++) {
                        ExistingKeyModelForTrip tempModel = existingKeyModelForTrips.get(j);
                        if (tempKey.equals(tempModel.getConductor1Key()) || tempKey.equals(tempModel.getConductor2Key())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        ConductorModel conductorModel = tempModels.get(i);
                        spinnerArrayListForConductor.add(new ModelForSpinner(conductorModel.getConductorName(), conductorModel.getConductorPhNo(), tempKey));
                    }
                }
                if(spinnerArrayListForConductor.size() == 0){
                    spinnerArrayListForConductor.add(0,new ModelForSpinner("You have not currently Available Conductor","",""));
                }else{
                    spinnerArrayListForConductor.add(0,new ModelForSpinner("Choose Conductors","Ph No",""));
                }
                conductor1SpinnerAdapter = new SpinnerAdapter(getActivity(), spinnerArrayListForConductor);
                setPinnerMaxSize(conductor1Spinner);
                conductor1Spinner.setAdapter(conductor1SpinnerAdapter);
                conductor2SpinnerAdapter = new SpinnerAdapter(getActivity(), spinnerArrayListForConductor);
                setPinnerMaxSize(conductor2Spinner);
                conductor2Spinner.setAdapter(conductor2SpinnerAdapter);
                Log.d("ConductorSPinnerSize", spinnerArrayListForConductor.size() + "");
                if(key!=null && !strConductor1Key.equals("")){
                    databaseReferenceForConductor.child(strConductor1Key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ConductorModel conductorModel = dataSnapshot.getValue(ConductorModel.class);
                            spinnerArrayListForConductor.add(1,new ModelForSpinner(conductorModel.getConductorName(),conductorModel.getConductorPhNo(),strConductor1Key));
                            conductor1SpinnerAdapter.notifyDataSetChanged();
                            conductor1Spinner.setSelection(1);
                            isDoneConductorSpinner = true;
                            updateUI();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(!strConductor2Key.equals("")){
                        databaseReferenceForConductor.child(strConductor2Key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ConductorModel conductorModel = dataSnapshot.getValue(ConductorModel.class);
                                spinnerArrayListForConductor.add(2,new ModelForSpinner(conductorModel.getConductorName(),conductorModel.getConductorPhNo(),strConductor2Key));
                                conductor2SpinnerAdapter.notifyDataSetChanged();
                                conductor2Spinner.setSelection(2);
                                isDoneConductorSpinner = true;
                                updateUI();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    isDoneConductorSpinner = true;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void searchForCars() {
        spinnerArrayListForCars.clear();
        databaseReferenceForCars.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> tempKeys = new ArrayList<>();
                ArrayList<CarModel> tempModels = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        tempModels.add(dataSnapshot1.getValue(CarModel.class));
                        tempKeys.add(dataSnapshot1.getKey());
                    }
                }
                for (int i = 0; i < tempKeys.size(); i++) {
                    String tempKey = tempKeys.get(i);
                    boolean isEqual = false;
                    for (int j = 0; j < existingKeyModelForTrips.size(); j++) {
                        ExistingKeyModelForTrip tempModel = existingKeyModelForTrips.get(j);
                        if (tempKey.equals(tempModel.getCarKey())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        CarModel carModel = tempModels.get(i);
                        spinnerArrayListForCars.add(new ModelForSpinner(carModel.getCarNo(), carModel.getCountSeats(), tempKey));
                    }
                }
                if(spinnerArrayListForCars.size() == 0){
                    spinnerArrayListForCars.add(0,new ModelForSpinner("You have not currently Available Cars","",""));
                }else{
                    spinnerArrayListForCars.add(0,new ModelForSpinner("Choose Cars","Seat No",""));
                }
                carSpinnerAdapter = new SpinnerAdapter(getActivity(), spinnerArrayListForCars);
                setPinnerMaxSize(carSpinner);
                carSpinner.setAdapter(carSpinnerAdapter);
                if(key!=null && !strCarKey.equals("")){
                    databaseReferenceForCars.child(strCarKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CarModel carModel = dataSnapshot.getValue(CarModel.class);
                            spinnerArrayListForCars.add(1,new ModelForSpinner(carModel.getCarNo(),carModel.getCountSeats(),strCarKey));
                            carSpinner.setSelection(1);
                            carSpinnerAdapter.notifyDataSetChanged();
                            isDoneCarSpinner = true;
                            updateUI();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    isDoneCarSpinner = true;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        if (isDoneCarSpinner && isDoneDriverSpinner && isDoneConductorSpinner && isDoneGuideSpinner) {
            spinnerLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Position","Enter Spinner "+parent.getId());
        switch (parent.getId()){
            case R.id.carSpinner:
                selectCarSpinner = position;
                Log.d("PositionCar",position+"Selected");
                break;
            case R.id.driverSpinner:
                selectDriverSpinner = position;
                Log.d("PositionDriver",position+"Selected");
                break;
            case R.id.conductor1Spinner:
                selectConductor1Spinner = position;
                break;
            case R.id.conductor2Spinner:
                selectConductor2Spinner = position;
                break;
            case R.id.guideSpinner:
                selectGuideSpinner = 0;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setKey(String key) {
        this.key = key;
        initDbReference();
        databaseReferenceForTripData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TripModel tripModel = dataSnapshot.getValue(TripModel.class);
                etTripName.setText(tripModel.getTripName());
                etRenderCharge.setText(tripModel.getRenderCharge());
                etRoadFee.setText(tripModel.getRoadFee());
                etCostForFood.setText(tripModel.getCostForFood());
                etGeneralExpense.setText(tripModel.getGeneralExpense());
                etDebit.setText(tripModel.getDebit());
                etProfit.setText(tripModel.getProfit());
                etClientName.setText(tripModel.getClientName());
                etClientPhNum.setText(tripModel.getClientPhNum());
                etClientCompany.setText(tripModel.getClientCompany());
                isDoneStartDate = true;
                isDoneEndDate = true;
                long startDateMilli = Long.parseLong(tripModel.getStartDate());
                Date stDate = new Date(startDateMilli);
                startDate.setTime(stDate);
                etStartDate.setText(tripModel.getStrStartDate());
                long endDateMilli = Long.parseLong(tripModel.getEndDate());
                Date edDate = new Date(endDateMilli);
                endDate.setTime(edDate);
                etEndDate.setText(tripModel.getStrEndDate());
                strCarKey = tripModel.getCarKey();
                strConductor1Key = tripModel.getConductor1Key();
                strConductor2Key = tripModel.getConductor2Key();
                strDriverKey = tripModel.getDriverKey();
                strGuideKey = tripModel.getGuideKey();
                startLoadingSpinners();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class SpinnerAdapter extends BaseAdapter {
        FragmentActivity activity;
        ArrayList<ModelForSpinner> modelLIst;
        LayoutInflater inflater;

        public SpinnerAdapter(FragmentActivity activity, ArrayList<ModelForSpinner> modelList){
            this.modelLIst=modelList;
            this.activity=activity;
        }
        @Override
        public int getCount() {
            return modelLIst.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater=LayoutInflater.from(activity);
            convertView=inflater.inflate(R.layout.spinner_view,null);
            TextView tvfirst= (TextView) convertView.findViewById(R.id.first);
            TextView tvsecond= (TextView) convertView.findViewById(R.id.second);
            tvfirst.setText(modelLIst.get(position).getFirst());
            tvsecond.setText(modelLIst.get(position).getSecond());
            return convertView;
        }
    }

    private class ValueEventListenerForTripData implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    tripModels.add(dataSnapshot1.getValue(TripModel.class));
                }
                Calendar tempServerStartDate = Calendar.getInstance();
                Calendar tempServerEndDate = Calendar.getInstance();
                Date tempStartDate, tempEndDate;
                for (int i = 0; i < tripModels.size(); i++) {
                    TripModel tripModel = tripModels.get(i);
                    long startMilli = Long.parseLong(tripModel.getStartDate());
                    long endMilli = Long.parseLong(tripModel.getEndDate());
                    tempStartDate = new Date(startMilli);
                    tempServerStartDate.setTime(tempStartDate);
                    tempEndDate = new Date(endMilli);
                    tempServerEndDate.setTime(tempEndDate);
                    if (startDate.compareTo(tempServerStartDate) == DATE_EQUAL || endDate.compareTo(tempServerStartDate) == DATE_EQUAL || startDate.compareTo(tempServerEndDate) == DATE_EQUAL || endDate.compareTo(tempServerEndDate) == DATE_EQUAL) {
                        existingKeyModelForTrips.add(new ExistingKeyModelForTrip(tripModel.getCarKey(), tripModel.getDriverKey(), tripModel.getConductor1Key(), tripModel.getConductor2Key(), tripModel.getGuideKey()));
                    } else {
                        if (startDate.compareTo(tempServerStartDate) == DATE_LESS) {
                            if (endDate.compareTo(tempServerStartDate) == DATE_GREATER) {
                                existingKeyModelForTrips.add(new ExistingKeyModelForTrip(tripModel.getCarKey(), tripModel.getDriverKey(), tripModel.getConductor1Key(), tripModel.getConductor2Key(), tripModel.getGuideKey()));
                            }
                        } else if (startDate.compareTo(tempServerStartDate) == DATE_GREATER) {
                            if (startDate.compareTo(tempServerEndDate) == DATE_LESS) {
                                existingKeyModelForTrips.add(new ExistingKeyModelForTrip(tripModel.getCarKey(), tripModel.getDriverKey(), tripModel.getConductor1Key(), tripModel.getConductor2Key(), tripModel.getGuideKey()));
                            }
                        }
                    }

                }
            }
            searchForCars();
            searchForConductors();
            searchForDrivers();
            searchForGuides();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
