package com.example.secondlog;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class AccountManager {
    private static AccountManager instance = new AccountManager();

    private String currentUID = "";

    public String getCurrentUID() {
        return currentUID;
    }

    public static AccountManager getInstance() {
        return instance;
    }
    private AccountManager(){
    }

    public static boolean isUidValid(String uid){
        //todo check uid
        return uid!=null && uid.length()>0;
    }

    public static boolean isPassValid(String pass){
        return pass!=null && pass.length()>0;
    }


    /**
     * this method is used to register, not check the uid and password
     * @param uid
     * @param pssword
     */
    public void registerAccountToServer(final Context context,final String uid, final String pssword){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("accountPool")
                .get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> documents =  task.getResult().getDocuments();

                            for(DocumentSnapshot documentSnapshot : documents){
                                documentSnapshot.getId();
                                if(documentSnapshot.getId().equals(uid)){
                                    Log.e("test","account already exist");
                                    return;
                                }
                            }

                            Map<String, Object> data = new HashMap<>();
                            data.put("password",pssword);
                            db.collection("accountPool")
                                    .document(uid)
                                    .set(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context, "registration success", Toast.LENGTH_SHORT).show();
                                                Log.e("test","success");
                                            }else {
                                                Log.e("test","fail: "+ task.getException());
                                            }
                                        }
                                    });

                        }else {
                            Log.e("test","fail: "+ task.getException());
                        }
                    }
                });



    }


    public void loginAccountToServer(final String uid, final String password, final Context context,final Runnable runnable){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("accountPool")
                .document(uid)
                .get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                if(password.equals(documentSnapshot.get("password"))){
                                    currentUID = uid;
                                    runnable.run();
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                }else {
                                    Log.e("test","login fail: password incorrect");
                                    Toast.makeText(context, "password incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Log.e("test","not exist");
                                Toast.makeText(context, "not exist", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Log.e("test","login fail: "+ task.getException());
                        }
                    }
                });


    }
}
