package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;

public class UnitActivity extends AppCompatActivity implements RecycleViewInUnitClickInterFace, Dialog_string_interface, Unit_interface {

    private RecyclerView recyclerView;
    private UnitAdapter unitAdapter;
    static public UnitViewModel unitViewModel;
    private FirebaseFirestore db;
    private Button Finish;
    private int scrollPositionX = 0;
    private int scrollPositionY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        InitActionBar();
        onCreateRecycleView();
        onCreateButtonFinish();
        InitFireStore();
        Unit.setActivity(this);
        Unit.setUnit_interface(this);
        Unit.setContext(this);
    }

    private void InitFireStore()
    {
        db = FirebaseFirestore.getInstance();
    }

    private void onCreateButtonFinish()
    {
        Finish = (Button)findViewById(R.id.btnFinish);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        int postion = unitViewModel.getPosition();
        if (postion != -1)
        {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("NameUnit", unitViewModel.getUnit(postion).getName());
            bundle.putString("IdUnit", unitViewModel.getIdUnit());
            intent.putExtra("data", bundle);
            setResult(RESULT_OK, intent);
        }
        else
        {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
        super.finish();
    }

    private void onCreateRecycleView()
    {
        recyclerView = (RecyclerView)findViewById(R.id.rcvUnit);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        unitViewModel.getmListUnitLiveData().observe(this, new Observer<List<Unit>>() {
            @Override
            public void onChanged(List<Unit> units) {
                unitAdapter = new UnitAdapter(units, UnitActivity.this);
                int posX = recyclerView.computeHorizontalScrollOffset();
                int posY = recyclerView.computeVerticalScrollOffset();
                recyclerView.setAdapter(unitAdapter);
                recyclerView.scrollToPosition(unitViewModel.getPosition() - 3);
                //Toast.makeText(UnitActivity.this ,String.valueOf(recyclerView.getItemDecorationCount()),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Minh họa
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String unitPresent = bundle.getString("present_unit");
        unitViewModel.ClearListUnit();
        ProgressDialog progressDialog = new ProgressDialog((this));
        progressDialog.setTitle("Vui lòng chờ");
        progressDialog.show();
        Unit.GetAllUnitOnFireStore(db, unitViewModel, unitPresent);
        progressDialog.dismiss();
    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đơn vị tính");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_add:
                onActionAddItemClicked();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onActionAddItemClicked()
    {
        DialogStringCustom dialogStringCustom = new DialogStringCustom(
                this, UnitActivity.this,"Thêm đơn vị tính", "Thêm đơn vị tính",
                "Nhập đơn vị tính", "", -1);
        dialogStringCustom.ShowDialogString(Gravity.CENTER);
    }

    @Override
    public void onItemClick(int position) {
        if (unitViewModel.getPosition() == position)
        {
            return;
        }
        unitViewModel.setPosition(position);
        unitViewModel.getUnit(position).setChecked(true);
        unitViewModel.SetUnit(position, unitViewModel.getUnit(position));
        for (int i = 0; i < unitViewModel.getSize(); i++)
        {
            if (i == position)
                continue;
            unitViewModel.getUnit(i).setChecked(false);
            unitViewModel.SetUnit(i, unitViewModel.getUnit(i));
        }
    }

    @Override
    public void onImageButtonInItemClick(int position) {
        DialogStringCustom dialogStringCustom = new DialogStringCustom(
                this, UnitActivity.this,"Sửa đơn vị tính", "Sửa đơn vị tính",
                 "Nhập đơn vị tính", unitViewModel.getUnit(position).getName(), position);
        dialogStringCustom.ShowDialogString(Gravity.CENTER);
    }

    @Override
    public void onImageButtonDeleteClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CÂU HỎI");
        builder.setMessage("Bạn có muốn xóa đơn vị tính này không?");
        builder.setIcon(R.drawable.ic_baseline_quiz_24);
        builder.setCancelable(false);
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unitViewModel.getUnit(position).DeleteUnit();
            }
        });
        builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onButtonSaveClicked(int position, String content) {
        if (position != -1)
        {
            Unit unit = unitViewModel.getUnit(position);
            unit.EditUnitOnFirebase(db, content, position);

        }
        else
        {
            Unit unit = new Unit(
                    "",
                    true,
                    content,
                    "");
            unit.AddUnitOnFireStore(db);
        }
    }

    @Override
    public void onAddUnit(boolean isExist, Unit unit) {
        if (Unit.progressDialog != null && Unit.progressDialog.isShowing())
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Unit.progressDialog.dismiss();
                }
            }, 1000);
        }
        if (!isExist)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Đơn vị tính này đã tồn tại!\nVui lòng nhập đơn vị tính khác!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }
        unitViewModel.AddUnit(unit);
        unitViewModel.setPosition(unitViewModel.getSize() - 1);
        for (int i = 0; i < unitViewModel.getSize() - 1; i++)
        {
            unitViewModel.getUnit(i).setChecked(false);
            unitViewModel.SetUnit(i, unitViewModel.getUnit(i));
        }
        TranAlertDialog dialog = new TranAlertDialog(
                "THÀNH CÔNG",
                "Thêm đơn vị tính thành công!",
                R.drawable.ic_check_circle_24
        );
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onEditUnit(boolean isSuccess, int position, String content) {
        if (Unit.progressDialog != null && Unit.progressDialog.isShowing())
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Unit.progressDialog.dismiss();
                }
            }, 1000);
        }
        if (isSuccess)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "THÀNH CÔNG",
                    "Sửa đơn vị tính thành công!",
                    R.drawable.ic_check_circle_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            unitViewModel.getUnit(position).setName(content);
            unitViewModel.SetUnit(position, unitViewModel.getUnit(position));
        }
        else
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    content,
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onGetUnit(boolean isSuccess, String content) {
        if (Unit.progressDialog != null && Unit.progressDialog.isShowing())
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Unit.progressDialog.dismiss();
                }
            }, 1000);
        }

        if (isSuccess)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    content,
                    R.drawable.ic_baseline_warning_24
            );
        }
        else
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    content,
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

}