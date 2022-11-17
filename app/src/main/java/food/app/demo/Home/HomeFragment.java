package food.app.demo.Home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.MainActivity;
import food.app.demo.R;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends Fragment {
    View view;
    ImageView bgHome,cloverImg;
    LinearLayout homeText,splashText;
    Animation aniBottom;
    RelativeLayout menus;

    private MainActivity mainActivity;

    //total
    TextView numberTrips,numberExs, totalAmount;

    //cardV to Tab

    CardView tripTab, ExTab;

    //Menu
    TextView food, hotel, transport, documents, others;

    public HomeFragment() {
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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Animation
        bgHome = view.findViewById(R.id.bgHome);
        splashText = view.findViewById(R.id.splashText);
        homeText = view.findViewById(R.id.homeText);
        aniBottom = AnimationUtils.loadAnimation(getContext(),R.anim.frombottom);
        menus = view.findViewById(R.id.menus);
        cloverImg = view.findViewById(R.id.cloverImg);
        tripTab = view.findViewById(R.id.cardVTrip);
        ExTab = view.findViewById(R.id.cardVEx);

        bgHome.animate().translationY(-2000).setDuration(1500).setStartDelay(300);
        splashText.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
        cloverImg.startAnimation(aniBottom);
        homeText.startAnimation(aniBottom);
        menus.startAnimation(aniBottom);

        //Get total expense and number trips
        numberTrips = view.findViewById(R.id.numberTrip);
        numberExs = view.findViewById(R.id.numberExpense);
        totalAmount = (TextView) view.findViewById(R.id.totalExpense);

        DatabaseHelper db =new DatabaseHelper(getContext());

        int numTrip = db.getAllTrip().size();
        int numEx = db.getListExpense().size();
        float totalExpense = db.getTotalExpenses();
        numberTrips.setText(numTrip+"");
        numberExs.setText(numEx+"");
        totalAmount.setText(totalExpense+"");

        //card view
        mainActivity = (MainActivity) getActivity();
        tripTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.gotoTripFragment();
            }
        });

        ExTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.gotoExpenseFragment();
            }
        });

        //Menus
        food = (TextView) view.findViewById(R.id.amount_food);
        hotel = (TextView) view.findViewById(R.id.amount_hotel);
        transport = (TextView) view.findViewById(R.id.amount_trans);
        documents = (TextView) view.findViewById(R.id.amount_documents);
        others = (TextView) view.findViewById(R.id.amount_others);

        ArrayList<Float> sum = db.getTotalTypeExpenses();
        food.setText(String.valueOf(sum.get(0)));
        hotel.setText(String.valueOf(sum.get(3)));
        transport.setText(String.valueOf(sum.get(2)));
        documents.setText(String.valueOf(sum.get(1)));
        others.setText(String.valueOf(sum.get(4)));

        //Pie Chart
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        float am_food = (sum.get(0)/totalExpense)*100;
        float am_docs = (sum.get(1)/totalExpense)*100;
        float am_trans = (sum.get(2)/totalExpense)*100;
        float am_hotel = (sum.get(3)/totalExpense)*100;
        float am_others = (sum.get(4)/totalExpense)*100;

        ArrayList<PieEntry> ex_type = new ArrayList<>();

        ex_type.add(new PieEntry(am_food,"Food"));
        ex_type.add(new PieEntry(am_docs,"Document"));
        ex_type.add(new PieEntry(am_trans,"Transport"));
        ex_type.add(new PieEntry(am_hotel,"Hotel"));
        ex_type.add(new PieEntry(am_others,"Others"));

        PieDataSet pieDataSet = new PieDataSet(ex_type, "Expense Type");
        pieDataSet.setColor(Color.GREEN);
        List pieData = new ArrayList<>();
        PieChartView pieChartView = view.findViewById(R.id.chart);
        PieChartData pieChartData;

        if(am_food == 0 && am_docs == 0 && am_hotel == 0 && am_trans == 0 && am_others == 0)
        {
            pieData.add(new SliceValue(100,Color.parseColor("#28A08D")));
            pieChartData = new PieChartData(pieData);
        }

        else {
            if (am_food != 0) {
                pieData.add(new SliceValue(am_food, Color.parseColor("#20C1BD")).setLabel("Food"));
            }
            if (am_docs != 0) {
                pieData.add(new SliceValue(am_docs, Color.parseColor("#B7569A")).setLabel("Documents"));
            }
            if (am_hotel != 0) {
                pieData.add(new SliceValue(am_hotel, Color.parseColor("#259F6C")).setLabel("Hotel"));
            }
            if (am_trans != 0) {
                pieData.add(new SliceValue(am_trans, Color.parseColor("#F5841A")).setLabel("Transport"));
            }
            if (am_others != 0) {
                pieData.add(new SliceValue(am_others, Color.parseColor("#810000")).setLabel("Others"));
            }

            pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);


        }

        pieChartData.setHasCenterCircle(true).setCenterText1("By Category").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#000000"));
        pieChartView.setPieChartData(pieChartData);


        return view;

    }
}