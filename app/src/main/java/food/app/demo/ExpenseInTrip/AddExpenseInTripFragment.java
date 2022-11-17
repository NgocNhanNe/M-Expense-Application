package food.app.demo.ExpenseInTrip;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Expense;
import food.app.demo.R;

public class AddExpenseInTripFragment extends Fragment {

    private View view;
    TextInputLayout title_type;
    AutoCompleteTextView dropdown_ExType;
    EditText ex_name;
    EditText ex_location;
    EditText ex_time;
    EditText ex_amount;
    EditText ex_note;
    EditText ex_date;
    ImageView calExpense;
    ImageView clockExpense,back_addExpensetoTripTab,expenseLocationImg;
    Button btnAddExpense,cancleAddExpense_bnt;

    private MainActivity mainActivity;

    ArrayList<String> arrayList_Type;
    ArrayAdapter<String> arrayAdapter_Type;



    public AddExpenseInTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_expense_in_trip, container, false);

        calExpense = view.findViewById(R.id.dateExpenseImg);
        clockExpense = view.findViewById(R.id.timeExpenseImg);
        ex_location = view.findViewById(R.id.expenseLocation_edit);
        ex_name = view.findViewById(R.id.expenseName_edit);
        ex_note = view.findViewById(R.id.ExpenseNotes_edit);
        ex_time = view.findViewById(R.id.ExpenseTime_edit);
        ex_date = view.findViewById(R.id.ExpenseDate_edit);
        ex_amount = view.findViewById(R.id.ExpenseAmount_edit);

        title_type = view.findViewById(R.id.title_typeEx);
        dropdown_ExType = view.findViewById(R.id.act_type);

        arrayList_Type = new ArrayList<>();
        arrayList_Type.add("Food");
        arrayList_Type.add("Hotel");
        arrayList_Type.add("Transports");
        arrayList_Type.add("Documents");
        arrayList_Type.add("Others");


        expenseLocationImg = view.findViewById(R.id.expenseLocationImg);
        whenClickLocation();
        arrayAdapter_Type = new ArrayAdapter<>(getContext(), R.layout.type_selected, arrayList_Type);
        dropdown_ExType.setAdapter(arrayAdapter_Type);
        dropdown_ExType.setThreshold(1);

        final Calendar calendar = Calendar.getInstance();

        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        calExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        ex_date.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        clockExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int mins = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        ex_time.setText(hour + ":" + minute);
                    }
                }, hour, mins, false);
                timePickerDialog.show();
            }
        });

        mainActivity = (MainActivity) getActivity();

        cancleAddExpense_bnt = view.findViewById(R.id.cancleAddExpense_bnt);
        cancleAddExpense_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backToTripFragment();
            }
        });

        back_addExpensetoTripTab = view.findViewById(R.id.back_addExpensetoTripTab);
        back_addExpensetoTripTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        btnAddExpense = view.findViewById(R.id.addExpense_bnt);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
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

    private void whenClickLocation() {
        expenseLocationImg.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            }
            else {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
                if (locationManager != null) {
                    Location Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = hereLocation(Location.getLatitude(), Location.getLongitude());
                        ex_location.setText(city);
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "Please turn on your location", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    private String hereLocation(double latitude, double longitude) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10);
            if (addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityName = adr.getLocality();
                        cityName = cityName + ", " + adr.getAdminArea() + ", " + adr.getCountryName();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;
    }

        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1000:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                    }else{
                        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
                        Location Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try {
                            String city = hereLocation(Location.getLatitude(), Location.getLongitude());
                            ex_location.setText(city);
                        }
                        catch (Exception e){Toast.makeText(getContext(), "Please turn on your location", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkCredentials() {

        String exName = ex_name.getText().toString().trim();
        String exLocation = ex_location.getText().toString().trim();
        String exDate = ex_date.getText().toString().trim();
        String exMount = ex_amount.getText().toString().trim();
        String exTime = ex_time.getText().toString().trim();

        if (exName.isEmpty()) {
            showError(ex_name, "Expense name is empty!");
        } else if (exLocation.isEmpty()) {
            showError(ex_location, "Expense location is empty!");
        } else if (exDate.isEmpty()) {
            showError(ex_date, "Expense date is empty!");
        } else if (exMount.isEmpty()) {
            showError(ex_amount, "Expense amount is empty!");
        }
        else if (exTime.isEmpty()) {
            showError(ex_time, "Expense time is empty!");
        }
        else {
            confirmInformation();
        }
    }

    public void confirmInformation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String exName = ex_name.getText().toString().trim();
        String exLocation = ex_location.getText().toString().trim();
        String exDate = ex_date.getText().toString().trim();
        String exMount = ex_amount.getText().toString().trim();
        String exTime = ex_time.getText().toString().trim();

        builder.setTitle("Information of Expense");
        builder.setMessage("Do you want to add expense ?");
        builder.setMessage(" Expense name: "+exName +"\n Expense Location: " +exLocation+
                "\n Expense Date: " + exDate + "\n Expense Time : " + exTime+
                "\n Expense Amount : " + exMount );
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                addExpense();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        builder.create().show();
    }


    private void addExpense(){
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Expense expense = new Expense();

        Bundle bundleReceive = getArguments();
        int trip_id = (int) bundleReceive.get("trip_id");

        expense.setExpense_name(ex_name.getText().toString().trim());
        expense.setExpense_notes("Pending");
        expense.setLocation(ex_location.getText().toString().trim());
        expense.setExpense_date(ex_date.getText().toString().trim());
        expense.setExpense_amount(Float.parseFloat(ex_amount.getText().toString().trim()));
        expense.setExpense_time(ex_time.getText().toString().trim());
        expense.setExpense_type(dropdown_ExType.getText().toString().trim());
        expense.setTrip_id(trip_id);

        long result = db.addExpense(expense);
        if(result==-1){
            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Add successfully!", Toast.LENGTH_SHORT).show();
            mainActivity.onBackPressed();
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}
