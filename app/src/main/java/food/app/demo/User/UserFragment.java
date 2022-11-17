package food.app.demo.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import food.app.demo.MainActivity;
import food.app.demo.R;
import food.app.demo.login.LoginActivity;

public class UserFragment extends Fragment {

    View view;
    Button logout_btn;
    MainActivity mainActivity;

    public UserFragment() {
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
        view = inflater.inflate(R.layout.fragment_user, container, false);
        logout_btn = view.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogout();
            }
        });
        return view;
    }

    private void confirmLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Log out");
        builder.setMessage("Do you want to log out?");
        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
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