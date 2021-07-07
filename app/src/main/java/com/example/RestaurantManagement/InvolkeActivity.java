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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.RestaurantManagement.ui.home.Bill;
import com.example.RestaurantManagement.ui.restaurant_menu.RestaurantMenuFragment;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;
import com.maltaisn.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class InvolkeActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback {

    private static int GUEST_MONEY_CODE = 1;
    private RecyclerView recyclerView;
    private InvolkeViewModel involkeViewModel;
    private InvolkeAdapter involkeAdapter;
    int totalMoney;
    int guestMoney;
    int returnMoney;
    private String id_table;
    private TextView tvTotal;
    private TextView tvGuestMoney;
    private TextView tvReturnMoney;
    private TextView tvDate;
    private TextView tvIdInvolke;
    private Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_involke);
        InitActionBar();
        recyclerView = (RecyclerView)findViewById(R.id.rvResMenu);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        involkeViewModel = new ViewModelProvider(InvolkeActivity.this).get(InvolkeViewModel.class);
        involkeViewModel.getMutableLiveDataItem().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                involkeAdapter = new InvolkeAdapter(involkeViewModel.getItems());
                recyclerView.setAdapter(involkeAdapter);
                totalMoney = involkeViewModel.getSumTotal();
                guestMoney = totalMoney;
                returnMoney = 0;
                tvTotal.setText(new DecimalFormat("#,###,###").format(totalMoney));
                tvGuestMoney.setText(new DecimalFormat("#,###,###").format(totalMoney));
                tvReturnMoney.setText(String.valueOf(0));
            }
        });
        Bill.setContext(this);
        Bill.setActivity(this);
        tvTotal = (TextView)findViewById(R.id.tvTotal);
        tvGuestMoney = (TextView)findViewById(R.id.tvGuestMoney);
        tvReturnMoney = (TextView)findViewById(R.id.tvReturnMoney);
        tvGuestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcDialog dialog = InitCalculator(guestMoney);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = 7;
        int year = 2021;
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvDate.setText(day + "/" + month + "/" + year);
        btnPay = (Button)findViewById(R.id.btnPay);
        tvIdInvolke = (TextView)findViewById(R.id.tvIdInvolke);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QuerySnapshot query = Tasks.await(FirebaseFirestore.getInstance().collection("report")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .get());
                    ((Activity)InvolkeActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvIdInvolke.setText("Số hóa đơn: " + "000" + (query.getDocuments().size() + 1));
                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill.PayInvolke(id_table, String.valueOf(tvDate.getText()), involkeViewModel);
                btnPay.setEnabled(false);
                btnPay.setText("Đã thanh toán");
            }
        });

    }

    @Override
    public void finish() {
        if (id_table.equals("-1"))
        {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Đang truy cập hệ thống...");
            dialog.show();
            Bill.DeleteBill(id_table);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity)InvolkeActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            InvolkeActivity.super.finish();
                            dialog.dismiss();
                        }
                    });
                }
            }, 3000);
        }
        else
            super.finish();
    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Phiếu thanh toán");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bill.setContext(this);
        Bill.setActivity(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        id_table = bundle.getString("id_table");
        Bill.GetDetailBillOnFirestore(involkeViewModel, id_table);
    }

    private CalcDialog InitCalculator(int value)
    {
        final CalcDialog calcDialog = new CalcDialog();
        calcDialog.getSettings().setInitialValue(BigDecimal.valueOf(value));
        calcDialog.getSettings().setZeroShownWhenNoValue(true);
        calcDialog.getSettings().setRequestCode(GUEST_MONEY_CODE);
        calcDialog.getSettings().setMinValue(BigDecimal.valueOf(totalMoney));
        return  calcDialog;
    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        if (requestCode == GUEST_MONEY_CODE)
        {
            guestMoney = Integer.parseInt(value.toString());
            tvGuestMoney.setText(new DecimalFormat("#,###,###").format(guestMoney));
            returnMoney = guestMoney - totalMoney;
            tvReturnMoney.setText(new DecimalFormat("#,###,###").format(guestMoney - totalMoney));
        }
    }
}