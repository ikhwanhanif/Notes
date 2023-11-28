package ikhwan.hanif.notes;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView, lupaPassword;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    SignInButton btnSignIn;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignIn);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("783089958411-ucavr3eh3m6mt95o9m4ga13tsv918nk8.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        btnSignIn.setOnClickListener(view -> {

            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, 100);

        });
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }



        lupaPassword = findViewById(R.id.lupaPasswordTV);
        lupaPassword.setOnClickListener(view -> lupaPasswordDialog());




        //membuat animasi background
        RelativeLayout relativeLayout = findViewById(R.id.ly);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //inisialisasi :
        emailEditText = findViewById(R.id.email_edit_text);//inisialisasi EdtTxt(email)
        passwordEditText = findViewById(R.id.password_edit_text);//inisialisasi EdtTxt(password)
        loginBtn = findViewById(R.id.login_btn);//inisialisasi Btn(login akun)
        progressBar = findViewById(R.id.progress_bar);//inisialisasi progressBar
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);//inisialisasi TxtView(ke laman daftar)

        loginBtn.setOnClickListener((v)-> loginUser() );//perintah ketika Btn(login akun) di klik

        //perintah ketika TxtView(ke laman daftar) di klik
        createAccountBtnTextView.setOnClickListener((v)-> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));//berganti ke laman daftar
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Autentikasi Google Berhasil";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    displayToast("Berhasil Masuk");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Autentikasi Gagal :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void displayToast(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

    }

    private void lupaPasswordDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Setel Ulang Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailET = new EditText(this);
        emailET.setMinEms(20);
        emailET.setHint("email anda");
        emailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailET.setGravity(Gravity.CENTER);
        linearLayout.addView(emailET);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setGravity(Gravity.CENTER);
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (emailET.getText().toString().isEmpty()){

                    Toast.makeText(LoginActivity.this,"isi email anda",Toast.LENGTH_SHORT).show();
                    lupaPasswordDialog();

                }
                else {

                    String email = emailET.getText().toString().trim();
                    beginRecovery(email);

                }



            }
        });
        builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        builder.create().show();

    }

    private void beginRecovery(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(LoginActivity.this,"request sent, Periksa Email Anda",Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Failed",Toast.LENGTH_LONG).show();

            }
        });

    }

    void loginUser(){

        //mengambil text menjadi string
        String email  = emailEditText.getText().toString();
        String password  = passwordEditText.getText().toString();


        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }

    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //jika berhasil login
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //pergi ke laman main
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        Utility.showToast(LoginActivity.this,"email belum terverifikasi, cek email anda untuk verifikasi.");//memunculkan pesan error, jika email belum terverifikasi
                    }

                }else{
                    //login gagal
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());//memunculkan pesan error, jika tidak ada jaringan
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);//membuat progressBar muncul/terlihat
            loginBtn.setVisibility(View.GONE);//ketika progressBar terlihat, Btn(login akun) dihilangkan
        }else{
            progressBar.setVisibility(View.GONE);//membuat progressBar tidak terlihat/hilang
            loginBtn.setVisibility(View.VISIBLE);//ketika progressBar tidak terlihat, Btn(login akun) dimunculkan
        }
    }

    boolean validateData(String email,String password){
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
        //jika password yang anda masukkan tidak sesuai dengan yang anda daftarkan
        if (!password.matches(password)){
            passwordEditText.setError("password yang anda masukkan salah.");//memunculkan pesan error
            return false;

        }
        return true;
    }

}