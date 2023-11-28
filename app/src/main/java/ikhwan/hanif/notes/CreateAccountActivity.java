package ikhwan.hanif.notes;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //membuat animasi background
        RelativeLayout relativeLayout = findViewById(R.id.ly);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //inisialisasi :
        emailEditText = findViewById(R.id.email_edit_text);//inisialisasi EdtTxt(email)
        passwordEditText = findViewById(R.id.password_edit_text);//inisialisasi EdtTxt(password)
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);//inisialisasi EdtTxt(confirm password)
        createAccountBtn = findViewById(R.id.create_account_btn);//inisialisasi Btn(buat akun)
        progressBar = findViewById(R.id.progress_bar);//inisialisasi progressBar
        loginBtnTextView = findViewById(R.id.login_text_view_btn);//inisialisasi TxtView(ke laman login)

        createAccountBtn.setOnClickListener(v-> createAccount());//perintah ketika Btn(buat akun) di klik

        //perintah ketika TxtView(ke laman login) di klik
        loginBtnTextView.setOnClickListener(v-> {
            finish();//kembali ke laman sebelumnya(laman login)
        });


    }

    void createAccount(){
        //mengambil text menjadi string
        String email  = emailEditText.getText().toString();
        String password  = passwordEditText.getText().toString();
        String confirmPassword  = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);


    }

    void createAccountInFirebase(String email,String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            //jika berhasil membuat akun
                            Utility.showToast(CreateAccountActivity.this,"berhasil membuat akun, cek email anda untuk verifikasi.");//memunculkan pesan berhasi
                            firebaseAuth.getCurrentUser().sendEmailVerification();//firebase mengirim verifikasi email
                            firebaseAuth.signOut();//mengeluarkan akun agar bisa masuk secara manual ketika laman berganti ke laman login
                            finish();
                        }else{
                            //gagal membuat akun
                            Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());//memunculkan pesan gagal
                        }
                    }
                }
        );



    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);//membuat progressBar muncul/terlihat
            createAccountBtn.setVisibility(View.GONE);//ketika progressBar terlihat, Btn(buat akun) dihilangkan
        }else{
            progressBar.setVisibility(View.GONE);//membuat progressBar tidak terlihat/hilang
            createAccountBtn.setVisibility(View.VISIBLE);//ketika progressBar tidak terlihat, Btn(buat akun) dimunculkan
        }
    }

    boolean validateData(String email,String password,String confirmPassword){
        //memvalidasi data yang di input oleh user.

        //jika alamat email tidak cocok/salah :
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("masukkan email yang benar.");//memunculkan pesan error
            return false;
        }
        //jika panjang password kurang dari 6 karakter :
        if(password.length()<6){
            passwordEditText.setError("panjang password harus setidaknya 6 karakter atau lebih.");//memunculkan pesan error
            return false;
        }
        //jika password dan confirm password tidak sama :
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("password dan confirm password harus sama.");//memunculkan pesan error
            return false;
        }
        return true;
    }

}