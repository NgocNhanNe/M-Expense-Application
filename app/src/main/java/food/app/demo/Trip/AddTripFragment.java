package food.app.demo.Trip;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Trip;
import food.app.demo.R;

public class AddTripFragment extends Fragment {

    private View view;
    EditText dateStart_txt,dateEnd_txt;
    ImageView calStart,calEnd, back_btn;
    Button btnAddTrip,cancleTrip_bnt;
    EditText trip_name, trip_departure, trip_destination,trip_notes;
    RadioGroup radio_type, radio_risk;
    private MainActivity mMainActivity;

    int type_trip, trip_risk;

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        dateStart_txt = view.findViewById(R.id.tripDateStart_edit);
        dateEnd_txt = view.findViewById(R.id.tripDateEnd_edit);
        calStart = view.findViewById(R.id.dateStartImg);
        calEnd = view.findViewById(R.id.dateEndImg);

        trip_name = view.findViewById(R.id.tripName_edit);
        trip_departure = view.findViewById(R.id.tripDeparture_edit);
        trip_destination = view.findViewById(R.id.tripDestination_edit);
        trip_notes = view.findViewById(R.id.tripNote_edit);
        radio_type = view.findViewById(R.id.tripType_txt);
        radio_risk = view.findViewById(R.id.Risktype);

        btnAddTrip = view.findViewById(R.id.addTrip_bnt);
        cancleTrip_bnt = view.findViewById(R.id.cancleAddTrip_bnt);

        cancleTrip_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.gotoTripFragment();
            }
        });

        final Calendar calendar = Calendar.getInstance();

        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        calStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
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

        radio_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        radio_risk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        mMainActivity = (MainActivity) getActivity();

        back_btn = view.findViewById(R.id.back_tripAddtoTripTab);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.onBackPressed();
            }
        });

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void checkCredentials() {

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
           confirmInformation();
        }
    }

    public void confirmInformation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String tripName = trip_name.getText().toString().trim();
        String tripDeparture = trip_departure.getText().toString().trim();
        String tripDestination = trip_destination.getText().toString().trim();
        String tripDateStart = dateStart_txt.getText().toString().trim();
        String tripDateEnd = dateEnd_txt.getText().toString().trim();
        String tripRisk, tripType;

        if(trip_risk ==0){
            tripRisk = "No";
        }
        else {
            tripRisk = "Yes";
        }

        if(type_trip ==0){
            tripType = "International";
        }
        else {
            tripType = "Domestic";
        }

        builder.setTitle("Information of Trip");
        builder.setMessage("Do you want to add trip ?");
        builder.setMessage("Trip name: "+tripName +"\n Trip Departure: " +tripDeparture+
                "\n Trip Destination: " + tripDestination + "\n Trip Date Start : " + tripDateStart+
                "\n Trip Date End: " + tripDateEnd + "\n Trip Risk : " + tripRisk + "\n Trip Type : " +tripType);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                addTrip();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        builder.create().show();
    }



    private void addTrip() {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Trip trip = new Trip();

        trip.setTrip_name(trip_name.getText().toString().trim());
        trip.setTrip_departure(trip_departure.getText().toString().trim().toString());
        trip.setTrip_destination(trip_destination.getText().toString().trim().toString());
        trip.setTrip_startDate(dateStart_txt.getText().toString().trim().toString());
        trip.setTrip_endDate(dateEnd_txt.getText().toString().trim().toString());
        trip.setTrip_description(trip_notes.getText().toString().trim().toString());
        trip.setTrip_risk(trip_risk);
        trip.setType(type_trip);

        //method call for insert function
        long result = db.addTrip(trip);
        if(result==-1){
            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Add successfully!", Toast.LENGTH_SHORT).show();
            mMainActivity.backToTripFragment();
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}