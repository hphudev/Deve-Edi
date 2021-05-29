package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.RestaurantManagement.R;
import com.maltaisn.calcdialog.CalcDialog;


import java.math.BigDecimal;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback{

    @Nullable
    private BigDecimal value = null;
    static private  int CALCULATER_REQUESTCODE = 9;
    static private  int UNIT_REQUESTCODE = 9;
    TextView textViewUnit;
    TextView textViewPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        InitActionBar();
        InitTextViewPrice();
        InitTextViewUnit();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void InitActionBar()
    {
        String title = getIntent().getBundleExtra("data").getString("title");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    private void InitTextViewUnit()
    {
        textViewUnit = (TextView)findViewById(R.id.tvUnit);
        textViewUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUnitActivity();
            }
        });
    }

    private void InitTextViewPrice()
    {
        textViewPrice = (TextView)findViewById(R.id.tvPrice);
        textViewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcDialog calcDialog = InitCalculator();
                calcDialog.show(getSupportFragmentManager(), "calc_dialog");
            }
        });
    }

    private CalcDialog InitCalculator()
    {
        final CalcDialog calcDialog = new CalcDialog();
        calcDialog.getSettings().setInitialValue(value);
        calcDialog.getSettings().setZeroShownWhenNoValue(true);
        calcDialog.getSettings().setRequestCode(CALCULATER_REQUESTCODE);
        calcDialog.getSettings().setMinValue(BigDecimal.valueOf(0));
        return  calcDialog;
    }

    private void CreateUnitActivity()
    {
        Bundle bundle = new Bundle();
        bundle.putString("present_unit", textViewUnit.getText().toString());
        Intent intent = new Intent(AddItemActivity.this, UnitActivity.class);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, UNIT_REQUESTCODE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        if (requestCode == CALCULATER_REQUESTCODE)
        {
            this.value = value;
            TextView textViewPrice = (TextView)findViewById(R.id.tvPrice);
            textViewPrice.setText(String.valueOf(value));
        }
        // if (requestCode == CALC_REQUEST_CODE) {} <-- If there were many dialogs, this would be used

        // The calculator dialog returned a value
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == UNIT_REQUESTCODE)
            {
                String string = data.getBundleExtra("data").getString("NameUnit");
                textViewUnit.setText(string);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}