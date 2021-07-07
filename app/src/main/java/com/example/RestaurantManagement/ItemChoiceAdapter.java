package com.example.RestaurantManagement;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.maltaisn.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemChoiceAdapter extends RecyclerView.Adapter<ItemChoiceAdapter.ItemViewHolder> implements CalcDialog.CalcDialogCallback {
    private List<Item> items;
    private static int position = -1;
    private static ItemChoiceAdapter.ItemViewHolder holder;
    static public int NUM_CHOICE_REQUESTCODE = 10;

    public void Exucte(int value)
    {
        Item item = items.get(position);
        int tmp = Integer.parseInt(holder.btnNumChoice.getText().toString());
        holder.btnNumChoice.setText(String.valueOf(value));
        int tmp2 = Integer.parseInt(String.valueOf(value));
        if (tmp - tmp2 > 0)
        {
            itemChoiceInterface.onMinusResClicked((tmp - tmp2) * item.getPrice());
        }
        else
        {
            itemChoiceInterface.onAddResClicked((tmp2 - tmp) * item.getPrice());
            }
    }

    private CalcDialog InitCalculator(int code, int value)
    {
        final CalcDialog calcDialog = new CalcDialog();
        if (code == NUM_CHOICE_REQUESTCODE)
            calcDialog.getSettings().setInitialValue(BigDecimal.valueOf(value));
        calcDialog.getSettings().setZeroShownWhenNoValue(true);
        calcDialog.getSettings().setRequestCode(code);
        calcDialog.getSettings().setMinValue(BigDecimal.valueOf(0));
        return  calcDialog;
    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {

    }

    public interface ItemChoiceInterface{
        public void onAddResClicked(int price);
        public void onMinusResClicked(int price);
    }
    private ItemChoiceAdapter.ItemChoiceInterface itemChoiceInterface;

    public ItemChoiceAdapter(List<Item> items) {
        this.items = new ArrayList<>(items);
        //this.itemResInterface = itemResInterface;
    }

    @NonNull
    @Override
    public ItemChoiceAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
        return new ItemChoiceAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemChoiceAdapter.ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameItem.setText(item.getName());
        holder.priceItem.setText(String.valueOf(new DecimalFormat("#,###,###").format(item.getPrice())) + " VNĐ");
        holder.btnNumChoice.setText(String.valueOf(item.getNumChoice()));
        holder.btnNumChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcDialog dialog = InitCalculator(NUM_CHOICE_REQUESTCODE, Integer.parseInt(holder.btnNumChoice.getText().toString()));
                ItemChoiceAdapter.holder = holder;
                ItemChoiceAdapter.position = position;
                dialog.show(((AppCompatActivity)holder.context).getSupportFragmentManager(), "dialog");
            }
        });
        if (Integer.parseInt(holder.btnNumChoice.getText().toString()) <= 0)
        {
            holder.layout_button.setVisibility(View.INVISIBLE);
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("restaurants/"+ Restaurant.getId() + "/" + item.getId());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = Integer.parseInt(holder.btnNumChoice.getText().toString());
                tmp++;
                items.get(position).setNumChoice(tmp);
                holder.btnNumChoice.setText(String.valueOf(item.getNumChoice()));
                itemChoiceInterface.onAddResClicked(item.price);
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = Integer.parseInt(holder.btnNumChoice.getText().toString());
                tmp--;
                items.get(position).setNumChoice(tmp);
                holder.btnNumChoice.setText(String.valueOf(item.getNumChoice()));
                if (tmp <= 0)
                {
                    holder.layout_button.setVisibility(View.INVISIBLE);
                }
                itemChoiceInterface.onMinusResClicked(item.price);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.layout_button.setVisibility(View.VISIBLE);
                int tmp = Integer.parseInt(holder.btnNumChoice.getText().toString());
                tmp++;
                items.get(position).setNumChoice(tmp);
                holder.btnNumChoice.setText(String.valueOf(item.getNumChoice()));
                itemChoiceInterface.onAddResClicked(item.price);
            }
        });
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(holder.context)
                        .load(downloadUrl)
                        .placeholder(R.drawable.imageemty)
                        .centerCrop()
                        .into(holder.imageItem);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                TranAlertDialog dialog = new TranAlertDialog(
//                    "CẢNH BÁO",
//                        "Quá trình tải ảnh đã bị lỗi!\nVui lòng kiểm tra lại kết nối!",
//                        R.drawable.ic_baseline_warning_24
//                );
//                dialog.show(((AppCompatActivity) holder.context).getSupportFragmentManager(), "dialog");
                    }
                });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Item> getItems()
    {
        return items;
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageItem;
        private TextView nameItem;
        private TextView priceItem;
        private Button btnNumChoice;
        private ImageButton btnAdd;
        private ImageButton btnMinus;
        private Context context;
        private CardView cardView;
        private View layout_button;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            imageItem = itemView.findViewById(R.id.item_img);
            nameItem = (TextView)itemView.findViewById(R.id.item_name);
            context = itemView.getContext();
            itemChoiceInterface = (ItemChoiceInterface) context;
            priceItem = (TextView)itemView.findViewById(R.id.item_price);
            btnNumChoice = itemView.findViewById(R.id.btnNumChoice);
            cardView = itemView.findViewById(R.id.cvItem);
            layout_button = itemView.findViewById(R.id.layout_button);
        }
    }
}
