package com.example.RestaurantManagement.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.R;

import java.util.List;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder>{

    private List<Bill> mListBill;

    public SellAdapter(List<Bill> mListBill) {
        this.mListBill = mListBill;
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
            listItem += bill.getItemChoiceList().get(i).getName() +
                    bill.getItemChoiceList().get(i).getNumChoice() + ", ";
        holder.btnChooseItem.setText(listItem);
        holder.tvIdTable.setText(bill.getNumTable());
        holder.tvNumCustomer.setText(bill.getNumCustomer());
        holder.tvTotal.setText(String.valueOf(bill.getTotal()));
    }

    @Override
    public int getItemCount() {
        return (mListBill == null) ? 0 : mListBill.size();
    }

    public class SellViewHolder extends RecyclerView.ViewHolder{
        private Button btnChooseItem;
        private TextView tvTotal;
        private TextView tvIdTable;
        private TextView tvNumCustomer;
        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            btnChooseItem = itemView.findViewById(R.id.btnChooseItem);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvIdTable = itemView.findViewById(R.id.tvIdTable);
            tvNumCustomer = itemView.findViewById(R.id.tvNumCustomer);
        }
    }
}
