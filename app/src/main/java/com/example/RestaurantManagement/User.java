package com.example.RestaurantManagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class User {

    static private String email = "";
    static private String id = "";
    static private String password = "";
    static private Activity activity;
    static private Context context;
    static int count;
    static  private LoginNormal_Interface loginNormalInterface;
    public User() {
    }

    static public void AddUser()
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User.setCount(-1);
        User.setPassword("");
        db.collection("user")
                .whereEqualTo("email", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                           //Toast.makeText(getContext(), String.valueOf(task.getResult().size()), Toast.LENGTH_SHORT).show();
                            if (task.getResult().size() != 0)
                            {
                                User.setId(task.getResult().getDocuments().get(0).get("id").toString());
                                User.setPassword(task.getResult().getDocuments().get(0).get("password").toString());
                                Restaurant.setId_user(User.getId());
                                Restaurant.setIdFromIdUser();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        activity.startActivity(intent);
                                    }
                                }, 1000);
                            }
                            else
                            {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setMessage("Hệ thống đang đăng ký...");
                                progressDialog.show();
                                db.collection("user").get()
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int count = 0;
                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                        count++;
                                                    }
                                                    setCount(count + 1);
                                                    setId(String.valueOf(getCount()));
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("id", String.valueOf(getCount()));
                                                    user.put("email", getEmail());
                                                    user.put("password", getPassword());
                                                    db.collection("user")
                                                            .add(user)
                                                            .addOnSuccessListener(User.getActivity(), new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    db.collection("restaurant")
                                                                            .get()
                                                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        int count = 0;
                                                                                        for (DocumentSnapshot document : task.getResult())
                                                                                        {
                                                                                            count++;
                                                                                        }
                                                                                        Map<String, Object> resta = new HashMap<>();
                                                                                        resta.put("id", String.valueOf(count + 1));
                                                                                        String s = User.getId();
                                                                                        resta.put("id_user", s);
                                                                                        Random r = new Random();
                                                                                        int i1 = r.nextInt(1000000 - 100000) + 100000;
                                                                                        resta.put("password", String.valueOf(i1));
                                                                                        db.collection("restaurant")
                                                                                                .add(resta)
                                                                                                .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                                        Restaurant.setId_user(Objects.requireNonNull(resta.get("id_user")).toString());
                                                                                                        Restaurant.setId(Objects.requireNonNull(resta.get("id")).toString());
                                                                                                        Restaurant.setPassword(Objects.requireNonNull(resta.get("password")).toString());
                                                                                                        progressDialog.dismiss();
                                                                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                                                                "THÔNG BÁO",
                                                                                                                "Đã đăng ký thành công!",
                                                                                                                R.drawable.ic_baseline_info_2_24
                                                                                                        );
                                                                                                        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                                                                        new Handler().postDelayed(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                dialog.dismiss();
                                                                                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                                                                                activity.startActivity(intent);
                                                                                                            }
                                                                                                        }, 1000);

                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });
    }

    public static LoginNormal_Interface getLoginNormalInterface() {
        return loginNormalInterface;
    }

    public static void setLoginNormalInterface(LoginNormal_Interface loginNormalInterface) {
        User.loginNormalInterface = loginNormalInterface;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        User.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        User.context = context;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        User.count = count;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }
}
