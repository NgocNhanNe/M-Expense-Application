package food.app.demo.Trip;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Trip;
import food.app.demo.R;

public class UpdateTripFragment extends Fragment {

    private View view;
    EditText dateStart_txt,dateEnd_txt;
    ImageView calStart,calEnd, back_btn;
    EditText trip_name, trip_departure, trip_destination,trip_notes;
    RadioGroup group_type, group_risk;
    Button tripBtnUpdate,cancelUpdateTrip_bnt;

    private MainActivity mainActivity;
    int type_trip, trip_risk;

    public UpdateTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_update_trip, container, false);
        Bundle bundleReceive = getArguments();
        Trip trip = (Trip) bundleReceive.get("object_trip");

        dateStart_txt = view.findViewById(R.id.tripDateStart_edit);
        dateEnd_txt = view.findViewById(R.id.tripDateEnd_edit);
        calStart = view.findViewById(R.id.dateStartImg);
        calEnd = view.findViewById(R.id.dateEndImg);

        trip_name = view.findViewById(R.id.tripName_edit);
        trip_departure = view.findViewById(R.id.tripDeparture_edit);
        trip_destination = view.findViewById(R.id.tripDestination_edit);
        trip_notes = view.findViewById(R.id.tripNote_edit);
        group_type = view.findViewById(R.id.tripType_txt);
        group_risk = view.findViewById(R.id.Risktype);

        RadioButton internal_radio=view.findViewById(R.id.international);
        RadioButton domestic_radio=view.findViewById(R.id.domestic);
        RadioButton risk_radio=view.findViewById(R.id.risk);
        RadioButton noRisk_radio=view.findViewById(R.id.notRisk);

        if( trip.getType()==0){
            internal_radio.setChecked(true);
        }else {
            domestic_radio.setChecked(true);
        }
        if(trip.getTrip_risk()==1){
            risk_radio.setChecked(true);
        }else {
            noRisk_radio.setChecked(true);
        }

        mainActivity = (MainActivity) getActivity();
        tripBtnUpdate = view.findViewById(R.id.updateTrip_bnt);

        cancelUpdateTrip_bnt = view.findViewById(R.id.cancelUpdateTrip_bnt);
        cancelUpdateTrip_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backToTripFragment();
            }
        });

       trip_name.setText(trip.getTrip_name());
       trip_departure.setText(trip.getTrip_departure());
       trip_destination.setText(trip.getTrip_destination());
       trip_notes.setText(trip.getTrip_description());
       dateStart_txt.setText(trip.getTrip_startDate());
       dateEnd_txt.setText(trip.getTrip_endDate());

        final Calendar calendar = Calendar.getInstance();

        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        calStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateStart_txt.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        calEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateEnd_txt.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        group_risk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.risk:
                        trip_risk=1;
                        break;
                    case R.id.notRisk:
                        trip_risk=0;
                        break;
                }
            }
        });

        group_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.international:
                        type_trip=0;
                        break;
                    case R.id.domestic:
                        type_trip=1;
                        break;
                }
            }
        });

        back_btn = view.findViewById(R.id.back_updateTriptoTripDTab);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        tripBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        return view;
    }
    private void checkCredentials(){
        String tripName = trip_name.getText().toString().trim();
        String tripDeparture = trip_departure.getText().toString().trim();
        String tripDestination = trip_destination.getText().toString().trim();
        String tripDateStart = dateStart_txt.getText().toString().trim();
        String tripDateEnd = dateEnd_txt.getText().toString().trim();

        if (tripName.isEmpty()) {
            showError(trip_name, "Trip name is empty!");
        } else if (tripDeparture.isEmpty()) {
            showError(trip_departure, "Trip departure is empty!");
        } else if (tripDestination.isEmpty()) {
            showError(trip_destination, "Trip destination is empty!");
        } else if (tripDateStart.isEmpty()) {
            showError(dateStart_txt, "Trip started date is empty!");
        } else if (tripDateEnd.isEmpty()) {
            showError(dateEnd_txt, "Trip ended date is empty!");
        }else if(new Date(tripDateStart).after(new Date(tripDateEnd))){
            showError(dateEnd_txt,"Ended date must be larger than started date");
        }
        else {
            updateTrip();
        }
    }

    private void updateTrip(){
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Trip updateTrip = new Trip();
        Bundle bundleReceive = getArguments();
        Trip trip = (Trip) bundleReceive.get("object_trip");

        updateTrip.setTrip_name(trip_name.getText().toString().trim());
        updateTrip.setTrip_departure(trip_departure.getText().toString().trim().toString());
        updateTrip.setTrip_destination(trip_destination.getText().toString().trim().toString());
        updateTrip.setTrip_startDate(dateStart_txt.getText().toString().trim().toString());
        updateTrip.setTrip_endDate(dateEnd_txt.getText().toString().trim().toString());
        updateTrip.setTrip_description(trip_notes.getText().toString().trim().toString());
        updateTrip.setTrip_risk(trip_risk);
        updateTrip.setType(type_trip);


        long result = db.updateTrip(updateTrip,trip.getTrip_id());
        if(result==-1){
            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Update trip successfully!", Toast.LENGTH_SHORT).show();
            mainActivity.backToTripFragment();
        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}