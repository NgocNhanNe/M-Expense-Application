package food.app.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import food.app.demo.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    TextView tv_wel, tv_mexpense;
    private static int Splash_timeout = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_wel = findViewById(R.id.tv_welcome);
        tv_mexpense = findViewById(R.id.tv_mexpense);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashintent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(splashintent);
                finish();
            }
        },Splash_timeout);

        Animation myannotation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animation1);
        tv_wel.startAnimation(myannotation);
        Animation myannotation1 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animation);
        tv_mexpense.startAnimation(myannotation1);

    }
}


