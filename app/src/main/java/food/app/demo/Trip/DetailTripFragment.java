package food.app.demo.Trip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Expense;
import food.app.demo.Model.Trip;
import food.app.demo.R;

public class DetailTripFragment extends Fragment {

    private View view;
    private RecyclerView rcvDetailTrip;
    DetailTripContainExpenseAdapter tripContainExpenseAdapter;
    private MainActivity mainActivity;

    TextView trip_nameD, trip_startDateD, trip_endDateD, trip_departureD, trip_destinationD, trip_amountD, trip_riskD, trip_typeD, trip_notesD;
    ImageView btn_EditTrip, btn_DeleteTrip,back_btn,exportData_btn;
    Button btn_addExinTrip;

    String FileName = "DownloadJsonFile.txt";
    ArrayList<String> SaveListDataExport= new ArrayList<>();

    public DetailTripFragment() {
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
        Trip trip = (Trip) bundleReceive.get("object_trip");
        DatabaseHelper db =new DatabaseHelper(getContext());
        mainActivity = (MainActivity) getActivity();

        view = inflater.inflate(R.layout.fragment_detail_trip, container, false);
        rcvDetailTrip = view.findViewById(R.id.recycleDetailTrip_Expense);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rcvDetailTrip.setLayoutManager(linearLayoutManager);


        trip_nameD = view.findViewById(R.id.tripName_detail);
        trip_startDateD = view.findViewById(R.id.dateStart_detail);
        trip_endDateD = view.findViewById(R.id.dateEnd_detail);
        trip_departureD = view.findViewById(R.id.departure_detail);
        trip_destinationD = view.findViewById(R.id.destination_detail);
        trip_amountD = view.findViewById(R.id.amount_detail);
        trip_notesD = view.findViewById(R.id.trip_notes);
        trip_riskD = view.findViewById(R.id.risk_detail);
        trip_typeD = view.findViewById(R.id.type_detail);

        trip_nameD.setText(trip.getTrip_name());
        trip_startDateD.setText(trip.getTrip_startDate());
        trip_endDateD.setText(trip.getTrip_endDate());
        trip_departureD.setText(trip.getTrip_departure());
        trip_destinationD.setText(trip.getTrip_destination());
        trip_notesD.setText(trip.getTrip_description());

        String tripRisk;
        if(trip.getTrip_risk() != 1){
            tripRisk ="No";
        }else tripRisk="Yes";
        trip_riskD.setText(tripRisk);

        String tripType;
        if(trip.getType() != 1){
            tripType ="International";
        }else tripType="Domestic";
        trip_typeD.setText(tripType);

        Float totalEx = db.getTotalExpensesInTrip(String.valueOf(trip.getTrip_id()));
        trip_amountD.setText(String.valueOf(totalEx));

        back_btn = view.findViewById(R.id.back_tripDtoTripTab);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        btn_EditTrip = view.findViewById(R.id.editTrip_btn);
        btn_EditTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.gotoUpdateTripFragment(trip);
            }
        });

        btn_DeleteTrip = view.findViewById(R.id.deleteTrip_btn);
        btn_DeleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteTripByID();
            }
        });

        tripContainExpenseAdapter = new DetailTripContainExpenseAdapter(db.getListExpensesByTripId(trip.getTrip_id()),
                new DetailTripContainExpenseAdapter.IClickItemListener() {
            @Override
            public void onClickItemExpenseInTrip(Expense expense) {
                mainActivity.gotoExpenseDetailInTripFragment(expense);
            }
        });

        rcvDetailTrip.setAdapter(tripContainExpenseAdapter);

        btn_addExinTrip = view.findViewById(R.id.btnAddNewExInTrip);
        btn_addExinTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.goToAddExpenseInTripFragment(trip.getTrip_id());
            }
        });

        exportData_btn = view.findViewById(R.id.exportData_btn);
        exportData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JsonDownload(trip.getTrip_id());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void JsonDownload (int id) throws IOException {
        DatabaseHelper db =new DatabaseHelper(getContext());
        SaveListDataExport = db.DownloadFile(id);
        if (saveFile(FileName, SaveListDataExport)){
            Toast.makeText(getContext(), "Exported to " + getContext().getExternalFilesDir(null) + "/" +FileName , Toast.LENGTH_SHORT).show();
            mainActivity.gotoExportDataFragment();
        } else{
            Toast.makeText(getContext(), "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean saveFile(String file, ArrayList<String> text) {
        try {
            FileOutputStream fos = getContext().openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(text);
            oos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving file", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void confirmDeleteTripByID(){
        DatabaseHelper db =new DatabaseHelper(getContext());
        Bundle bundleReceive = getArguments();

        Trip trip = (Trip) bundleReceive.get("object_trip");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Trip?");
        builder.setMessage("Do you want to delete trip " +trip.getTrip_name()+ " ?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                mainActivity = (MainActivity) getActivity();
                db.deleteTripById(trip.getTrip_id());
                mainActivity.backToTripFragment();
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