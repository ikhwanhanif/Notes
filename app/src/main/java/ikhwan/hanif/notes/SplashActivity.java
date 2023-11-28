package ikhwan.hanif.notes;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //membuat animasi background
        RelativeLayout relativeLayout = findViewById(R.id.ly);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser==null){

                    //jika sebelumnya user logout atau belum mempunyai akun maka laman akan berganti ke laman login
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else{
                    //jika user telah login/tidak mengeluarkan akun(logout), maka laman akan berganti ke laman main
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
                finish();
            }
        },5000);//delay sebelum laman berganti

    }
}