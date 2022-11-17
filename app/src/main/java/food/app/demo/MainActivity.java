package food.app.demo;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import food.app.demo.Expense.ExpenseFragment;
import food.app.demo.ExpenseInTrip.AddExpenseInTripFragment;
import food.app.demo.ExpenseInTrip.DetailExpenseInTripFragment;
import food.app.demo.ExpenseInTrip.ExportDataFragment;
import food.app.demo.ExpenseInTrip.UpdateExpenseInTripFragment;
import food.app.demo.Home.HomeFragment;
import food.app.demo.Model.Expense;
import food.app.demo.Model.Trip;
import food.app.demo.Trip.AddTripFragment;
import food.app.demo.Trip.DetailTripFragment;
import food.app.demo.Trip.TripFragment;
import food.app.demo.Trip.UpdateTripFragment;
import food.app.demo.User.UserFragment;
import food.app.demo.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mbottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frame_layout);
        setFragment(new HomeFragment());

        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        setFragment(new HomeFragment());
                        return true;
                    case R.id.trip:
                        setFragment(new TripFragment());
                        return true;
                    case R.id.expense:
                        setFragment(new ExpenseFragment());
                        return true;
                    case R.id.user:
                        setFragment(new UserFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void gotoTripFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TripFragment tripFragment = new TripFragment();
        fragmentTransaction.replace(R.id.frame_layout,tripFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void gotoAddTripFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddTripFragment addTripFragment = new AddTripFragment();
        fragmentTransaction.replace(R.id.frame_layout,addTripFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void gotoUpdateTripFragment(Trip trip) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UpdateTripFragment updateTripFragment = new UpdateTripFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_trip",trip);
        updateTripFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,updateTripFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void gotoTripDetailFragment(Trip trip){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailTripFragment detailTripFragment = new DetailTripFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_trip",trip);
        detailTripFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,detailTripFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void backToTripFragment (){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TripFragment tripFragment = new TripFragment();

        fragmentTransaction.replace(R.id.frame_layout,tripFragment,null);
        fragmentTransaction.commit();
    }
    public void backToDetailTripFragment (){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DetailTripFragment detailTripFragment = new DetailTripFragment();

        fragmentTransaction.replace(R.id.frame_layout,detailTripFragment,null);
        fragmentTransaction.commit();
    }

    public void goToAddExpenseInTripFragment(Integer trip_id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddExpenseInTripFragment addExpenseFragment = new AddExpenseInTripFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("trip_id", trip_id);
        addExpenseFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,addExpenseFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void gotoUpdateExpenseInTripFragment(Expense expense) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UpdateExpenseInTripFragment updateExpenseFragment = new UpdateExpenseInTripFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_expense", expense);
        updateExpenseFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,updateExpenseFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }


    public void gotoExpenseDetailInTripFragment(Expense expense){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailExpenseInTripFragment detailExpenseFragment = new DetailExpenseInTripFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_expense", expense);
        detailExpenseFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,detailExpenseFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void gotoExpenseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ExpenseFragment expenseFragment = new ExpenseFragment();
        fragmentTransaction.replace(R.id.frame_layout,expenseFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void gotoExportDataFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ExportDataFragment exportDataFragment = new ExportDataFragment();
        fragmentTransaction.replace(R.id.frame_layout,exportDataFragment,null);
        fragmentTransaction.addToBackStack(null).commit();
    }


}
