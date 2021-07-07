package com.example.RestaurantManagement.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.ChooseItemActivity;
import com.example.RestaurantManagement.InvolkeActivity;
import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.Support;
import com.example.RestaurantManagement.ui.TranAlertDialog;

import java.text.DecimalFormat;
import java.util.List;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder>{

    private List<Bill> mListBill;
    public interface  SellInterface
    {
        void onCancelBill();
    }
    public static Activity activity;
    public static Context context;
    private SellInterface sellInterface;
    public SellAdapter(List<Bill> mListBill, SellInterface sellInterface) {
        this.mListBill = mListBill;
        this.sellInterface = sellInterface;
    }

    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bill, parent, false);
        return new SellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellViewHolder holder, int position) {
        Bill bill = mListBill.get(position);
        if (bill == null)
            return;

        String listItem = "";
        for (int i = 0; i < bill.getItemChoiceList().size(); i++)
        {
            if (bill.getItemChoiceList().get(i).getNumChoice() > 0)
            {
                listItem += bill.getItemChoiceList().get(i).getName() + " (" +
                        bill.getItemChoiceList().get(i).getNumChoice() + ")";
                if (i < bill.getItemChoiceList().size() - 1)
                    listItem += ", ";
            }
        }

        holder.btnChooseItem.setText(listItem);
        holder.btnChooseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_table", bill.getId_table());
                bundle.putInt("guest", bill.getNum_customer());
                bundle.putInt("price", bill.getTotal());
                intent.putExtra("data", bundle);
                ((Activity)context).startActivity(intent);
            }
        });
        holder.tvIdTable.setText(bill.getId_table());
        holder.tvNumCustomer.setText(String.valueOf(bill.getNum_customer()));
        holder.tvTotal.setText(String.valueOf(new DecimalFormat("#,###,###").format(bill.getTotal())) + " VNĐ");
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InvolkeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_table", bill.getId_table());
                intent.putExtra("data", bundle);
                ((Activity)context).startActivity(intent);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("CẢNH BÁO");
                dialog.setMessage("Bạn có muốn xóa bàn này không?");
                dialog.setIcon(R.drawable.ic_baseline_warning_24);
                dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bill.DeleteBill();
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListBill == null) ? 0 : mListBill.size();
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        SellAdapter.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SellAdapter.context = context;
    }

    public class SellViewHolder extends RecyclerView.ViewHolder{
        private Button btnChooseItem;
        private Button btnPay;
        private Button btnCancel;
        private TextView tvTotal;
        private TextView tvIdTable;
        private TextView tvNumCustomer;
        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvIdTable = itemView.findViewById(R.id.tvIdTable);
            tvNumCustomer = itemView.findViewById(R.id.tvNumCustomer);
            btnChooseItem = itemView.findViewById(R.id.btnChooseItem);

            btnPay = itemView.findViewById(R.id.btnPay);

            btnCancel = itemView.findViewById(R.id.btnCancel);

        }
    }
}
