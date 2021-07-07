package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maltaisn.calcdialog.CalcDialog;


import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback, Item.onItemExInterface{

    static private  int CALCULATER_REQUESTCODE = 9;
    static private  int UNIT_REQUESTCODE = 8;
    static private  int ITEM_REQUESTCODE = 10;
    private String idUnit = null;
    EditText edtItemName;
    TextView textViewUnit;
    TextView textViewPrice;
    ImageView item_img;
    Button btnSave;
    FirebaseFirestore db;
    Uri imageURL = null;
    BigDecimal value = BigDecimal.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        InitActionBar();
        InitTextViewPrice();
        InitTextViewUnit();
        InitFirestore();
        InitEditTextName();
        InitButtonSave();
        Item.setActivity(this);
        Item.setContext(this);
        InitImageItem();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void InitImageItem()
    {
        try
        {
            item_img = (ImageView)findViewById(R.id.ivItem);
            item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, ITEM_REQUESTCODE);
                }
            });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }
    private void InitFirestore()
    {
        db = FirebaseFirestore.getInstance();
    }

    private void InitActionBar()
    {
        String title = getIntent().getBundleExtra("data").getString("title");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    private void InitEditTextName()
    {
        edtItemName = (EditText)findViewById(R.id.edtItemName);
    }

    private void InitButtonSave()
    {
        try
        {
            Button btnSave = (Button)findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonSave();
                }
            });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }

    }

    private void onButtonSave()
    {
        if (edtItemName.getText() == null
                || edtItemName.getText().toString().equals("")
                || textViewPrice.getText() == null
                || textViewPrice.getText().toString().equals("")
                || idUnit == null || imageURL == null )
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Vui lòng điền đầy đủ thông tin món!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }
        Item item = new Item(
                "",
                idUnit,
                imageURL.toString(),
                edtItemName.getText().toString(),
                Integer.parseInt(value.toString()));
        item.AddItemToFireStore(db);
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
        try {
            Bundle bundle = new Bundle();
            bundle.putString("present_unit", textViewUnit.getText().toString());
            Intent intent = new Intent(AddItemActivity.this, UnitActivity.class);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, UNIT_REQUESTCODE);
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_item:
                onButtonSave();
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
            textViewPrice.setText(String.valueOf(new DecimalFormat("#,###,###").format(value)));
        }
        // if (requestCode == CALC_REQUEST_CODE) {} <-- If there were many dialogs, this would be used

        // The calculator dialog returned a value
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UNIT_REQUESTCODE && resultCode == RESULT_OK)
        {
            String string = data.getBundleExtra("data").getString("NameUnit");
            idUnit = data.getBundleExtra("data").getString("IdUnit");
            textViewUnit.setText(string);
        }
        if (requestCode == ITEM_REQUESTCODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageURL = data.getData();
            item_img.setImageURI(imageURL);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void AddItemSuccess() {
        edtItemName.setText("");
        textViewPrice.setText("0");
        textViewUnit.setText("");
        value = BigDecimal.valueOf(0);
        item_img.setImageURI(null);
        imageURL = null;
        idUnit = null;
    }
}