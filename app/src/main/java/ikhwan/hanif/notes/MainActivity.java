package ikhwan.hanif.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.CaseMap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton logOutBtn;
    NoteAdapter noteAdapter;
    TextView textView, buatNote;
    ImageView akunInfo,devInfo;

    FirebaseAuth auth;
    FirebaseUser user;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        instance = this;

        buatNote = findViewById(R.id.buatNoteText);


        devInfo = findViewById(R.id.devInfo);
        devInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, DevInfoActivity.class));

            }
        });

        akunInfo = findViewById(R.id.akunInfo);
        akunInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AkunInfoActivity.class));
                finish();

            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        textView = findViewById(R.id.page_title);

        //membuat animasi background
        RelativeLayout relativeLayout = findViewById(R.id.ly);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //inisialisasi :
        addNoteBtn = findViewById(R.id.add_note_btn);//inisialisasi Btn(tambah note)
        recyclerView = findViewById(R.id.recyler_view);//inisialisasi recyclerView(note yang telah jadi)
        logOutBtn = findViewById(R.id.logout_btn);//inisialisasi Btn(keluar/logout)

        //perintah ketika Btn(tambah note) di klik
        addNoteBtn.setOnClickListener((v)-> {
            startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class));//berganti ke laman NoteDetailsActivity(Add, Edit, Delete note)

        });
        //perintah ketika Btn(keluar/logout) di klik
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();//mengeluarkan akun
                Toast.makeText(MainActivity.this,"Keluar",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));//laman berganti ke laman login
                finish();

            }
        });
        setupRecyclerView();
    }

    public static MainActivity getInstance(){

        return instance;

    }
    public void myMethod(){

        buatNote.setVisibility(View.GONE);

    }

    void setupRecyclerView(){

        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);//mengambil timestamp di class Utility, memunculkan date(jam dan tanggal) ke recyclerView(note yang telah jadi)
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//memunculkan recyclerView(note yang telah jadi)
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}