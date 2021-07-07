package com.example.RestaurantManagement;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.ItemViewHolder> {

    private List<Item> items;
    public interface ResMeAdaInterface{
        public void onItemResClicked(Item item);
    }
    private ResMeAdaInterface itemResInterface;

    public RestaurantMenuAdapter(List<Item> items, ResMeAdaInterface itemResInterface) {
        this.items = new ArrayList<>(items);
        this.itemResInterface = itemResInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameItem.setText(item.getName());
        holder.priceItem.setText(String.valueOf(new DecimalFormat("#,###,###").format(item.getPrice())) + " VNĐ");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("restaurants/"+ Restaurant.getId() + "/" + item.getId());
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
        private TextView  nameItem;
        private TextView priceItem;
        private TextView tvChoice;
        private Context context;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.item_img);
            nameItem = (TextView)itemView.findViewById(R.id.item_name);
            context = itemView.getContext();
            priceItem = (TextView)itemView.findViewById(R.id.item_price);
            tvChoice = (TextView)itemView.findViewById(R.id.tvChoice);
            tvChoice.setOnClickListener(View -> {
                itemResInterface.onItemResClicked(items.get(getAdapterPosition()));
            });
        }
    }
}
