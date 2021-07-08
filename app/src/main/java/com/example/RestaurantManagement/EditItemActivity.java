package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.maltaisn.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

public class EditItemActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback, Item.onItemExInterface{

    private static final int CALCULATER_REQUESTCODE = 2;
    private static final int UNIT_REQUESTCODE = 3;
    private static final int ITEM_REQUESTCODE = 4;
    Item item;
    EditText edtName;
    TextView tvPrice;
    TextView tvUnitName;
    ImageView imageItem;
    BigDecimal value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        item = new Item(
                bundle.getString("id"),
                bundle.getString("id_unit"),
                bundle.getString("image_url"),
                bundle.getString("name"),
                bundle.getInt("price")
        );
        Item.setContext(this);
        Item.setActivity(this);
        edtName = (EditText)findViewById(R.id.edtItemName);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvUnitName = (TextView)findViewById(R.id.tvUnit);
        imageItem = (ImageView)findViewById(R.id.ivItem);
        value = BigDecimal.valueOf(item.getPrice());
        edtName.setText(item.getName());
        tvPrice.setText(String.valueOf(item.getPrice()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Hệ thống đang lấy dữ liệu...");
        dialog.setCancelable(false);
        dialog.show();
        db.collection("unit")
                .whereEqualTo("id", item.getId_unit())
                .get(Source.SERVER)
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult()!= null && task.getResult().size() > 0)
                            {
                                tvUnitName.setText(task.getResult().getDocuments().get(0).get("name").toString());
                            }
                        }
                    }
                });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child(item.getImage_url());
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                dialog.dismiss();
                Glide.with(EditItemActivity.this)
                        .load(downloadUrl)
                        .placeholder(R.drawable.imageemty)
                        .centerCrop()
                        .into(imageItem);
            }
        });

        InitActionBar();
        InitCalculator();
        InitButtonSave();
        InitTextViewPrice();
        InitTextViewUnit();
        InitImageItem();
        CreateButtonDelete();
    }

    private void CreateButtonDelete()
    {
        Button btnDelete = (Button)findViewById(R.id.btnDelate);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("detail_bill")
                        .whereEqualTo("id_res", Restaurant.getId())
                        .whereEqualTo("id_item", item.getId())
                        .get(Source.SERVER)
                        .addOnCompleteListener(EditItemActivity.this, new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    if (task.getResult().getDocuments().size() > 0)
                                    {
                                        TranAlertDialog dialog = new TranAlertDialog(
                                                "CẢNH BÁO",
                                                "Món này đang được gọi! Bạn không thể xóa!",
                                                R.drawable.ic_baseline_warning_24
                                        );
                                        dialog.show(getSupportFragmentManager(), "dialog");
                                        return;
                                    }
                                    item.DeleteItem();
                                }
                            }
                        });
            }
        });
    }
    private void InitImageItem()
    {
        try
        {
            imageItem = (ImageView)findViewById(R.id.ivItem);
            imageItem.setOnClickListener(new View.OnClickListener() {
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
        if (edtName.getText() == null
                || edtName.getText().toString().equals("")
                || tvPrice.getText() == null
                || tvPrice.getText().toString().equals("")
               )
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "CẢNH BÁO",
                    "Vui lòng điền đầy đủ thông tin món!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }
        //Toast.makeText(this, item.getId_unit(), Toast.LENGTH_SHORT).show();
        item.setName(edtName.getText().toString());
        item.setPrice(Integer.parseInt(value.toString()));
        item.UpdateItem();
    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sửa món");
    }

    private void InitTextViewPrice()
    {
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvPrice.setOnClickListener(new View.OnClickListener() {
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

    private void InitTextViewUnit()
    {
        tvUnitName = (TextView)findViewById(R.id.tvUnit);
        tvUnitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUnitActivity();
            }
        });
    }

    private void CreateUnitActivity()
    {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("present_unit", tvUnitName.getText().toString());
            Intent intent = new Intent(EditItemActivity.this, UnitActivity.class);
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
            item.setId_unit(data.getBundleExtra("data").getString("IdUnit"));
            tvUnitName.setText(string);
        }
        if (requestCode == ITEM_REQUESTCODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageURL = data.getData();
            imageItem.setImageURI(imageURL);
            item.setImage_url(imageURL.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void AddItemSuccess() {
        finish();
    }
}