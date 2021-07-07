package com.example.RestaurantManagement;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class InvolkeAdapter extends RecyclerView.Adapter<InvolkeAdapter.ItemViewHolder> {

    private List<Item> items;
    public static int SumTotal;

    public InvolkeAdapter(List<Item> items) {
        this.items = items;
        SumTotal = 0;
    }


    public static int getSumTotal() {
        return SumTotal;
    }

    public static void setSumTotal(int sumTotal) {
        SumTotal = sumTotal;
    }

    public static void addSumTotal(int sumTotal) {
        InvolkeAdapter.SumTotal += sumTotal;
    }

    @NonNull
    @Override
    public InvolkeAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.involke_layout, parent, false);
        return new InvolkeAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvolkeAdapter.ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvNameItem.setText(item.getName());
        holder.tvNumChoice.setText(String.valueOf(item.getNumChoice()));
        holder.tvPriceItem.setText(String.valueOf(new DecimalFormat("#,###,###").format(item.getPrice())));
        holder.tvTotal.setText(String.valueOf(new DecimalFormat("#,###,###").format(item.getPrice() * item.getNumChoice())));
        addSumTotal(item.getPrice() * item.getNumChoice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameItem;
        private TextView tvPriceItem;
        private TextView tvNumChoice;
        private TextView tvTotal;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameItem = itemView.findViewById(R.id.tvItemName);
            tvNumChoice = itemView.findViewById(R.id.tvNumChoice);
            tvPriceItem = itemView.findViewById(R.id.tvPriceRoot);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
