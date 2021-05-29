package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.RestaurantManagement.R;

import java.util.List;
import java.util.Objects;

public class UnitActivity extends AppCompatActivity implements RecycleViewInUnitClickInterFace, Dialog_string_interface {

    private RecyclerView recyclerView;
    private UnitAdapter unitAdapter;
    private UnitViewModel unitViewModel;
    private Button Finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        InitActionBar();
        onCreateRecycleView();
        onCreateButtonFinish();


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
                recyclerView.setAdapter(unitAdapter);
            }
        });
    }

    //Minh họa
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Load các đơn vị tính trong hàm onStart tại UnitActivity",
                Toast.LENGTH_SHORT).show();
        unitViewModel.ClearListUnit();
        for (int i = 0; i < 3; i++)
        {
            unitViewModel.AddUnit(new Unit(false, "Name " + i));
        }
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
    public void onButtonSaveClicked(int position, String content) {
        if (position != -1)
        {
            unitViewModel.getUnit(position).setName(content);
            unitViewModel.SetUnit(position, unitViewModel.getUnit(position));
        }
        else
        {
            unitViewModel.AddUnit(new Unit(false, content));
        }

    }
}