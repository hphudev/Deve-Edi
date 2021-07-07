package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements JavaMailAPI.onInterface, Dialog_string_interface{

    EditText edtUsername;
    EditText edtPassword;
    Button bRegis;
    FirebaseFirestore db;
    int otp = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitActionBar();
        edtUsername = (EditText)findViewById(R.id.edtUserName);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        bRegis = (Button)findViewById(R.id.button);
        db = FirebaseFirestore.getInstance();
        CreateButtonRegis();
    }

    private void AddAccout()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Hệ thống đang đăng ký...");
        progressDialog.show();
        db.collection("user")
                .whereEqualTo("email", edtUsername.getText().toString().trim())
                .get()
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().size() == 0)
                            {
                                db.collection("user")
                                        .get()
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful())
                                                {
                                                    int count = 0;
                                                    for (DocumentSnapshot document : task.getResult())
                                                    {
                                                        count++;
                                                    }
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("id", String.valueOf(count + 1));
                                                    user.put("email", edtUsername.getText().toString().trim());
                                                    user.put("password", edtPassword.getText().toString().trim());
                                                    db.collection("user")
                                                            .add(user)
                                                            .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    User.setId(user.get("id").toString());
                                                                    User.setEmail(user.get("email").toString());
                                                                    User.setPassword(user.get("password").toString());
                                                                    db.collection("restaurant")
                                                                            .get()
                                                                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<QuerySnapshot>() {
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
                                                                                                .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<DocumentReference>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                                        progressDialog.dismiss();
                                                                                                        Restaurant.setId(resta.get("id").toString());
                                                                                                        Restaurant.setId_user(resta.get("id_user").toString());
                                                                                                        Restaurant.setPassword(resta.get("password").toString());
                                                                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                                                                "THÔNG BÁO",
                                                                                                                "Đã đăng ký thành công!",
                                                                                                                R.drawable.ic_baseline_info_2_24
                                                                                                        );
                                                                                                        dialog.show(getSupportFragmentManager(), "dialog");
                                                                                                        new Handler().postDelayed(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                dialog.dismiss();
                                                                                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                                                                startActivity(intent);
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
                            else
                            {
                                progressDialog.dismiss();
                                TranAlertDialog dialog = new TranAlertDialog(
                                        "CẢNH BÁO",
                                        "Email này đã tồn tại!\nVui lòng chọn email khác!",
                                        R.drawable.ic_baseline_warning_24
                                );
                                dialog.show(getSupportFragmentManager(), "dialog");
                            }
                        }
                    }
                });

    }

    private void CreateButtonRegis() {
        bRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().trim().equals("") || edtUsername.getText().toString().trim().equals(""))
                {
                    TranAlertDialog dialog = new TranAlertDialog(
                            "CẢNH BÁO",
                            "Email hoặc mật khẩu còn trống!\nBạn vui lòng nhập đầy đủ thông tin!",
                            R.drawable.ic_baseline_warning_24
                    );
                    dialog.show(getSupportFragmentManager(), "dialog");
                    return;
                }
                SendMail();
            }
        });
    }

    private void SendMail()
    {
        String toEmail = edtUsername.getText().toString().trim();
        if (toEmail.equals(""))
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Vui lòng nhập email!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang kiểm tra...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("email", toEmail)
                .get(Source.SERVER)
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful() && task.getResult().getDocuments().size() > 0)
                        {
                            TranAlertDialog dialog = new TranAlertDialog(
                                    "CẢNH BÁO",
                                    "Tài khoản này đã tồn tại!",
                                    R.drawable.ic_baseline_warning_24
                            );
                            dialog.show(getSupportFragmentManager(), "dialog");
                        }
                        else
                        {
                            Random r = new Random();
                            otp = r.nextInt(1000000 - 100000) + 100000;
                            String message = "Mã xác nhận đăng ký của bạn là: " + otp + ".";
                            String subject = "DEVE EDI - ĐĂNG KÝ TÀI KHOẢN";
                            JavaMailAPI javaMailAPI = new JavaMailAPI(RegisterActivity.this, toEmail, subject, message);
                            javaMailAPI.execute();
                        }
                    }
                });

    }
    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng ký");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSendSuccess() {
        DialogStringCustom dialog = new DialogStringCustom(
                this,
                null,
                "NHẬP MÃ XÁC NHẬN EMAIL",
                "Nhập mã xác nhận email",
                "Vui lòng nhập mã nhận email",
                "",
                0
        );
        dialog.ShowDialogString(Gravity.CENTER);
    }

    @Override
    public void onButtonSaveClicked(int position, String content) {
        if (String.valueOf(otp).equals(content))
        {
            AddAccout();
            otp = -1;
        }
        else
        {
            otp = -1;
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Mã xác nhận đã bị hủy vì không đúng!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }
}