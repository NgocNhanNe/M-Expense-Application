package food.app.demo.ExpenseInTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import food.app.demo.R;


public class ExportDataFragment extends Fragment {

    View view;
    String FileName = "DownloadJsonFile.txt";
    ArrayList<String> SaveListDataExport= new ArrayList<>();
    TextView showJson;

    public ExportDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_export_data, container, false);

        showJson = view.findViewById(R.id.ShowJson);

        SaveListDataExport = readFile(FileName);

        showJson.setText(SaveListDataExport.get(0));


        // Inflate the layout for this fragment
        return view;
    }

    public ArrayList<String> readFile(String file) {
        ArrayList<String> text = new ArrayList<>();
        try {
            FileInputStream fis = getContext().openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            text = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return text;
    }
}