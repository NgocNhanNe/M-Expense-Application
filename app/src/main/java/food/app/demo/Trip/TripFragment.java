package food.app.demo.Trip;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Trip;
import food.app.demo.R;


public class TripFragment extends Fragment {

    private View view;
    RecyclerView recyclerView;
    List<Trip> tripList;
    TripAdapter tripAdapter;
    FloatingActionButton btnNewTrip;
    Button deleteAll_bnt;
    ImageView empty_imageview, searchVoice;
    TextView no_data;
    SearchView searchTrip;
    MainActivity mainActivity;

    public TripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trip, container, false);
        recyclerView = view.findViewById(R.id.recycleviewTrip_id);

        empty_imageview = view.findViewById(R.id.emptyImage);
        no_data = view.findViewById(R.id.emptyText);

        displayOrNot();


        DatabaseHelper db =new DatabaseHelper(getActivity());
        mainActivity = (MainActivity) getActivity();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        tripAdapter = new TripAdapter(db.getAllTrip(), getContext(), new TripAdapter.IClickItemListener() {
            @Override
            public void onLickItemTrip(Trip trip) {
                mainActivity.gotoTripDetailFragment(trip);
            }
        });

        recyclerView.setAdapter(tripAdapter);

        btnNewTrip = view.findViewById(R.id.addTrip_btn);
        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.gotoAddTripFragment();
            }
        });

        searchTrip = view.findViewById(R.id.searchView);
        searchTrip.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tripAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tripAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchVoice = view.findViewById(R.id.searchVoice);
        searchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVoiceDialog();
            }
        });


        deleteAll_bnt = view.findViewById(R.id.deleteAll_btn);
        deleteAll_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteAllTrip();
            }
        });
        return view;
    }

    public void confirmDeleteAllTrip(){
        DatabaseHelper db =new DatabaseHelper(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete All Trip?");
        builder.setMessage("Do you want to delete all trip ?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                mainActivity = (MainActivity) getActivity();
                db.deleteAllTrip();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void displayOrNot() {
        tripList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(getActivity());
        tripList = db.getAllTrip();
        if (tripList.size() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    public void openVoiceDialog(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent,200);
    }

    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 200 && resultCode == RESULT_OK){
            String voice = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            searchTrip.setQuery(voice,false);
            searchTrip.clearFocus();
        }
        else {
            Toast.makeText(getContext(), "Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
}