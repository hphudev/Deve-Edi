package com.example.RestaurantManagement;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Objects;

public class Restaurant {
    static private String id;
    static private String id_user;
    static private String password;
    static Activity activity;
    static Context context;
    public Restaurant() {
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Restaurant.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Restaurant.context = context;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Restaurant.password = password;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Restaurant.id = id;
    }

    public static void setIdFromIdUser()
    {
        final Source source;
        if (!Support.checkConnect(getActivity()))
        {
            source = Source.CACHE;
        }
        else
            source = Source.SERVER;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurant")
                .whereEqualTo("id_user", getId_user())
                .get(source)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = task.getResult().size();
                        Restaurant.setId(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments().get(0).get("id")).toString());
                        Restaurant.setPassword(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments().get(0).get("password")).toString());
                    }
                });
    }
    public static String getId_user() {
        return id_user;
    }

    public static void setId_user(String id_user) {
        Restaurant.id_user = id_user;
    }
}
