package food.app.demo.ExpenseInTrip;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Expense;
import food.app.demo.R;

public class DetailExpenseInTripFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    TextView ex_nameD, ex_typeD, ex_dateD, ex_locationD, ex_notesD, ex_timeD, ex_amountD;
    ImageView btn_EditEx, btn_DeleteEx,back_btn;
    CircleImageView type_exImg;


    public DetailExpenseInTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundleReceive = getArguments();
        Expense expense = (Expense) bundleReceive.get("object_expense");

        view = inflater.inflate(R.layout.fragment_detail_expense, container, false);
        mainActivity = (MainActivity) getActivity();

        ex_nameD = view.findViewById(R.id.exName_detail);
        ex_typeD = view.findViewById(R.id.exType_detail);
        ex_dateD = view.findViewById(R.id.exDate_detail);
        ex_timeD = view.findViewById(R.id.ex_Time_detail);
        ex_locationD = view.findViewById(R.id.exLocation_detail);
        ex_notesD = view.findViewById(R.id.ex_notes_detail);
        ex_amountD = view.findViewById(R.id.amount_detail);
        type_exImg = view.findViewById(R.id.type_img);

        ex_nameD.setText(expense.getExpense_name());
        ex_typeD.setText(expense.getExpense_type());
        ex_dateD.setText(expense.getExpense_date());
        ex_timeD.setText(expense.getExpense_time());
        ex_locationD.setText(expense.getLocation());
        ex_notesD.setText(expense.getExpense_notes());
        ex_amountD.setText(String.valueOf(expense.getExpense_amount()));

        if(expense.getExpense_type().equals("Food")){
            type_exImg.setImageResource(R.drawable.food);
        }
        else if (expense.getExpense_type().equals("Hotel")){
            type_exImg.setImageResource(R.drawable.hotel);
        }
        else if (expense.getExpense_type().equals("Documents")){
            type_exImg.setImageResource(R.drawable.doc);
        }
        else if (expense.getExpense_type().equals("Transports")){
            type_exImg.setImageResource(R.drawable.transportation);
        }
        else if (expense.getExpense_type().equals("Others")){
            type_exImg.setImageResource(R.drawable.other);
        }

        back_btn = view.findViewById(R.id.back_ExDtoTripDTab);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        btn_EditEx = view.findViewById(R.id.editEx_btn);
        btn_EditEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.gotoUpdateExpenseInTripFragment(expense);
            }
        });

        btn_DeleteEx = view.findViewById(R.id.deleteEx_btn);
        btn_DeleteEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteExpenseByID();
            }
        });

        return view;
    }
    public void confirmDeleteExpenseByID(){
        DatabaseHelper db =new DatabaseHelper(getContext());
        Bundle bundleReceive = getArguments();

        Expense expense = (Expense) bundleReceive.get("object_expense");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Expense?");
        builder.setMessage("Do you want to delete expense " +expense.getExpense_name()+ " ?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                mainActivity = (MainActivity) getActivity();
                db.deleteExpensesById(expense.getExpense_id());
                mainActivity.onBackPressed();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        builder.create().show();
    }
}