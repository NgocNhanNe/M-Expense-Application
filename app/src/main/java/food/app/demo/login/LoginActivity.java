package food.app.demo.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import food.app.demo.MainActivity;
import food.app.demo.R;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText employeeID, password;
    MaterialButton login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        employeeID = findViewById(R.id.email_txt);
        password = findViewById(R.id.password_txt);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeID.getText().toString().equals("EM002") && password.getText().toString().equals("12345")){
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login fail!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}