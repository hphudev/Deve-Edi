package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class LoginNormalActivity extends AppCompatActivity implements LoginNormal_Interface{

    int size = -1;
    private FirebaseFirestore db;
    private EditText username;
    private EditText password;
    private TextInputLayout layout;

    @Override
    public void VisiWarming() {
    }

    @Override
    public void ShowDialogWelcome() {
        TranAlertDialog dialog = new TranAlertDialog(
                "THÔNG BÁO",
                "Chào mừng bạn đến với DEVE EDI!",
                R.drawable.ic_baseline_info_2_24);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_normal);
        InitActionBar();
        CreateButtonLogin();
        CreateImangeButtonInfo();
        username = (EditText)findViewById(R.id.edtUserName);
        password = (EditText)findViewById(R.id.edtPassword);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        db = FirebaseFirestore.getInstance();

        User.setLoginNormalInterface(LoginNormalActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        final Handler handler = new Handler();
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                work();
//            }
//        };
//        thread.start();
    }


//    private void work ()
//    {
//        final Query query = db.collection("item");
//        Task<QuerySnapshot> a = query.get();
//        try {
//            QuerySnapshot b = Tasks.await(a);
//            if (a.isSuccessful())
//                size = b.size();
//            else
//                size = -2;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(LoginNormalActivity.this, String.valueOf(size), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng nhập");
    }

    private void HideActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void CreateImangeButtonInfo()
    {
        ImageButton imageButton = (ImageButton)findViewById(R.id.ibInfo);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginNormalActivity.this, ProductInformationActivity.class);
                        startActivity(intent);
                    }
                }, 500);
            }
        });
    }

    private void CheckAccount()
    {

        final Source source = Source.SERVER;
        if (!Support.checkConnect(this)) {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Không tìm thấy kết nối mạng!\nVui lòng kiểm tra lại kết nối của bạn!",
                    R.drawable.ic_baseline_warning_24);
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        int i = 0;
        String semail = username.getText().toString().trim();
        String spassword = password.getText().toString().trim();
        if (spassword.equals("") || semail.equals(""))
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Email hoặc mật khẩu còn trống!\nBạn vui lòng nhập đầy đủ thông tin!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hệ thống đang kiểm tra...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("email", semail)
                .whereEqualTo("password", spassword)
                .get(source)
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            if (task.getResult() != null && task.getResult().size() > 0)
                            {
                                User.setId(task.getResult().getDocuments().get(0).get("id").toString());
                                User.setPassword(task.getResult().getDocuments().get(0).get("password").toString());
                                User.setEmail(task.getResult().getDocuments().get(0).get("email").toString());
                                Restaurant.setId_user(User.getId());
                                db.collection("restaurant")
                                        .whereEqualTo("id_user", User.getId())
                                        .get(source)
                                        .addOnCompleteListener(LoginNormalActivity.this, new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful())
                                                {
                                                    if (task.getResult() != null && task.getResult().size() > 0)
                                                    {
                                                        Restaurant.setId(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments().get(0).get("id")).toString());
                                                        Restaurant.setPassword(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments().get(0).get("password")).toString());
                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                "THÔNG BÁO",
                                                                "Đăng nhập thành công",
                                                                R.drawable.ic_baseline_info_2_24
                                                        );
                                                        dialog.show(getSupportFragmentManager(), "dialog");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();
                                                                Intent intent = new Intent(LoginNormalActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }, 1000);
                                                    }
                                                    else
                                                    {
                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                "LỖI",
                                                                "Hệ thống đã gặp lỗi!",
                                                                R.drawable.ic_baseline_dangerous_24
                                                        );
                                                        dialog.show(getSupportFragmentManager(), "dialog");
                                                    }
                                                }
                                                else
                                                {
                                                    TranAlertDialog dialog = new TranAlertDialog(
                                                            "LỖI",
                                                            "Hệ thống đã gặp lỗi!",
                                                            R.drawable.ic_baseline_dangerous_24
                                                    );
                                                    dialog.show(getSupportFragmentManager(), "dialog");
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                TextView warming = (TextView)findViewById(R.id.textView5);
                                warming.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        warming.setVisibility(View.GONE);
                                    }
                                }, 5000);
                            }

                        }
                        else
                        {
                            TranAlertDialog dialog = new TranAlertDialog(
                                    "LỖI",
                                    "Hệ thống đã gặp lỗi!",
                                    R.drawable.ic_baseline_dangerous_24
                            );
                            dialog.show(getSupportFragmentManager(), "dialog");
                        }
                    }
                })
        .addOnCanceledListener(this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                progressDialog.dismiss();
                TextView warming = (TextView)findViewById(R.id.textView5);
                warming.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        warming.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });
    }

    private void CreateButtonLogin()
    {
        Button btnLogin = (Button)findViewById(R.id.btnLoginOfNormalLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim() == "" || password.getText().toString().trim() == "")
                {
                    TranAlertDialog dialog = new TranAlertDialog(
                            "CẢNH BÁO",
                            "Email hoặc mật khẩu còn trống!\nVui lòng điền đầy đủ!",
                            R.drawable.ic_baseline_warning_24);
                    dialog.show(getSupportFragmentManager(), "dialog");
                    return;
                }
                CheckAccount();
            }
        });
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


}
