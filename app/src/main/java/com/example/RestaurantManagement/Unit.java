package com.example.RestaurantManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.RestaurantManagement.ui.TranAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;


import static com.facebook.FacebookSdk.getApplicationContext;

public class Unit {
    private String id;
    private boolean Checked;
    private String Name;
    private static int count;
    private static boolean checkExistUnit;
    private static Context context;
    private static Activity activity;
    private static Unit_interface unit_interface;
    private String idDocument;
    public static ProgressDialog progressDialog;
    public static Source source = Source.SERVER;
    public Unit(String id, boolean checked, String name, String idDocument) {
        this.id = id;
        Checked = checked;
        Name = name;
        this.idDocument = idDocument;
    }

    public Unit() {
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static int getCount(FirebaseFirestore db) {
        //AutoCountUnitOnFireStore(db);
        return Unit.count;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Unit.activity = activity;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public static boolean isCheckExistUnit() {
        return checkExistUnit;
    }

    public static void setCheckExistUnit(boolean checkExistUnit) {
        Unit.checkExistUnit = checkExistUnit;
    }

    public static void setCount(int count) {
        Unit.count = count;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Unit.context = context;
    }

    public static Unit_interface getUnit_interface() {
        return unit_interface;
    }

    public static void setUnit_interface(Unit_interface unit_interface) {
        Unit.unit_interface = unit_interface;
    }

    public void AddUnitOnFireStore(FirebaseFirestore db)
    {
        try
        {
            if (getName() == null || getName().equals(""))
            {
                Toast toast = Toast.makeText(getApplicationContext(), "T??n ????n v??? t??nh tr???ng", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            if (!Support.checkConnect(getActivity()))
            {
                TranAlertDialog dialog = new TranAlertDialog(
                        "C???NH B??O",
                        "Kh??ng t??m th???y k???t n???i m???ng!\\nVui l??ng ki???m tra l???i k???t n???i c???a b???n!",
                        R.drawable.ic_baseline_warning_24
                );
                dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "dialog");
                return;
            }
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("??ang th??m ????n v??? t??nh...");
            progressDialog.show();
            db.collection("unit")
                    .get(source)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean checkExist = false;
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                    if (document.get("name").equals(getName()) && document.get("id_res").equals(Restaurant.getId()))
                                    {
                                        unit_interface.onAddUnit(false, Unit.this);
                                        return;
                                    }
                                }
//                            if (source == Source.CACHE)
//                            {
//                                TranAlertDialog alertDialog = new TranAlertDialog(
//                                        "Th??ng b??o",
//                                        "Qu?? tr??nh ?????ng b??? th???t b???i!\nH??y ki???m tra k???t n???i m???ng",
//                                        R.drawable.ic_baseline_info_2_24);
//                                alertDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
//                                progressDialog.dismiss();
//                                unit_interface.onAddUnit(true, Unit.this);
//                                return;
//                            }

                                setId(String.valueOf(count + 1));
                                Map<String, Object> unit = new HashMap<>();
                                unit.put("id", getId());
                                unit.put("id_res", Restaurant.getId());
                                unit.put("name", getName());
                                if (!Support.checkConnect(getActivity()))
                                {
                                    progressDialog.dismiss();
                                }
                                db.collection("unit")
                                        .add(unit)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                unit_interface.onAddUnit(true, Unit.this);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                unit_interface.onAddUnit(false, Unit.this);

                                            }
                                        });
                            } else {
                                //Log.d(, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "L???I",
                    "???? x???y ra l???i h??? th???ng!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }

    }

    public void DeleteUnit()
    {
        try
        {if (!Support.checkConnect(getActivity()))
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "C???NH B??O",
                    "Kh??ng t??m th???y k???t n???i m???ng!\nVui l??ng ki???m tra l???i k???t n???i c???a b???n!",
                    R.drawable.ic_baseline_warning_24
            );
            dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "dialog");
            return;
        }
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("H??? th???ng ??ang x??a ????n v??? t??nh...");
            progressDialog.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("unit")
                    .whereEqualTo("id", getId())
                    .get()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful())
                            {
                                if (task.getResult() != null)
                                {
                                    //String s = task.getResult().getDocuments().get(0).get("name").toString();
                                    db.collection("unit")
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .delete()
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    TranAlertDialog dialog = new TranAlertDialog(
                                                            "TH??NH C??NG",
                                                            "???? x??a ????n v??? t??nh th??nh c??ng!",
                                                            R.drawable.ic_baseline_check_circle_24
                                                    );
                                                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                                    Intent intent = getActivity().getIntent();
                                                    Bundle bundle = intent.getBundleExtra("data");
                                                    String unitPresent = bundle.getString("present_unit");
                                                    UnitActivity.unitViewModel.ClearListUnit();
                                                    ProgressDialog progressDialog = new ProgressDialog((getContext()));
                                                    progressDialog.setTitle("Vui l??ng ch???");
                                                    progressDialog.show();
                                                    Unit.GetAllUnitOnFireStore(db, UnitActivity.unitViewModel, unitPresent);
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }
                                else
                                {
                                    TranAlertDialog dialog = new TranAlertDialog(
                                            "L???I",
                                            "H??? th???ng kh??ng x??a ???????c ????n v??? t??nh!\nVui l??ng th??? l???i!",
                                            R.drawable.ic_baseline_dangerous_24
                                    );
                                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                                }
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "L???I",
                    "???? x???y ra l???i h??? th???ng!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }
    }
    public void EditUnitOnFirebase(FirebaseFirestore db, String name, int position)
    {
        try
        {
            if (!Support.checkConnect(getActivity()))
            {
                TranAlertDialog dialog = new TranAlertDialog(
                        "C???NH B??O",
                        "Kh??ng t??m th???y k???t n???i m???ng!\nVui l??ng ki???m tra l???i k???t n???i c???a b???n!",
                        R.drawable.ic_baseline_warning_24
                );
                dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "dialog");
                return;
            }
            //Keyboard.hideKeyboard(activity);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("??ang th???c hi???n ch???nh s???a...");
            progressDialog.show();
            if (name.equals(""))
            {
                unit_interface.onEditUnit(false, position, "T??n ????n v??? t??nh kh??ng ???????c ????? tr???ng");
                return;
            }
            db.collection("unit")
                    .whereEqualTo("id_res", Restaurant.getId())
                    .whereEqualTo("name", name)
                    .get(Source.SERVER)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                if (task.getResult().size() != 0) {
                                    unit_interface.onEditUnit(false, position, "????n v??? t??nh b??? tr??ng" + Restaurant.getId());
                                } else {
                                    Map<String, Object> unit = new HashMap<>();
                                    unit.put("id", getId());
                                    unit.put("id_res", Restaurant.getId());
                                    unit.put("name", name);
                                    db.collection("unit").document(getIdDocument())
                                            .set(unit, SetOptions.merge())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        unit_interface.onEditUnit(true, position, name);
                                                    }
                                                    else
                                                    {
                                                        unit_interface.onEditUnit(false, position, "???? x???y ra l???i");
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            }
                            else
                            {
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "L???I",
                    "???? x???y ra l???i h??? th???ng!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }

    }

     static public void GetAllUnitOnFireStore(FirebaseFirestore db, UnitViewModel unitViewModel, String unitPresent)
    {
        try
        {
            if (!Support.checkConnect(getActivity()))
            {
                TranAlertDialog dialog = new TranAlertDialog(
                        "C???NH B??O",
                        "Kh??ng t??m th???y k???t n???i m???ng!\\nVui l??ng ki???m tra l???i k???t n???i c???a b???n!",
                        R.drawable.ic_baseline_warning_24
                );
                dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "dialog");
                return;
            }
            Keyboard.hideKeyboard(activity);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("??ang l???y d??? li???u...");
            progressDialog.show();
            db.collection("unit")
                    .whereEqualTo("id_res", Restaurant.getId())
                    .get(Source.SERVER)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Unit unit = new Unit(
                                            document.getString("id"),
                                            unitPresent.equals(document.getString("name")),
                                            document.getString("name"),
                                            document.getId());
                                    unitViewModel.AddUnit(unit);
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                unit_interface.onGetUnit(true, "L???y d??? li???u th??nh c??ng");
                            } else {
                                unit_interface.onGetUnit(true, "L???y d??? li???u th???t b???i");
                            }
                        }
                    });
        }
        catch (Exception a)
        {
            TranAlertDialog dialog = new TranAlertDialog(
                    "L???I",
                    "???? x???y ra l???i h??? th???ng!\n" + a.getMessage().toString(),
                    R.drawable.ic_baseline_dangerous_24
            );
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }

    }

}
