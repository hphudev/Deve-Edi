package com.example.RestaurantManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.example.RestaurantManagement.ui.restaurant_menu.RestaurantMenuFragment;
import com.example.RestaurantManagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Dialog_string_interface, Dialog_string_two_content_interface, RestaurantMenuFragment.OnSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private int navIDChoice = R.id.nav_sell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.tvEmailUI);
        navUsername.setText(User.getEmail());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sell, R.id.nav_res_menu, R.id.nav_report)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                navIDChoice = menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                switch (menuItem.getItemId())
                {
//                    case R.id.nav_save_data:
//                        onCreateBackupActivity();
//                        break;
//                    case R.id.nav_recovery:
//                        onCreateRecoveryActivity();
//                        break;
//                    case R.id.nav_connect_account:
//                        onCreateConnectAccountActivity();
//                        break;
                    case  R.id.nav_your_opinion:
                        composeEmail("Deve Edi - Phản hồi từ người dùng");
                        break;
                    case R.id.nav_recovery_password:
                        onCreateDialogChangePassword(true);
                        break;
                    case R.id.nav_information:
                        onCreateProductInformationActivity();
                        break;
                    case  R.id.nav_sign_out:
                        finish();
                        break;
                    default:
                        break;
                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void composeEmail(String subject) {
        String[] addresses = {/*ycc.yennhi@gmail.com", */"deveediapp@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add:
                NavigationView navigationView = findViewById(R.id.nav_view);
                if (navigationView.getCheckedItem().getItemId() == R.id.nav_res_menu)
                {
                    onCreateAddItemActivity();
                }
                if (navigationView.getCheckedItem().getItemId() == R.id.nav_sell)
                {
                    onCreateChooseItemActivity();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateAddItemActivity()
    {
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "Thêm món");
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    private void onCreateBackupActivity()
    {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent = new Intent(MainActivity.this, BackupDataActivity.class);
                                          startActivity(intent);
                                      }
                                  }, 500);

    }

    private void onCreateRecoveryActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, RecoveryActivity.class);
                startActivity(intent);
            }
        }, 500);

    }
    private void onCreateConnectAccountActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ConnectAccountActivity.class);
                startActivity(intent);
            }
        }, 500);

    }
    private void onCreateChooseItemActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ChooseItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_table", null);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        }, 500);

    }
    private void onCreateProductInformationActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ProductInformationActivity.class);
                startActivity(intent);
            }
        }, 500);

    }
    private void onCreateDialogChangePassword(boolean type)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type)
                {
                    DialogStringTwoContentCustom dialog = new DialogStringTwoContentCustom(
                            "Thay đổi mật khẩu",
                            "Thay đổi mật khẩu",
                            "Nhập mật khẩu mới",
                            "Nhập lại mật khẩu",
                            "",
                            "",
                            -1,
                            MainActivity.this,
                            MainActivity.this);
                    dialog.ShowDialogString(Gravity.CENTER);
                }
                else
                {
                    DialogStringCustom dialogStringCustom = new DialogStringCustom(
                            MainActivity.this, MainActivity.this,"Thay đổi mật khẩu", "Thay đổi mật khẩu",
                            "Nhập mật khẩu mới", "", -1);
                    dialogStringCustom.ShowDialogString(Gravity.CENTER);
                }
            }
        }, 500);

    }

    @Override
    public void onButtonSaveClicked(int position, String contentOne, String contentTwo) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang đổi mật khẩu...");
        progressDialog.show();
        User.setPassword(contentOne);
        Map<String, Object> user = new HashMap<>();
        user.put("id", User.getId());
        user.put("email", User.getEmail());
        user.put("password", User.getPassword());
        FirebaseFirestore.getInstance().collection("user")
                .whereEqualTo("email", User.getEmail())
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().getDocuments().size() > 0)
                        {
                            FirebaseFirestore.getInstance().collection("user")
                                    .document(task.getResult().getDocuments().get(0).getId())
                                    .set(user, SetOptions.merge())
                                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            TranAlertDialog dialog = new TranAlertDialog(
                                                    "THÀNH CÔNG",
                                                    "Đã đổi mật khẩu thành công!",
                                                    R.drawable.ic_settings_suggest_24
                                            );
                                            dialog.show(getSupportFragmentManager(), "dialog");
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onButtonSaveClicked(int position, String content) {

        Toast.makeText(this, "Thay đổi thành công 2!", Toast.LENGTH_SHORT).show();
    }
}