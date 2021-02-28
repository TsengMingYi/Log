package com.example.secondlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText input_edit;
    private Button send_btn;
    private DocumentReference room1Reference =
            FirebaseFirestore.getInstance()
                    .collection("chatroom")
                    .document("room1");

    private MessageAdapter messageAdapter = new MessageAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerview);
        input_edit = findViewById(R.id.input_edit);
        send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = AccountManager.getInstance().getCurrentUID();
                String message = input_edit.getText().toString();
//                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
//                Date date = new Date();
//                String timestamp = sdFormat.format(date);
                long timestamp = System.currentTimeMillis();
                Log.e("Hello",timestamp+"");
                String sendData = uid + ":" + timestamp + ":" + message;

                Log.e("test", sendData);
                Map<String, Object> data = new HashMap<>();
                data.put("message", FieldValue.arrayUnion(sendData));
                room1Reference.update(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.e("test", "success ");
                                } else {
                                    Log.e("test", "error " + task.getException());
                                }
                            }
                        });
            }
        });

        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        room1Reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                List<String> messages = (List<String>)documentSnapshot.get("message");
                messageAdapter.setMessages(messages);
            }
        });
    }
}