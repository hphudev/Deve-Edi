package com.example.RestaurantManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.R;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder>{
    private int presentPosition = -1;
    private List<Unit> mlistUnit;
    private OnItemClickListener mListener;
    private RecycleViewInUnitClickInterFace recycleViewInUnitClickInterFace;

    public UnitAdapter(List<Unit> mlistUnit, RecycleViewInUnitClickInterFace recycleViewInUnitClickInterFace) {
        this.mlistUnit = mlistUnit;
        this.recycleViewInUnitClickInterFace = recycleViewInUnitClickInterFace;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = mlistUnit.get(position);
        if (unit == null)
            return;
        holder.ivCheck.setVisibility(!unit.isChecked() ? View.INVISIBLE : View.VISIBLE);
        holder.tvUnitName.setText(unit.getName());

    }

    public void setPresentPosition(int presentPosition) {
        this.presentPosition = presentPosition;
    }

    @Override
    public int getItemCount() {
        return (mlistUnit != null) ? mlistUnit.size() : 0;
    }

    public int getPresentPosition()
    {
        return presentPosition;
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivCheck;
        private TextView tvUnitName;
        private ImageButton ibEdit;
        private ImageButton ibDelete;
        private Context context;
        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ivCheck = itemView.findViewById(R.id.ivTickUnit);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            ibEdit = itemView.findViewById(R.id.ibEdit);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            itemView.setOnClickListener((View) -> {
                recycleViewInUnitClickInterFace.onItemClick(getAdapterPosition());
            });
            ibEdit.setOnClickListener((View) -> {
                recycleViewInUnitClickInterFace.onImageButtonInItemClick(getAdapterPosition());
            });
            ibDelete.setOnClickListener((View) -> {
                recycleViewInUnitClickInterFace.onImageButtonDeleteClick(getAdapterPosition());
            });

        }
    }
}
