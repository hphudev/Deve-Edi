package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.example.RestaurantManagement.ui.home.Bill;
import com.example.RestaurantManagement.ui.restaurant_menu.RestaurantMenuFragment;
import com.maltaisn.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseItemActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback, ItemChoiceAdapter.ItemChoiceInterface {

    static private int ID_TABLE_RESQUESTCODE = 1;
    static private int GUEST_RESQUESTCODE = 2;
    private RecyclerView recyclerView;
    private ItemChoiceViewModel itemChoiceViewModel;
    private ItemChoiceAdapter itemChoiceAdapter;
    private String id_table = "";
    private String id_table_tmp = "";
    int price;
    int guest;
    private Button btnSave;
    private Button btnPay;
    private Button btnIdTable;
    private Button btnGuest;
    private TextView tvPrice;
    private Boolean willAdd = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);
        recyclerView = (RecyclerView)findViewById(R.id.rvResMenu);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        itemChoiceViewModel =
                new ViewModelProvider(ChooseItemActivity.this).get(ItemChoiceViewModel.class);
        itemChoiceViewModel.getmListItemLiveData().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemChoiceAdapter = new ItemChoiceAdapter(items);
                recyclerView.setAdapter(itemChoiceAdapter);
            }
        });
        InitActionBar();
        Bill.setActivity(this);
        Bill.setContext(this);
        btnGuest = (Button)findViewById(R.id.btnGuest);
        btnIdTable = (Button)findViewById(R.id.btnIdTable);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnPay = (Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (willAdd)
                {
                    //Bill.DeleteDetailBill(id_table);
                    Bill bill = new Bill(
                            id_table,
                            guest,
                            price,
                            itemChoiceAdapter.getItems()
                    );
                    bill.AddBillOnFireStore();
                }
                else
                {
                    Bill.DeleteDetailBill(id_table);
                    Bill bill = new Bill(
                            id_table,
                            guest,
                            price,
                            itemChoiceAdapter.getItems());
                    bill.UpdateBillOnFireStore(id_table_tmp);
                }
                ProgressDialog dialog = new ProgressDialog(ChooseItemActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Đang lưu vào hệ thống...");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Intent intent = new Intent(ChooseItemActivity.this, InvolkeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id_table", id_table);
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);

            }
        });
        CreateButtonIDTable();
        CreateButtonGuest();
        CreateButtonSave();
    }

    private void CreateButtonSave()
    {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnGuest.getText().equals("") || btnIdTable.getText().equals(""))
                {
                    TranAlertDialog dialog = new TranAlertDialog(
                            "CẢNH BÁO",
                            "Bạn vui lòng nhập đầy đủ thông tin!",
                            R.drawable.ic_baseline_warning_24);
                    dialog.show(getSupportFragmentManager(), "dialog");
                    return;
                }
                if (willAdd)
                {
                    //Bill.DeleteDetailBill(id_table);
                    Bill bill = new Bill(
                            id_table,
                            guest,
                            price,
                            itemChoiceAdapter.getItems()
                    );
                    bill.AddBillOnFireStore();
                }
                else
                {
                    Bill.DeleteDetailBill(id_table);
                    Bill bill = new Bill(
                            id_table,
                            guest,
                            price,
                            itemChoiceAdapter.getItems());
                    bill.UpdateBillOnFireStore(id_table_tmp);
                }

                finish();

            }
        });
    }
    private void CreateButtonGuest()
    {
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcDialog dialog = InitCalculator(GUEST_RESQUESTCODE);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void CreateButtonIDTable()
    {
        btnIdTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcDialog dialog = InitCalculator(ID_TABLE_RESQUESTCODE);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        id_table = bundle.getString("id_table", "-1");
        if (!id_table.equals("-1"))
        {
            willAdd = false;
            btnIdTable.setText(id_table);
            id_table_tmp = id_table;
        }
        else
            btnIdTable.setText("");
        guest = bundle.getInt("guest", 0);
        price = bundle.getInt("price", 0);
        btnGuest.setText(String.valueOf(guest));
        tvPrice.setText(String.valueOf(price));
        Bill.GetDetailBillOnFirestore(itemChoiceViewModel, id_table);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        id_table = bundle.getString("id_table", "-1");
        if (!id_table.equals("-1"))
        {
            willAdd = false;
            btnIdTable.setText(id_table);
            id_table_tmp = id_table;
        }
        else
            btnIdTable.setText("");
        guest = bundle.getInt("guest", 0);
        price = bundle.getInt("price", 0);
        btnGuest.setText(String.valueOf(guest));
        tvPrice.setText(String.valueOf(price));
        Bill.GetDetailBillOnFirestore(itemChoiceViewModel, id_table);
    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chọn món");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_item:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    private CalcDialog InitCalculator(int code)
    {
        final CalcDialog calcDialog = new CalcDialog();
        if (code == ID_TABLE_RESQUESTCODE)
            calcDialog.getSettings().setInitialValue(BigDecimal.valueOf(Integer.parseInt(id_table)));
        else if (code == GUEST_RESQUESTCODE)
            calcDialog.getSettings().setInitialValue(BigDecimal.valueOf(Integer.parseInt(btnGuest.getText().toString())));
        calcDialog.getSettings().setZeroShownWhenNoValue(true);
        calcDialog.getSettings().setRequestCode(code);
        calcDialog.getSettings().setMinValue(BigDecimal.valueOf(0));
        return  calcDialog;
    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        if (requestCode == ID_TABLE_RESQUESTCODE)
        {
            id_table = value.toString();
            btnIdTable.setText(id_table);
        }
        else
            if (requestCode == GUEST_RESQUESTCODE)
            {
                guest = Integer.parseInt(String.valueOf(value));
                btnGuest.setText(String.valueOf(guest));
            }
            else
                if (requestCode == ItemChoiceAdapter.NUM_CHOICE_REQUESTCODE)
                {
                    itemChoiceAdapter.Exucte(Integer.parseInt(value.toString()));
                }
    }

    @Override
    public void onAddResClicked(int price) {
        this.price += price;
        tvPrice.setText(new DecimalFormat("#,###,###").format(this.price));
    }

    @Override
    public void onMinusResClicked(int price) {
        this.price -= price;
        tvPrice.setText(new DecimalFormat("#,###,###").format(this.price));
    }
}