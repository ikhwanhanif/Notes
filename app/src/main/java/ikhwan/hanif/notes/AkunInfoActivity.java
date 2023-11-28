package ikhwan.hanif.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AkunInfoActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView textViewEmail, deleteAkunTV, akunName;
    ImageView deleteAkunIV,backBtn;
    ShapeableImageView akunImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_info);

        akunImage = findViewById(R.id.gambarAkun);
        akunName = findViewById(R.id.namaAkun);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        Glide.with(AkunInfoActivity.this).load(firebaseUser.getPhotoUrl()).into(akunImage);
        akunName.setText(firebaseUser.getDisplayName());

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AkunInfoActivity.this, MainActivity.class));
                finish();

            }
        });

        deleteAkunTV = findViewById(R.id.deleteAkunTV);
        deleteAkunIV = findViewById(R.id.deleteAkunIV);
        deleteAkunTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AkunInfoActivity.this);
                builder.setTitle("Hapus Akun");
                TextView textView = new TextView(AkunInfoActivity.this);
                textView.setText("anda yakin untuk Hapus Akun?");
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                LinearLayout linearLayout = new LinearLayout(AkunInfoActivity.this);
                linearLayout.addView(textView);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setPadding(10,10,10,10);
                builder.setIcon(getResources().getDrawable(R.drawable.baseline_warning_24));
                builder.setView(linearLayout);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DeleteAkun();

                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                builder.create().show();

            }

        });
        deleteAkunIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AkunInfoActivity.this);
                builder.setTitle("Hapus Akun");
                TextView textView = new TextView(AkunInfoActivity.this);
                textView.setText("anda yakin untuk Hapus Akun?");
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                LinearLayout linearLayout = new LinearLayout(AkunInfoActivity.this);
                linearLayout.addView(textView);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setPadding(10,10,10,10);
                builder.setIcon(getResources().getDrawable(R.drawable.baseline_warning_24));
                builder.setView(linearLayout);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DeleteAkun();

                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                builder.create().show();

            }
        });



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        textViewEmail = findViewById(R.id.emailAkun);

        textViewEmail.setText(user.getEmail());

    }

    private void DeleteAkun() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AkunInfoActivity.this,"Sukses Menghapus Akun",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AkunInfoActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });



    }
}