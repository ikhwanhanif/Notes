package ikhwan.hanif.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Locale;
import java.util.Random;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);//mengganti/menampilkan judul dengan judul yang di input user
        holder.contentTextView.setText(note.content);//mengganti/menampilkan isi dengan isi yang di input user
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));//mengganti date(jam dan tanggal) sesuai kapan user mengupdate note

        MainActivity.getInstance().myMethod();

        Random random = new Random();
        int red = random.nextInt(255 - 0 + 1) + 0;
        int green = random.nextInt(255 - 0 +1) + 0;
        int blue = random.nextInt(255 - 0 + 1) + 0;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.rgb(red, green, blue));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(16);
        holder.layout.setBackground(drawable);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.title);//mengambil judul di firebase database(yang di imput user) ke judul(note)
            intent.putExtra("content",note.content);//mengambil isi di firebase database(yang di imput user) ke isi(note)
            String docId = this.getSnapshots().getSnapshot(position).getId();//inisialisasi dokumen id
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timestampTextView;
        LinearLayout layout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            //inisialisasi :
            titleTextView = itemView.findViewById(R.id.note_title_text_view);//inisialisasi TextView(judul)
            contentTextView = itemView.findViewById(R.id.note_content_text_view);//inisialisasi TextView(isi)
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);//inisialisasi TextView(date(jam dan tanggal))
            layout = itemView.findViewById(R.id.layoutBox);

        }
    }
}
