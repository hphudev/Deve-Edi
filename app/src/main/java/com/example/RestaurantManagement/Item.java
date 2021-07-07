package com.example.RestaurantManagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Item {
    String id;
    String id_unit;
    String image_url;
    String name;
    int numChoice = 0;
    int price;
    static Activity activity;
    static Context context;
    public interface onItemExInterface{
        public void AddItemSuccess();
    }

    private static int count;
    private onItemExInterface item_interface;
    public Item(String id, String id_unit, String image_url, String name, int price) {
        this.id = id;
        this.id_unit = id_unit;
        this.image_url = image_url;
        this.name = name;
        this.price = price;
    }

    public Item() {
    }

    public static int getCount(FirebaseFirestore db) {
        CountItemInFireStore(db);
        return count;
    }

    public int getNumChoice() {
        return numChoice;
    }

    public void setNumChoice(int numChoice) {
        this.numChoice = numChoice;
    }

    public static void setCount(int count) {
        Item.count = count;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_unit() {
        return id_unit;
    }

    public void setId_unit(String id_unit) {
        this.id_unit = id_unit;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    static public void CountItemInFireStore(FirebaseFirestore db)
    {
        if (Restaurant.getId() == "")
        {
            Item.setCount(0);
            return;
        }

        db.collection("item")
                .whereEqualTo("id_res", Restaurant.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                             Item.setCount(count);
                            }
                        } else {
                            //Log.d(, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Item.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Item.context = context;
    }

    public void AddItemToFireStore(FirebaseFirestore db)
    {
        try
        {
            final Source source = Source.SERVER;
            if (!Support.checkConnect(getActivity()))
            {
                TranAlertDialog dialog = new TranAlertDialog(
                        "CẢNH BÁO",
                        "Không tìm thấy kết nối mạng!\nVui lòng kiểm tra lại kết nối của bạn!",
                        R.drawable.ic_baseline_warning_24);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                return;
            }
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Hệ thống đang thêm món...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            db.collection("item")
                    .get(source)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                int count = Objects.requireNonNull(task.getResult()).size() + 1;
                                setId(String.valueOf(count));
                                Map<String, Object> item = new HashMap<>();
                                item.put("id", getId());
                                item.put("id_res", Restaurant.getId());
                                item.put("id_unit", getId_unit());
                                item.put("image_url", "restaurants/" + Restaurant.getId() + "/" + item.get("id"));
                                item.put("name", getName());
                                item.put("price", getPrice());
                                db.collection("item")
                                        .whereEqualTo("id_res", Restaurant.getId())
                                        .whereEqualTo("name", getName())
                                        .get(source)
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful())
                                                {
                                                    if (Objects.requireNonNull(task.getResult()).size() == 0)
                                                    {
                                                        db.collection("item")
                                                                .add(item)
                                                                .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                        StorageReference storageRef = storage.getReference();
                                                                        Uri file = Uri.parse(getImage_url());
                                                                        StorageReference riversRef = storageRef.child("restaurants/" + Restaurant.getId() + "/" + item.get("id"));
                                                                        UploadTask uploadTask = riversRef.putFile(file);
                                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception exception) {
                                                                                // Handle unsuccessful uploads
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                progressDialog.dismiss();
                                                                                TranAlertDialog dialog = new TranAlertDialog(
                                                                                        "THÀNH CÔNG",
                                                                                        "Đã thêm món thành công!",
                                                                                        R.drawable.ic_check_circle_24);
                                                                                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "dialog");
                                                                                item_interface = (onItemExInterface) getContext();
                                                                                item_interface.AddItemSuccess();
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                "CẢNH BÁO",
                                                                "Món này đã tồn tại!",
                                                                R.drawable.ic_baseline_warning_24);
                                                        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        catch (Exception a) {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }
    }

    public void UpdateItem()
    {
        try {
            final Source source = Source.SERVER;
            if (!Support.checkConnect(getActivity())) {
                TranAlertDialog dialog = new TranAlertDialog(
                        "CẢNH BÁO",
                        "Không tìm thấy kết nối mạng!\nVui lòng kiểm tra lại kết nối của bạn!",
                        R.drawable.ic_baseline_warning_24);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Hệ thống đang sửa món...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("item")
                    .whereEqualTo("id", getId())
                    .get(Source.SERVER)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null && task.getResult().size() > 0) {
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("id", getId());
                                    item.put("id_res", Restaurant.getId());
                                    item.put("id_unit", getId_unit());
                                    item.put("image_url", "restaurants/" + Restaurant.getId() + "/" + item.get("id"));
                                    item.put("name", getName());
                                    item.put("price", getPrice());
                                    Toast.makeText(getContext(), getId_unit(), Toast.LENGTH_SHORT).show();
                                    db.collection("item").document(document.getId())
                                            .set(item, SetOptions.merge())
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                                        StorageReference storageRef = storage.getReference();
                                                        Uri file = Uri.parse(getImage_url());
                                                        StorageReference riversRef = storageRef.child("restaurants/" + Restaurant.getId() + "/" + item.get("id"));
                                                        //UploadTask uploadTask =
                                                        riversRef.putFile(file)
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                progressDialog.dismiss();
                                                                item_interface = (onItemExInterface) getContext();
                                                                item_interface.AddItemSuccess();
                                                            }
                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        progressDialog.dismiss();
                                                                        TranAlertDialog dialog = new TranAlertDialog(
                                                                                "THÀNH CÔNG",
                                                                                "Đã sửa món thành công!",
                                                                                R.drawable.ic_check_circle_24);
                                                                        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                                        item_interface = (onItemExInterface) getContext();
                                                                        item_interface.AddItemSuccess();
                                                                    }
                                                                }, 5000);
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }

    }
    static public void GetAllItemInFirestore(RestaurantMenuViewModel menuViewModel)
    {
        try
        {
            if (!Support.checkConnect(getActivity())) {
                TranAlertDialog dialog = new TranAlertDialog(
                        "CẢNH BÁO",
                        "Không tìm thấy kết nối mạng!\nVui lòng kiểm tra lại kết nối của bạn!",
                        R.drawable.ic_baseline_warning_24);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang tải danh sách món...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final  Source source = Source.SERVER;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("item")
                    .whereEqualTo("id_res", Restaurant.getId())
                    .get(source)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                if (task.getResult() == null)
                                    return;
                                List<Item> items = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult())
                                {
                                    Item item = new Item(
                                            document.get("id").toString(),
                                            document.get("id_unit").toString(),
                                            document.get("image_url").toString(),
                                            document.get("name").toString(),
                                            Integer.valueOf(document.get("price").toString())
                                    );
                                    items.add(item);
                                }
                                menuViewModel.setItems(items);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        if (items.size() == 0)
                                        {
                                            TranAlertDialog dialog = new TranAlertDialog(
                                                    "THÔNG BÁO",
                                                    "Thực đơn của bạn đang trống!\nVui lòng thêm món!",
                                                    R.drawable.ic_baseline_warning_24);
                                            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                        }
                                    }
                                }, 1111);
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }
    }

    public void DeleteItem()
    {
        try
        {
            if (!Support.checkConnect(getActivity())) {
                TranAlertDialog dialog = new TranAlertDialog(
                        "CẢNH BÁO",
                        "Không tìm thấy kết nối mạng!\nVui lòng kiểm tra lại kết nối của bạn!",
                        R.drawable.ic_baseline_warning_24);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang xóa món...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("item")
                    .whereEqualTo("id", getId())
                    .get()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                if (task.getResult()!= null && task.getResult().size() > 0)
                                {
                                    db.collection("item").document(task.getResult().getDocuments().get(0).getId())
                                            .delete()
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    TranAlertDialog dialog = new TranAlertDialog(
                                                            "THÀNH CÔNG",
                                                            "Đã xóa món thành công!",
                                                            R.drawable.ic_check_circle_24);
                                                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            item_interface = (onItemExInterface) getContext();
                                                            item_interface.AddItemSuccess();
                                                        }
                                                    }, 1111);

                                                }
                                            })
                                            .addOnFailureListener(getActivity(), new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    TranAlertDialog dialog = new TranAlertDialog(
                                                            "LỖI",
                                                            "Hệ thống không thể xóa món này!\nVui lòng thử lại!",
                                                            R.drawable.ic_baseline_dangerous_24);
                                                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "LỖI",
                    "Đã xảy ra lỗi hệ thống!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }
    }
}
