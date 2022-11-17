package food.app.demo.ExpenseInTrip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

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

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Expense;
import food.app.demo.R;

public class UpdateExpenseInTripFragment extends Fragment {

    private View view;
    TextInputLayout title_type;
    AutoCompleteTextView dropdown_ExType;
    EditText ex_name, ex_location, ex_time, ex_amount,ex_note, ex_date;
    ImageView calExpense;
    ImageView clockExpense;
    ImageView back_btn;
    Button btnSaveExpense,cancelUpdateExpense_bnt;

    private MainActivity mainActivity;

    ArrayList<String> arrayList_Type;
    ArrayAdapter<String> arrayAdapter_Type;


    public UpdateExpenseInTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_expense_in_trip, container, false);
        Bundle bundleReceive = getArguments();
        Expense expense = (Expense) bundleReceive.get("object_expense");

        calExpense = view.findViewById(R.id.dateExpenseImg);
        clockExpense = view.findViewById(R.id.timeExpenseImg);

        ex_location = view.findViewById(R.id.expenseLocation_edit);
        ex_name = view.findViewById(R.id.expenseName_edit);
        ex_note = view.findViewById(R.id.ExpenseNotes_edit);
        ex_time = view.findViewById(R.id.ExpenseTime_edit);
        ex_date = view.findViewById(R.id.ExpenseDate_edit);
        ex_amount = view.findViewById(R.id.ExpenseAmount_edit);


        dropdown_ExType = view.findViewById(R.id.act_type);

        arrayAdapter_Type = new ArrayAdapter<>(getContext(),R.layout.type_selected,arrayList_Type);
        dropdown_ExType.setAdapter(arrayAdapter_Type);
        dropdown_ExType.setThreshold(1);

        ex_name.setText(expense.getExpense_name());
        dropdown_ExType.setText(expense.getExpense_type());
        ex_date.setText(expense.getExpense_date());
        ex_time.setText(expense.getExpense_time());
        ex_location.setText(expense.getLocation());
        ex_note.setText(expense.getExpense_notes());
        ex_amount.setText(String.valueOf(expense.getExpense_amount()));


        title_type = view.findViewById(R.id.title_typeEx);
        dropdown_ExType = view.findViewById(R.id.act_type);

        arrayList_Type = new ArrayList<>();
        arrayList_Type.add("Food");
        arrayList_Type.add("Hotel");
        arrayList_Type.add("Transport");
        arrayList_Type.add("Documents");
        arrayList_Type.add("Others");

        arrayAdapter_Type = new ArrayAdapter<>(getContext(),R.layout.type_selected,arrayList_Type);
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
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
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
                        ex_time.setText(hour+":"+minute);
                    }
                },hour,mins,false);
                timePickerDialog.show();
            }
        });


        mainActivity = (MainActivity) getActivity();

        back_btn = view.findViewById(R.id.back_updateExtoExD);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        cancelUpdateExpense_bnt = view.findViewById(R.id.cancelUpdateExpense_bnt);
        cancelUpdateExpense_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backToTripFragment();
            }
        });

        btnSaveExpense = view.findViewById(R.id.updateExpense_bnt);
        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        return view;
    }
    private void checkCredentials() {

        String exName = ex_name.getText().toString().trim();
        String exLocation = ex_location.getText().toString().trim();
        String exDate = ex_date.getText().toString().trim();
        String exMount = ex_amount.getText().toString().trim();
        String exTime = ex_time.getText().toString().trim();
        String exType = dropdown_ExType.getText().toString().trim();

        if (exName.isEmpty()) {
            showError(ex_name, "This is a required field");
        } else if (exLocation.isEmpty()) {
            showError(ex_location, "This is a required field");
        } else if (exDate.isEmpty()) {
            showError(ex_date, "This is a required field");
        } else if (exMount.isEmpty()) {
            showError(ex_amount, "This is a required field");
        }else if (exTime.isEmpty()) {
            showError(ex_time, "This is a required field");
        }else if(exType.isEmpty()){
            showError(dropdown_ExType, "This is a required field");
            dropdown_ExType.requestFocus();
        }
        else {
            updateExpense();
        }
    }

    private void updateExpense(){
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Expense updateExpense = new Expense();
        Bundle bundleReceive = getArguments();
        Expense expense = (Expense) bundleReceive.get("object_expense");

        updateExpense.setExpense_name(ex_name.getText().toString().trim());
        updateExpense.setExpense_notes(ex_note.getText().toString().trim());
        updateExpense.setLocation(ex_location.getText().toString().trim());
        updateExpense.setExpense_date(ex_date.getText().toString().trim());
        updateExpense.setExpense_amount(Float.parseFloat(ex_amount.getText().toString().trim()));
        updateExpense.setExpense_time(ex_time.getText().toString().trim());
        updateExpense.setExpense_type(dropdown_ExType.getText().toString().trim());


        long result = db.updateExpenses(updateExpense,expense.getExpense_id());
        if(result==-1){
            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Update successfully!", Toast.LENGTH_SHORT).show();
            mainActivity.backToTripFragment();
        }

    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}