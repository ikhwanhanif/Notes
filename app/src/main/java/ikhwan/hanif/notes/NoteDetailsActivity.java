package ikhwan.hanif.notes;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Random;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;
    ImageView deleteNoteImageViewBtn;
    LinearLayout deleteLayout, layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        layout = findViewById(R.id.ln1);

        Random random = new Random();
        int red = random.nextInt(255 - 0 + 1) + 0;
        int green = random.nextInt(255 - 0 +1) + 0;
        int blue = random.nextInt(255 - 0 + 1) + 0;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.rgb(red, green, blue));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(16);

        layout.setBackground(drawable);


        //membuat animasi background
        RelativeLayout relativeLayout = findViewById(R.id.ly);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //inisialisasi...
        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn  = findViewById(R.id.delete_note_text_view_btn);
        deleteNoteImageViewBtn  = findViewById(R.id.delete_note_image_view_btn);
        deleteLayout = findViewById(R.id.deleteLayout);

        //menerima/mengubah data
        title = getIntent().getStringExtra("title");//data(judul)
        content= getIntent().getStringExtra("content");//data(isi)
        docId = getIntent().getStringExtra("docId");//data(dokumen id)

        //jika dokumen id tidak kosong(null) maka bisa di edit
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);//mengubah EditText(judul)
        contentEditText.setText(content);//mengubah EditText(isi)

        if(isEditMode){
            pageTitleTextView.setText("Edit");//mengubah pageTitleAwal(Add) menjadi (Edit) jika dalam mode edit
            deleteLayout.setVisibility(View.VISIBLE);//memunculkan text/btn(delete)
        }

        saveNoteBtn.setOnClickListener( (v)-> {
            saveNote();
        });

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );
        deleteNoteImageViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("kolom judul harus terisi");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //membuat note baru
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //ketika berhasil menambahkan note
                    Utility.showToast(NoteDetailsActivity.this,"berhasil menambahkan note");
                    finish();
                }else{
                    //gagal menambahkan note
                    Utility.showToast(NoteDetailsActivity.this,"gagal menambahkan note");
                }
            }
        });

    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //ketika berhasil menghapus note
                    Utility.showToast(NoteDetailsActivity.this,"berhasil menghapus note");
                    finish();
                }else{
                    //gagal menghapus note
                    Utility.showToast(NoteDetailsActivity.this,"gagal menghapus note");
                }
            }
        });
    }


}