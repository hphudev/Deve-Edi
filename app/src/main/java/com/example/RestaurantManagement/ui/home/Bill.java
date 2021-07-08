package com.example.RestaurantManagement.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.HandlerThread;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.RestaurantManagement.InvolkeViewModel;
import com.example.RestaurantManagement.Item;
import com.example.RestaurantManagement.ItemChoiceViewModel;
import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.Restaurant;
import com.example.RestaurantManagement.Support;
import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.sax.TemplatesHandler;

public class Bill {
    private String id_res;
    private String id_table;
    private int num_customer;
    private int total;
    private List<Item> itemChoiceList;
    static public Activity activity;
    static public Context context;

    public Bill(String id_table, int num_customer, int total, List<Item> itemChoiceList) {
        this.num_customer = num_customer;
        this.id_table = id_table;
        this.total = total;
        this.itemChoiceList = itemChoiceList;
    }

    public void Add(Item item)
    {
        itemChoiceList.add(item);
    }

    public void DelItem(String itemName)
    {
        for (int i = 0; i < itemChoiceList.size(); i++)
        {
            if (itemChoiceList.get(i).getName().equals(itemName))
            {
                itemChoiceList.remove(i);
                break;
            }
        }
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Bill.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Bill.context = context;
    }

    public String getId_res() {
        return id_res;
    }

    public void setId_res(String id_res) {
        this.id_res = id_res;
    }

    public int getNum_customer() {
        return num_customer;
    }

    public void setNum_customer(int num_customer) {
        this.num_customer = num_customer;
    }

    public String getId_table() {
        return id_table;
    }

    public void setId_table(String id_table) {
        this.id_table = id_table;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Item> getItemChoiceList() {
        return itemChoiceList;
    }

    public void setItemChoiceList(List<Item> itemChoiceList) {
        this.itemChoiceList = itemChoiceList;
    }

    public static void PayInvolke(String id_table, String date, final InvolkeViewModel involkeViewModel)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang thanh toán...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 1;
                try {
                    QuerySnapshot querySnapshot = Tasks.await(db.collection("report")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .get());
                    count += querySnapshot.getDocuments().size();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Map<String, Object> report = new HashMap<>();
                report.put("id", String.valueOf(count));
                report.put("id_res", Restaurant.getId());
                report.put("date", date);
                report.put("total", String.valueOf(involkeViewModel.getSumTotal()));
                db.collection("report")
                        .add(report)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                for (Item item : involkeViewModel.getItems())
                                {
                                    Map<String, Object> detail_report = new HashMap<>();
                                    detail_report.put("id_item", item.getId());
                                    detail_report.put("id_rep", report.get("id"));
                                    detail_report.put("number_of", item.getNumChoice());
                                    detail_report.put("price", item.getPrice());
                                    db.collection("detail_report")
                                            .add(detail_report)
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    DeleteBill(id_table);
//                                                    int check = 1;
//                                                    while (!(check == 0))
//                                                    {
//                                                        try {
//                                                            QuerySnapshot isCheck = Tasks.await(db.collection("bill")
//                                                                    .whereEqualTo("id_table", id_table)
//                                                                    .whereEqualTo("id_res", Restaurant.getId())
//                                                                    .get());
//                                                            check = isCheck.getDocuments().size();
//                                                            isCheck = Tasks.await(db.collection("detail_bill")
//                                                                    .whereEqualTo("id_res", Restaurant.getId())
//                                                                    .whereEqualTo("id_table", id_table)
//                                                                    .get());
//                                                            check += isCheck.getDocuments().size();
//                                                        } catch (ExecutionException e) {
//                                                            e.printStackTrace();
//                                                        } catch (InterruptedException e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
                                                    ((Activity) context).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            //((Activity) context).finish();
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
                        });
            }
        });
        thread.start();
    }

    public void UpdateBillOnFireStore(String id_table)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang cập nhật thông tin bàn...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String documentId = "";
                try {
                    QuerySnapshot countId = Tasks.await(db.collection("bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .whereEqualTo("id_table", id_table)
                            .get());
                    documentId = countId.getDocuments().get(0).getId();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = 0;
                try {
                    QuerySnapshot countId = Tasks.await(db.collection("bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .whereEqualTo("id_table", getId_table())
                            .get());
                    count = countId.size();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count > 0 && !id_table.equals(getId_table()))
                {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            TranAlertDialog dialog = new TranAlertDialog(
                                    "CẢNH BÁO",
                                    "ID bàn đã tồn tại!\nVui lòng nhập lại ID bàn!",
                                    R.drawable.ic_baseline_warning_24);
                            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                        }
                    });
                    return;
                }
                Map<String, Object> bill = new HashMap<>();
                bill.put("id_res", Restaurant.getId());
                bill.put("id_table", String.valueOf(getId_table()));
                bill.put("num_customer", String.valueOf(getNum_customer()));
                bill.put("total", String.valueOf(getTotal()));
                db.collection("bill").document(documentId)
                        .set(bill, SetOptions.merge())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                for (Item item : itemChoiceList)
                                {
                                    if (item.getNumChoice() <= 0)
                                        continue;
                                    Map<String, Object> detailBill = new HashMap<>();
                                    detailBill.put("id_res", Restaurant.getId());
                                    detailBill.put("id_table", String.valueOf(getId_table()));
                                    detailBill.put("id_item", item.getId());
                                    detailBill.put("num_of_item", String.valueOf(item.getNumChoice()));
                                    db.collection("detail_bill")
                                            .add(detailBill)
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                }
                                            });
                                }
                                ((Activity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });
        thread.start();
    }

    public void AddBillOnFireStore()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang thêm bàn...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    QuerySnapshot countId = Tasks.await(db.collection("bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .whereEqualTo("id_table", getId_table())
                            .get());
                    count = countId.size();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count > 0)
                {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            TranAlertDialog dialog = new TranAlertDialog(
                                    "CẢNH BÁO",
                                    "ID bàn đã tồn tại!\nVui lòng nhập lại ID bàn!",
                                    R.drawable.ic_baseline_warning_24);
                            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                        }
                    });
                    return;
                }
                Map<String, Object> bill = new HashMap<>();
                bill.put("id_res", Restaurant.getId());
                bill.put("id_table", String.valueOf(getId_table()));
                bill.put("num_customer", String.valueOf(getNum_customer()));
                bill.put("total", String.valueOf(getTotal()));
                db.collection("bill")
                        .add(bill)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                for (Item item : itemChoiceList)
                                {
                                    if (item.getNumChoice() <= 0)
                                        continue;
                                    Map<String, Object> detailBill = new HashMap<>();
                                    detailBill.put("id_res", Restaurant.getId());
                                    detailBill.put("id_table", String.valueOf(getId_table()));
                                    detailBill.put("id_item", item.getId());
                                    detailBill.put("num_of_item", String.valueOf(item.getNumChoice()));
                                    db.collection("detail_bill")
                                            .add(detailBill)
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getContext(), "Đã thêm bàn thành công!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                }
                                ((Activity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });
        thread.start();
    }

    public static void GetDetailBillOnFirestore(final InvolkeViewModel involkeViewModel, String id_table)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        involkeViewModel.ClearItem();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải phiếu thanh toán...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QuerySnapshot queryDetailBill = Tasks.await(db.collection("detail_bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .whereEqualTo("id_table", id_table)
                            .get());
                    List<Item> items = new ArrayList<>();
                    QuerySnapshot queryItem = Tasks.await(db.collection("item")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .get());
                    if (queryDetailBill.size() > 0)
                    {
                        for (DocumentSnapshot documentDetailBill : queryDetailBill.getDocuments())
                        {
                            if (queryItem.size() > 0)
                            {
                                for (DocumentSnapshot document : queryItem.getDocuments())
                                {
                                    Item item = new Item(
                                            document.get("id").toString(),
                                            document.get("id_unit").toString(),
                                            document.get("image_url").toString(),
                                            document.get("name").toString(),
                                            Integer.parseInt(document.get("price").toString()));
                                    if (item.getId().equals(documentDetailBill.get("id_item").toString()))
                                    {
                                        item.setNumChoice(Integer.parseInt(documentDetailBill.get("num_of_item").toString()));
                                        items.add(item);
                                    }

                                }
                            }
                        }
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                involkeViewModel.setItems(items);
                            }
                        });
                    }
                    else
                    {
//                        for (DocumentSnapshot document : queryItem.getDocuments())
//                        {
//                            Item item = new Item(
//                                    document.get("id").toString(),
//                                    document.get("id_unit").toString(),
//                                    document.get("image_url").toString(),
//                                    document.get("name").toString(),
//                                    Integer.parseInt(document.get("price").toString()));
//                            item.setNumChoice(0);
//                            items.add(item);
//
//                        }
//                        ((Activity)getContext()).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressDialog.dismiss();
//                                itemChoiceViewModel.setItems(items);
//                            }
//                        });
                    }
                } catch (ExecutionException e) {
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void GetDetailBillOnFirestore(final ItemChoiceViewModel itemChoiceViewModel, String id_table)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        itemChoiceViewModel.ClearItems();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải danh sách món...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QuerySnapshot queryDetailBill = Tasks.await(db.collection("detail_bill")
                    .whereEqualTo("id_res", Restaurant.getId())
                    .whereEqualTo("id_table", id_table)
                    .get());
                    List<Item> items = new ArrayList<>();
                    QuerySnapshot queryItem = Tasks.await(db.collection("item")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .get());
                    if (queryDetailBill.size() > 0)
                    {

                        for (DocumentSnapshot documentDetailBill : queryDetailBill.getDocuments())
                        {
                            if (queryItem.size() > 0)
                            {
                                for (DocumentSnapshot document : queryItem.getDocuments())
                                {
                                    Item item = new Item(
                                            document.get("id").toString(),
                                            document.get("id_unit").toString(),
                                            document.get("image_url").toString(),
                                            document.get("name").toString(),
                                            Integer.parseInt(document.get("price").toString()));
                                    if (item.getId().equals(documentDetailBill.get("id_item").toString()))
                                    {
                                        item.setNumChoice(Integer.parseInt(documentDetailBill.get("num_of_item").toString()));
                                        items.add(item);
                                    }

                                }
                            }
                        }

                        if (queryItem.size() > 0)
                        {
                            for (DocumentSnapshot document : queryItem.getDocuments())
                            {
                                Item item = new Item(
                                        document.get("id").toString(),
                                        document.get("id_unit").toString(),
                                        document.get("image_url").toString(),
                                        document.get("name").toString(),
                                        Integer.parseInt(document.get("price").toString()));
                                boolean check = true;
                                for (DocumentSnapshot documentDetailBill : queryDetailBill.getDocuments())
                                {
                                    if (documentDetailBill.get("id_item").toString().equals(item.getId()))
                                        check = false;
                                }
                                if (check)
                                {
                                    item.setNumChoice(0);
                                    items.add(item);
                                }

                            }
                        }
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                itemChoiceViewModel.setItems(items);
                            }
                        });
                    }
                    else
                    {
                        for (DocumentSnapshot document : queryItem.getDocuments())
                        {
                            Item item = new Item(
                                    document.get("id").toString(),
                                    document.get("id_unit").toString(),
                                    document.get("image_url").toString(),
                                    document.get("name").toString(),
                                    Integer.parseInt(document.get("price").toString()));
                            item.setNumChoice(0);
                            items.add(item);

                        }
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                itemChoiceViewModel.setItems(items);
                            }
                        });
                    }
                } catch (ExecutionException e) {
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void DeleteDetailBill(String id_table)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("detail_bill")
                .whereEqualTo("id_table", id_table)
                .whereEqualTo("id_res", Restaurant.getId())
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult() != null && task.getResult().size() > 0)
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    db.collection("detail_bill").document(document.getId())
                                            .delete();
                                }
                            }
                        }
                    }
                });
    }

    public static void DeleteBill(String id_table)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DeleteDetailBill(id_table);
                db.collection("bill")
                        .whereEqualTo("id_table", id_table)
                        .whereEqualTo("id_res", Restaurant.getId())
                        .get()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    if (task.getResult() != null && task.getResult().size() > 0)
                                    {
                                        for (QueryDocumentSnapshot document : task.getResult())
                                        {
                                            db.collection("bill").document(document.getId())
                                                    .delete();
                                        }
                                    }
                                }
                            }
                        });
            }
        });
        thread.start();
    }

    public void DeleteBill()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DeleteDetailBill(getId_table());
        db.collection("bill")
                .whereEqualTo("id_table", getId_table())
                .whereEqualTo("id_res", Restaurant.getId())
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult() != null && task.getResult().size() > 0)
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    db.collection("bill").document(document.getId())
                                            .delete();
                                }
                            }
                        }
                    }
                });
    }

    public static void GetBillsonFirestore(final HomeViewModel homeViewModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        homeViewModel.ClearBills();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải danh sách bàn...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    QuerySnapshot querySnapshot = Tasks.await(db.collection("bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .get());
                    int i = querySnapshot.getDocuments().size();
                    if (querySnapshot.getDocuments().size() > 0)
                    {
                        List<Bill> bills = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments())
                        {
                            Bill bill = new Bill(
                                    document.get("id_table").toString(),
                                    Integer.parseInt(document.get("num_customer").toString()),
                                    Integer.parseInt(document.get("total").toString()),
                                    new ArrayList<>()
                            );
                            QuerySnapshot querySnapshotItem = Tasks.await(db.collection("detail_bill")
                                    .whereEqualTo("id_res", Restaurant.getId())
                                    .whereEqualTo("id_table", bill.getId_table())
                                    .get());
                            if (querySnapshotItem.getDocuments().size() > 0)
                            {
                                List<Item> items = new ArrayList<>();
                                for (DocumentSnapshot documentItem : querySnapshotItem.getDocuments())
                                {
                                    QuerySnapshot detailItem = Tasks.await(db.collection("item")
                                    .whereEqualTo("id", documentItem.get("id_item"))
                                    .get());
                                    if (detailItem.size() > 0)
                                    {
                                        Item item = new Item(
                                                documentItem.get("id_item").toString(),
                                                detailItem.getDocuments().get(0).get("id_unit").toString(),
                                                detailItem.getDocuments().get(0).get("image_url").toString(),
                                                detailItem.getDocuments().get(0).get("name").toString(),
                                                Integer.parseInt(detailItem.getDocuments().get(0).get("price").toString()));
                                        item.setNumChoice(Integer.parseInt(documentItem.get("num_of_item").toString()));
                                        items.add(item);
                                    }

                                }
                                bill.setItemChoiceList(items);
                            }
                            bills.add(bill);
                        }
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                homeViewModel.setBills(bills);
                                progressDialog.dismiss();
                                //Toast.makeText(getContext(), "aaaa" + String.valueOf(homeViewModel.getBills().size()), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                //Toast.makeText(getContext(), "aaaa" + String.valueOf(homeViewModel.getBills().size()), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ExecutionException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }});
        thread.start();
        //while (thread.isAlive());
        //Toast.makeText(getContext(), String.valueOf(homeViewModel.getBills().size()), Toast.LENGTH_SHORT).show();
        }
}
