package com.example.RestaurantManagement.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.Restaurant;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeFragment extends Fragment implements SellAdapter.SellInterface {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SellAdapter adapter;
    private CheckedTextView AutoUpdate;
    private Button Refresh;
    ListenerRegistration autoUpdateRecycle = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bill.setActivity(getActivity());
        Bill.setContext(getContext());
        SellAdapter.setActivity(getActivity());
        SellAdapter.setContext(getContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(HomeFragment.this).get(HomeViewModel.class);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvResBill);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setBackground(new ColorDrawable(Color.parseColor("#59DBE2E3")));
        homeViewModel.getBillMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> bills) {
                adapter = new SellAdapter(homeViewModel.getBills(), HomeFragment.this);
                int i = 0;
//                for (i = 0; i < homeViewModel.getBills().size(); i++)
//                    Toast.makeText(getContext(), homeViewModel.getBills().get(i).getId_table(),Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(adapter);
            }
        });
        AutoUpdate = (CheckedTextView) root.findViewById(R.id.chtAutoUpdate);
        Refresh = (Button) root.findViewById(R.id.btnRefresh);
        AutoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoUpdate.toggle();
                if (AutoUpdate.isChecked())
                {
                    Refresh.setVisibility(View.GONE);
                    if (autoUpdateRecycle != null)
                        autoUpdateRecycle.remove();
                    autoUpdateRecycle = db.collection("bill")
                            .whereEqualTo("id_res", Restaurant.getId())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    Bill.GetBillsonFirestore(homeViewModel);
                                }
                            });
                }
                else
                {
                    Refresh.setVisibility(View.VISIBLE);
                    if (autoUpdateRecycle != null)
                        autoUpdateRecycle.remove();
                    autoUpdateRecycle = null;
                }
            }
        });
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill.GetBillsonFirestore(homeViewModel);
            }
        });
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        Bill.setActivity(getActivity());
        Bill.setContext(getContext());
        SellAdapter.setActivity(getActivity());
        SellAdapter.setContext(getContext());
        Bill.GetBillsonFirestore(homeViewModel);
//        db.collection("detail_bill")
//                .whereEqualTo("id_res", Restaurant.getId())
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        Bill.GetBillsonFirestore(homeViewModel);
//                    }
//                });
    }

    @Override
    public void onCancelBill() {

    }
}