package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginMenuActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN_GOOGLE = 2;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Google google;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HideActionBar();
        CreateButtonLoginNormal();
        CreateButtonSignUp();
        SignInWithGoogle();
        User.setContext(this);
        User.setActivity(this);
        Restaurant.setActivity(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Đang tiến hành...");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void SignInWithGoogle()
    {

        google = new Google(this , RC_SIGN_IN_GOOGLE);
        google.RequestWithGoogle();
        Button btnLoginWithGoogle = (Button)findViewById(R.id.btnGoogle);
        btnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Support.checkConnect(LoginMenuActivity.this))
                {
                    TranAlertDialog alertDialog = new TranAlertDialog(
                            "CẢNH BÁO",
                            "Không tìm thấy kết nối mạng!\\nVui lòng kiểm tra lại kết nối của bạn!",
                            R.drawable.ic_baseline_warning_24);
                    alertDialog.show(getSupportFragmentManager(), "dialog");
                    return;
                }
                google.SignOut();
                google.SignIn();
                //dialog.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //dialog.dismiss();
    }

    private void HideActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void CreateButtonLoginNormal()
    {
        Button btnLoginNormal = (Button)findViewById(R.id.btnLoginNormal);
        btnLoginNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMenuActivity.this, LoginNormalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CreateButtonSignUp()
    {
        TextView btnSignUp = (TextView)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMenuActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE)
            google.onActivityResult(data);
    }
}