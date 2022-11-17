package food.app.demo.Expense;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.Model.Expense;
import food.app.demo.R;

public class ExpenseFragment extends Fragment {

    private View view;
    SearchView searchExpense;
    RecyclerView recyclerView;
    List<Expense> expenseList;
    ExpenseAdapter expenseAdapter;
    MainActivity mainActivity;
    ImageView empty_imageview;
    TextView no_data;

    public ExpenseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expenseList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expense, container, false);
        recyclerView = view.findViewById(R.id.recycleviewEx_id);

        empty_imageview = view.findViewById(R.id.emptyImage);
        no_data = view.findViewById(R.id.emptyText);

        displayOrNot();

        DatabaseHelper db =new DatabaseHelper(getActivity());
        mainActivity = (MainActivity) getActivity();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        expenseAdapter = new ExpenseAdapter(db.getListExpense(), getContext(), new ExpenseAdapter.IClickItemListener() {
            @Override
            public void onLickItemExpense(Expense expense) {
                mainActivity.gotoExpenseDetailInTripFragment(expense);
            }
        });

        searchExpense = view.findViewById(R.id.searchView);
        searchExpense.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                expenseAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                expenseAdapter.getFilter().filter(newText);
                return false;
            }
        });
        recyclerView.setAdapter(expenseAdapter);


        return view;
    }

    void displayOrNot() {
        expenseList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(getActivity());
        expenseList = db.getListExpense();
        if (expenseList.size() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

}