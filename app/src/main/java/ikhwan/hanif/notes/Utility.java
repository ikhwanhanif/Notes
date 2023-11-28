package ikhwan.hanif.notes;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context,String message){
        //mengatur pesan
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")//membuat collection(di firebase database) notes
                .document(currentUser.getUid()).collection("my_notes");//membuat collection(di firebase database) my_notes
    }

    static String timestampToString(Timestamp timestamp){
        //mengatur date(jam dan tanggal)
        return new SimpleDateFormat("HH:mm | dd MMM yyyy").format(timestamp.toDate());
    }

}
